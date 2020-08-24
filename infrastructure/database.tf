/**
 * Created by IntelliJ IDEA
 * freightmate-harbour
 * User: brandon.pierce
 * Date: 3/7/20
 * 
 */


resource "random_integer" "ApplicationDatabaseFinalSnapshotEntropy" {
  max = 99999
  min = 10000
}

resource "aws_rds_cluster" "ApplicationDatabase" {
  cluster_identifier = lower("${var.application-name}-${terraform.workspace}-Database")
  engine             = "aurora"
  engine_mode        = "serverless"
  engine_version     = "5.6.10a"
  port               = 3306

  db_subnet_group_name   = aws_db_subnet_group.ApplicationDatabase.name
  vpc_security_group_ids = [aws_security_group.DatabaseHosts.id]

  deletion_protection       = (lower(terraform.workspace) == "production") ? true : false
  skip_final_snapshot       = (lower(terraform.workspace) == "production") ? false : true
  final_snapshot_identifier = lower("${var.application-name}-${terraform.workspace}-ApplicationDatabaseFinal-${random_integer.ApplicationDatabaseFinalSnapshotEntropy.result}")

  master_username = "root"
  master_password = random_password.DatabaseMasterPassword.result

  scaling_configuration {
    auto_pause   = true
    min_capacity = 2
    max_capacity = 8
  }

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}


resource "aws_db_subnet_group" "ApplicationDatabase" {
  name        = lower("${var.application-name}-${terraform.workspace}-ApplicationDatabaseSubnetGroup")
  description = "Specifies which subnets will have database instances placed into them"

  subnet_ids = [aws_subnet.DataSubnet1.id, aws_subnet.DataSubnet2.id]

  tags = {
    Environment = terraform.workspace
    Application = var.application-name
  }
}