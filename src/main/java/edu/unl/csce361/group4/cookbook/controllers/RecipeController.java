/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.Recipe;
import edu.unl.csce361.group4.cookbook.dao.RecipeDAO;
import java.util.LinkedList;
import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    private final SolrServer solrClient;

    private final static Logger logger = Logger.getLogger("RecipeController");
    
    public RecipeController() {
        solrClient = new HttpSolrServer("http://localhost:8983/solr/cookbook");
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<Recipe> findRecipes(@RequestParam("query") String query,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "query", defaultValue = "10") long count) {
        List<Recipe> recipes = new LinkedList<>();
        
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("recipeName:"+ query +"* OR ingredients:"+ query +"*");
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

    @RequestMapping(value = "/createRecipe", method = RequestMethod.POST)
    public Recipe createRecipe(Recipe recipe) {
        recipeDAO.create(recipe);
        return recipe;
    }

    @RequestMapping(value = "/modifyRecipe", method = RequestMethod.POST)
    public Recipe modifyRecipe(Recipe recipe) {
        recipeDAO.modify(recipe);
        return recipe;
    }

    @RequestMapping(value = "/deleteRecipe", method = RequestMethod.POST)
    public Recipe deleteRecipe(Recipe recipe) {
        recipeDAO.delete(recipe);
        return recipe;
    }

    @RequestMapping(value = "/get/{recipeName}", method = RequestMethod.GET)
    public Recipe getRecipe(@PathVariable("recipeName") String recipeName) {
        return recipeDAO.getRecipeForName(recipeName);
    }
}
