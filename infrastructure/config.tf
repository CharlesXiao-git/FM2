/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 2/7/20
 *
 */

terraform {
  backend "s3" {
    bucket               = "tf-state-freightmate-web-app"
    key                  = "state.tfstate"
    region               = "ap-southeast-2"
    workspace_key_prefix = "env"
  }
}

provider "aws" {
  version = ">= 2.68, < 3.0.0"
  region  = var.aws-region
}

provider "aws" {
  alias   = "us-east-1"

  version = ">= 2.68, < 3.0.0"
  region  = "us-east-1"
}

provider "template" {
  version = "2.1"
}

provider "random" {
  version = "2.2"
}