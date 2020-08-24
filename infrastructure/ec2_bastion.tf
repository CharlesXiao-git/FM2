/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 *
 */

resource "aws_instance" "BastionHost" {
  ami = data.aws_ami.ubuntu.id
  instance_type = "t3.micro"

  vpc_security_group_ids = [aws_security_group.BastionHosts.id]
  subnet_id = aws_subnet.PublicDMZSubnet1.id

  key_name = var.bastion-key-name

  tags = {
    Name = "${var.application-name}-${terraform.workspace}-bastion-host"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_eip" "BastionHostEip" {
  instance = aws_instance.BastionHost.id
  vpc = true

  tags = {
    Name = "${var.application-name}-${terraform.workspace}-bastion-host-eip"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-bionic-18.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}