/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 * 
 */


// Client App Bucket CDN
resource "aws_cloudfront_distribution" "ApplicationClient" {
  enabled             = true
  default_root_object = "index.html"
  aliases             = [trimsuffix(lower("${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}"), ".")]

  price_class = "PriceClass_All" // Australia is in the "All" class

  default_cache_behavior {
    allowed_methods        = ["HEAD", "GET", "OPTIONS"]
    cached_methods         = ["HEAD", "GET", "OPTIONS"]
    target_origin_id       = "myS3Origin"
    viewer_protocol_policy = "redirect-to-https"
    compress               = true

    forwarded_values {
      query_string = true
      headers      = ["Authorization", "Access-Control-Request-Headers", "Access-Control-Request-Method", "Accept", "Origin", "Referer"]

      cookies {
        forward = "all"
      }
    }
  }

  // For SPA apps, we have to push all not-found or not-auth'd requests on the client bucket to the SPA app for handling
  custom_error_response {
    error_code         = 400
    response_code      = 200
    response_page_path = "/index.html"
  }

  custom_error_response {
    error_code         = 403
    response_code      = 200
    response_page_path = "/index.html"
  }

  custom_error_response {
    error_code         = 404
    response_code      = 200
    response_page_path = "/index.html"
  }

  origin {
    domain_name = aws_s3_bucket.AppClient.bucket_regional_domain_name
    origin_id   = "myS3Origin"
    origin_path = ""

    s3_origin_config {
      origin_access_identity = aws_cloudfront_origin_access_identity.ClientDistribution.cloudfront_access_identity_path
    }
  }

  restrictions {
    geo_restriction {
      restriction_type = "none"
    }
  }

  viewer_certificate {
    acm_certificate_arn      = aws_acm_certificate.ApplicationClient.arn // Note: Needs to be a us-east-1 certificate (Global)
    minimum_protocol_version = "TLSv1"
    ssl_support_method       = "sni-only"
  }

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationClient"
    Environment = terraform.workspace
    Application = var.application-name
  }
}

resource "aws_cloudfront_origin_access_identity" "ClientDistribution" {
  comment = "${var.application-name}-${terraform.workspace}-OriginAccessIdentity"
}

// TODO: Create CloudFront Distribution for Static Assets
