/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author mstark
 */
public class RecipeDAOImpl implements RecipeDAO {
    
    private JdbcTemplate dataSource;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new JdbcTemplate(dataSource);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Recipe getRecipe(long recipeId) 
    {
        if (recipeId == 0)
        {
            return null;
        }
        
        String sql = "SELECT * FROM recipes WHERE recipe_id = ?";
        
        List<Recipe> recipes = dataSource.query(sql,
                new Object[]
                { 
                    recipeId 
                }, new BeanPropertyRowMapper(Recipe.class));
        
        if (recipes.isEmpty())
            return null;
        
        Recipe recipe = recipes.get(0);
        
        sql = "SELECT ingredients.* FROM ingredients LEFT JOIN recipe_ingredients"
                + " ON recipe_ingredients.ingredient_id = ingredients.ingredient_id"
                + " WHERE recipe_id = ?";
        
        recipe.setIngredients(dataSource.query(sql, new Object[]{recipe.getRecipeId() }, new BeanPropertyRowMapper(Ingredient.class)));
        
        return recipe;
    }

    @Override
    public void create(final Recipe recipe) 
    {
        if (recipe == null 
                || recipe.getRecipeName() == null || recipe.getRecipeName().isEmpty()
                || recipe.getDescription() == null || recipe.getDescription().isEmpty()
                || recipe.getCategory() == null || recipe.getCategory().isEmpty()
                || recipe.getImageUrl() == null /*|| recipe.getImageUrl().isEmpty()*/
                || recipe.getIngredients() == null
                )
        {
            return;
        }
        
    	//Add recipe to recipe table
        KeyHolder holder = new GeneratedKeyHolder();
        
        dataSource.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                String sql = "INSERT INTO recipes (recipe_name, description, category, image_url) VALUES (?, ?, ?, ?)";
                PreparedStatement ps = cnctn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                ps.setString(1, recipe.getRecipeName());
                ps.setString(2, recipe.getDescription());
                ps.setString(3, recipe.getCategory());
                ps.setString(4, recipe.getImageUrl());

                return ps;
            }
        }, holder);
        
        recipe.setRecipeId(holder.getKey().longValue());
    	
    	//For each ingredient, create an entry in the recipe_ingredients table
    	for (final Ingredient ingredient : recipe.getIngredients())
    	{
            if(ingredient.getIngredientId() == 0) {
                holder = new GeneratedKeyHolder();
        
                dataSource.update(new PreparedStatementCreator() {

                    @Override
                    public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                        String sql = "INSERT INTO ingredients "
                                + "(ingredient_name, measuring_units, retail_price, serving_size, container_amount) "
                                + "VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement ps = cnctn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        ps.setString(1, ingredient.getIngredientName());
                        ps.setString(2, ingredient.getMeasuringUnits().toString());
                        ps.setFloat(3, ingredient.getRetailPrice());
                        ps.setFloat(4, ingredient.getServingSize());
                        ps.setFloat(5, ingredient.getContainerAmount());

                        return ps;
                    }
                }, holder);

                ingredient.setIngredientId(holder.getKey().longValue());
            }
            String sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id) VALUES (?, ?)";

            dataSource.update(sql, 
                    new Object[]
                    {
                            recipe.getRecipeId(), 
                            ingredient.getIngredientId()
                    });
    	}
    }

    @Override
    public void modify(Recipe recipe) //Not required for Phase 1
    {
        if (recipe == null 
                || recipe.getRecipeName() == null || recipe.getRecipeName().isEmpty()
                || recipe.getDescription() == null || recipe.getDescription().isEmpty()
                || recipe.getCategory() == null || recipe.getCategory().isEmpty()
                || recipe.getImageUrl() == null /*|| recipe.getImageUrl().isEmpty()*/
                /*|| recipe.getIngredients() == null /* If no ingredients specified, leave unmodified **/ 
                )
        {
            return;
        }
        
        //Modify recipe table
    	String sql = "UPDATE recipes "
    			+ "SET recipe_name = ?, description = ?, category = ?, image_url = ? "
    			+ "WHERE recipe_id = ?";
    	
    	dataSource.update(sql, 
    			new Object[]
    			{
    				recipe.getRecipeName(),
    				recipe.getDescription(),
    				recipe.getCategory(),
                                recipe.getImageUrl(),
    				recipe.getRecipeId()
    			});
    	
        if (recipe.getIngredients() == null) /* || recipe.getIngredients().isEmpty()) /* Have to be able to delete the list */
        {
            return; //Don't need to update ingredients
        }
        
        //TODO: Make this implementation better (doesn't delete all items and then re-add them)
        
        //Modify join table to
        for (Ingredient item : recipe.getIngredients())
    	{
            sql = "SELECT recipe_ingredient_id FROM recipe_ingredients WHERE recipe_id = ? and ingredient_id = ?";
            
            Long recipe_ingredient_id = (long)0;
            try
            {                   
                recipe_ingredient_id = dataSource.queryForObject(sql, 
                                new Object[]
                                {
                                        recipe.getRecipeId(),
                                        item.getIngredientId()
                                }, 
                                Long.class);
            } 
            catch (EmptyResultDataAccessException erdae){}

            if ( !(recipe_ingredient_id == null || recipe_ingredient_id == 0))
            {
                sql = "DELETE FROM recipe_ingredients where recipe_id = ?";
                dataSource.update(sql, 
                            new Object[]
                            {
                                recipe.getRecipeId()
                            });
            } 
            
            // Insert back into DB
            sql = "INSERT INTO recipe_ingredients "
                + "(recipe_id, ingredient_id) "
                + "VALUES "
                + "(?, ?)";
    			
            dataSource.update(sql, 
                            new Object[]
                            {
                                recipe.getRecipeId(),
                                item.getIngredientId()
                            });
        }
        
        /*
    	//Modify join table to
    	for (Ingredient item : recipe.getIngredients())
    	{
    		sql = "SELECT recipe_ingredient_id FROM recipe_ingredients WHERE recipe_id = ? and ingredient_id = ?";
    		Long recipe_ingredient_id = dataSource.queryForObject(sql, 
    				new Object[]
    				{
    					recipe.getRecipeId(),
    					item.getIngredientId()
    				}, 
    				Long.class);
    		
    		if (recipe_ingredient_id == null || recipe_ingredient_id == 0)
    		{
    			sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id) VALUES (?, ?)";
    			dataSource.update(sql, 
    					new Object[]
                                        {
                                            recipe.getRecipeId(),
                                            item.getIngredientId()
                                        });
    		}
    		else
    		{
    			sql = "UPDATE recipe_ingredients "
    					+ "SET recipe_id = ?, ingredient_id = ? "
    					+ "WHERE recipe_ingredient_id = ?";
    			
    			dataSource.update(sql, 
    					new Object[]
                                        {
                                            recipe.getRecipeId(),
                                            item.getIngredientId(),
                                            recipe_ingredient_id
                                        });
    		}
    	}*/
    }

    @Override
    public void delete(Recipe recipe) 
    {
        if (recipe == null
                || recipe.getRecipeId() == 0
                )
        {
            return;
        }
    	//Delete all referencing entries in the join table
    	String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
    	
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
        
        sql = "DELETE FROM favorite_recipes WHERE recipe_id = ?";
        
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
        
    	//Delete recipe
    	sql = "DELETE FROM recipes WHERE recipe_id = ?";
    	
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Recipe> getRecipesForCategory(String category, long offset, long count) 
    {
        if (category == null)
        {
            return null;
        }
        
    	String sql = "SELECT * FROM recipes where category = ? LIMIT ?, ?";
        if (category.isEmpty())
        {
            sql = "SELECT * FROM recipes LIMIT ?, ?";
            
            return (List<Recipe>) dataSource.query(sql,
                    new Object[]
    			{
                            offset,
                            count
    			},
    			new BeanPropertyRowMapper(Recipe.class));
        }

        return (List<Recipe>) dataSource.query(sql, 
    			new Object[]
    			{
                            category,
                            offset,
                            count
    			},
    			new BeanPropertyRowMapper(Recipe.class));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Recipe getRecipeForName(String recipeName) 
    {
        if (recipeName == null || recipeName.isEmpty())
        {
            return null;
        }
        
    	String sql = "SELECT * FROM recipes WHERE recipe_name = ?";
        
        List<Recipe> recipes = dataSource.query(sql,
                new Object[]
                { 
                    recipeName 
                }, new BeanPropertyRowMapper(Recipe.class));
        
        if (recipes.isEmpty())
            return null;
        
        Recipe recipe = recipes.get(0);
        
        sql = "SELECT ingredients.* FROM ingredients LEFT JOIN recipe_ingredients"
                + " ON recipe_ingredients.ingredient_id = ingredients.ingredient_id"
                + " WHERE recipe_id = ?";
        
        recipe.setIngredients(
                (List<Ingredient>)dataSource.query(
                        sql, 
                        new Object[]
                        {
                            recipe.getRecipeId() 
                        }, 
                        new BeanPropertyRowMapper(Ingredient.class)));
        
        return recipe;
    }
    
}
