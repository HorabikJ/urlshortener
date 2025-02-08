-- Run this script in MySql Workbench to create database, and users.
DROP DATABASE IF EXISTS urlshortener;
DROP USER IF EXISTS `urlshortener-admin`@`%`;
DROP USER IF EXISTS `urlshortener-user`@`%`;
CREATE DATABASE IF NOT EXISTS urlshortener CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `urlshortener-admin`@`%` IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `urlshortener`.* TO `urlshortener-admin`@`%`;
CREATE USER IF NOT EXISTS `urlshortener-user`@`%` IDENTIFIED BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, SHOW VIEW ON `urlshortener`.* TO `urlshortener-user`@`%`;
FLUSH PRIVILEGES;
