/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mstark
 */
@RestController
@RequestMapping("/ingredients/")
public class IngredientController {
    private IngredientDAO ingredientDAO;
    private final SolrServer solrClient;
    
    private final static Logger logger = Logger.getLogger("IngredientController");
    
    public IngredientController() {
        solrClient = new HttpSolrServer("http://localhost:8983/solr/cookbook");
    }
    
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<Ingredient> findIngredients(@RequestParam("query") String query,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "query", defaultValue = "10") long count) {
        List<Long> ingredients = new LinkedList<>();
        
        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("ingredientName:"+ query +"*");
            solrQuery.setStart((int)offset);
            solrQuery.setRows((int)count);
            
            QueryResponse response = solrClient.query(solrQuery);
            SolrDocumentList results = response.getResults();
            for (SolrDocument result : results) {
                ingredients.add(Long.parseLong(result.get("id").toString()));
            }
        } catch (SolrServerException e) {
            logger.log(Level.INFO, "Could not search the index:", e);
        }
        return ingredientDAO.getIngredients(ingredients);
    }
    
    @RequestMapping(value="/createIngredient", method = RequestMethod.POST)
    public Ingredient createIngredient(Ingredient ingredient) {
        ingredientDAO.create(ingredient);
        return ingredient;
    }
    
    @RequestMapping(value="/createIngredients", method = RequestMethod.POST)
    public List<Ingredient> createIngredients(List<Ingredient> ingredients) {
        ingredientDAO.create(ingredients);
        return ingredients;
    }
    
    @RequestMapping(value="/modifyIngredient", method = RequestMethod.POST)
    public Ingredient modifyIngredient(Ingredient ingredient) {
        ingredientDAO.modify(ingredient);
        return ingredient;
    }
    
    @RequestMapping(value="/modifyIngredients", method = RequestMethod.POST)
    public List<Ingredient> modifyIngredients(List<Ingredient> ingredients) {
        ingredientDAO.modify(ingredients);
        return ingredients;
    }
    
    @RequestMapping(value="/deleteIngredient", method = RequestMethod.POST)
    public Ingredient deleteIngredient(Ingredient ingredient) {
        ingredientDAO.delete(ingredient);
        return ingredient;
    }
    
    @RequestMapping(value="/deleteIngredients", method = RequestMethod.POST)
    public List<Ingredient> deleteIngredients(List<Ingredient> ingredients) {
        ingredientDAO.delete(ingredients);
        return ingredients;
    }
}
