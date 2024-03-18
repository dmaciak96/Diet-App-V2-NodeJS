create table if not exists recipe_product_entry
(
    id              uuid PRIMARY KEY unique,
    recipe_id       uuid,
    product_id      uuid,
    amount_in_grams double precision not null,
    foreign key (recipe_id) references recipe (id),
    foreign key (product_id) references product (id)
)