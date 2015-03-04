/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;
import java.util.List;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mstark
 */
@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private RecipeDAO recipeDAO;
    private SolrServer solrClient;
    
    public List<Recipe> findRecipes(String query, long offset, long count) {
        return null;
    }
    
    public Recipe createRecipe(Recipe recipe) {
        recipeDAO.create(recipe);
        return recipe;
    }
    
    public Recipe modifyRecipe(Recipe recipe) {
        recipeDAO.modify(recipe);
        return recipe;
    }
    
    public Recipe deleteRecipe(Recipe recipe) {
        recipeDAO.delete(recipe);
        return recipe;
    }
    
    public Recipe getRecipe(String recipeName) {
        return null;
    }
}
