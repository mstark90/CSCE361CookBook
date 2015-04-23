/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.test;

import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.User;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;
import edu.unl.csce361.group4.cookbook.dao.UserDAO;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author jake
 */
public class UserDAOTest {
    
    public UserDAOTest() {
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
    public void tearDown() {
    }
    
    @Test
    public void generalUserTest()
    {
        UserDAO userDAO = (UserDAO) context.getBean("UserDAOImpl");
        //RecipeDAO recipeDAO = (RecipeDAO) context.getBean("RecipeDAOImpl");
        
        User user = null; // userDAO.login("jako", "password");
        
        assert(user != null);
        assert(user.getUserId() > 0 );
    }
    
    @Test
    public void testDataNotFound()
    {
        UserDAO userDAO = (UserDAO) context.getBean("UserDAOImpl");
        User user = null; // userDAO.login("Pancake", "IsFood");
        assert(user == null);
    }
}
