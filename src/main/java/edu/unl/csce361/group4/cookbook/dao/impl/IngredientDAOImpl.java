/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 *
 * @author mstark
 */
public class IngredientDAOImpl implements IngredientDAO {
    
    private JdbcTemplate dataSource;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = new JdbcTemplate(dataSource);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public List<Ingredient> findIngredient(String name, long offset, long count) 
    {
    	String sql = "SELECT * FROM ingredients WHERE ingredient_name = ?";
    	
        List<Ingredient> ingredients = dataSource.query(sql, 
                new Object[]
                {
                    name
                }, 
                new BeanPropertyRowMapper(Ingredient.class));
    	
    	return ingredients;
    }

    @Override
    public void create(final Ingredient ingredient) 
    {
        KeyHolder holder = new GeneratedKeyHolder();
        
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

    @Override
    public void create(List<Ingredient> ingredients) 
    {
        for (Ingredient ingr : ingredients)
        {
        	this.create(ingr);
        }
    }

    @Override
    public void modify(Ingredient ingredient) 
    {
    	String sql = "UPDATE ingredients SET ingredient_name = ?, measuring_units = ?, "
    			+ "retail_price = ?, serving_size = ?, container_amount = ? "
    			+ "WHERE ingredient_id = ?";
    	
    	dataSource.update(sql, new Object[]
    		{
    			ingredient.getIngredientName(),
    			ingredient.getMeasuringUnits().toString(),
    			ingredient.getRetailPrice(),
    			ingredient.getServingSize(),
    			ingredient.getContainerAmount(),
    			
    			ingredient.getIngredientId()
    		});
    }

    @Override
    public void modify(List<Ingredient> ingredients) 
    {
    	for (Ingredient ingr : ingredients)
    	{
    		this.modify(ingr);
    	}
    }

    @Override
    public void delete(Ingredient ingredient) 
    {
    	String sql = "DELETE FROM ingredients WHERE ingredient_id = ?";
    	
    	dataSource.update(sql, 
    		new Object[] 
    		{
    			ingredient.getIngredientId()
    		});
    }

    @Override
    public void delete(List<Ingredient> ingredients) 
    {
    	for (Ingredient ingr : ingredients)
    	{
    		this.delete(ingr);
    	}
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public List<Ingredient> getIngredients(List<Long> ingredientIds) 
    {
    	List<Ingredient> ingredients = new ArrayList<>();
    	
    	for (Long id : ingredientIds)
    	{
    		String sql = "SELECT * FROM ingredients WHERE ingredient_id = ?";
    		
    		ingredients.add(
    			(Ingredient) dataSource.query(sql, 
    			new Object[]
    			{
    				id
    			},
    			new BeanPropertyRowMapper(Ingredient.class)).get(0));
    	}
    	
    	return ingredients;
    }
}
