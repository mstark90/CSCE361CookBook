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
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Resource(lookup="cookbook/dataGovApiKey")
    private String govApiKey;

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
            solrQuery.setQuery("allContent:" + query + "*");
            solrQuery.setStart((int) offset);
            solrQuery.setRows((int) count);

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

    public Recipe createRecipe(@RequestBody Recipe recipe) {
        recipeDAO.create(recipe);

        return recipe;
    }

    private List<IngredientNutritionInformation> downloadNutritionInformation(Recipe recipe) {
        List<IngredientNutritionInformation> nutritionInformationList = new LinkedList<>();

        HttpClient client = null;
        

        try {
            for (Ingredient ingredient : recipe.getIngredients()) {
                client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://api.nal.usda.gov/usda/ndb/search/?format=json&q="+ ingredient.getIngredientName() +"&sort=n&max=25&offset=0&api_key="+ govApiKey));
                HttpResponse response = client.execute(request);
                JsonReader parser = javax.json.Json.createReader(response.getEntity().getContent());
                JsonObject obj = parser.readObject();
                JsonObject ingredientInfo = (JsonObject)obj.getJsonObject("list").getJsonArray("item").get(0);
                
                client = new DefaultHttpClient();
                request = new HttpGet();
                request.setURI(new URI("http://api.nal.usda.gov/usda/ndb/nutrients/?format=json&nutrients=205&nutrients=204&nutrients=208&nutrients=269&ndbno="+ ingredientInfo.getString("ndbno") +"&api_key="+ govApiKey));
                response = client.execute(request);
                parser = javax.json.Json.createReader(response.getEntity().getContent());
                obj = parser.readObject();
                ingredientInfo = (JsonObject)obj.getJsonObject("report").getJsonArray("foods").get(0);
                JsonArray nutrientJson = ingredientInfo.getJsonArray("nutrients");
                
                for(JsonValue ingredientDataRaw : nutrientJson) {
                    JsonObject ingredientData = (JsonObject) ingredientDataRaw;
                    
                    IngredientNutritionInformation info = new IngredientNutritionInformation();
                    info.setIngredientId(ingredient.getIngredientId());
                    info.setNutrientName(ingredientData.getString("nutrient"));
                    info.setNutrientAmount(Float.parseFloat(ingredientData.getString("value")));
                    info.setUnits(MeasuringUnits.UNIT);
                    
                    nutritionInformationList.add(info);
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not process the ingredient data because of: ", e);
        }

        return nutritionInformationList;
    }

    @RequestMapping(value = "modifyRecipe", method = RequestMethod.POST, consumes = "application/json")

    public Recipe modifyRecipe(@RequestBody Recipe recipe) {
        recipeDAO.modify(recipe);
        return recipe;
    }

    @RequestMapping(value = "deleteRecipe", method = RequestMethod.POST, consumes = "application/json")

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
        if (id == 0) {
            return null;
        }
        return recipeDAO.getRecipe(id);
    }

    @RequestMapping(value = "get/id/{recipeId}/nutritionInformation", method = RequestMethod.GET)
    public Map<String, Float> getNutritionInformation(@PathVariable("recipeId") long recipeId) {
        Map<String, Float> info = new HashMap<>();

        Recipe recipe = recipeDAO.getRecipe(recipeId);

        for (Ingredient ingredient : recipe.getIngredients()) {
            List<IngredientNutritionInformation> nutritionInformation = ingredientDAO.getNutritionInformation(ingredient.getIngredientId());
            for (IngredientNutritionInformation ingredientNutrition : nutritionInformation) {
                float amount = (float) ingredientNutrition.getNutrientAmount();
                info.put(ingredientNutrition.getNutrientName(), amount);
            }
        }

        return info;
    }
}
