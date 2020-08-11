/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 * 
 */


/**
 * VPC Resources
 *
 */
resource "aws_internet_gateway" "ApplicationIG" {
  vpc_id = aws_vpc.application.id

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-InternetGateway"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_eip" "ApplicationNATGateway" {
  vpc = true

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-NATGatewayEIP"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_nat_gateway" "ApplicationNATGateway" {
  allocation_id = aws_eip.ApplicationNATGateway.id
  subnet_id     = aws_subnet.PublicDMZSubnet1.id

  depends_on = [aws_eip.ApplicationNATGateway]

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-NATGateway"
    Environment = terraform.workspace
    Application = var.application-name
  }
}


/**
 * Public DMZ Subnets
 *
 */
resource "aws_subnet" "PublicDMZSubnet1" {
  cidr_block        = "10.0.0.0/24"
  vpc_id            = aws_vpc.application.id
  availability_zone = "${var.aws-region}a"


  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-PublicDMZ1"
    Environment = terraform.workspace
    Application = var.application-name
  }

}

resource "aws_subnet" "PublicDMZSubnet2" {
  cidr_block        = "10.0.5.0/24"
  vpc_id            = aws_vpc.application.id
  availability_zone = "${var.aws-region}b"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-PublicDMZ2"
    Environment = terraform.workspace
    Application = var.application-name
  }

}

resource "aws_route_table" "PublicDMZRouteTable" {
  vpc_id = aws_vpc.application.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.ApplicationIG.id
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-PublicDMZRouteTable"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_route_table_association" "PublicDMZSubnet1" {
  route_table_id = aws_route_table.PublicDMZRouteTable.id
  subnet_id      = aws_subnet.PublicDMZSubnet1.id
}
resource "aws_route_table_association" "PublicDMZSubnet2" {
  route_table_id = aws_route_table.PublicDMZRouteTable.id
  subnet_id      = aws_subnet.PublicDMZSubnet2.id
}


/**
 * Application Subnets
 *
 */
resource "aws_subnet" "ApplicationSubnet1" {
  cidr_block        = "10.0.10.0/24"
  vpc_id            = aws_vpc.application.id
  availability_zone = "${var.aws-region}a"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationSubnet1"
    Environment = terraform.workspace
    Application = var.application-name
  }

}

resource "aws_subnet" "ApplicationSubnet2" {
  cidr_block        = "10.0.15.0/24"
  vpc_id            = aws_vpc.application.id
  availability_zone = "${var.aws-region}b"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationSubnet2"
    Environment = terraform.workspace
    Application = var.application-name
  }

}


resource "aws_route_table" "ApplicationRouteTable" {
  vpc_id = aws_vpc.application.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.ApplicationNATGateway.id
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationRouteTable"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_route_table_association" "ApplicationSubnet1" {
  route_table_id = aws_route_table.ApplicationRouteTable.id
  subnet_id      = aws_subnet.ApplicationSubnet1.id
}
resource "aws_route_table_association" "ApplicationSubnet2" {
  route_table_id = aws_route_table.ApplicationRouteTable.id
  subnet_id      = aws_subnet.ApplicationSubnet2.id
}

/**
 * Data/Persistence Subnets
 *
 */
resource "aws_subnet" "DataSubnet1" {
  cidr_block        = "10.0.20.0/24"
  vpc_id            = aws_vpc.application.id
  availability_zone = "${var.aws-region}a"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-DataSubnet1"
    Environment = terraform.workspace
    Application = var.application-name
  }

}

resource "aws_subnet" "DataSubnet2" {
  cidr_block        = "10.0.25.0/24"
  vpc_id            = aws_vpc.application.id
  availability_zone = "${var.aws-region}b"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-DataSubnet2"
    Environment = terraform.workspace
    Application = var.application-name
  }

}


resource "aws_route_table" "DataRouteTable" {
  vpc_id = aws_vpc.application.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.ApplicationNATGateway.id
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-DataRouteTable"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_route_table_association" "DataSubnet1" {
  route_table_id = aws_route_table.DataRouteTable.id
  subnet_id      = aws_subnet.DataSubnet1.id
}
resource "aws_route_table_association" "DataSubnet2" {
  route_table_id = aws_route_table.DataRouteTable.id
  subnet_id      = aws_subnet.DataSubnet2.id
}