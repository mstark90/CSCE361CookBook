/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao;

import edu.unl.csce361.group4.cookbook.Recipe;
import java.util.List;

/**
 *
 * @author mstark
 */
public interface RecipeDAO {
    Recipe getRecipe(long recipeId);
    Recipe getRecipeForName(String recipeName);
    void create(Recipe recipe);
    void modify(Recipe recipe);
    void delete(Recipe recipe);
    List<Recipe> getRecipesForCategory(String category, long offset, long count);
}
