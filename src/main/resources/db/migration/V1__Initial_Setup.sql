--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

CREATE TABLE `m_tenants` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`tenant_id` VARCHAR(32) NOT NULL,
	`tenant_app_key` VARCHAR(100) NOT NULL,
	`description` VARCHAR(500) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;


CREATE TABLE `m_sms_bridge` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`tenant_id` BIGINT(20) NOT NULL,
	`tenant_phone_no` VARCHAR(255) NOT NULL,
	`provider_name` VARCHAR(100) NOT NULL,
	`country_code` VARCHAR(5) NOT NULL,
	`provider_key` VARCHAR(100) NOT NULL,
	`tenant_keyword` VARCHAR(100) NULL DEFAULT NULL,
	`description` VARCHAR(500) NOT NULL,
	`created_on` TIMESTAMP NULL DEFAULT NULL,
	`last_modified_on` TIMESTAMP NULL DEFAULT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `m_sms_bridge_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenants` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;


CREATE TABLE `m_outbound_messages` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`tenant_id` VARCHAR(32) NOT NULL,
	`external_id` VARCHAR(100) NULL DEFAULT NULL,
	`internal_id` VARCHAR(100) NOT NULL,
	`delivery_error_message` VARCHAR(500) NULL DEFAULT NULL,
	`source_address` VARCHAR(100) NULL DEFAULT NULL,
	`sms_bridge_id` BIGINT(20) NOT NULL,
	`mobile_number` VARCHAR(255) NOT NULL,
	`submitted_on_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`delivered_on_date` TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
	`delivery_status` INT(3) NOT NULL,
	`message` VARCHAR(4096) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `m_outbound_messages_1` FOREIGN KEY (`sms_bridge_id`) REFERENCES `m_sms_bridge` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1721
;


CREATE TABLE `m_sms_bridge_configuration` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`sms_bridge_id` BIGINT(20) NOT NULL,
	`config_name` VARCHAR(100) NULL DEFAULT NULL,
	`config_value` VARCHAR(100) NOT NULL,
	PRIMARY KEY (`id`),
	INDEX `m_provider_configuration_1` (`sms_bridge_id`),
	CONSTRAINT `m_provider_configuration_1` FOREIGN KEY (`sms_bridge_id`) REFERENCES `m_sms_bridge` (`id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
AUTO_INCREMENT=7
;


