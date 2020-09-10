#!/bin/bash -xe
exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1

# Install Utilities
sudo yum update -y
sudo yum install -y https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
sudo yum install -y mysql-community-client python3 jq
sudo -Hu ec2-user pip3 install --user mycli

# Create individual users and add their public keys
aws ssm get-parameter --region ${Region} --name ${SshKeysParam} | jq -rM '.Parameter.Value' | tr , '\n' | while read -r enc pubkey name; do
  sudo adduser -m "$name"
  sudo mkdir "/home/$name/.ssh/"
  echo "$enc $pubkey $name" | sudo tee "/home/$name/.ssh/authorized_keys"
  sudo chown -R "$name:$name" "/home/$name/.ssh/"
  sudo chmod 600 "/home/$name/.ssh/authorized_keys"
  if [ "$name" = "tsposato" ]
  then
    echo "tsposato ALL=(ALL) NOPASSWD:ALL" | sudo tee -a /etc/sudoers.d/90-cloud-init-users
  fi
done

# Alias Database URL to env-db-host
DBHOST="$(aws ssm get-parameter --region ${Region} --name ${EnvDbHost} | jq -rM '.Parameter.Value')"
echo "env-db-endpoint $DBHOST" > /etc/hosts.aliases
echo "export HOSTALIASES=/etc/hosts.aliases" >> /etc/profile
. /etc/profile

# Set Hostname, Bash Prompt and Timezone
sudo hostnamectl set-hostname "${BastionHostname}"
echo "export NICKNAME=${Prompt}" | sudo tee /etc/profile.d/prompt.sh
sudo sed -i 's/\\h/$NICKNAME/' /etc/bashrc
sudo sed -i '0,/UTC/s//Australia\/Melbourne/' /etc/sysconfig/clock
sudo ln -sf /usr/share/zoneinfo/Australia/Melbourne /etc/localtime
