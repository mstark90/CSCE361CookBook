DROP TABLE IF EXISTS recipe_owners;
DROP TABLE IF EXISTS favorite_recipes;
DROP TABLE IF EXISTS recipe_ingredients;
DROP TABLE IF EXISTS recipes;
DROP TABLE IF EXISTS ingredients;
DROP TABLE IF EXISTS users;

CREATE TABLE users
    (user_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(70) NOT NULL,
    full_name VARCHAR(1024) NOT NULL,
    email_address VARCHAR(510) NOT NULL);

CREATE TABLE ingredients
    (ingredient_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     ingredient_name VARCHAR(255) NOT NULL,
     serving_size DOUBLE NOT NULL DEFAULT 0.0,
     measuring_units VARCHAR(20) NOT NULL,
     container_amount DOUBLE NOT NULL DEFAULT 0.0,
     retail_price DOUBLE NOT NULL DEFAULT 0.0);

CREATE TABLE recipes
    (recipe_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     recipe_name VARCHAR(255) NOT NULL,
     category VARCHAR(100) NOT NULL,
     image_url VARCHAR(2048) NOT NULL,
     description TEXT NOT NULL);

CREATE TABLE recipe_ingredients
    (recipe_ingredient_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     recipe_id BIGINT NOT NULL,
     ingredient_id BIGINT NOT NULL,
     FOREIGN KEY (recipe_id) REFERENCES recipes (recipe_id),
     FOREIGN KEY (ingredient_id) REFERENCES ingredients (ingredient_id));

CREATE TABLE favorite_recipes
    (favorite_recipe_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     user_id BIGINT NOT NULL,
     recipe_id BIGINT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users (user_id),
     FOREIGN KEY (recipe_id) REFERENCES recipes (recipe_id));

CREATE TABLE recipe_owners
    (recipe_owner_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     user_id BIGINT NOT NULL,
     recipe_id BIGINT NOT NULL,
     FOREIGN KEY (user_id) REFERENCES users (user_id),
     FOREIGN KEY (recipe_id) REFERENCES recipes (recipe_id));