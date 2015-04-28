/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.controllers;

import edu.unl.csce361.group4.cookbook.Ingredient;
import edu.unl.csce361.group4.cookbook.IngredientNutritionInformation;
import edu.unl.csce361.group4.cookbook.MeasuringUnits;
import edu.unl.csce361.group4.cookbook.dao.IngredientDAO;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
import org.apache.http.util.EntityUtils;
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
@RequestMapping("/ingredients")
public class IngredientController {

    @Autowired
    private IngredientDAO ingredientDAO;

    @Resource(lookup = "cookbook/dataGovApiKey")
    private String govApiKey;

    private final SolrServer solrClient;

    private final static Logger logger = Logger.getLogger("IngredientController");

    public IngredientController() {
        solrClient = new HttpSolrServer("http://localhost:8983/solr/cookbook");
    }

    @RequestMapping(value = "/importNutritionInfo", method = RequestMethod.GET)
    public String importNutritionInfo() {
        List<Ingredient> ingredients = ingredientDAO.getIngredients();
        List<IngredientNutritionInformation> nutritionInfo = downloadNutritionInformation(ingredients);
        loadNutritionInformation(nutritionInfo);
        return "Done";
    }

    private List<IngredientNutritionInformation> downloadNutritionInformation(List<Ingredient> ingredients) {
        List<IngredientNutritionInformation> nutritionInformationList = new LinkedList<>();

        HttpClient client = null;

        for (Ingredient ingredient : ingredients) {
            try {
                client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://api.nal.usda.gov/usda/ndb/search/?format=json&q=" + URLEncoder.encode(ingredient.getIngredientName(), "utf-8") + "&sort=n&max=25&offset=0&api_key=" + govApiKey));
                HttpResponse response = client.execute(request);
                JsonReader parser = javax.json.Json.createReader(response.getEntity().getContent());
                JsonObject obj = parser.readObject();
                JsonObject ingredientInfo = (JsonObject) obj.getJsonObject("list").getJsonArray("item").get(0);
                EntityUtils.consumeQuietly(response.getEntity());

                client = new DefaultHttpClient();
                request = new HttpGet();
                request.setURI(new URI("http://api.nal.usda.gov/usda/ndb/nutrients/?format=json&nutrients=205&nutrients=204&nutrients=208&nutrients=269&ndbno=" + ingredientInfo.getString("ndbno") + "&api_key=" + govApiKey));
                response = client.execute(request);
                parser = javax.json.Json.createReader(response.getEntity().getContent());
                obj = parser.readObject();
                ingredientInfo = (JsonObject) obj.getJsonObject("report").getJsonArray("foods").get(0);
                JsonArray nutrientJson = ingredientInfo.getJsonArray("nutrients");

                for (JsonValue ingredientDataRaw : nutrientJson) {
                    JsonObject ingredientData = (JsonObject) ingredientDataRaw;

                    IngredientNutritionInformation info = new IngredientNutritionInformation();
                    info.setIngredientId(ingredient.getIngredientId());
                    info.setNutrientName(ingredientData.getString("nutrient"));
                    info.setNutrientAmount(Float.parseFloat(ingredientData.getString("value")));
                    info.setUnits(MeasuringUnits.UNIT);

                    nutritionInformationList.add(info);
                }
                
                EntityUtils.consumeQuietly(response.getEntity());
            } catch (Exception e) {
                logger.log(Level.WARNING, "Could not process the ingredient data because of: ", e);
            }
        }

        return nutritionInformationList;
    }

    @RequestMapping(value = "/get/id/{ingredientId}", method = RequestMethod.GET)
    public Ingredient getIngredient(@PathVariable("ingredientId") long ingredientId,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "count", defaultValue = "10") long count) {
        return ingredientDAO.getIngredient(ingredientId, offset, count);
    }

    @RequestMapping(value = "/get/name/{ingredientName}", method = RequestMethod.GET)
    public Ingredient getIngredient(@PathVariable("ingredientName") String ingredientName,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "count", defaultValue = "10") long count) {
        List<Ingredient> list = null;

        list = ingredientDAO.findIngredient(ingredientName, offset, count);

        if (list == null || list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public List<Ingredient> findIngredients(@RequestParam("query") String query,
            @RequestParam(value = "offset", defaultValue = "0") long offset,
            @RequestParam(value = "count", defaultValue = "10") long count) {
        List<Long> ingredients = new LinkedList<>();

        try {
            SolrQuery solrQuery = new SolrQuery();
            solrQuery.setQuery("ingredientName:" + query + "*");
            solrQuery.setStart((int) offset);
            solrQuery.setRows((int) count);

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

    @RequestMapping(value = "/createIngredient", method = RequestMethod.POST, consumes = "application/json")
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        ingredientDAO.create(ingredient);
        
        List<IngredientNutritionInformation> nutritionInfo = downloadNutritionInformation(Arrays.asList(ingredient));
        loadNutritionInformation(nutritionInfo);
        
        return ingredient;
    }

    @RequestMapping(value = "/createIngredients", method = RequestMethod.POST, consumes = "application/json")
    public List<Ingredient> createIngredients(@RequestBody LinkedList<Ingredient> ingredients) {
        ingredientDAO.create(ingredients);
        return ingredients;
    }

    @RequestMapping(value = "/modifyIngredient", method = RequestMethod.POST, consumes = "application/json")
    public Ingredient modifyIngredient(@RequestBody Ingredient ingredient) {
        ingredientDAO.modify(ingredient);
        
        List<IngredientNutritionInformation> nutritionInfo = downloadNutritionInformation(Arrays.asList(ingredient));
        loadNutritionInformation(nutritionInfo);
        
        return ingredient;
    }

    @RequestMapping(value = "/modifyIngredients", method = RequestMethod.POST, consumes = "application/json")
    public List<Ingredient> modifyIngredients(@RequestBody LinkedList<Ingredient> ingredients) {
        ingredientDAO.modify(ingredients);
        
        List<IngredientNutritionInformation> nutritionInfo = downloadNutritionInformation(ingredients);
        loadNutritionInformation(nutritionInfo);
        
        return ingredients;
    }

    @RequestMapping(value = "/deleteIngredient", method = RequestMethod.POST, consumes = "application/json")
    public Ingredient deleteIngredient(@RequestBody Ingredient ingredient) {
        ingredientDAO.delete(ingredient);
        return ingredient;
    }

    @RequestMapping(value = "/deleteIngredients", method = RequestMethod.POST, consumes = "application/json")
    public List<Ingredient> deleteIngredients(@RequestBody LinkedList<Ingredient> ingredients) {
        ingredientDAO.delete(ingredients);
        return ingredients;
    }

    @RequestMapping(value = "/loadNutritionInformation", method = RequestMethod.POST, consumes = "application/json")
    public List<IngredientNutritionInformation> loadNutritionInformation(List<IngredientNutritionInformation> nutritionInformation) {
        ingredientDAO.loadNutritionInformation(nutritionInformation);
        return nutritionInformation;
    }
}
