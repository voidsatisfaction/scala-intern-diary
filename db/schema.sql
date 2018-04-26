DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` BIGINT UNSIGNED NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `created` TIMESTAMP NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `diary`;
CREATE TABLE `diary` (
  `diary_id` BIGINT UNSIGNED NOT NULL,
  `title` VARCHAR(150) NOT NULL,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `created` TIMESTAMP NOT NULL,
  PRIMARY KEY (`diary_id`),
  UNIQUE KEY (`title`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- QUESTION: これないと、errorが出る。どうすれば良い？
SET SQL_MODE='ALLOW_INVALID_DATES';

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `article_id` BIGINT UNSIGNED NOT NULL,
  `title` VARCHAR(150) NOT NULL,
  `body` VARCHAR(5000) NOT NULL,
  `diary_id` BIGINT UNSIGNED NOT NULL,
  `created` TIMESTAMP NOT NULL,
  `updated` TIMESTAMP NOT NULL,
  PRIMARY KEY (`article_id`),
  UNIQUE KEY (`title`, `diary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
