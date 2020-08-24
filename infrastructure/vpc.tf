/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 *
 */

resource "aws_vpc" "application" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-Environment"
    Environment = terraform.workspace
    Application = var.application-name
  }
}
