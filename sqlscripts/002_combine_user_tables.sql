# Can squash broker, custoemr, client -> user + customer_properties tables.
USE freightmate;
## Inserting a system user which is a placeholder for data migration
INSERT INTO user (id, username, password, email, created_by, updated_by, is_deleted, deleted_by, deleted_at)
VALUES (0, 'system', 'THISISASYSTEMPLACEHOLDER', 'THISISASYSTEMPLACEHOLDER', 1, 1, 1, 1, NOW());
## Populate broker into user
INSERT INTO user (username,
                  password,
                  email,
                  original_id,
                  created_by,
                  updated_by)
SELECT CASE WHEN COALESCE(c.username, cli.username) is not null THEN CONCAT(b.username, '_Broker') ELSE b.username END,
       b.password,
       b.bookings_email,
       b.id,
       1,
       1
FROM freightmate_secure_login.broker b
         LEFT JOIN user u on b.username = u.username
         LEFT JOIN freightmate_secure_login.customers c on b.username = c.username
         LEFT JOIN freightmate_secure_login.clients cli on b.username = cli.username
WHERE u.id is null;
## Populate user broker
INSERT INTO user_broker (`user_id`,
                         `bookings_email`,
                         `service_email`,
                         `created_by`,
                         `updated_by`)
SELECT u.id,
       b.bookings_email,
       b.service_email,
       1,
       1
FROM freightmate_secure_login.broker b
         LEFT JOIN user u on b.id = u.original_id and b.username = u.username
         LEFT JOIN user_broker ub ON ub.user_id = u.id
WHERE ub.id is null
  AND u.id is not null;
## Populate customer into user
INSERT INTO user (username,
                  password,
                  email,
                  original_id,
                  is_admin,
                  created_by,
                  updated_by)
SELECT c.username,
       c.password,
       c.email,
       c.id,
       COALESCE(c.admin,0),
       1,
       1
FROM freightmate_secure_login.customers c
         LEFT JOIN user u on c.username = u.username
WHERE u.id is null;
## Populate the user customer
INSERT INTO user_customer (`user_id`,
                           `user_broker_id`,
                           `is_manifesting_active`,
                           `goods_pickup`,
                           `status`,
                           `shared_address`,
                           `dispatch_date_rollover`,
                           `notification_email`,
                           `service`,
                           `consolidation`,
                           `difot`,
                           `pickup_cut_off`,
                           `extended_labels`,
                           `created_by`,
                           `updated_by`)
SELECT u.id,
       ub.id,
       c.active,
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
       1,
       1
FROM freightmate_secure_login.customers c
         LEFT JOIN user u on c.id = u.original_id and c.username = u.username
         LEFT JOIN freightmate_secure_login.broker b on c.broker_id = b.id
         LEFT JOIN user u2 on b.id = u2.original_id and u2.username = b.username
         LEFT JOIN user_broker ub on u2.id = ub.user_id
         LEFT JOIN user_customer uc ON uc.user_id = u.id
WHERE uc.id is null
  AND u2.id is not null;
## Populate client into user
INSERT INTO user (username,
                  password,
                  email,
                  original_id,
                  preferred_unit,
                  created_by,
                  updated_by)
SELECT c.username,
       c.password,
       c.email,
       c.id,
       COALESCE(NULLIF(UPPER(c.preferred_units),''),'M'),
       1,
       1
FROM freightmate_secure_login.clients c
         LEFT JOIN user u on c.username = u.username
WHERE u.id is null
  AND NULLIF(c.email, '') is not null;
## Populate user_client
INSERT INTO user_client (`user_id`,
                         `user_customer_id`,
                         `created_by`,
                         `updated_by`)
SELECT u.id,
       ucus.id,
       1,
       1
FROM freightmate_secure_login.clients c
         LEFT JOIN user u on c.username = u.username and c.id = u.original_id
         LEFT JOIN freightmate_secure_login.customers cus on c.customer_id = cus.id
         LEFT JOIN user u2 on cus.id = u2.original_id AND cus.username = u2.username
         LEFT JOIN user_customer ucus on u2.id = ucus.user_id
         LEFT JOIN user_client uc on u.id = uc.user_id
WHERE uc.id is null
  AND u.id is not null
  AND ucus.id is not null;