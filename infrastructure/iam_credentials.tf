/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 *
 */


data "aws_iam_role" "AWSServiceRoleForECS" {
  name = "AWSServiceRoleForECS"
}

resource "aws_iam_role" "ECSServiceRole" {
  name        = "${var.application-name}-${terraform.workspace}-ECSServiceRole"
  path        = "/Harbour/${terraform.workspace}/roles/"
  description = "Role for ECS Service Tasks"

  assume_role_policy = file("assets/policies/ECSSTSAssumePolicy.json")

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_iam_role_policy_attachment" "ApplicationECSTaskRolePolicyAttachment" {
  policy_arn = aws_iam_policy.ApplicationECSTaskPolicy.arn
  role       = aws_iam_role.ECSServiceRole.name
}

resource "aws_iam_policy" "ApplicationECSTaskPolicy" {
  name        = "${var.application-name}${terraform.workspace}ApplicationECSTaskPolicy"
  description = "Policy rules allowing the Harbour Web Application to transact with additional AWS resources. Recommended by AWS."
  path        = "/Harbour/${terraform.workspace}/policies/"
  policy      = file("assets/policies/AWSServiceRoleForECS.json")
}


resource "aws_iam_role_policy_attachment" "WebAPIPolicyForECS" {
  policy_arn = aws_iam_policy.ApplicationWebPolicy.arn
  role       = aws_iam_role.ECSServiceRole.name
}


resource "aws_iam_role" "ECSTaskServiceRole" {
  name        = "${var.application-name}-${terraform.workspace}-ECSTaskRole"
  path        = "/Harbour/${terraform.workspace}/roles/"
  description = "Role for ECS Web Service Tasks"

  assume_role_policy = file("assets/policies/ECSTaskSTSAssumePolicy.json")

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_iam_role_policy_attachment" "ApplicationWebAPIRolePolicyAttachment" {
  policy_arn = aws_iam_policy.ApplicationWebPolicy.arn
  role       = aws_iam_role.ECSTaskServiceRole.name
}

resource "aws_iam_policy" "ApplicationWebPolicy" {
  name        = "${var.application-name}${terraform.workspace}ApplicationWebPolicy"
  description = "Policy rules allowing the Harbour Web Application to transact with AWS resources."
  path        = "/Harbour/${terraform.workspace}/policies/"
  policy      = data.template_file.ApplicationWebPolicy.rendered

}

data "template_file" "ApplicationWebPolicy" {
  template = file("assets/policies/IAMWebPolicy.json")

  vars = {
    ApplicationAPILoadBalancerTargetGroup = aws_lb_target_group.ApplicationAPITargetGroup.arn
    ApplicationSubnet1CidrBlock           = aws_subnet.ApplicationSubnet1.cidr_block
    ApplicationSubnet2CidrBlock           = aws_subnet.ApplicationSubnet2.cidr_block
    s3Bucket                              = "arn:aws:s3:::someS3BucketArn/*"
    snsTopic                              = "arn:aws:sns:ap-southeast-2:687523771005:someSnsTopicArn"
    ApplicationEcrRepository              = aws_ecr_repository.Application.arn
    CloudWatchLogGroup                    = aws_cloudwatch_log_group.ApplicationLogGroup.arn
    CloudWatchLogStream                   = aws_cloudwatch_log_stream.ApplicationLogStream.arn
    ssmParameters = jsonencode([
      aws_ssm_parameter.DatabaseUsername.arn,
      aws_ssm_parameter.DatabasePassword.arn,
      aws_ssm_parameter.AusPostAPIKey.arn,
      aws_ssm_parameter.JWTSecret.arn,
      aws_ssm_parameter.DatabaseApplicationDatabasePassword.arn
    ])
  }
}
