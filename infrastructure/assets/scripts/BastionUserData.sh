#!/bin/bash -xe
exec > >(tee /var/log/user-data.log|logger -t user-data -s 2>/dev/console) 2>&1

# Install Utilities
sudo yum update -y
sudo yum install -y https://dev.mysql.com/get/mysql57-community-release-el7-11.noarch.rpm
sudo yum install -y mysql-community-client python3 jq
sudo -Hu ec2-user pip3 install --user mycli

# Append keys to Authorized Keys
aws ssm get-parameter --region ${Region} --name ${SshKeysParam} | jq -rM '.Parameter.Value' | tr , '\n' >> /home/ec2-user/.ssh/authorized_keys
sudo chown ec2-user:ec2-user /home/ec2-user/.ssh/authorized_keys

# Alias Database URL to env-db-host
DBHOST="$(aws ssm get-parameter --region ${Region} --name ${EnvDbHost} | jq -rM '.Parameter.Value')"

echo "env-db-endpoint $DBHOST" > /etc/hosts.aliases
echo "export HOSTALIASES=/etc/hosts.aliases" >> /etc/profile
. /etc/profile
