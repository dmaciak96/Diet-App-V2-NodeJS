create table if not exists scheduled_job
(
    id                    uuid PRIMARY KEY unique,
    application_user_id uuid         NOT NULL,
    send_to_email_address varchar(255) not null,
    creation_date         date         not null,
    schedule_type         varchar(255) not null,
    last_run_date         date,
    day_of_week           varchar(255),
    foreign key (application_user_id) references application_user (id)
)