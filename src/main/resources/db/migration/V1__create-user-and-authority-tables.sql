CREATE TABLE IF NOT EXISTS application_user
(
    id         uuid PRIMARY KEY UNIQUE,
    email      varchar(255) UNIQUE,
    password   varchar(255),
    first_name varchar(255),
    last_name  varchar(255),
    enabled    boolean,
    version    int,
    avatar     bytea
);

CREATE TABLE IF NOT EXISTS authority
(
    id                  uuid PRIMARY KEY UNIQUE,
    email               varchar(255) NOT NULL,
    application_user_id uuid         NOT NULL,
    role                varchar(255),
    foreign key (application_user_id) references application_user (id)
);
