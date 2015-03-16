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
        String sql = "SELECT * FROM User WHERE userName = ? and password = ?";
        
        return (User)dataSource.query(sql, new Object[]{}, new BeanPropertyRowMapper(Recipe.class)).get(0);
    }

    @Override
    public void create(User user) 
    {
    	String sql = "INSERT INTO User (userName, fullName, oAuthSecret, oAuthToken) VALUES (?, ?, ?, ?)";
    	
    	dataSource.update(sql, 
    			new Object[]
    			{
    				user.getUserName(),
    				user.getFullName(),
    				user.getOauthSecret(),
    				user.getOauthToken()    				
    			});
    	
    	for (Recipe favs : user.getFavoriteRecipes())
    	{
    		sql = "INSERT INTO FavoriteRecipeJoin (userId, recipeId) VALUES (?, ?)";
    		
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(User user) 
    {
    	//Delete FavoriteRecipes
        String sql = "DELETE FROM FavoriteRecipeJoin WHERE userId = ?";
        
        dataSource.update(sql, new Object[]{ user.getUserId() });
        
        //Delete User
        sql = "DELETE FROM User WHERE userId = ?";
        
        dataSource.update(sql, new Object[]{ user.getUserId() });
    }
}
