-- user
insert into usr (email, name, password, location)
values ('ihor.sliunko.dev@gmail.com', 'Ihor', '$2a$10$yajlD/48aQ6lH/1sAQo4B.GN4ZS9e8gu.Ovk6gD.4tKgJ1QXc.mMy', 'Kyiv');
-- product (Iphone 14)
insert into product (user_id, title, description, location, price, added_at, preview_photo_file_name)
values (1, 'Iphone 14', 'Very cool smartphone!', 'Kyiv', 1000, now(), 'ddbab814-1d0b-4d03-bfd1-f5fad1eea32b.jpg');
insert into product_photo (product_id, photo_file_name)
values (1, '1984ffe5-4f3b-4a72-a9f6-340cbc9bcf34.jpg');
insert into product_photo (product_id, photo_file_name)
values (1, '25a48803-fc8e-47b9-a842-8fb41341d8ff.jpg');
-- product (PS5)
insert into product (user_id, title, description, location, price, added_at, preview_photo_file_name)
values (1, 'PS5', 'Good performance, cool gameplay!', 'Odesa', 500, now(), 'bf0f722b-b064-4709-9c70-180c2b5ae0dd.png');
insert into product_photo (product_id, photo_file_name)
values (2, 'cf536ffc-9140-48d9-bc5d-a6c74c7f6260.jpg');
insert into product_photo (product_id, photo_file_name)
values (2, '6cd6e750-799d-48d9-868b-0836dbc14ef3.png');
-- product (AirPods)
insert into product (user_id, title, description, location, price, added_at, preview_photo_file_name)
values (1, 'AirPods', 'Cool sound!', 'Dnipro', 500, now(), '236e6be6-b742-41b9-b510-43dfc555897e.jpg');
insert into product_photo (product_id, photo_file_name)
values (3, 'b1993544-03ad-42f9-8ae1-f5db4790e8bd.jpg');
insert into product_photo (product_id, photo_file_name)
values (3, '01a0ec6c-1fb1-44b2-a07a-d4eb3155f4b2.jpg');