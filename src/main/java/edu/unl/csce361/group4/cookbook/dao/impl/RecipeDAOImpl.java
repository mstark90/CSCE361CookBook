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
        String sql = "SELECT * FROM Recipe WHERE recipeId = ?";
        
        Recipe recipe = (Recipe) dataSource.query(sql,
        				new Object[]{ recipeId }, 
        				new BeanPropertyRowMapper(Recipe.class)).get(0);
        
        return recipe;
    }

    @Override
    public void create(Recipe recipe) 
    {
    	//Add recipe to recipe table
    	String sql = "INSERT INTO Recipe (recipeName, recipeDescription) VALUES (?, ?)";
        
        dataSource.update(sql, 
        	new Object[]
    		{
        		recipe.getRecipeName(),
        		recipe.getDescription(),
        		//ingredients?
    		});
        
    	//Get recipeID
        long recipeId = getRecipeForName(recipe.getRecipeName()).getRecipeId();
    	
    	//For each ingredient, create an entry in the ingredient_recipe_join table
    	for (Ingredient item : recipe.getIngredients())
    	{
    		sql = "INSERT INTO RecipeIngredientJoin (recipeId, ingredientId) VALUES (?, ?)";
    		
    		dataSource.update(sql, 
    			new Object[]
    			{
    				recipeId, item.getIngredientId()
    			});
    	}
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void modify(Recipe recipe) 
    {
    	//Depends on database table arrangement -- Cannot proceed
    	if (recipe != null) throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    	
        //Modify recipe table
    	String sql = "UPDATE Recipe "
    			+ "SET recipeName = ?, recipeDescription = ?"
    			+ "WHERE recipeId = ?";
    	
    	dataSource.update(sql, 
    			new Object[]
    			{
    				recipe.getRecipeName(),
    				recipe.getDescription(),
    				recipe.getRecipeId()
    			});
    	
    	//Modify join table to
    	for (Ingredient item : recipe.getIngredients())
    	{
    		sql = "SELECT * WHERE recipeId = ? and ingredientId = ?";
    		int sizeOfResult = dataSource.query(sql, 
    				new Object[]
    				{
    					recipe.getRecipeId(),
    					item.getIngredientId()
    				}, 
    				new BeanPropertyRowMapper(Recipe.class)).size();
    		
    		if (sizeOfResult == 0)
    		{
    			sql = "INSERT INTO IngredientRecipeJoin (recipeId, ingredientId) VALUES (?, ?)";
    			dataSource.update(sql, 
    					new Object[]
						{
    						recipe.getRecipeId(),
    						item.getIngredientId()
						});
    		}
    		else
    		{
    			
    		}
    	}
    }

    @Override
    public void delete(Recipe recipe) 
    {
    	//Delete all referencing entries in the join table
    	String sql = "DELETE FROM IngredientRecipeJoin WHERE recipeId = ?";
    	
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
    	
    	//Delete recipe
    	sql = "DELETE FROM Recipe WHERE recipeId = ?";
    	
    	dataSource.update(sql, new Object[]{ recipe.getRecipeId() });
    }

    @Override
    public List<Recipe> getRecipesForCategory(String category, long offset, long count) 
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Recipe getRecipeForName(String recipeName) 
    {
    	String sql = "SELECT * FROM Recipe WHERE recipeName = ?";
    	
    	return (Recipe) dataSource.query(sql, new Object[]{ recipeName }, new BeanPropertyRowMapper(Recipe.class)).get(0);
    	
    }
    
}
