create table if not exists recipe_shopping_list
(
    id               uuid PRIMARY KEY unique,
    shopping_list_id uuid,
    recipe_id        uuid,
    foreign key (shopping_list_id) references shopping_list (id),
    foreign key (recipe_id) references recipe (id)
)