create table message
(
    id         bigserial primary key,
    body       varchar(2000) not null,
    sent_at    timestamp(6),
    product_id bigint references product (id),
    user_id    bigint references usr (id)
);