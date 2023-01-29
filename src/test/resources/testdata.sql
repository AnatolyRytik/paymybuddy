create TABLE IF NOT EXISTS users
(
    id               INTEGER PRIMARY KEY AUTO_INCREMENT,
    firstname        VARCHAR                           NOT NULL,
    lastname         VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    balance          NUMERIC(20,2)               DEFAULT 0.00  ,
    registered       TIMESTAMP                   DEFAULT NOW()
);
create unique index users_unique_email_idx on users (email);

create TABLE IF NOT EXISTS connections
(
     id                      INTEGER    PRIMARY KEY AUTO_INCREMENT,
     user_id                 INTEGER    NOT NULL,
     friend_id               INTEGER    NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id) ON delete CASCADE,
	 FOREIGN KEY (friend_id) REFERENCES users(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS transactions
(
    id                  INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_email          VARCHAR                                  NOT NULL,
    date_time           TIMESTAMP                           DEFAULT NOW(),
    amount              NUMERIC(10,2)                            NOT NULL,
    userIban            VARCHAR                                  ,
    email_recipient     VARCHAR                                  ,
    FOREIGN KEY (user_email) REFERENCES users (email) ON delete CASCADE
);

insert into users (firstName, lastName, email, password, balance)
values ('Thomas', 'Toto', 'thomas@gmail.com', '$2a$10$lr1QLlbssfqupUpJSCx.2up09FLJ372DesaZdwyn2QJ8s4suIwtxe', 5382.0),
       ('Bernard', 'Tata', 'bernard@gmail.com', '$2a$10$qIoAE2PvpjJZYhXKUuR48uXKxhIbVQGGTfinwtz2gsLuAUO3V23ny', 4111.0),
       ('George', 'Titi', 'george@gmail.com', '$2a$10$tTzSRYrlzgPJEIurP/SY9O4L5E2vniyjsyUT/iIq6OJXITffPcLN2', 2190.0);

insert into transactions (user_email, amount, email_recipient)
values ('george@gmail.com', 100, 'thomas@gmail.com'),
       ('george@gmail.com', 200, 'bernard@gmail.com');

insert into transactions (user_email, amount, userIban)
values ('george@gmail.com', 100, 'FR89328902'),
       ('bernard@gmail.com', 200, 'FR8392187');

insert into connections (user_id, friend_id)
values (100000,100001),
       (100000,100002),
       (100001,100002);