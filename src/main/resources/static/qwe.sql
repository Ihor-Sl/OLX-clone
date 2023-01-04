create table usr
(
    id                    bigserial primary key,
    email                 varchar(255) not null unique,
    name                  varchar(100) not null unique,
    password              varchar(255) not null,
    location              varchar(255),
    last_visit            timestamp(6),
    is_account_non_locked boolean      not null default true,
    is_deleted            boolean      not null default false
);
create table product
(
    id                      bigserial primary key,
    user_id                 bigint references usr (id),
    title                   varchar(100) not null,
    description             varchar(2000),
    location                varchar(255) not null,
    price                   bigint       not null,
    added_at                timestamp(6) not null default now(),
    preview_photo_file_name varchar(255) not null unique,
    is_closed               boolean      not null default false
);
create table product_photo
(
    id              bigserial primary key,
    product_id      bigint references product (id),
    photo_file_name varchar(255) unique
);
create table user_role
(
    user_id bigint references usr (id) not null,
    roles   varchar(255)
);