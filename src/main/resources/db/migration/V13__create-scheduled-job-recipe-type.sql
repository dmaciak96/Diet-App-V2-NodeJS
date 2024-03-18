create table if not exists scheduled_job_recipe_type
(
    id               uuid PRIMARY KEY unique,
    scheduled_job_id uuid,
    recipe_type      varchar(255) not null,
    foreign key (scheduled_job_id) references scheduled_job (id)
)