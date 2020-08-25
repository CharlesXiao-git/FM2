/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 * 
 */


// Bucket for JS App Client
resource "aws_s3_bucket" "AppClient" {
  bucket = lower("${terraform.workspace}.staging.${trimsuffix(data.aws_route53_zone.Public.name, ".")}")
  acl    = "private"

  region = var.aws-region

  website {
    index_document = "index.html"
    error_document = "index.html"
  }

  cors_rule {
    allowed_methods = ["GET", "HEAD"]
    allowed_origins = [lower("${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}")]
    max_age_seconds = 300
  }

  policy = <<POLICY
{
  "Version":"2012-10-17",
  "Statement":[
    {
      "Sid":"AddPerm",
      "Effect":"Allow",
      "Principal": "*",
      "Action":["s3:GetObject"],
      "Resource":[
        "arn:aws:s3:::${lower(terraform.workspace)}.staging.${trimsuffix(data.aws_route53_zone.Public.name, ".")}/*"
      ]
    }
  ]
}
POLICY

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_s3_bucket_policy" "AppClientCloudFrontPolicy" {
  bucket = aws_s3_bucket.AppClient.id
  policy = data.template_file.S3ClientBucketPolicy.rendered
}

data template_file "S3ClientBucketPolicy" {
  template = file("assets/policies/S3BucketClientPolicy.json")
  vars = {
    S3BucketResource = "arn:aws:s3:::${lower(terraform.workspace)}.staging.${trimsuffix(data.aws_route53_zone.Public.name, ".")}/*"
    CloudFrontDistribution = aws_cloudfront_origin_access_identity.ClientDistribution.iam_arn
  }
}

// Bucket for Static Assets
resource "aws_s3_bucket" "StaticAssets" {
  bucket = lower("static.${terraform.workspace}.staging.${trimsuffix(data.aws_route53_zone.Public.name, ".")}")
  acl    = "private"

  region = var.aws-region

  website {
    index_document = "index.html"
    error_document = "index.html"
  }

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}
