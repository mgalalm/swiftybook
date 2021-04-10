CREATE SCHEMA IF NOT EXISTS place;

create sequence place.hibernate_sequence START WITH 100 INCREMENT BY 1;

create table place.categories
(
    "id"                 bigint       not null,
    "created_date"       timestamp    not null,
    "last_modified_date" timestamp,
    "description"        varchar(255) not null,
    "name"               varchar(255) not null
);
alter table place.categories
    add constraint "categories_pk" primary key ("id");

create table place.places
(
    "id"                 bigint         not null,
    "created_date"       timestamp      not null,
    "last_modified_date" timestamp,
    "available_from"     date           not null,
    "available_to"       date           not null,
    "description"        varchar(255)   not null,
    "title"               varchar(255)   not null,
    "price"              decimal(10, 2) not null,
    "status"             varchar(255)   not null,
    "address_1"          varchar(255),
    "address_2"          varchar(255),
    "city"               varchar(255),
    "country"            varchar(2),
    "postcode"           varchar(10),
    "image_url"          varchar(255),
    "category_id"        bigint
);
alter table place.places
    add constraint "place_pk" primary key ("id");

create table place.places_reviews
(
    "place_id" bigint not null,
    "reviews_id" bigint not null
);
alter table place.places_reviews
    add constraint "places_reviews_pk" primary key ("place_id", "reviews_id");

create table place.reviews
(
    "id"                 bigint       not null,
    "created_date"       timestamp    not null,
    "last_modified_date" timestamp,
    "description"        varchar(255) not null,
    "rating"             bigint       not null,
    "title"              varchar(255) not null
);
alter table place.places
    add constraint "place_fk" foreign key ("category_id") references place.categories ("id");

alter table place.reviews
    add constraint "review_pk" primary key ("id");

alter table place.places_reviews
    add constraint "places_reviews_uk" unique ("reviews_id");

alter table place.places_reviews
    add constraint "places_reviews_fk2" foreign key ("place_id") references place.places ("id");

alter table place.places_reviews
    add constraint "places_reviews_fk1" foreign key ("reviews_id") references place.reviews ("id");
