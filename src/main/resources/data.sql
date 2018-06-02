
insert into user (id, first_name, last_name, username, password) values (1, 'Mohammad Zeyaul', 'Haque', 'mzhaque', '$2a$10$kz18T5oQd7fVgbLZq.2LAeeE6fwD20blKWSSODdTS9mzY63sJRley');
insert into user (id, first_name, last_name, username, password) values (2, 'Md Zeyaul', 'Haque', 'haque', '$2a$10$kz18T5oQd7fVgbLZq.2LAeeE6fwD20blKWSSODdTS9mzY63sJRley');

insert into user_role(id, role) values (1, 'USER');
insert into user_role(id, role) values (2, 'ADMIN');
insert into user_role(id, role) values (3, 'DBA');

insert into granted_authority(user_id, role_id) values (1, 2);
insert into granted_authority(user_id, role_id) values (2, 1);