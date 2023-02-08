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
    user_iban           VARCHAR                                  ,
    description         VARCHAR                                  ,
    email_recipient     VARCHAR                                  ,
    FOREIGN KEY (user_email) REFERENCES users (email) ON delete CASCADE
);