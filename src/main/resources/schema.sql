
--
-- Table structure for table `account`
--

CREATE TABLE IF NOT EXISTS `account` (
  `id` varchar(255) NOT NULL,
  `account_type` varchar(400) DEFAULT NULL,
  `account_status` varchar(400) DEFAULT NULL,
  `is_internal` char(1) DEFAULT NULL,
  `time_active_from` timestamp NULL DEFAULT NULL,
  `time_active_until` timestamp NULL DEFAULT NULL,
  `time_access_restricted_from` timestamp NULL DEFAULT NULL,
  `time_access_restricted_until` timestamp NULL DEFAULT NULL,
  `time_created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `internal_properties` longtext,
  PRIMARY KEY (`id`),
  KEY `account_type_idx` (`account_type`),
  KEY `account_name_idx` (`name`)
);

-- --------------------------------------------------------

--
-- Table structure for table `account_owner`
--

CREATE TABLE IF NOT EXISTS `account_owner` (
  `id` varchar(255) NOT NULL,
  `account_id` char(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` varchar(255) NOT NULL,
  `account_id` varchar(255) NOT NULL,
  `user_type` varchar(100) DEFAULT NULL,
  `provider` varchar(20) DEFAULT NULL,
  `is_admin` char(1) DEFAULT NULL,
  `contact_email` varchar(100) DEFAULT NULL,
  `profile_image` blob,
  `is_primary` char(1) DEFAULT NULL,
  `cell_phone` char(36) DEFAULT NULL,
  `screen_name` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `visibility_status` varchar(100) DEFAULT NULL,
  `address` varchar(100) DEFAULT NULL,
  `account_usercol` varchar(45) DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL,
  `locked` tinyint(1) NOT NULL,
  `time_created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `time_updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `internal_properties` longtext,
  PRIMARY KEY (`id`),
  KEY `account_id_idx` (`account_id`),
  KEY `user_type_idx` (`user_type`),
  KEY `user_name_idx` (`name`),
  CONSTRAINT `user_account_id_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
);

-- --------------------------------------------------------

--
-- Table structure for table `user_group`
--

 CREATE TABLE IF NOT EXISTS `user_group` (
   `id` varchar(255) NOT NULL,
   `group_name` varchar(100) DEFAULT NULL,
   PRIMARY KEY (`id`)
 );

-- --------------------------------------------------------

--
-- Table structure for table `user_in_group`
--

CREATE TABLE IF NOT EXISTS `user_in_group` (
  `user_id` varchar(255) NOT NULL,
  `group_id` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
    constraint `group_id_fk_idx` foreign key (`group_id`) references `user_group` (`id`),
    constraint `user_id_fk_idx` foreign key (`user_id`) references `user` (`id`)
);

-- --------------------------------------------------------

--
-- Table structure for table `confirmation_token`
--

CREATE TABLE IF NOT EXISTS `confirmation_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `confirmed_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `expires_at` datetime NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_idx` (`user_id`),
  CONSTRAINT `confirmation_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);