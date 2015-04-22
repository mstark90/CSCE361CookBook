/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.IngredientNutritionInformation;
import edu.unl.csce361.group4.cookbook.MeasuringUnits;
import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mstark
 */
@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeDAO recipeDAO;
    
    @Autowired
    private IngredientDAO ingredientDAO;
    
    private final SolrServer solrClient;

    private final static Logger logger = Logger.getLogger("RecipeController");
    
    public RecipeController() {
        solrClient = new HttpSolrServer("http://localhost:8983/solr/cookbook");
    }
    
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public List<Recipe> getRecipes(@RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "count", defaultValue = "10") long count) {
        return recipeDAO.getRecipesForCategory("", offset, count);
    }
    
    @RequestMapping(value = "category/{category}", method = RequestMethod.GET)
    public List<Recipe> getRecipesByCategory(@PathVariable(value = "category") String category,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "count", defaultValue = "10") long count) {
        return recipeDAO.getRecipesForCategory(category, offset, count);
    }

    @RequestMapping(value = "find", method = RequestMethod.GET)
    public List<Recipe> findRecipes(@RequestParam("query") String query,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "count", defaultValue = "10") long count) {
        List<Recipe> recipes = new LinkedList<>();
        
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("allContent:"+ query +"*");
            solrQuery.setStart((int)offset);
            solrQuery.setRows((int)count);
            
            QueryResponse response = solrClient.query(solrQuery);
            SolrDocumentList results = response.getResults();
            for (SolrDocument result : results) {
                recipes.add(recipeDAO.getRecipe(Long.parseLong(result.get("id").toString())));
            }
        } catch (SolrServerException e) {
            logger.log(Level.INFO, "Could not search the index:", e);
        }
        return recipes;
    }

    @RequestMapping(value = "createRecipe", method = RequestMethod.POST, consumes = "application/json")
    @PreAuthorize("isAuthenticated()")
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        recipeDAO.create(recipe);
        return recipe;
    }

    @RequestMapping(value = "modifyRecipe", method = RequestMethod.POST, consumes = "application/json")
    @PreAuthorize("isAuthenticated()")
    public Recipe modifyRecipe(@RequestBody Recipe recipe) {
        recipeDAO.modify(recipe);
        return recipe;
    }

    @RequestMapping(value = "deleteRecipe", method = RequestMethod.POST, consumes = "application/json")
    @PreAuthorize("isAuthenticated()")
    public Recipe deleteRecipe(@RequestBody Recipe recipe) {
        recipeDAO.delete(recipe);
        return recipe;
    }

    @RequestMapping(value = "get/name/{recipeName}", method = RequestMethod.GET)
    public Recipe getRecipe(@PathVariable("recipeName") String recipeName) {
        return recipeDAO.getRecipeForName(recipeName);
    }
    
    @RequestMapping(value = "get/id/{recipeId}", method = RequestMethod.GET)
    public Recipe getRecipeById(@PathVariable("recipeId") long id) {
        if(id == 0) {
            return null;
        }
        return recipeDAO.getRecipe(id);
    }
    
    @RequestMapping(value = "get/id/{recipeId}/nutritionInformation", method = RequestMethod.GET)
    public Map<String, Float> getNutritionInformation(@PathVariable("recipeId") long recipeId) {
        Map<String, Float> info = new HashMap<>();
        
        Recipe recipe = recipeDAO.getRecipe(recipeId);
        
        for(Ingredient ingredient : recipe.getIngredients()) {
            List<IngredientNutritionInformation> nutritionInformation = ingredientDAO.getNutritionInformation(ingredient.getIngredientId());
            for(IngredientNutritionInformation ingredientNutrition : nutritionInformation) {
                float amount = (float)ingredientNutrition.getNutrientAmount() / ingredientNutrition.getServingSize() * ingredient.getServingSize();
                info.put(ingredientNutrition.getNutrientName(), amount);
            }
        }
        
        return info;
    }
}
