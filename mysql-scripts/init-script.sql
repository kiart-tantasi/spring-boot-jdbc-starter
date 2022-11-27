-- 1: select db

USE mydb;

-- 2: create 'test' table and insert some dummy data

DROP TABLE IF EXISTS test;

CREATE TABLE test (name VARCHAR(254), age INT, career VARCHAR(254));

INSERT INTO test (name, age, career) VALUES
('Kiart', 25, 'Engineer'),
('Polo', 25, 'CTO'),
('James', 25, 'Project Manager'),
('Keith', 27, 'Engineer'),
('Petch', 19, 'Engineer');

-- 3: create stored procedures

-- 1

DROP PROCEDURE IF EXISTS get_all;

DELIMITER //

CREATE PROCEDURE get_all()
BEGIN
	SELECT *  FROM test;
END //

DELIMITER ;

-- 2

DROP PROCEDURE IF EXISTS get_by_age;

DELIMITER //

CREATE PROCEDURE get_by_age(
	IN age int
)
BEGIN
	SELECT * FROM test t
    WHERE t.age   = age
    ;
END //

DELIMITER ;

-- 3

DROP PROCEDURE IF EXISTS get_by_career;

DELIMITER //

CREATE PROCEDURE get_by_career(
    IN career varchar(254)
)
BEGIN
	SELECT * FROM test t
    WHERE t.career = career
    ;
END //

DELIMITER ;
