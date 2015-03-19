/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var recipeloader = {
    recipeTemplate: Handlebars.compile("<div class=\"recipe-item\">\n\
                <div class=\"recipe-item-img\">\n\
                    <img src=\"{{imageUrl}}\" alt=\"{{recipeName}}\" />\n\
                </div>\n\
                <div class=\"recipe-item-info\">\n\
                    <h2>\n\
                        <a href=\"recipage.jsp?recipeId={{recipeId}}\">{{recipeName}}</a>\n\
                    </h2>\n\
                    <p>\n\
                        <a href=\"recipage.jsp?recipeId={{recipeId}}\">{{description}}</a>\n\
                    </p>\n\
                </div>\n\
            </div>"),
    ingredientTemplate: Handlebars.compile("<li>{{servingSize}} {{measuringUnits}} {{ingredientName}}</li>"),
    getRecipes: function() {
        $.get("rest/recipes/all", function(data) {
            $.each(data, function(index, item) {
                $("#recipe-list").append(recipeloader.recipeTemplate(item));
                
            });
        });
    },
    getRecipe: function(recipeId) {
        $.get("rest/recipes/get/id/"+ recipeId, function(data) {
            $("#description-container").text(data.description);
            $("#recipe-image").attr("src", data.imageUrl);
            $("#recipe-name").text(data.recipeName);
            $.each(data.ingredients, function(index, item) {
                item.measuringUnits = item.measuringUnits.toLowerCase();
                $("#ingredients").append(recipeloader.ingredientTemplate(item));
            });
        });
    }
};