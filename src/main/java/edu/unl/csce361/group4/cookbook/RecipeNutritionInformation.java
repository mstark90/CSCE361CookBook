/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook;

import java.util.Map;

/**
 *
 * @author mstark
 */
public class RecipeNutritionInformation {
    private int calories, caloriesFromFat;
    private int sodiumAmount, sugarAmount;
    private Map<String, Integer> fatAmounts;
    private Map<String, Integer> vitaminAmounts;
    private Map<String, Integer> mineralAmounts;

    /**
     * @return the calories
     */
    public int getCalories() {
        return calories;
    }

    /**
     * @param calories the calories to set
     */
    public void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * @return the caloriesFromFat
     */
    public int getCaloriesFromFat() {
        return caloriesFromFat;
    }

    /**
     * @param caloriesFromFat the caloriesFromFat to set
     */
    public void setCaloriesFromFat(int caloriesFromFat) {
        this.caloriesFromFat = caloriesFromFat;
    }

    /**
     * @return the sodiumAmount
     */
    public int getSodiumAmount() {
        return sodiumAmount;
    }

    /**
     * @param sodiumAmount the sodiumAmount to set
     */
    public void setSodiumAmount(int sodiumAmount) {
        this.sodiumAmount = sodiumAmount;
    }

    /**
     * @return the sugarAmount
     */
    public int getSugarAmount() {
        return sugarAmount;
    }

    /**
     * @param sugarAmount the sugarAmount to set
     */
    public void setSugarAmount(int sugarAmount) {
        this.sugarAmount = sugarAmount;
    }

    /**
     * @return the fatAmounts
     */
    public Map<String, Integer> getFatAmounts() {
        return fatAmounts;
    }

    /**
     * @param fatAmounts the fatAmounts to set
     */
    public void setFatAmounts(Map<String, Integer> fatAmounts) {
        this.fatAmounts = fatAmounts;
    }

    /**
     * @return the vitaminAmounts
     */
    public Map<String, Integer> getVitaminAmounts() {
        return vitaminAmounts;
    }

    /**
     * @param vitaminAmounts the vitaminAmounts to set
     */
    public void setVitaminAmounts(Map<String, Integer> vitaminAmounts) {
        this.vitaminAmounts = vitaminAmounts;
    }

    /**
     * @return the mineralAmounts
     */
    public Map<String, Integer> getMineralAmounts() {
        return mineralAmounts;
    }

    /**
     * @param mineralAmounts the mineralAmounts to set
     */
    public void setMineralAmounts(Map<String, Integer> mineralAmounts) {
        this.mineralAmounts = mineralAmounts;
    }
    
}
