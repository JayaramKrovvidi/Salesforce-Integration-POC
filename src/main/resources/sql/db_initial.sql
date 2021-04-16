-- -----------------------------------------------------
-- Schema api-workflow
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `api-workflow` ;
CREATE SCHEMA IF NOT EXISTS `api-workflow` ;
USE `api-workflow` ;

-- -----------------------------------------------------
-- Table `Organization`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Organization` (
  `org_id` INT NOT NULL AUTO_INCREMENT,
  `org_nm` VARCHAR(45) NOT NULL,
  `integration_typ_nm` VARCHAR(45) NOT NULL,
  `last_modified_tm` TIMESTAMP NULL,
  PRIMARY KEY (`org_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `org_json_store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `org_json_store` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `org_id` INT NOT NULL,
  `json_key` VARCHAR(45) NOT NULL,
  `json_string` JSON NOT NULL,
  `last_modified_tm` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `api_state`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_state` (
  `api_id` INT NOT NULL AUTO_INCREMENT,
  `wf_id` INT NOT NULL,
  `api_key` VARCHAR(45) NOT NULL,
  `request_config` JSON NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  `detail_msg_txt` VARCHAR(255) NULL,
  `retry` VARCHAR(45) NOT NULL,
  `on_success` VARCHAR(255) NOT NULL,
  `on_failure` VARCHAR(255) NOT NULL,
  `last_modified_tm` TIMESTAMP NULL,
  PRIMARY KEY (`api_id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `workflow_state`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `workflow_state` (
  `wf_id` INT NOT NULL AUTO_INCREMENT,
  `json_id` INT NOT NULL,
  `run_config_mapper` JSON NULL,
  `status` VARCHAR(45) NOT NULL,
  `detail_msg_txt` VARCHAR(255) NULL,
  `current_api_id` INT NULL,
  `last_modified_tm` TIMESTAMP NULL,
  PRIMARY KEY (`wf_id`))
ENGINE = InnoDB;

ALTER TABLE `workflow_state` MODIFY `run_config_mapper` VARCHAR(255);
-- -----------------------------------------------------
-- Table `runtime_variables`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `runtime_variables` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `wf_id` INT NOT NULL,
  `key` VARCHAR(45) NOT NULL,
  `value` TEXT NULL,
  `last_modified_tm` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mapping_store`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mapping_store` (
  `mapping_id` INT NOT NULL AUTO_INCREMENT,
  `json_id` INT NOT NULL,
  `mapping_json` JSON NULL,
  `last_modified_tm` TIMESTAMP NULL,
  PRIMARY KEY (`mapping_id`))
ENGINE = InnoDB;

ALTER TABLE org_json_store ADD CONSTRAINT `org_mapping` FOREIGN KEY (`org_id`) REFERENCES `api-workflow`.`Organization` (`org_id`);
ALTER TABLE workflow_state ADD CONSTRAINT `full_json_config` FOREIGN KEY (`json_id`) REFERENCES `api-workflow`.`org_json_store` (`id`);
ALTER TABLE workflow_state ADD CONSTRAINT `current api` FOREIGN KEY (`current_api_id`) REFERENCES `api-workflow`.`api_state` (`api_id`);
ALTER TABLE api_state ADD CONSTRAINT `workflow reference` FOREIGN KEY (`wf_id`) REFERENCES `api-workflow`.`workflow_state` (`wf_id`);
ALTER TABLE runtime_variables ADD CONSTRAINT `workflow`FOREIGN KEY (`wf_id`) REFERENCES `api-workflow`.`workflow_state` (`wf_id`);
ALTER TABLE mapping_store ADD CONSTRAINT `composite_api` FOREIGN KEY (`json_id`) REFERENCES `api-workflow`.`org_json_store` (`id`);