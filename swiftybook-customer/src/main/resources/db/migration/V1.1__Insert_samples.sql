INSERT INTO customers (id, created_date, last_modified_date, email, enabled, first_name, last_name, telephone)
VALUES  (1, current_timestamp, current_timestamp, 'a@yahho.com', true, 'Mohamed', 'Awad', '000-0000'),
        (2, current_timestamp, current_timestamp, 'a1@yahho.com', true, 'Dave', 'OBrein', '000-0000');
insert into payments
values  (1, current_timestamp, current_timestamp, 'somePaymentId', 'ACCEPTED', 999.00);