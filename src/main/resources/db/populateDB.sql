delete from users;
delete from transactions;
delete from connections;

insert into users (firstName, lastName, email, password, balance)
values ('Thomas', 'Toto', 'thomas@gmail.com', '$2a$10$lr1QLlbssfqupUpJSCx.2up09FLJ372DesaZdwyn2QJ8s4suIwtxe', 5382.0),
       ('Bernard', 'Tata', 'bernard@gmail.com', '$2a$10$qIoAE2PvpjJZYhXKUuR48uXKxhIbVQGGTfinwtz2gsLuAUO3V23ny', 4111.0),
       ('George', 'Titi', 'george@gmail.com', '$2a$10$tTzSRYrlzgPJEIurP/SY9O4L5E2vniyjsyUT/iIq6OJXITffPcLN2', 2190.0);

insert into transactions (user_email, amount, email_recipient, description)
values ('george@gmail.com', 100, 'thomas@gmail.com', 'send to friend'),
       ('george@gmail.com', 200, 'bernard@gmail.com', 'send to friend');

insert into transactions (user_email, amount, user_iban, description)
values ('george@gmail.com', 100, 'FR89328902', 'top up balance'),
       ('bernard@gmail.com', 200, 'FR8392187', 'top up balance');

insert into connections (user_id, friend_id)
values (1000,1001),
       (1000,1002),
       (1001,1002);