/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.csce361.group4.cookbook;

import java.util.List;

/**
 *
 * @author mstark
 */
public class User {
    private long userId;
    private String userName, fullName;
    private List<Recipe> favoriteRecipes;
    private String oauthToken, oauthSecret;

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the favoriteRecipes
     */
    public List<Recipe> getFavoriteRecipes() {
        return favoriteRecipes;
    }

    /**
     * @param favoriteRecipes the favoriteRecipes to set
     */
    public void setFavoriteRecipes(List<Recipe> favoriteRecipes) {
        this.favoriteRecipes = favoriteRecipes;
    }

    /**
     * @return the oauthToken
     */
    public String getOauthToken() {
        return oauthToken;
    }

    /**
     * @param oauthToken the oauthToken to set
     */
    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    /**
     * @return the oauthSecret
     */
    public String getOauthSecret() {
        return oauthSecret;
    }

    /**
     * @param oauthSecret the oauthSecret to set
     */
    public void setOauthSecret(String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }
    
}
