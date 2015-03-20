/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.test;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.MeasuringUnits;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author jake
 */
public class IngredientDAOTest {

    public IngredientDAOTest() {
    }
    
    private ApplicationContext context;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        context = new ClassPathXmlApplicationContext("Beans.xml");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void generalIngredientTest() 
    {

        IngredientDAO ingredientDAO = (IngredientDAO) context.getBean("IngredientDAOImpl");

        Ingredient ingr = (Ingredient) ingredientDAO.findIngredient("Beer", 0, 0).get(0);

        assert(ingr.getIngredientId() > 0);
		
        ingredientDAO.delete(ingr);
        
        ingr.setContainerAmount((float) 0.3);
        ingr.setIngredientName("Beer");
        ingr.setMeasuringUnits(MeasuringUnits.POUNDS);
        ingr.setRetailPrice((float) 10.2);
        ingr.setServingSize(1);

        ingredientDAO.create(ingr);
        
        assert(ingr.getIngredientId() > 0);
        
        ingr.setContainerAmount((float) 0.7);
        
        ingredientDAO.modify(ingr);
        
        ingr.setContainerAmount(0); //To prevent the below assertion to not succeed if the next line does nothing
        
        ingr = (Ingredient) ingredientDAO.findIngredient("Beer", 0, 0).get(0);

        assert(ingr.getContainerAmount() > 0.6 && ingr.getContainerAmount() < 0.8);
    }
        
    public void testDataNotFound()
    {
        IngredientDAO ingredientDAO = (IngredientDAO)context.getBean("IngredientDAOImpl");
        List<Ingredient> ingredientList = ingredientDAO.findIngredient("tacoSauce", 0, 0);
        
        assert(ingredientList.isEmpty());
    }
    
    @Test
    public void testDemo()
    {
        IngredientDAO ingredientDAO = (IngredientDAO)context.getBean("IngredientDAOImpl");
        
        
        //CREATE INGREDIENT
        Ingredient ingr = new Ingredient();
        ingr.setIngredientName("TestIngredient");
        ingr.setMeasuringUnits(MeasuringUnits.POUNDS);
        ingr.setRetailPrice(1);
        ingr.setServingSize(1);
        
        ingredientDAO.create(ingr);
      
        assert(ingr.getIngredientId() != 0);
        
        
        //MODIFY INGREDIENT
        List<Ingredient> ingredients = ingredientDAO.findIngredient("TestIngredient", 0, 10);
        
        if (ingredients == null || ingredients.isEmpty())
            return;
        
        ingr = ingredients.get(0);
        ingr.setMeasuringUnits(MeasuringUnits.FLUID_OUNCES);
        
        ingredientDAO.modify(ingr);
        
        ingredients = ingredientDAO.findIngredient("TestIngredient", 0, 10);
        
        assert(ingredients != null);
        assert(ingredients.isEmpty() == false);
        
        ingr = ingredients.get(0);
        
        assert(ingr.getMeasuringUnits().equals(MeasuringUnits.FLUID_OUNCES));
        
        //DELETE INGGREDIENT
        ingredientDAO.delete(ingr);
        
        ingredients = ingredientDAO.findIngredient("TestIngredient", 0, 10);
        
        assert(ingredients == null || ingredients.isEmpty());
    }
}
