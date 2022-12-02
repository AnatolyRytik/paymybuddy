create DATABASE payme;
\c payme;

drop table IF EXISTS accounts;
drop table IF EXISTS transactions;
drop table IF EXISTS connections;
drop table IF EXISTS users CASCADE;
drop sequence IF EXISTS global_seq;

create sequence global_seq start with 100000;

create TABLE IF NOT EXISTS users
(
    id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    firstname        VARCHAR                           NOT NULL,
    lastname         VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT now() NOT NULL
);
create unique index users_unique_email_idx on users (email);

create TABLE IF NOT EXISTS connections
(
     id                      INTEGER    PRIMARY KEY DEFAULT nextval('global_seq'),
     user_id                 INTEGER    NOT NULL,
     friend_id               INTEGER    NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id) ON delete CASCADE,
	 FOREIGN KEY (friend_id) REFERENCES users(id) ON delete CASCADE
);
create TABLE IF NOT EXISTS accounts
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    user_email  VARCHAR                                  NOT NULL,
    balance     NUMERIC(20,2)                            NOT NULL,
    FOREIGN KEY (user_email) REFERENCES users (email) ON delete CASCADE
);

create TABLE IF NOT EXISTS transactions
(
    id                  INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
    user_email          VARCHAR                                  NOT NULL,
    date_time           TIMESTAMP                  DEFAULT now() NOT NULL,
    amount              NUMERIC(10,2)                            NOT NULL,
    email_recipient     VARCHAR                                  NOT NULL,
    FOREIGN KEY (user_email) REFERENCES users (email) ON delete CASCADE
);


insert into users (firstName, lastName, email, password)
values ('Thomas', 'Toto', 'thomas@gmail.com', 'password'),
       ('Bernard', 'Tata', 'bernard@gmail.com', 'admin'),
       ('George', 'Titi', 'george@gmail.com', 'george');


insert into accounts (user_email, balance)
values ('thomas@gmail.com',  1000000),
       ('bernard@gmail.com',  2200000),
       ('george@gmail.com',  9900500);

insert into transactions (user_email, amount, email_recipient)
values ('george@gmail.com', 100, 'thomas@gmail.com'),
       ('george@gmail.com', 200, 'bernard@gmail.com');

insert into connections (user_id, friend_id)
values (100000,100001),
       (100000,100002),
       (100001,100002);