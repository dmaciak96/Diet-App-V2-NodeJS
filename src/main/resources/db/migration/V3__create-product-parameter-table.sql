create table if not exists product_parameter
(
    id         uuid PRIMARY KEY unique,
    product_id uuid,
    name       varchar(255) not null,
    value      varchar(255) not null,
    foreign key (product_id) references product (id)
)