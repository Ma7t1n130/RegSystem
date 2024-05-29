CREATE DATABASE `regsystem`;

CREATE USER 'regsystem'@'%' IDENTIFIED BY 'heslo';
GRANT USAGE ON *.* TO 'regsystem'@'%';
GRANT EXECUTE, SELECT, SHOW VIEW, ALTER, ALTER ROUTINE, CREATE, CREATE ROUTINE, CREATE TEMPORARY TABLES, CREATE VIEW, DELETE, DROP, EVENT, INDEX, INSERT, REFERENCES, TRIGGER, UPDATE, LOCK TABLES  ON `regsystem`.* TO 'regsystem'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

USE regsystem;

CREATE TABLE `PersonNumber` (
	`IDPersonNumber` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`PersonNumber` VARCHAR(12) NOT NULL COLLATE 'utf8mb4_czech_ci',
	PRIMARY KEY (`IDPersonNumber`) USING BTREE,
	UNIQUE INDEX `PersonNumber_UIDX` (`PersonNumber`) USING BTREE
)
COLLATE='utf8mb4_czech_ci'
ENGINE=InnoDB
;

INSERT INTO PersonNumber(PersonNumber) VALUES('jXa4g3H7oPq2');
INSERT INTO PersonNumber(PersonNumber) VALUES('yB9fR6tK0wLm');
INSERT INTO PersonNumber(PersonNumber) VALUES('cN1vZ8pE5sYx');
INSERT INTO PersonNumber(PersonNumber) VALUES('tQdG2kP3mJfB');
INSERT INTO PersonNumber(PersonNumber) VALUES('iM5sO6zXcW7v');
INSERT INTO PersonNumber(PersonNumber) VALUES('rU8nA9eT2bYh');
INSERT INTO PersonNumber(PersonNumber) VALUES('wV6eH1fK7qZj');
INSERT INTO PersonNumber(PersonNumber) VALUES('sL4gN9dC3bXz');
INSERT INTO PersonNumber(PersonNumber) VALUES('kR0aZ7vW2nDl');
INSERT INTO PersonNumber(PersonNumber) VALUES('eI1oY6tQ9dKj');
INSERT INTO PersonNumber(PersonNumber) VALUES('gT4cR7wS0lVx');
INSERT INTO PersonNumber(PersonNumber) VALUES('xF9hD2yJ3sWv');
INSERT INTO PersonNumber(PersonNumber) VALUES('hM5bZ8nK4aVf');
INSERT INTO PersonNumber(PersonNumber) VALUES('qE3lY6uT0vKd');
INSERT INTO PersonNumber(PersonNumber) VALUES('bG2zC7jR9xVp');
INSERT INTO PersonNumber(PersonNumber) VALUES('vB1fX4rH7iNt');
INSERT INTO PersonNumber(PersonNumber) VALUES('aO8kP3mZ6dIw');
INSERT INTO PersonNumber(PersonNumber) VALUES('dW9pL2eU1yNc');
INSERT INTO PersonNumber(PersonNumber) VALUES('nS7tJ0qR5wGh');
INSERT INTO PersonNumber(PersonNumber) VALUES('mY6sT1jA3cLz');

CREATE TABLE `Person` (
	`IDPerson` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`Name` VARCHAR(250) NOT NULL COLLATE 'utf8mb4_czech_ci',
	`Surname` VARCHAR(250) NULL DEFAULT NULL COLLATE 'utf8mb4_czech_ci',
	`IDPersonNumber` BIGINT(20) UNSIGNED NOT NULL,
	`UUID` VARCHAR(250) NULL DEFAULT NULL COLLATE 'utf8mb4_czech_ci',
	`CreatedAt` DATETIME NOT NULL DEFAULT current_timestamp(),
	PRIMARY KEY (`IDPerson`) USING BTREE,
	UNIQUE INDEX `Person_PersonNumber_UIDX` (`IDPersonNumber`) USING BTREE,
	UNIQUE INDEX `Person_UUID_UIDX` (`UUID`) USING BTREE
)
COLLATE='utf8mb4_czech_ci'
ENGINE=InnoDB
;

ALTER TABLE Person ADD CONSTRAINT Person_PersonNumber_FK FOREIGN KEY(IDPersonNumber) REFERENCES PersonNumber(IDPersonNumber);

