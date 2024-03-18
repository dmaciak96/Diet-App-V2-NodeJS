create table if not exists shopping_list_product_entry
(
    id               uuid PRIMARY KEY unique,
    shopping_list_id uuid,
    product_id       uuid,
    amount_in_grams  double precision not null,
    foreign key (shopping_list_id) references shopping_list (id),
    foreign key (product_id) references product (id)
)