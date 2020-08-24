/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 13/7/20
 * 
 */

resource "aws_lb" "ApplicationAPIWeb" {
  name               = "${var.application-name}-${terraform.workspace}-application-api"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.PublicALBAccess.id]
  subnets            = [aws_subnet.PublicDMZSubnet1.id, aws_subnet.PublicDMZSubnet2.id]

  enable_deletion_protection = false

  //  access_logs {
  //    bucket = ""
  //  }

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_lb_target_group" "ApplicationAPITargetGroup" {
  name        = "${var.application-name}-${terraform.workspace}-API"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.application.id
  target_type = "ip"
}

resource "aws_lb_listener" "ApplicationAPI" {
  load_balancer_arn = aws_lb.ApplicationAPIWeb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = aws_acm_certificate.ApplicationAPI.arn

  depends_on = [aws_lb_target_group.ApplicationAPITargetGroup]

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.ApplicationAPITargetGroup.arn
  }
}

resource "aws_lb_listener" "ApplicationAPIHTTPRedirect" {
  load_balancer_arn = aws_lb.ApplicationAPIWeb.arn
  port              = 80
  protocol          = "HTTP"

  depends_on = [aws_lb_target_group.ApplicationAPITargetGroup]

  default_action {
    type = "redirect"

    redirect {
      status_code = "HTTP_301"
      protocol    = "HTTPS"
      port        = "443"
    }
  }
}