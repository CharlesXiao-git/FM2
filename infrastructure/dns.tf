/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 * 
 */


/**
 * Public DNS Settings
 *
 */
data "aws_route53_zone" "Public" {
  name         = var.freight-com-domain
  private_zone = false
}

resource "aws_route53_record" "BastionHost" {
  name = lower("bastion.${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}")
  type = "A"
  zone_id = data.aws_route53_zone.Public.id
  ttl = 300

  records = [aws_eip.BastionHostEip.public_ip]
}

resource "aws_route53_record" "StagingClientRecord" {
  zone_id = data.aws_route53_zone.Public.id
  name    = "${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}"
  type    = "A"

  alias {
    evaluate_target_health = false
    name                   = aws_cloudfront_distribution.ApplicationClient.domain_name
    zone_id                = aws_cloudfront_distribution.ApplicationClient.hosted_zone_id
  }
}

resource "aws_route53_record" "StaticAssetsRecord" {
  zone_id = data.aws_route53_zone.Public.id
  name    = "static.${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}"
  type    = "A"

  alias {
    evaluate_target_health = false
    name                   = aws_s3_bucket.StaticAssets.website_domain
    zone_id                = aws_s3_bucket.StaticAssets.hosted_zone_id
  }
}

resource "aws_route53_record" "ApplicationAPI" {
  zone_id = data.aws_route53_zone.Public.id
  name    = "api.${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}"
  type    = "A"

  alias {
    evaluate_target_health = false
    name                   = aws_lb.ApplicationAPIWeb.dns_name
    zone_id                = aws_lb.ApplicationAPIWeb.zone_id
  }
}