# Can squash broker, custoemr, client -> user + customer_properties tables.
USE freightmate_db;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS customer_properties;

CREATE TABLE IF NOT EXISTS user
(
    id                    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username              VARCHAR(100) NOT NULL UNIQUE,
    password              VARCHAR(255),
    email                 VARCHAR(255) NOT NULL,
    broker_service_email  VARCHAR(255),
    is_manifesting_active BOOLEAN               DEFAULT true NOT NULL,
    user_role             ENUM ('CUSTOMER','BROKER','CLIENT','ADMIN'),
    broker_id             INT,
    customer_id           INT,
    preferred_unit        ENUM('CM', 'M', 'MM'),
    token                 VARCHAR(512),
    token_created_at      DATETIME,
    is_deleted            BOOLEAN               DEFAULT false NOT NULL,
    deleted_at            DATETIME,
    created_at            DATETIME     NOT NULL DEFAULT NOW(),
    updated_at            DATETIME     NOT NULL DEFAULT NOW(),
    deleted_by            INT,
    created_by            INT          NOT NULL DEFAULT -1,
    updated_by            INT          NOT NULL DEFAULT -1,
    original_id           INT,
    original_user_type    VARCHAR(10)
);


CREATE TABLE IF NOT EXISTS customer_properties
(
    customer_id                    INT PRIMARY KEY NOT NULL,
    goods_pickup                   BOOLEAN,
    status                         BOOLEAN,
    shared_address                 BOOLEAN,
    dispatch_date_rollover         BOOLEAN,
    notification_email             BOOLEAN,
    service                        BOOLEAN,
    consolidation                  BOOLEAN,
    difot                          BOOLEAN,
    pickup_cut_off                 BOOLEAN,
    extended_labels                BOOLEAN,
    custom_email                   BOOLEAN,
    is_deleted                     BOOLEAN,
    item_type_bag                  BOOLEAN,
    item_type_carton               BOOLEAN,
    item_type_envelope             BOOLEAN,
    item_type_item                 BOOLEAN,
    item_type_stillage             BOOLEAN,
    item_type_pallet               BOOLEAN,
    item_type_chilled_pallet       BOOLEAN,
    item_type_frozen_pallet        BOOLEAN,
    item_type_1kg_satchel          BOOLEAN,
    item_type_3kg_satchel          BOOLEAN,
    item_type_5kg_satchel          BOOLEAN,
    item_type_3kg_satchel_label    BOOLEAN,
    item_type_500g_optical_satchel BOOLEAN,
    item_type_3kg_optical_satchel  BOOLEAN,
    item_type_skid                 BOOLEAN,
    item_type_bulka_bag            BOOLEAN,
    account_manager_id             INT(11)
);


INSERT INTO user (username,
                  password,
                  email,
                  broker_service_email,
                  user_role,
                  original_id,
                  original_user_type)
SELECT CASE WHEN COALESCE(c.username, cli.username) is not null THEN CONCAT(b.username, '_Broker') ELSE b.username END,
       b.password,
       b.bookings_email,
       b.service_email,
       'BROKER',
       b.id,
       'BROKER'
FROM freightmate_secure_login.broker b
         LEFT JOIN user u on b.username = u.username
         LEFT JOIN freightmate_secure_login.customers c on b.username = c.username
         LEFT JOIN freightmate_secure_login.clients cli on b.username = cli.username
WHERE u.id is null;

# SELECT *
# FROM user
# WHERE user_role = 'BROKER';

INSERT INTO user (username,
                  password,
                  email,
                  is_manifesting_active,
                  is_deleted,
                  user_role,
                  broker_id,
                  token,
                  original_id,
                  original_user_type)
SELECT c.username,
       c.password,
       c.email,
       COALESCE(c.active, false),
       c.del_flag,
       IF(c.admin = 1, 'ADMIN', 'CUSTOMER'),
       u2.id,
       c.`key`,
       c.id,
       'CUSTOMER'
FROM freightmate_secure_login.customers c
         LEFT JOIN user u on c.username = u.username
         LEFT JOIN freightmate_secure_login.broker b on c.broker_id = b.id
         LEFT JOIN user u2 on u2.username = b.username
WHERE u.id is null;


INSERT INTO customer_properties (customer_id,
                                 goods_pickup,
                                 status,
                                 shared_address,
                                 dispatch_date_rollover,
                                 notification_email,
                                 service,
                                 consolidation,
                                 difot,
                                 pickup_cut_off,
                                 extended_labels,
                                 custom_email,
                                 is_deleted,
                                 item_type_bag,
                                 item_type_carton,
                                 item_type_envelope,
                                 item_type_item,
                                 item_type_stillage,
                                 item_type_pallet,
                                 item_type_chilled_pallet,
                                 item_type_frozen_pallet,
                                 item_type_1kg_satchel,
                                 item_type_3kg_satchel,
                                 item_type_5kg_satchel,
                                 item_type_3kg_satchel_label,
                                 item_type_500g_optical_satchel,
                                 item_type_3kg_optical_satchel,
                                 item_type_skid,
                                 item_type_bulka_bag,
                                 account_manager_id)
SELECT u.id,
       c.goods_pickup,
       c.status,
       c.shared_address,
       c.dispatch_date_rollover,
       c.notification_email,
       c.service,
       c.consolidation,
       c.difot,
       c.pickup_cut_off,
       c.extended_labels,
       c.custom_email,
       c.del_flag,
       c.item_type_bag,
       c.item_type_carton,
       c.item_type_envelope,
       c.item_type_item,
       c.item_type_stillage,
       c.item_type_pallet,
       c.item_type_chilled_pallet,
       c.item_type_frozen_pallet,
       c.item_type_1kg_satchel,
       c.item_type_3kg_satchel,
       c.item_type_5kg_satchel,
       c.item_type_3kg_satchel_label,
       c.item_type_500g_optical_satchel,
       c.item_type_3kg_optical_satchel,
       c.item_type_skid,
       c.item_type_bulka_bag,
       c.account_manager_id
FROM freightmate_secure_login.customers c
         JOIN user u on u.username = c.username
WHERE u.user_role = 'CUSTOMER';

UPDATE user u
    JOIN freightmate_secure_login.clients c on u.username = c.username
SET u.username = CONCAT(u.username, '_Master')
WHERE u.user_role = 'CUSTOMER';

#
# SELECT *
# FROM user
# WHERE user_role = 'CUSTOMER';

INSERT INTO user (username,
                  password,
                  email,
                  is_manifesting_active,
                  is_deleted,
                  user_role,
                  broker_id,
                  customer_id,
                  preferred_unit,
                  token,
                  original_id,
                  original_user_type)
SELECT c.username,
       c.password,
       c.email,
       COALESCE(cus.active, false),
       c.del_flag,
       'CLIENT',
       u2.broker_id,
       u2.id,
       COALESCE(NULLIF(UPPER(c.preferred_units ),''), 'CM'),  # enpty strings and nulls are converted to M
       c.`key`,
       c.id,
       'CLIENT'
FROM freightmate_secure_login.clients c
         LEFT JOIN user u on c.username = u.username
         LEFT JOIN freightmate_secure_login.customers cus on c.customer_id = cus.id
         LEFT JOIN user u2 on cus.username = u2.username
WHERE u.id is null;

# Update client that has no customer_id and broker_id but they have valid reference in original
# Note: one client left due to this client has no valid customer_id reference
UPDATE user u
    INNER JOIN (
        SELECT us.id, cus.id customer_id, cus.broker_id
        FROM user us
                 INNER JOIN freightmate_secure_login.clients c
                            ON us.original_id = c.id
                 INNER JOIN freightmate_db.user cus
                            ON c.customer_id = cus.original_id
        WHERE us.user_role = 'CLIENT'
          AND us.broker_id IS NULL
          AND us.customer_id IS NULL
          AND cus.user_role = 'CUSTOMER') AS x ON u.id = x.id
SET u.customer_id = x.customer_id,
    u.broker_id   = x.broker_id
WHERE u.user_role = 'CLIENT'
  AND u.broker_id IS NULL
  AND u.customer_id IS NULL;

#
# SELECT *
# FROM user
# WHERE user_role = 'CLIENT';
#
# SELECT c.username AS duplicate_usernames
# FROM freightmate_secure_login.clients c
#          LEFT JOIN user u ON c.username = u.username
#     AND u.user_role = 'CLIENT'
#          LEFT JOIN freightmate_secure_login.customers cus on c.customer_id = cus.id
#          LEFT JOIN user u2 on cus.username = u2.username
# WHERE u.id is null;

# SELECT *
# FROM user u
#          JOIN user u2 on u.broker_id = u2.id
# WHERE u.user_role = 'CLIENT'
#   and u2.username = 'tuco';
#
#
#
# SELECT (SELECT count(DISTINCT username)
#         FROM freightmate_secure_login.customers) as original_customers,
#        (SELECT count(DISTINCT username)
#         FROM user
#         where user_role = 'CUSTOMER')            as customers,
#        (SELECT count(DISTINCT username)
#         FROM freightmate_secure_login.clients)   as original_clients,
#        (SELECT count(DISTINCT username)
#         FROM user
#         where user_role = 'CLIENT')              as clients,
#        (SELECT count(DISTINCT username)
#         FROM freightmate_secure_login.broker)    as original_brokers,
#        (SELECT count(DISTINCT username)
#         FROM user
#         where user_role = 'BROKER')              as brokers
# FROM dual;

# Move railroad user/pass to config

# UPDATE user set password = '$2y$12$PD9NqjjhkemdzQqFBdkkceoQkiH5ajKG/mw0uvsUC8tdu2MZgcEUu' WHERE username in ('kurtis', 'michelle','tuco');