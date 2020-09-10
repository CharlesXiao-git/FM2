/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 *
 */

resource "aws_instance" "BastionHost" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  vpc_security_group_ids = [aws_security_group.BastionHosts.id]
  subnet_id              = aws_subnet.PublicDMZSubnet1.id

  key_name = var.bastion-key-name

  user_data_base64 = base64encode(data.template_file.BastionHostUserData.rendered)

  iam_instance_profile = aws_iam_instance_profile.BastionIamInstanceProfile.id

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-bastion-host"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_iam_instance_profile" "BastionIamInstanceProfile" {
  name = join("-", [var.application-name, terraform.workspace, "BastionHostProfile"])
  role = aws_iam_role.EC2BastionHostRole.name
}

resource "aws_eip" "BastionHostEip" {
  instance = aws_instance.BastionHost.id
  vpc      = true

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-bastion-host-eip"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-2.0.*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["137112412989"] # Amazon
}

data template_file "BastionHostUserData" {
  template = file("assets/scripts/BastionUserData.sh")
  vars = {
    Region          = var.aws-region
    SshKeysParam    = aws_ssm_parameter.BastionHostPubKeys.name
    EnvDbHost       = aws_ssm_parameter.DatabaseEndpoint.name
    BastionHostname = aws_route53_record.BastionHost.name
    Prompt          = terraform.workspace
  }
}