/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 * 
 */


resource "aws_ssm_parameter" "DatabaseUsername" {
  name  = "${var.application-name}-${terraform.workspace}-database-master-username"
  type  = "String"
  value = "harbour-app"

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_ssm_parameter" "DatabasePassword" {
  name  = "${var.application-name}-${terraform.workspace}-database-master-password"
  type  = "SecureString"
  value = random_password.DatabaseMasterPassword.result

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_ssm_parameter" "DatabaseApplicationDatabasePassword" {
  name  = "${var.application-name}-${terraform.workspace}-database-application-password"
  type  = "SecureString"
  value = random_password.DatabaseAppPassword.result

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_ssm_parameter" "DatabaseEndpoint" {
  name  = join("-", [var.application-name, terraform.workspace, "database-endpoint"])
  type  = "String"
  value = aws_rds_cluster.ApplicationDatabase57.endpoint
}

resource "aws_ssm_parameter" "JWTSecret" {
  name  = "${var.application-name}-${terraform.workspace}-jwt-secret"
  type  = "SecureString"
  value = random_password.JWTSecretKey.result

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_ssm_parameter" "AusPostAPIKey" {
  name  = "${var.application-name}-${terraform.workspace}-auspost-api-key"
  type  = "String"
  value = "foobar"

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }

  lifecycle {
    ignore_changes = [value]
  }
}

resource "aws_ssm_parameter" "BastionHostPubKeys" {
  name  = join("-", [var.application-name, terraform.workspace, "Bastion-Host-Keys"])
  type  = "StringList"
  value = "foo,bar"
  tier = "Advanced"

  lifecycle {
    ignore_changes = [value]
  }
}
