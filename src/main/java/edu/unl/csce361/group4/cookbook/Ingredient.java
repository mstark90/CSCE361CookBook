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
public class Ingredient {
    private long ingredientId;
    private String ingredientName;
    private float servingSize;
    private MeasuringUnits measuringUnits;
    private float containerAmount;
    private float retailPrice;

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
     * @return the ingredientName
     */
    public String getIngredientName() {
        return ingredientName;
    }

    /**
     * @param ingredientName the ingredientName to set
     */
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
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
     * @return the measuringUnits
     */
    public MeasuringUnits getMeasuringUnits() {
        return measuringUnits;
    }

    /**
     * @param measuringUnits the measuringUnits to set
     */
    public void setMeasuringUnits(MeasuringUnits measuringUnits) {
        this.measuringUnits = measuringUnits;
    }

    /**
     * @return the containerAmount
     */
    public float getContainerAmount() {
        return containerAmount;
    }

    /**
     * @param containerAmount the containerAmount to set
     */
    public void setContainerAmount(float containerAmount) {
        this.containerAmount = containerAmount;
    }

    /**
     * @return the retailPrice
     */
    public float getRetailPrice() {
        return retailPrice;
    }

    /**
     * @param retailPrice the retailPrice to set
     */
    public void setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
    }
}
