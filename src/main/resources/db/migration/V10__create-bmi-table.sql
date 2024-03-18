create table if not exists bmi
(
    id         uuid PRIMARY KEY unique,
    application_user_id uuid         NOT NULL,
    weight     double precision not null,
    height     double precision not null,
    value      double precision not null,
    unit       varchar(255)     not null,
    rate       varchar(255)     not null,
    added_date date             not null,
    foreign key (application_user_id) references application_user (id)
)