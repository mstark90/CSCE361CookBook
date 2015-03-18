/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.User;
import edu.unl.csce361.group4.cookbook.dao.UserDAO;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author mstark
 */
public class UserDAOImpl implements UserDAO {
    private JdbcTemplate dataSource;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new JdbcTemplate(dataSource);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public User login(String userName, String password) 
    {
        String sql = "SELECT * FROM users WHERE user_name = ? and password = ?";
        
        return (User)dataSource.query(sql,
        		new Object[]
        		{
        			userName,
        			password
        		},
        		new BeanPropertyRowMapper(Recipe.class)).get(0);
    }

    @Override
    public void create(User user) 
    {
    	String sql = "INSERT INTO users (user_name, password, full_name) VALUES (?, ?, ?)";
    	
    	dataSource.update(sql, 
    			new Object[]
    			{
    				user.getUserName(),
    				user.getPassword(), //??
    				user.getFullName(),
    			});
    	
    	for (Recipe favs : user.getFavoriteRecipes())
    	{
    		sql = "INSERT INTO favorite_recipes (user_id, recipe_id) VALUES (?, ?)";
    		
    		dataSource.update(sql, 
    				new Object[]
    				{
    					user.getUserId(),
    					favs.getRecipeId()
    				});
    	}
    }

    @Override
    public void modify(User user)
    {
    	//Update favorite_recipes
    	String sql = "";
    	
    	for (Recipe item : user.getFavoriteRecipes())
    	{
    		sql = "SELECT favorite_recipe_id FROM favorite_recipes WHERE user_id = ? and recipe_id = ?";
    		
    		long favorite_recipe_id = dataSource.queryForObject(sql, 
    				new Object[]
    				{
    					user.getUserId(),
    					item.getRecipeId()
    				},
    				Long.class);
    		
    		if (favorite_recipe_id == 0)
    		{
    			sql = "INSERT INTO favorite_recipes (user_id, recipe_id) VALUES (?, ?)";
    			
    			dataSource.update(sql, 
    					new Object[]
    					{
    						user.getUserId(),
    						item.getRecipeId()
    					});
    		}
    		else
    		{
    			sql = "UPDATE favorite_recipes "
        				+ "SET user_id = ? "
        				+ "recipe_id = ? "
        				+ "WHERE favorite_recipe_id = ?";
    			
    			dataSource.update(sql,
    					new Object[]
						{
    						user.getUserId(),
    						item.getRecipeId(),
    						favorite_recipe_id
						});
    		}
    	}
    	
    	//Update user entry
    	sql = "UPDATE users "
    			+ "SET user_name = ? "
    			+ "password = ? "
    			+ "full_name = ? "
    			+ "WHERE user_id = ?";
    	
    	dataSource.update(sql,
    			new Object[]
    			{
    				user.getUserName(),
    				user.getPassword(), //??
    				user.getFullName(),
    				user.getUserId()
    			});
    	
    	user.getFavoriteRecipes();
    }

    @Override
    public void delete(User user) 
    {
    	//Delete favorite_recipes
        String sql = "DELETE FROM favorite_recipes WHERE user_id = ?";
        
        dataSource.update(sql, new Object[]{ user.getUserId() });
        
        //Delete recipe_owners recipes
        sql = "DELETE FROM recipe_owners WHERE user_id = ?";
        
        dataSource.update(sql, new Object[]{ user.getUserId() });
        
        //Delete user's recipes
        sql = "DELETE FROM recipes WHERE user_id = ?";
        
        dataSource.update(sql, new Object[]{ user.getUserId() });
        
        //Delete user
        sql = "DELETE FROM users WHERE user_id = ?";
        
        dataSource.update(sql, new Object[]{ user.getUserId() });
    }
}
