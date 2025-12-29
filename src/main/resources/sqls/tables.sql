use localdb;

CREATE TABLE localdb.address
(
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    street   VARCHAR(255) NOT NULL,
    city     VARCHAR(255) NOT NULL,
    postcode VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE localdb.customer
(
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    dob        DATE         NOT NULL,
    PRIMARY KEY (id)
);
