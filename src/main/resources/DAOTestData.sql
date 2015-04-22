# Delete test data
delete from ingredients where ingredient_name = "Beer" and ingredient_id != 0;
delete from ingredients where ingredient_name = "Pizza" and ingredient_id != 0;
delete from ingredients where ingredient_name = "Breakfast" and ingredient_id != 0;
delete from users where user_name = "jako" and user_id != 0;

# Add new test data
insert into ingredients 
	(ingredient_name, serving_size, measuring_units, container_amount, retail_price) 
	values 
	("Beer", 1, "POUNDS", 1, 1);

insert into ingredients 
	(ingredient_name, serving_size, measuring_units, container_amount, retail_price) 
	values 
	("Pizza", 1, "POUNDS", 1, 1);

insert into ingredients 
	(ingredient_name, serving_size, measuring_units, container_amount, retail_price) 
	values 
	("Breakfast", 1, "POUNDS", 1, 1);

insert into users
	(user_name, `password`, full_name, email_address)
	values
	("jako", "password", "j", "j@g.c");


