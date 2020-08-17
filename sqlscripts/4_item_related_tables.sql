USE freightmate_db;

## Create Consignment Item table
DROP TABLE IF EXISTS item;

CREATE TABLE item
(
    id             INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    consignment_id INT      NOT NULL,
    quantity       INT      NOT NULL,
    item_type_id   INT      NOT NULL,
    length         FLOAT    NOT NULL,
    width          FLOAT    NOT NULL,
    height         FLOAT    NOT NULL,
    weight         FLOAT    NOT NULL,
    total_weight   FLOAT    NOT NULL,
    volume         FLOAT    NOT NULL,
    is_hazardous   BOOLEAN           DEFAULT FALSE NOT NULL,
    is_deleted     BOOLEAN           DEFAULT FALSE NOT NULL,
    deleted_at     DATETIME,
    created_at     DATETIME NOT NULL DEFAULT NOW(),
    updated_at     DATETIME NOT NULL DEFAULT NOW(),
    deleted_by     VARCHAR(100),
    created_by     VARCHAR(100)      DEFAULT -1,
    updated_by     VARCHAR(100)      DEFAULT -1
);

## Create Item Type table

DROP TABLE IF EXISTS item_type;

CREATE TABLE item_type
(
    id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    quantity   INT,
    length     FLOAT,
    width      FLOAT,
    height     FLOAT,
    weight     FLOAT,
    is_mutable BOOLEAN DEFAULT TRUE,
    is_custom  BOOLEAN DEFAULT FALSE,
    is_deleted     BOOLEAN           DEFAULT FALSE NOT NULL,
    deleted_at     DATETIME,
    created_at     DATETIME NOT NULL DEFAULT NOW(),
    updated_at     DATETIME NOT NULL DEFAULT NOW(),
    deleted_by     VARCHAR(100),
    created_by     VARCHAR(100)      DEFAULT -1,
    updated_by     VARCHAR(100)      DEFAULT -1
);

# Insert the default item types without prefill value
INSERT INTO item_type (name)
VALUES ('Bag');
INSERT INTO item_type (name)
VALUES ('Carton');
INSERT INTO item_type (name)
VALUES ('Envelope');
INSERT INTO item_type (name)
VALUES ('Item');
INSERT INTO item_type (name)
VALUES ('Stillage');
INSERT INTO item_type (name)
VALUES ('Pallet');
INSERT INTO item_type (name)
VALUES ('Chilled Pallet');
INSERT INTO item_type (name)
VALUES ('Frozen Pallet');
INSERT INTO item_type (name)
VALUES ('Skid');
INSERT INTO item_type (name)
VALUES ('Bulka Bag');

#Insert the default item types with prefill value
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('1kg Satchel', 1, 0.3, 0.2, 0.05, 1, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('3kg Satchel', 1, 0.35, 0.25, 0.05, 3, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('5kg Satchel', 1, 0.4, 0.3, 0.05, 5, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('10kg Satchel', 1, 0.43, 0.38, 0.3, 10, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('20kg Satchel', 1, 0.46, 0.41, 0.42, 20, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('3kg Satchel Label', 1, 0.35, 0.25, 0.05, 3, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('500g Satchel Label', 1, 0.25, 0.15, 0.05, 0.5, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('3kg Optical Label', 1, 0.35, 0.25, 0.05, 3, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('Semi', 1, 1.32, 0.24, 0.24, NULL, FALSE);
INSERT INTO item_type (name, quantity, length, width, height, weight, is_mutable)
VALUES ('BDouble', 1, 2.0, 0.24, 0.24, NULL, FALSE);
