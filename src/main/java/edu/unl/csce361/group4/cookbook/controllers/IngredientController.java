/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import java.util.List;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mstark
 */
@RestController
@RequestMapping("/ingredients/")
public class IngredientController {
    private IngredientDAO ingredientDAO;
    private SolrServer solrClient;
    
    public List<Ingredient> findIngredients(String query, long offet, long count) {
        return null;
    }
    
    public Ingredient createIngredient(Ingredient ingredient) {
        ingredientDAO.create(ingredient);
        return ingredient;
    }
    
    public List<Ingredient> createIngredients(List<Ingredient> ingredients) {
        ingredientDAO.create(ingredients);
        return ingredients;
    }
    
    public Ingredient modifyIngredient(Ingredient ingredient) {
        ingredientDAO.modify(ingredient);
        return ingredient;
    }
    
    public List<Ingredient> modifyIngredients(List<Ingredient> ingredients) {
        ingredientDAO.modify(ingredients);
        return ingredients;
    }
    
    public Ingredient deleteIngredient(Ingredient ingredient) {
        ingredientDAO.delete(ingredient);
        return ingredient;
    }
    
    public List<Ingredient> deleteIngredients(List<Ingredient> ingredients) {
        ingredientDAO.delete(ingredients);
        return ingredients;
    }
}
