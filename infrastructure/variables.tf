/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 *
 */


variable "aws-region" {
  description = "AWS Region defined for this project"
  default     = "ap-southeast-2"
}

variable "application-name" {
  description = "The name of the application"
  default     = "Harbour"
}

variable "freight-com-domain" {
  description = "The domain used for mounting a staging environment"
  default     = "freightmate.com."
}

variable "application-spring-port" {
  description = "Application port to use on Fargate container, and spring app"
  default = 80
}

resource "random_password" "DatabaseMasterPassword" {
  length           = 24
  special          = true
  override_special = "!#$%^*()-=+_?{}|"
}

resource "random_password" "DatabaseAppPassword" {
  length           = 24
  override_special = "!#$%^*()-=+_?{}|"
}

variable "application-database-username" {
  description = "API Application Datatabase Username"
  default     = "freightmate_app"
}

variable "application-database-name" {
  description = "Application Database name"
  default     = "freightmate_user"
}

variable "application-ecs-container-name" {
  description = "Name of the ECS API Application Container"
  default     = "HarbourJavaAPI"
}

resource "random_password" "JWTSecretKey" {
  length = 24
}