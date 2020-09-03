/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 11/8/20
 * 
 */


resource "aws_cloudwatch_log_group" "ApplicationLogGroup" {
  name = "/fargate/${var.application-name}/${terraform.workspace}/api-application"

  retention_in_days = 90

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_cloudwatch_log_stream" "ApplicationLogStream" {
  log_group_name = aws_cloudwatch_log_group.ApplicationLogGroup.name
  name           = "${var.application-name}-${terraform.workspace}-api-stream"
}