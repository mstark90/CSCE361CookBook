/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.test;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.MeasuringUnits;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;
import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author jake
 */
public class RecipeDAOTest 
{
    
    public RecipeDAOTest() {
    }
    
    private ApplicationContext context;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() 
    {
        context = new ClassPathXmlApplicationContext("Beans.xml");
    }
    
    @After
    public void tearDown() 
    {
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        Recipe recipe = recipeDAO.getRecipeForName("TESTNAME");
        
        while (recipe != null)
        {
            recipeDAO.delete(recipe);
            
            recipe = recipeDAO.getRecipeForName("TESTNAME");
        }
    }
    
    Boolean enableTests = true;
    
    @Test
    public void generalRecipeTest()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        IngredientDAO ingredientDAO = (IngredientDAO) context.getBean("IngredientDAOImpl");
        
        Recipe recipe = new Recipe();
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add((Ingredient) ingredientDAO.findIngredient("Pizza", 0, 0).get(0));
        ingredients.add((Ingredient) ingredientDAO.findIngredient("Breakfast", 0, 0).get(0));
        
        recipe.setCategory("Breakfast");
        recipe.setDescription("Pizza + Breakfast");
        recipe.setRecipeName("Breakfast Pizza");
        recipe.setIngredients(ingredients);
        recipe.setImageUrl("");
        
        recipeDAO.create(recipe);
        
        assert(recipe.getRecipeId() > 0);
        
        Recipe testRecipe = (Recipe) recipeDAO.getRecipeForName("Breakfast Pizza");
        
        assert(testRecipe.getRecipeName().equals("Breakfast Pizza"));
        assert(testRecipe.getIngredients() != null || !testRecipe.getIngredients().isEmpty());
        
        testRecipe.setDescription("Pizza & Breakfast");
        recipeDAO.modify(testRecipe);
        
        Recipe testRecipe2 = (Recipe) recipeDAO.getRecipe(testRecipe.getRecipeId());
        
        assert(testRecipe2.getDescription().equals("Pizza & Breakfast"));
        
        recipeDAO.delete(testRecipe2);
        
        Recipe deleteRecipe;
        deleteRecipe = (Recipe) recipeDAO.getRecipeForName("Breakfast Pizza");
        
        while(deleteRecipe != null)
        {            
            recipeDAO.delete(deleteRecipe);
            deleteRecipe = (Recipe) recipeDAO.getRecipeForName("Breakfast Pizza");
        }
        
        deleteRecipe = (Recipe) recipeDAO.getRecipeForName("Breakfast Pizza");
        assert(deleteRecipe == null);
    }
    
    @Test
    public void testDataNotFound()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Recipe recipe = recipeDAO.getRecipeForName("notARecipe");
        
        assert(recipe == null);
    }
    
    @Test
    public void testCreateFromNullObject()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Recipe recipe = null;
        recipeDAO.create(recipe);
        
        //assert(program didn't break)
    }
    
    @Test
    public void testCreateFromObjectWithNullProperties1()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Ingredient ingr1 = new Ingredient();
        
        ingr1.setContainerAmount(0);
        ingr1.setIngredientName("TESTINGREDIENTNAME");
        ingr1.setMeasuringUnits(MeasuringUnits.POUNDS);
        ingr1.setRetailPrice(0);
        ingr1.setServingSize(0);
        
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingr1);
        
        Recipe recipe = new Recipe();
        //recipe.setCategory("TESTCATEGORY");
        recipe.setDescription("TESTDESCRIPTION");
        recipe.setImageUrl("TESTURL");
        recipe.setIngredients(ingredients);
        recipe.setRecipeName("TESTNAME");
        
        recipeDAO.create(recipe);
        
        recipe = recipeDAO.getRecipeForName("TESTNAME");
        assert(recipe == null);
    }
    
    @Test
    public void testCreateFromObjectWithNullProperties2()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Ingredient ingr1 = new Ingredient();
        
        ingr1.setContainerAmount(0);
        ingr1.setIngredientName("TESTINGREDIENTNAME");
        ingr1.setMeasuringUnits(MeasuringUnits.POUNDS);
        ingr1.setRetailPrice(0);
        ingr1.setServingSize(0);
        
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingr1);
        
        Recipe recipe = new Recipe();
        recipe.setCategory("TESTCATEGORY");
        //recipe.setDescription("TESTDESCRIPTION");
        recipe.setImageUrl("TESTURL");
        recipe.setIngredients(ingredients);
        recipe.setRecipeName("TESTNAME");
        
        recipeDAO.create(recipe);
        
        recipe = recipeDAO.getRecipeForName("TESTNAME");
        assert(recipe == null);
    }
    
    @Test
    public void testCreateFromObjectWithNullProperties3()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Ingredient ingr1 = new Ingredient();
        
        ingr1.setContainerAmount(0);
        ingr1.setIngredientName("TESTINGREDIENTNAME");
        ingr1.setMeasuringUnits(MeasuringUnits.POUNDS);
        ingr1.setRetailPrice(0);
        ingr1.setServingSize(0);
        
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingr1);
        
        Recipe recipe = new Recipe();
        recipe.setCategory("TESTCATEGORY");
        recipe.setDescription("TESTDESCRIPTION");
        //recipe.setImageUrl("TESTURL");
        recipe.setIngredients(ingredients);
        recipe.setRecipeName("TESTNAME");
        
        recipeDAO.create(recipe);
        
        recipe = recipeDAO.getRecipeForName("TESTNAME");
        assert(recipe == null);
    }
    
    @Test
    public void testCreateFromObjectWithNullProperties4()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Recipe recipe = new Recipe();
        recipe.setCategory("TESTCATEGORY");
        recipe.setDescription("TESTDESCRIPTION");
        recipe.setImageUrl("TESTURL");
        //recipe.setIngredients(ingredients);
        recipe.setRecipeName("TESTNAME");
        
        recipeDAO.create(recipe);
        
        recipe = recipeDAO.getRecipeForName("TESTNAME");
        assert(recipe == null);
    }
    
    @Test
    public void testCreateFromObjectWithNullProperties5()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        Ingredient ingr1 = new Ingredient();
        
        ingr1.setContainerAmount(0);
        ingr1.setIngredientName("TESTINGREDIENTNAME");
        ingr1.setMeasuringUnits(MeasuringUnits.POUNDS);
        ingr1.setRetailPrice(0);
        ingr1.setServingSize(0);
        
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingr1);
        
        Recipe recipe = new Recipe();
        recipe.setCategory("TESTCATEGORY");
        recipe.setDescription("TESTDESCRIPTION");
        recipe.setImageUrl("TESTURL");
        recipe.setIngredients(ingredients);
        //recipe.setRecipeName("TESTNAME");
        
        recipeDAO.create(recipe);
        
        recipe = recipeDAO.getRecipeForName("TESTNAME");
        assert(recipe == null);
    }
    
    @Test
    public void testCreateFromObjectWithEmptyIngredients()
    {
        if (!enableTests) return;
        
        RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        List<Ingredient> ingredients = new ArrayList<>();
        
        Recipe recipe = new Recipe();
        recipe.setCategory("TESTCATEGORY");
        recipe.setDescription("TESTDESCRIPTION");
        recipe.setImageUrl("TESTURL");
        recipe.setIngredients(ingredients);
        recipe.setRecipeName("TESTNAME");
        
        recipeDAO.create(recipe);
        
        recipe = recipeDAO.getRecipeForName("TESTNAME");
        assert(recipe != null);
        
        recipeDAO.delete(recipe);
    }
}
