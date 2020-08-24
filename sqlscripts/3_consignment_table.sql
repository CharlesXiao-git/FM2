USE freightmate;

## Check and rename the original consignment table
SELECT count(*)
INTO @exists
FROM information_schema.TABLES t1
WHERE t1.TABLE_SCHEMA = 'freightmate'
  AND t1.TABLE_TYPE = 'BASE TABLE'
  AND t1.TABLE_NAME = 'consignment'
  AND NOT EXISTS(SELECT 1
                 FROM information_schema.TABLES t2
                 WHERE t2.TABLE_SCHEMA = 'freightmate'
                   AND t2.TABLE_TYPE = 'BASE TABLE'
                   AND t2.TABLE_NAME = 'consignment_original'
    );

SET @query = IF(
        @`exists` > 0,
        'RENAME TABLE consignment TO consignment_original',
        'SELECT \'Nothing to rename\' rename_status'
    );

PREPARE statement from @query;
EXECUTE statement;

## Create new consignment table
DROP TABLE IF EXISTS consignment;

CREATE TABLE consignment
(
    id                       INT                              NOT NULL AUTO_INCREMENT PRIMARY KEY,
    owner_id                 INT                              NOT NULL,
    sender_address_id        INT                              NOT NULL,
    delivery_address_id      INT                              NOT NULL,
    connote_id               VARCHAR(100), # todo this field may need to be update to be not null
    dispatch_date_at         DATETIME,
    delivery_window_start_at DATETIME,
    delivery_window_end_at   DATETIME,
    address_class ENUM ('BUSINESS', 'RESIDENTIAL') NOT NULL,
    is_allowed_to_leave      BOOLEAN                                   DEFAULT FALSE NOT NULL,
    is_tailgate_required     BOOLEAN                                   DEFAULT FALSE NOT NULL,
    is_deleted               BOOLEAN                                   DEFAULT FALSE NOT NULL,
    deleted_at               DATETIME,
    created_at               DATETIME                         NOT NULL DEFAULT NOW(),
    updated_at               DATETIME                         NOT NULL DEFAULT NOW(),
    deleted_by               VARCHAR(100),
    created_by               VARCHAR(100)                              DEFAULT -1,
    updated_by               VARCHAR(100)                              DEFAULT -1,
    original_id              INT
);

## Insert the consignment data from the original table with the required fields
## delivery and sender address ID are set to -1 as a placeholder
INSERT INTO consignment (owner_id, sender_address_id, delivery_address_id, dispatch_date_at,
                         address_class, delivery_window_start_at, delivery_window_end_at, original_id)
SELECT u.id,
       -1,
       -1,
       NULLIF(co.dispatch_date, '0000-00-00'),
       IF(COALESCE(co.residential_address_charge, 0) > 0, 'RESIDENTIAL', 'BUSINESS'),
       NULLIF(co.delivery_window_begin, '0000-00-00 00:00:00'),
       NULLIF(co.delivery_window_end, '0000-00-00 00:00:00'),
       co.id
FROM consignment_original co
         INNER JOIN freightmate.user u ON co.client_id = u.original_id
WHERE u.user_role = 'CLIENT';
#only for client

## Update the consignment table to have the delivery address ID populated with the new address ID
UPDATE consignment c
    INNER JOIN (SELECT co.id consignment_id, co.delivery_id, a.id address_id
                FROM address a
                         INNER JOIN consignment_original co ON co.delivery_id = a.original_id
                WHERE a.address_type = 'DELIVERY'
                  AND is_deleted = FALSE) AS x ON c.original_id = x.consignment_id
SET c.delivery_address_id = x.address_id
WHERE c.delivery_address_id = -1;

## Update the consignment table to have the sender address ID populated with the new address ID
UPDATE consignment c
    INNER JOIN (SELECT co.id consignment_id, co.sender_id, a.id address_id
                FROM address a
                         INNER JOIN consignment_original co ON co.sender_id = a.original_id
                WHERE a.address_type = 'SENDER'
                  AND is_deleted = FALSE) AS x ON c.original_id = x.consignment_id
SET c.sender_address_id = x.address_id
WHERE c.sender_address_id = -1;

## Update the consignment table to remove any 0000-00-00 dates
UPDATE consignment c
SET c.delivery_window_end_at = NULL
WHERE YEAR(c.delivery_window_end_at) = 0;
UPDATE consignment c
SET c.delivery_window_start_at = NULL
WHERE YEAR(c.delivery_window_start_at) = 0;

## Check how many consignments having delivery address ID not updated
select *
from consignment
where delivery_address_id = -1;

## Check how many consignments having sender address ID not updated
select *
from consignment
where sender_address_id = -1;

## Create index on the consignment.original_id
CREATE INDEX idx_consignment_original_id ON consignment (original_id);

## Check how many consignments that are not migrated
select *
from consignment_original co
where not exists(select 1
                 from consignment c
                 where c.original_id = co.id);