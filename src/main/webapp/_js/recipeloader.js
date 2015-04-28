/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var recipeloader = {
    count: 0,
    recipeTemplate: Handlebars.compile("<div class=\"recipe-item\" data-recipe-id=\"{{recipeId}}\">\n\
                <div class=\"recipe-item-img\">\n\
                    <img src=\"{{imageUrl}}\" alt=\"{{recipeName}}\" />\n\
                </div>\n\
                <div class=\"recipe-item-info\">\n\
                    <h2>\n\
                        {{recipeName}}\n\
                    </h2>\n\
                    <span>\n\
                    {{description}}\n\
                    </span>\n\
                </div>\n\
            </div>"),
    ingredientTemplate: Handlebars.compile("<li>{{servingSize}} {{measuringUnits}} {{ingredientName}}</li>"),
    getRecipes: function() {
        $.get("rest/recipes/all?offset="+ recipeloader.count, function(data) {
            recipeloader.count += 10;
            $.each(data, function(index, item) {
                var recipeElement = $(recipeloader.recipeTemplate(item));
                
                recipeElement.click(function() {
                    window.location = "recipage.jsp?recipeId="+ $(this).attr("data-recipe-id");
                });
                
                $("#recipe-list").append(recipeElement);
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
        $.get("rest/recipes/get/id/"+ recipeId+"/nutritionInformation", function(data) {
            $("#nutrition-container").children().remove();
            $("#recipe-image").attr("src", data.imageUrl);
            $("#recipe-name").text(data.recipeName);
            $.each(Object.keys(data), function(index, key) {
                var nutrient = $("<span>"+ key +": &nbsp;"+ data[key] +"</span><br />");
                $("#nutrition-container").children().append(nutrient);
            });
        });
    }
};

$(document).ready(function() {
    
    var recipes = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        prefetch: 'rest/recipes/all',
        remote: 'rest/recipes/find?query=%QUERY&count=10'
    });

    // kicks off the loading/processing of `local` and `prefetch`
    recipes.initialize();
    
    $("#search-query").typeahead(null, {
        name: 'best-pictures',
        displayKey: 'value',
        source: recipes.ttAdapter(),
        templates: {
          empty: [
            '<div class="empty-message">',
            'Unable to find any recipes that match the current query.',
            '</div>'
          ].join('\n'),
          suggestion: Handlebars.compile("<div class=\"search-suggestion\"><a href=\"recipage.jsp?recipeId={{recipeId}}\"><img class=\"search-image\" src=\"{{imageUrl}}\" /><span>{{recipeName}}</span></a></div>")
        }
      });
    
    $("#user-settings-button").click(function() {
        if($("#settings-menu").hasClass("active")) {
            $("#settings-menu").removeClass("active");
            $("#settings-menu").addClass("inactive");
        } else {
            $("#settings-menu").addClass("active");
        }
    });
});