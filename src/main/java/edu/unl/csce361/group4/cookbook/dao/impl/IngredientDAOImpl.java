/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.MeasuringUnits;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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
    	String sql = "SELECT * FROM Ingredients WHERE Name = ?";
    	
		List<Ingredient> ingredients = dataSource.query(sql, new Object[]{name}, new BeanPropertyRowMapper(Ingredient.class));
    	
    	return ingredients;
    }

    @Override
    public void create(Ingredient ingredient) 
    {
        String sql = "INSERT INTO Ingredient "
        		+ "(ingredientName, measuringUnits, retailPrice, servingSize, containerAmount) "
        		+ "VALUES (?, ?, ?, ?, ?)";
        
        dataSource.update(sql, new Object[]
        		{
        			ingredient.getIngredientName(), 
        			ingredient.getMeasuringUnits(),
        			ingredient.getRetailPrice(),
        			ingredient.getServingSize(),
        			ingredient.getContainerAmount()
        		});
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
    	String sql = "UPDATE Ingredient SET ingredientName = ?, measuringUnits = ?, "
    			+ "retailPrice = ?, servingSize = ?, containerAmount = ? "
    			+ "WHERE ingredientId = ?";
    	
    	dataSource.update(sql, new Object[]
    		{
    			ingredient.getIngredientName(),
    			ingredient.getMeasuringUnits(),
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
    	String sql = "DELETE FROM Ingredient WHERE `ingredientId` = ?";
    	
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
    	List<Ingredient> ingredients = new ArrayList<Ingredient>();
    	
    	for (Long id : ingredientIds)
    	{
    		String sql = "SELECT * FROM Ingredient WHERE `ingredientId` = ?";
    		
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
