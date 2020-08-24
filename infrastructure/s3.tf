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

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
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
