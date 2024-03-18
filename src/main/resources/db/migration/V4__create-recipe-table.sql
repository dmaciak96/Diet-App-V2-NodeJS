create table if not exists recipe
(
    id                  uuid PRIMARY KEY unique,
    application_user_id uuid         NOT NULL,
    name                varchar(255)  not null,
    description         varchar(2550) not null,
    type                varchar(255)  not null,
    foreign key (application_user_id) references application_user (id)
)