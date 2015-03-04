/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook.dao;

import edu.unl.csce361.group4.cookbook.Ingredient;
import java.util.List;

/**
 *
 * @author mstark
 */
public interface IngredientDAO {
    List<Ingredient> findIngredient(String name, long offset, long count);
    void create(Ingredient ingredient);
    void create(List<Ingredient> ingredients);
    void modify(Ingredient ingredient);
    void modify(List<Ingredient> ingredients);
    void delete(Ingredient ingredient);
    void delete(List<Ingredient> ingredients);
    List<Ingredient> getIngredients(List<Long> ingredientIds);
}
