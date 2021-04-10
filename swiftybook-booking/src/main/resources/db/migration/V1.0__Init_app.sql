
create sequence hibernate_sequence START WITH 100 INCREMENT BY 1;
create table bookings
(
    "id"                 bigint         not null,
    "created_date"       timestamp      not null,
    "last_modified_date" timestamp,
    "booked_from"          date            not null,
    "booked_to"            date            not null,
    "guest_number"         int               not null default 1,
    "status"             varchar(255)   not null,
    "total_price"        decimal(10, 2) not null,
    "place_id"           bigint,
    "payment_id"         bigint,
    "customer_id"        bigint
);
alter table bookings
    add constraint "bookings_pk" primary key ("id");
