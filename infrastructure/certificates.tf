/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 * 
 */

// Application Client Certificate
resource "aws_acm_certificate" "ApplicationClient" {
  provider = aws.us-east-1

  domain_name       = "${terraform.workspace}.staging.${data.aws_route53_zone.Public.name}"
  validation_method = "DNS"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationClient"
    Environment = terraform.workspace
    Application = var.application-name
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_acm_certificate_validation" "ApplicationClientValidation" {
  provider = aws.us-east-1

  certificate_arn         = aws_acm_certificate.ApplicationClient.arn
  validation_record_fqdns = [aws_route53_record.ApplicationClientCertificateValidation.fqdn]
  depends_on              = [aws_route53_record.ApplicationClientCertificateValidation]
}

resource "aws_route53_record" "ApplicationClientCertificateValidation" {
  provider = aws.us-east-1

  depends_on = [aws_acm_certificate.ApplicationClient]
  name       = aws_acm_certificate.ApplicationClient.domain_validation_options.0.resource_record_name
  type       = aws_acm_certificate.ApplicationClient.domain_validation_options.0.resource_record_type
  zone_id    = data.aws_route53_zone.Public.zone_id
  records    = [aws_acm_certificate.ApplicationClient.domain_validation_options.0.resource_record_value]
  ttl        = 60
}


// Static Assets Certificate
resource "aws_acm_certificate" "ApplicationStaticAssets" {
  provider = aws.us-east-1

  domain_name       = aws_route53_record.StaticAssetsRecord.name
  validation_method = "DNS"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationStaticAssets"
    Environment = terraform.workspace
    Application = var.application-name
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_acm_certificate_validation" "ApplicationStaticAssetsValidation" {
  provider                = aws.us-east-1
  certificate_arn         = aws_acm_certificate.ApplicationStaticAssets.arn
  validation_record_fqdns = [aws_route53_record.ApplicationStaticAssetsCertificateValidation.fqdn]
  depends_on              = [aws_route53_record.ApplicationStaticAssetsCertificateValidation]
}

resource "aws_route53_record" "ApplicationStaticAssetsCertificateValidation" {
  provider   = aws.us-east-1
  depends_on = [aws_acm_certificate.ApplicationStaticAssets]
  name       = aws_acm_certificate.ApplicationStaticAssets.domain_validation_options.0.resource_record_name
  type       = aws_acm_certificate.ApplicationStaticAssets.domain_validation_options.0.resource_record_type
  zone_id    = data.aws_route53_zone.Public.zone_id
  records    = [aws_acm_certificate.ApplicationStaticAssets.domain_validation_options.0.resource_record_value]
  ttl        = 60
}

// Application API Certificate
resource "aws_acm_certificate" "ApplicationAPI" {
  domain_name       = aws_route53_record.ApplicationAPI.name
  validation_method = "DNS"

  tags = {
    Name        = "${var.application-name}-${terraform.workspace}-ApplicationAPI"
    Environment = terraform.workspace
    Application = var.application-name
  }

  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_acm_certificate_validation" "ApplicationAPIValidation" {
  certificate_arn         = aws_acm_certificate.ApplicationAPI.arn
  validation_record_fqdns = [aws_route53_record.ApplicationAPICertificateValidation.fqdn]
  depends_on              = [aws_route53_record.ApplicationAPICertificateValidation]
}

resource "aws_route53_record" "ApplicationAPICertificateValidation" {
  depends_on = [aws_acm_certificate.ApplicationAPI]
  name       = aws_acm_certificate.ApplicationAPI.domain_validation_options.0.resource_record_name
  type       = aws_acm_certificate.ApplicationAPI.domain_validation_options.0.resource_record_type
  zone_id    = data.aws_route53_zone.Public.zone_id
  records    = [aws_acm_certificate.ApplicationAPI.domain_validation_options.0.resource_record_value]
  ttl        = 60
}
