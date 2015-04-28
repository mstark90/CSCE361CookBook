/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook;

/**
 *
 * @author mstark
 */
public class IngredientNutritionInformation {
    private long ingredientNutritionId;
    private long ingredientId;
    private String nutrientName;
    private float nutrientAmount;
    private float servingSize;
    private MeasuringUnits units;

    /**
     * @return the ingredientNutritionId
     */
    public long getIngredientNutritionId() {
        return ingredientNutritionId;
    }

    /**
     * @param ingredientNutritionId the ingredientNutritionId to set
     */
    public void setIngredientNutritionId(long ingredientNutritionId) {
        this.ingredientNutritionId = ingredientNutritionId;
    }

    /**
     * @return the ingredientId
     */
    public long getIngredientId() {
        return ingredientId;
    }

    /**
     * @param ingredientId the ingredientId to set
     */
    public void setIngredientId(long ingredientId) {
        this.ingredientId = ingredientId;
    }

    /**
     * @return the nutrientName
     */
    public String getNutrientName() {
        return nutrientName;
    }

    /**
     * @param nutrientName the nutrientName to set
     */
    public void setNutrientName(String nutrientName) {
        this.nutrientName = nutrientName;
    }

    /**
     * @return the nutrientAmount
     */
    public float getNutrientAmount() {
        return nutrientAmount;
    }

    /**
     * @param nutrientAmount the nutrientAmount to set
     */
    public void setNutrientAmount(float nutrientAmount) {
        this.nutrientAmount = nutrientAmount;
    }

    /**
     * @return the servingSize
     */
    public float getServingSize() {
        return servingSize;
    }

    /**
     * @param servingSize the servingSize to set
     */
    public void setServingSize(float servingSize) {
        this.servingSize = servingSize;
    }

    /**
     * @return the units
     */
    public MeasuringUnits getUnits() {
        return units;
    }

    /**
     * @param units the units to set
     */
    public void setUnits(MeasuringUnits units) {
        this.units = units;
    }
}
