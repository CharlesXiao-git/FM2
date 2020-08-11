/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 * 
 */


resource "aws_security_group" "PublicALBAccess" {
  name        = "${var.application-name}-${terraform.workspace}-PublicALB"
  description = "Allow Traffic ingress and egress from Application ALB"
  vpc_id      = aws_vpc.application.id

  egress {
    description = "Allow ALB to send response traffic"
    from_port   = 0
    to_port     = 0
    protocol    = -1
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow ingress traffic to ALB on HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow ingress traffic to ALB on HTTPS"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-PublicALB"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_security_group" "ApplicationECSTasks" {
  name        = "${var.application-name}-${terraform.workspace}-ApplicationECSTasks"
  description = "Allow ingress access from HTTP/S to ALB"
  vpc_id      = aws_vpc.application.id

  egress {
    description = "Allow Application ECS Tasks to egress to internet"
    from_port   = 0
    to_port     = 0
    protocol    = -1
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description     = "Allow HTTP traffic from ALB to ECS Tasks"
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.PublicALBAccess.id]
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationECSTasks"
    Environment = terraform.workspace
    Application = var.application-name
  }
}


resource "aws_security_group" "DatabaseHosts" {
  name        = "${var.application-name}-${terraform.workspace}-DatabaseHosts"
  description = "Allow traffic transit to RDS hosts"
  vpc_id      = aws_vpc.application.id

  egress {
    description     = "Allow MySQL Traffic to egress to Application Tasks"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.ApplicationECSTasks.id, aws_security_group.BastionHosts.id]
  }

  ingress {
    description     = "Allow MySQL ingress traffic from application tasks"
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.ApplicationECSTasks.id, aws_security_group.BastionHosts.id]
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-DatabaseHosts"
    Environment = terraform.workspace
    Application = var.application-name
  }
}


resource "aws_security_group" "BastionHosts" {
  name        = "${var.application-name}-${terraform.workspace}-BastionHosts"
  description = "Allow Limited Traffic to BastionHosts"
  vpc_id      = aws_vpc.application.id

  egress {
    description = "Allow all traffic out from bastion host"
    from_port   = 0
    to_port     = 0
    protocol    = -1
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow SSH traffic into bastion host"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-BastionHosts"
    Environment = terraform.workspace
    Application = var.application-name
  }
}
