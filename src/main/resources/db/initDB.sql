create DATABASE payme;
\c payme;

drop table IF EXISTS transactions;
drop table IF EXISTS connections;
drop table IF EXISTS users CASCADE;
drop sequence IF EXISTS global_seq;

create sequence global_seq start with 100000;

create TABLE IF NOT EXISTS users
(
    id               BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    firstname        VARCHAR                           NOT NULL,
    lastname         VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    balance          DOUBLE PRECISION                 DEFAULT 0.00,
    registered       TIMESTAMP                   DEFAULT now()
);
create unique index users_unique_email_idx on users (email);

create TABLE IF NOT EXISTS connections
(
     id                      BIGINT    PRIMARY KEY DEFAULT nextval('global_seq'),
     user_id                 BIGINT    NOT NULL,
     friend_id               BIGINT    NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users(id) ON delete CASCADE,
	 FOREIGN KEY (friend_id) REFERENCES users(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS transactions
(
    id                  BIGINT PRIMARY KEY DEFAULT nextval('global_seq'),
    user_email          VARCHAR                                  NOT NULL,
    date_time           TIMESTAMP                           DEFAULT now(),
    amount              DOUBLE PRECISION                         NOT NULL,
    user_iban            VARCHAR                                  ,
    email_recipient     VARCHAR                                  ,
    FOREIGN KEY (user_email) REFERENCES users (email) ON delete CASCADE
);