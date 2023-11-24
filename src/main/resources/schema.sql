-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8;
USE `mydb`;

-- -----------------------------------------------------
-- Table `mydb`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`member`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `nickname` VARCHAR(45)  NOT NULL,
    `email`    VARCHAR(150) NOT NULL,
    `profile`  VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `login_id_UNIQUE` (`nickname` ASC)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`category`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(45)  NOT NULL,
    `category_image` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`item`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(100) NOT NULL,
    `content`     LONGTEXT     NOT NULL,
    `price`       INT          NULL,
    `thumbnail`   VARCHAR(512) NOT NULL,
    `status`      VARCHAR(20)  NOT NULL,
    `region`      VARCHAR(100) NOT NULL,
    `chat_count`  BIGINT       NOT NULL,
    `wish_count`  BIGINT       NOT NULL,
    `view_count`  BIGINT       NOT NULL,
    `created_at`  TIMESTAMP    NOT NULL,
    `member_id`   BIGINT       NOT NULL,
    `category_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_item_member2_idx` (`member_id` ASC),
    INDEX `fk_item_category2_idx` (`category_id` ASC),
    CONSTRAINT `fk_item_member2`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_item_category2`
        FOREIGN KEY (`category_id`)
            REFERENCES `mydb`.`category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`region`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`region`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`memberTown`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`memberTown`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id`   BIGINT       NOT NULL,
    `name`        VARCHAR(100) NOT NULL,
    `region_id`   BIGINT       NOT NULL,
    `is_selected` TINYINT      NOT NULL,
    PRIMARY KEY (`id`, `member_id`),
    INDEX `fk_memberTown_Member1_idx` (`member_id` ASC),
    INDEX `fk_memberTown_region1_idx` (`region_id` ASC),
    CONSTRAINT `fk_memberTown_Member1`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_memberTown_region1`
        FOREIGN KEY (`region_id`)
            REFERENCES `mydb`.`region` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wish`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`wish`
(
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT NOT NULL,
    `item_id`   BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_wish_member1_idx` (`member_id` ASC),
    INDEX `fk_wish_item1_idx` (`item_id` ASC),
    CONSTRAINT `fk_wish_member1`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_wish_item1`
        FOREIGN KEY (`item_id`)
            REFERENCES `mydb`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `mydb`.`chat_room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chatroom`
(
    `id`         BIGINT    NOT NULL AUTO_INCREMENT,
    `created_at` TIMESTAMP NOT NULL,
    seller_id    BIGINT    NOT NULL,
    buyer_id     BIGINT    NOT NULL,
    `item_id`    BIGINT    NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`chat_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chat_log`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `message`     VARCHAR(512) NOT NULL,
    `seller`      VARCHAR(45)  NOT NULL,
    `buyer`       VARCHAR(45)  NOT NULL,
    `created_at`  TIMESTAMP    NOT NULL,
    `new_massage` BIGINT       NOT NULL,
    chatroom_id   BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_chat_log_chat_room2_idx` (chatroom_id ASC),
    CONSTRAINT `fk_chat_log_chat_room2`
        FOREIGN KEY (chatroom_id)
            REFERENCES `mydb`.`chatroom` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`image`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `image_url` VARCHAR(512) NOT NULL,
    `thumbnail` TINYINT      NOT NULL,
    `item_id`   BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_image_item2_idx` (`item_id` ASC),
    CONSTRAINT `fk_image_item2`
        FOREIGN KEY (`item_id`)
            REFERENCES `mydb`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`member`
(
    `id`       BIGINT       NOT NULL AUTO_INCREMENT,
    `nickname` VARCHAR(45)  NOT NULL,
    `email`    VARCHAR(150) NOT NULL,
    `profile`  VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `login_id_UNIQUE` (`nickname` ASC)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`region`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`region`
(
    `id`   BIGINT       NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`member_town`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`member_town`
(
    `id`        BIGINT      NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(45) NOT NULL,
    `member_id` BIGINT      NOT NULL,
    `region_id` BIGINT      NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_member_town_member1_idx` (`member_id` ASC),
    INDEX `fk_member_town_region1_idx` (`region_id` ASC),
    CONSTRAINT `fk_member_town_member1`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_member_town_region1`
        FOREIGN KEY (`region_id`)
            REFERENCES `mydb`.`region` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`category`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `name`           VARCHAR(45)  NOT NULL,
    `category_image` VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`item`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `title`       VARCHAR(100) NOT NULL,
    `content`     LONGTEXT     NOT NULL,
    `price`       INT          NULL,
    `thumbnail`   VARCHAR(512) NOT NULL,
    `status`      VARCHAR(20)  NOT NULL,
    `region`      VARCHAR(100) NOT NULL,
    `chat_count`  BIGINT       NOT NULL,
    `wish_count`  BIGINT       NOT NULL,
    `view_count`  BIGINT       NOT NULL,
    `created_at`  TIMESTAMP    NOT NULL,
    `member_id`   BIGINT       NOT NULL,
    `category_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_item_member2_idx` (`member_id` ASC),
    INDEX `fk_item_category2_idx` (`category_id` ASC),
    CONSTRAINT `fk_item_member2`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_item_category2`
        FOREIGN KEY (`category_id`)
            REFERENCES `mydb`.`category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`image`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`image`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `image_url` VARCHAR(512) NOT NULL,
    `thumbnail` TINYINT      NOT NULL,
    `item_id`   BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_image_item2_idx` (`item_id` ASC),
    CONSTRAINT `fk_image_item2`
        FOREIGN KEY (`item_id`)
            REFERENCES `mydb`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wish`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`wish`
(
    `id`        BIGINT NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT NOT NULL,
    `item_id`   BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_wish_member1_idx` (`member_id` ASC),
    INDEX `fk_wish_item1_idx` (`item_id` ASC),
    CONSTRAINT `fk_wish_member1`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_wish_item1`
        FOREIGN KEY (`item_id`)
            REFERENCES `mydb`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`chat_room`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chat_room`
(
    `id`         BIGINT    NOT NULL AUTO_INCREMENT,
    `created_at` TIMESTAMP NOT NULL,
    `member_id`  BIGINT    NOT NULL,
    `item_id`    BIGINT    NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_chat_room_member2_idx` (`member_id` ASC),
    INDEX `fk_chat_room_item2_idx` (`item_id` ASC),
    CONSTRAINT `fk_chat_room_member2`
        FOREIGN KEY (`member_id`)
            REFERENCES `mydb`.`member` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_chat_room_item2`
        FOREIGN KEY (`item_id`)
            REFERENCES `mydb`.`item` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`chat_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`chat_log`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `message`      VARCHAR(512) NOT NULL,
    `to`           VARCHAR(45)  NOT NULL,
    `form`         VARCHAR(45)  NOT NULL,
    `created_at`   TIMESTAMP    NOT NULL,
    `new_massage`  BIGINT       NOT NULL,
    `chat_room_id` BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_chat_log_chat_room2_idx` (`chat_room_id` ASC),
    CONSTRAINT `fk_chat_log_chat_room2`
        FOREIGN KEY (`chat_room_id`)
            REFERENCES `mydb`.`chat_room` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
