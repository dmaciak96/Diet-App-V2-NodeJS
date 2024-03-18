create table if not exists shopping_list
(
    id                  uuid PRIMARY KEY unique,
    application_user_id uuid         NOT NULL,
    name                varchar(255) not null,
    foreign key (application_user_id) references application_user (id)
)