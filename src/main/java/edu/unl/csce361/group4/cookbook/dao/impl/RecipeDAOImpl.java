/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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
        String sql = "SELECT * FROM recipes WHERE recipe_id = ?";
        
        Recipe recipe = (Recipe) dataSource.query(sql,
        				new Object[]{ recipeId }, 
        				new BeanPropertyRowMapper(Recipe.class)).get(0);
        
        return recipe;
    }

    @Override
    public void create(Recipe recipe) 
    {
    	//Add recipe to recipe table
    	String sql = "INSERT INTO recipes (recipe_name, description) VALUES (?, ?)";
        
        dataSource.update(sql, 
        	new Object[]
    		{
        		recipe.getRecipeName(),
        		recipe.getDescription()
    		});
        
    	//Get recipeID
        long recipeId = getRecipeForName(recipe.getRecipeName()).getRecipeId();
    	
    	//For each ingredient, create an entry in the recipe_ingredients table
    	for (Ingredient item : recipe.getIngredients())
    	{
    		sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id) VALUES (?, ?)";
    		
    		dataSource.update(sql, 
    			new Object[]
    			{
    				recipeId, 
    				item.getIngredientId()
    			});
    	}
    }

    @Override
    public void modify(Recipe recipe) 
    {
        //Modify recipe table
    	String sql = "UPDATE recipes "
    			+ "SET recipe_name = ?, description = ?, category = ?"
    			+ "WHERE recipeId = ?";
    	
    	dataSource.update(sql, 
    			new Object[]
    			{
    				recipe.getRecipeName(),
    				recipe.getDescription(),
    				recipe.getCategory(),
    				recipe.getRecipeId()
    			});
    	
    	//Modify join table to
    	for (Ingredient item : recipe.getIngredients())
    	{
    		sql = "SELECT favorite_recipe_id WHERE recipe_id = ? and ingredient_id = ?";
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
    	}
    }

    @Override
    public void delete(Recipe recipe) 
    {
    	//Delete all referencing entries in the join table
    	String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
    	
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
    	
    	//Delete recipe
    	sql = "DELETE FROM recipes WHERE recipe_id = ?";
    	
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Recipe> getRecipesForCategory(String category, long offset, long count) 
    {
    	String sql = "SELECT * FROM recipes where category = ?";
    	
    	return (List<Recipe>) dataSource.query(sql, 
    			new Object[]
    			{
    				category
    			},
    			new BeanPropertyRowMapper(Recipe.class));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Recipe getRecipeForName(String recipeName) 
    {
    	String sql = "SELECT * FROM recipes WHERE recipe_name = ?";
    	
    	return (Recipe) dataSource.query(sql, new Object[]{ recipeName }, new BeanPropertyRowMapper(Recipe.class)).get(0);
    	
    }
    
}
