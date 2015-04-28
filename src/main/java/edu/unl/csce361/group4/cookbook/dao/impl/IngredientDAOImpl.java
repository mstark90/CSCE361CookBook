/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao.impl;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.IngredientNutritionInformation;
import edu.unl.csce361.group4.cookbook.MeasuringUnits;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
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
        if (name == null || name.isEmpty())
        {
            return null;
        }
        
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
        if (ingredient == null
               || ingredient.getIngredientName() == null || ingredient.getIngredientName().isEmpty()
               || ingredient.getMeasuringUnits() == null)               
        {
            return;
        }
        
        
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
        if (ingredients == null)
        {        
            return;
        }
        
        for (Ingredient ingr : ingredients)
        {
            this.create(ingr);
        }
    }

    @Override
    public void modify(Ingredient ingredient) 
    {
        if (ingredient == null
               || ingredient.getIngredientName() == null || ingredient.getIngredientName().isEmpty()
               || ingredient.getMeasuringUnits() == null)               
        {
            return;
        }
        
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
        if (ingredients == null)
        {
            return;
        }
        
    	for (Ingredient ingr : ingredients)
    	{
            this.modify(ingr);
    	}
    }

    @Override
    public void delete(Ingredient ingredient) 
    {
        if (ingredient == null || ingredient.getIngredientId() == 0)
        {
            return;
        }
        
        String sql = "DELETE FROM recipe_ingredients WHERE ingredient_id = ?";
        
        dataSource.update(sql, 
    		new Object[] 
    		{
                    ingredient.getIngredientId()
    		});
        
    	sql = "DELETE FROM ingredients WHERE ingredient_id = ?";
    	
    	dataSource.update(sql, 
    		new Object[] 
    		{
                    ingredient.getIngredientId()
    		});
    }

    @Override
    public void delete(List<Ingredient> ingredients) 
    {
        if (ingredients == null || ingredients.isEmpty())
        {
            return;
        }
        
    	for (Ingredient ingr : ingredients)
    	{
            this.delete(ingr);
    	}
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<Ingredient> getIngredients(List<Long> ingredientIds) 
    {
        if (ingredientIds == null || ingredientIds.isEmpty())
        {
            return null;
        }
        
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
    
    @Override
    public List<Ingredient> getIngredients() 
    {
        String sql = "SELECT * FROM ingredients";
        
    	List<Ingredient> ingredients = dataSource.query(sql, 
                    new BeanPropertyRowMapper(Ingredient.class));
    	
    	return ingredients;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Ingredient getIngredient(long ingredientId, long offset, long count)
    {
        if (ingredientId == 0)
        {
            return null;
        }
        
        String sql = "SELECT * FROM ingredients WHERE ingredient_id = ?";

        Ingredient ingr = (Ingredient) dataSource.queryForObject(sql, 
                new Object[]
                {
                    ingredientId
                },
                new BeanPropertyRowMapper(Ingredient.class));
    	
        if (ingr.getIngredientId() == 0)
        {
            return null;
        }
        
    	return ingr;
    }

    @Override
    public List<IngredientNutritionInformation> getNutritionInformation(long ingredientId) {
        return dataSource.query("SELECT * FROM nutrition_information WHERE ingredient_id = ?",
                new RowMapper<IngredientNutritionInformation>() {

            @Override
            public IngredientNutritionInformation mapRow(ResultSet rs, int rowNum) throws SQLException {
                IngredientNutritionInformation nutritionInformation = new IngredientNutritionInformation();
                
                nutritionInformation.setIngredientNutritionId(rs.getLong("nutrition_information_id"));
                nutritionInformation.setIngredientId(rs.getLong("ingredient_id"));
                nutritionInformation.setNutrientName(rs.getString("nutrient_name"));
                nutritionInformation.setNutrientAmount(rs.getFloat("nutrient_amount"));
                nutritionInformation.setServingSize(rs.getFloat("serving_size"));
                nutritionInformation.setUnits(MeasuringUnits.valueOf(rs.getString("units")));
                
                return nutritionInformation;
            }
        }, ingredientId);
    }

    @Override
    public void loadNutritionInformation(final List<IngredientNutritionInformation> nutritionInformation) {
        dataSource.batchUpdate("INSERT INTO nutrition_information SET ingredient_id = ?,"
                + " nutrient_name = ?, nutrient_amount = ?, serving_size = ?,"
                + " units = ?", new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                IngredientNutritionInformation info = nutritionInformation.get(i);
                
                ps.setLong(1, info.getIngredientId());
                ps.setString(2, info.getNutrientName());
                ps.setFloat(3, info.getNutrientAmount());
                ps.setFloat(4, info.getServingSize());
                ps.setString(5, info.getUnits().toString());
            }

            @Override
            public int getBatchSize() {
                return nutritionInformation.size();
            }
        });
    }
}
