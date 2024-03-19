create table if not exists backup_metadata
(
    id                  uuid PRIMARY KEY unique,
    application_user_id uuid NOT NULL,
    creation_date       timestamptz NOT NULL,
    last_modified_date  timestamptz,
    version             int  NOT NULL,
    products            bytea,
    recipes             bytea,
    shopping_lists      bytea,
    bmi                 bytea,
    caloric_needs       bytea,
    foreign key (application_user_id) references application_user (id)
)