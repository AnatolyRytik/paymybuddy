delete from users;
delete from transactions;
delete from connections;
ALTER SEQUENCE global_seq RESTART WITH 1000;

insert into users (firstName, lastName, email, password, balance)
values ('Thomas', 'Toto', 'thomas@gmail.com', '$2a$10$bok3TkG4I7lys7D8Ut50YeIQsoBn1jb.Izq6K8enzbOog1mDz8sze', 5000.0),
       ('Bernard', 'Tata', 'bernard@gmail.com', '$2a$10$E0yBUZITUSRflwVyUERMnu0SaXLMwJ2yenduyamKhtopp1vU3jtEu', 4000.0),
       ('George', 'Titi', 'george@gmail.com', '$2a$10$V2Y.abPq9PoxorxewwZdeO7pXoZatPV9lyk11TwoICO/Z.kw4v1cm', 2000.0);

insert into transactions (user_email, amount, email_recipient, description)
values ('george@gmail.com', 100, 'thomas@gmail.com', 'send to friend'),
       ('george@gmail.com', 200, 'bernard@gmail.com', 'send to friend');

insert into transactions (user_email, amount, user_iban, description)
values ('george@gmail.com', 100, 'FR89328902', 'top up balance'),
       ('bernard@gmail.com', 200, 'FR8392187', 'top up balance');

insert into connections (user_id, friend_id)
values (1002,1001),
       (1002,1000),
       (1001,1000),
	   (1000,1001),
       (1000,1002);