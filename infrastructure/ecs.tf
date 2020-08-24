/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 *
 */


resource "aws_ecs_cluster" "ApplicationCluster" {
  name               = "${var.application-name}-${terraform.workspace}-Application"
  capacity_providers = ["FARGATE"]

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_ecs_service" "WebService" {
  name             = "WebAPI"
  cluster          = aws_ecs_cluster.ApplicationCluster.id
  task_definition  = aws_ecs_task_definition.Application.id # Will always pick the latest ACTIVE defn.
  desired_count    = 1
  depends_on       = [aws_lb_target_group.ApplicationAPITargetGroup]
  launch_type      = "FARGATE"
  platform_version = "1.3.0"

  network_configuration {
    subnets          = [aws_subnet.ApplicationSubnet1.id, aws_subnet.ApplicationSubnet2.id]
    security_groups  = [aws_security_group.ApplicationECSTasks.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.ApplicationAPITargetGroup.id
    container_name   = var.application-ecs-container-name
    container_port   = var.application-spring-port
  }

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_ecs_task_definition" "Application" {
  container_definitions    = data.template_file.ApplicationContainerDefinition.rendered
  family                   = "Harbour-WebApp-${terraform.workspace}"
  execution_role_arn       = aws_iam_role.ECSTaskServiceRole.arn
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  cpu    = "2048"
  memory = "4096"

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

data "template_file" "ApplicationContainerDefinition" {
  template = file("assets/tasks/harbourWebApp.json")

  vars = {
    ApplicationImage         = "${aws_ecr_repository.Application.repository_url}:latest"
    ApplicationContainerName = var.application-ecs-container-name
    DatabaseHost             = aws_rds_cluster.ApplicationDatabase.endpoint
    DatabaseName             = var.application-database-name
    DatabaseUsername         = var.application-database-username
    DatabasePassword         = aws_ssm_parameter.DatabaseApplicationDatabasePassword.arn
    AusPostApiKey            = aws_ssm_parameter.AusPostAPIKey.arn
    JwtSecret                = aws_ssm_parameter.JWTSecret.arn
    SpringPort               = var.application-spring-port
    CloudWatchLogGroup       = aws_cloudwatch_log_group.ApplicationLogGroup.name
    AwsRegion                = var.aws-region
  }

}

resource "aws_ecr_repository" "Application" {
  name                 = "freightmate-harbour-${lower(terraform.workspace)}-application-api"
  image_tag_mutability = "MUTABLE"

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}