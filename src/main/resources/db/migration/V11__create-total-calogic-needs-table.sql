create table if not exists total_caloric_needs
(
    id             uuid PRIMARY KEY unique,
    application_user_id uuid         NOT NULL,
    weight         double precision not null,
    height         double precision not null,
    value          double precision not null,
    unit           varchar(255)     not null,
    gender         varchar(255)     not null,
    added_date     date             not null,
    activity_level varchar(255)     not null,
    age            int              not null,
    foreign key (application_user_id) references application_user (id)
)