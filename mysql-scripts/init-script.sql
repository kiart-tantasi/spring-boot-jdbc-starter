-- 1: select db

USE mydb;

-- 2: create 'test' table and insert some dummy data

DROP TABLE IF EXISTS test;

CREATE TABLE test (
    name VARCHAR(254) NOT NULL,
    age INT NOT NULL,
    career VARCHAR(254) NOT NULL,
    created_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    points FLOAT(2) DEFAULT 99.99,
    balance DECIMAL(10, 2) DEFAULT 1000.55,
    dob DATE DEFAULT "2022-01-01"
);

INSERT INTO test (name, age, career) VALUES
('Kiart', 25, 'Engineer'),
('Polo', 32, 'CTO'),
('James', 25, 'Project Manager'),
('Keith', 25, 'Engineer'),
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
	IN $age INT
)
BEGIN
	SELECT * FROM test t
    WHERE t.age = $age
    ;
END //

DELIMITER ;

-- 3

DROP PROCEDURE IF EXISTS get_by_career;

DELIMITER //

CREATE PROCEDURE get_by_career(
    IN $career VARCHAR(254)
)
BEGIN
	SELECT * FROM test t
    WHERE t.career = $career
    ;
END //

DELIMITER ;

-- 4

DROP PROCEDURE IF EXISTS get_by_age_and_career;

DELIMITER //

CREATE PROCEDURE get_by_age_and_career(
    IN $age INT,
    IN $career VARCHAR(254)
)
BEGIN
	SELECT * FROM test t
    WHERE t.age = $age
    and t.career = $career
    ;
END //

DELIMITER ;


-- 5

DROP PROCEDURE IF EXISTS insert_employee;

DELIMITER //

CREATE PROCEDURE insert_employee(
    IN $name VARCHAR(254),
    IN $age INT,
    IN $career VARCHAR(254)
)
BEGIN
	INSERT INTO test    (name, age, career)
    VALUES              ($name, $age, $career)
    ;
END //

DELIMITER ;

-- 6

DROP PROCEDURE IF EXISTS delete_employee;

DELIMITER //

CREATE PROCEDURE delete_employee(
    IN $name VARCHAR(254),
    IN $age INT,
    IN $career VARCHAR(254)
)
BEGIN
	DELETE FROM test
    WHERE name = $name
    AND age = $age
    AND career = $career
    ;
END //

DELIMITER ;
