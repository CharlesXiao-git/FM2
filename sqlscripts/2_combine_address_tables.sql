USE freightmate_db;

DROP TABLE IF EXISTS address;

CREATE TABLE IF NOT EXISTS address
(
    id             INT                        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    address_type   ENUM ('DELIVERY','SENDER') NOT NULL,
    customer_id    INT,
    client_id      INT,
    reference_id   VARCHAR(255),
    company_name   VARCHAR(255)               NOT NULL,
    address_line_1 VARCHAR(255)               NOT NULL,
    address_line_2 VARCHAR(255),
    town           VARCHAR(255)               NOT NULL,
    postcode       INT                 NOT NULL,
    country        VARCHAR(100)               NOT NULL DEFAULT 'Australia',
    state          VARCHAR(20)                NOT NULL,
    contact_name   VARCHAR(255)               NOT NULL,
    contact_no     VARCHAR(255),
    contact_email  VARCHAR(100),
    notes          VARCHAR(255),
    is_default     BOOLEAN                             DEFAULT false NOT NULL,
    count_used     INT,
    is_deleted     BOOLEAN                             DEFAULT false NOT NULL,
    deleted_at     DATETIME,
    created_at     DATETIME                   NOT NULL DEFAULT NOW(),
    updated_at     DATETIME                   NOT NULL DEFAULT NOW(),
    deleted_by     VARCHAR(100),
    created_by     VARCHAR(100)                        DEFAULT -1,
    updated_by     VARCHAR(100)                        DEFAULT -1,
    original_id    INT
);

# Insert delivery addresses for clients
INSERT INTO address (address_type, customer_id, client_id, reference_id, company_name, address_line_1, address_line_2,
                     town, postcode, country, state, contact_name, contact_no, contact_email, notes,
                     count_used, original_id)
SELECT 'DELIVERY',
       u.customer_id,
       u.id,
       a.address_ref_no,
       COALESCE(NULLIF(a.company, ''), a.name),      #set company name to contact name if null
       a.address_line_1,
       a.address_line_2,
       a.town,
       COALESCE(NULLIF(a.postcode, ''), 999),        #null postcode will be set to 999
       COALESCE(NULLIF(a.country, ''), 'Australia'), #null country will be defaulted to Australia
       COALESCE(NULLIF(a.state, ''), 'OTHER'),       #null state will be defaulted to OTHER
       COALESCE(NULLIF(a.name, ''), a.company),      #set contact name to company name if null
       a.phone_number,
       NULLIF(a.email, ''),
       a.special_instructions,
       a.count_times_used,
       a.id
FROM delivery_address a
         LEFT JOIN user u ON a.client_id = u.original_id
WHERE u.user_role = 'CLIENT'
  AND (COALESCE(a.company, '') <> '' OR COALESCE(a.name, '') <> '')
  AND (a.postcode REGEXP '[0-9]' OR COALESCE(a.postcode, '') = '');
# only get numeric postcode and exclude alphanumeric;

# Add index on address.original_id
CREATE INDEX idx_address_original_id ON address (original_id);

# Insert delivery addresses for customers
INSERT INTO address (address_type, customer_id, reference_id, company_name, address_line_1, address_line_2,
                     town, postcode, country, state, contact_name, contact_no, contact_email, notes,
                     count_used, original_id)
SELECT 'DELIVERY',
       u.id,
       a.address_ref_no,
       COALESCE(NULLIF(a.company, ''), a.name),      #set company name to contact name if null
       a.address_line_1,
       a.address_line_2,
       a.town,
       COALESCE(NULLIF(a.postcode, ''), 999),        #null postcode will be set to 999
       COALESCE(NULLIF(a.country, ''), 'Australia'), #null country will be defaulted to Australia
       COALESCE(NULLIF(a.state, ''), 'OTHER'),       #null state will be defaulted to OTHER
       COALESCE(NULLIF(a.name, ''), a.company),      #set contact name to company name if null
       a.phone_number,
       NULLIF(a.email, ''),
       a.special_instructions,
       a.count_times_used,
       a.id
FROM delivery_address a
         INNER JOIN user u ON a.customer_id = u.original_id
         LEFT JOIN address ad ON a.id = ad.original_id
WHERE u.user_role = 'CUSTOMER'
  AND (COALESCE(a.company, '') <> '' OR COALESCE(a.name, '') <> '')
  AND (a.postcode REGEXP '[0-9]' OR COALESCE(a.postcode, '') = '') # only get numeric postcode and exclude alphanumeric
  AND ad.id IS NULL;

# Insert sender_address
INSERT INTO address (address_type, client_id, company_name, address_line_1, address_line_2,
                     town, postcode, country, state, contact_name, contact_no, contact_email,
                     count_used, original_id)
select 'SENDER',
       u.id,
       COALESCE(NULLIF(s.company, ''), s.name),      #set company name to contact name if null
       s.address_line_1,
       s.address_line_2,
       s.town,
       COALESCE(NULLIF(s.postcode, ''), 999),        #null postcode will be set to 999
       COALESCE(NULLIF(s.country, ''), 'Australia'), #null country will be defaulted to Australia
       COALESCE(NULLIF(s.state, ''), 'OTHER'),       #null state will be defaulted to OTHER
       COALESCE(NULLIF(s.name, ''), s.company),      #set contact name to company name if null
       s.phone_number,
       s.email,
       s.count_times_used,
       s.id
FROM sender_address s
         INNER JOIN user u ON s.client_id = u.original_id
WHERE u.user_role = 'CLIENT'
  AND (COALESCE(s.company, '') <> '' OR COALESCE(s.name, '') <> '')
  AND (s.postcode REGEXP '[0-9]' OR COALESCE(s.postcode, '') = '');
# only get numeric postcode and exclude alphanumeric

# Check how many delivery_address records are not inserted
select *
from freightmate_db.delivery_address a
where not exists(
        select 1
        from freightmate_db.address x
        where a.id = x.original_id
          and x.address_type = 'DELIVERY'
    );

# Check how many sender_address records are not inserted
select *
from freightmate_db.sender_address a
where not exists(
        select 1
        from freightmate_db.address x
        where a.id = x.original_id
          and x.address_type = 'SENDER'
    );

