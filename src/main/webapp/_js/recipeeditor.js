/* 
 * Document   : recipeeditor
 * Created on : Apr 20, 2015
 * Author     : Greenwood
 */
var ingredientList;

var recipeEditor= {
    /*
    ingredientTemplate: Handlebars.compile("<li name=\"ingredientrow-item\">\
                                            <textarea data-id=\"{{ingredientId}}\" cols=\"8\" rows=\"1\">{{servingSize}}</textarea>\
                                            <textarea data-id=\"{{ingredientId}}\" cols=\"12\" rows=\"1\">{{measuringUnits}}</textarea>\
                                            <textarea data-id=\"{{ingredientId}}\" cols=\"25\" rows=\"1\">{{ingredientName}}</textarea>\
                                            </li>"
                                            ),*/
    ingredientTemplate: Handlebars.compile("<li name=\"ingredientrow-item\">\
                                            <textarea onchange='recipeEditor.checkIngredient($(this))' cols=\"25\" rows=\"1\">{{ingredientName}}</textarea>\
                                            <button onclick='recipeEditor.deleteIngredient($(this))'>-</button>\
                                            </li>"
                                            ),
    getRecipe: function(recipeId) {
        $.get("rest/recipes/get/id/"+ recipeId, function(data) {
            $("#description-container").text(data.description);
            $("#recipe-image").attr("src", data.imageUrl);
            $("#recipe-name").text(data.recipeName);
            $("#recipe-image-url").text(data.imageUrl);
            $("#category-container").text(data.category);
            $.each(data.ingredients, function(index, item) {
                item.measuringUnits = item.measuringUnits.toLowerCase();
                $("#ingredients").append(recipeEditor.ingredientTemplate(item));
            });
        });
    },
    
    checkIngredient: function(textareaobj)
    {
        $.get("rest/ingredients/get/name/"+ textareaobj.val(), 
            function(data) 
            {
                if (data.ingredientId == 0 || data.ingredientId == undefined)
                {
                    $(textareaobj).css('color', 'red');
                }
                else
                {
                    $(textareaobj).css('color', 'black');
                }
            });        
    },
    
    addIngredient: function()
    {
        $("#ingredients").append(recipeEditor.ingredientTemplate());
    },  
    
    deleteIngredient: function(buttonObj)
    {
        $(buttonObj.parent()).remove();
    },
    
    saveRecipe: function (recipeId)
    {
        //Clear ingredient list
        ingredientList = new Array();
        
        //Reload ingredient list
        $("#ingredients textarea").each(
                function(index, item)
                {
                    $.get("rest/ingredients/get/name/" + $(item).val(), 
                    function(data) 
                    {
                        ingredientList.push(data);
                    });
                });
        
    },
    
    postRecipe: function (recipeId)
    {
        $.ajax({
                type: "POST", 
                url: "rest/recipes/modifyRecipe", 
                data: 
                JSON.stringify({
                    "recipeId": recipeId,
                    "recipeName": $("#recipe-name").val(),
                    "imageUrl": $("#recipe-image-url").val(),
                    "description": $("#description-container").val(),
                    "category": $("#category-container").val(),
                    "ingredients": ingredientList
                            
                }),
                success:
                function(data)
                {
                    if (data.recipeId == 0)
                    {
                        $("#saveStatus").show();
                        $("#saveStatus").text("Failed..."); 
                    }
                    else
                    {
                        $("#saveStatus").show();
                        $("#saveStatus").text("Successful!"); 
                    }
                },
                dataType: "JSON",
                contentType: "application/json; charset=utf-8"
            });
    }            
};

