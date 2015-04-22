/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var ingredientEditor = {
    ingredientTemplate: Handlebars.compile("<li name=\"ingredientrow-item\">\
                                            <textarea data-id=\"{{ingredientId}}\" cols=\"8\" rows=\"1\">{{servingSize}}</textarea>\
                                            <textarea data-id=\"{{ingredientId}}\" cols=\"12\" rows=\"1\">{{measuringUnits}}</textarea>\
                                            <textarea data-id=\"{{ingredientId}}\" cols=\"25\" rows=\"1\">{{ingredientName}}</textarea>\
                                            </li>"
                                            ),
    getIngredient: function(name)
    {
         $.get("rest/ingredients/get/name/"+ name, 
            function(data) 
            {
                $("#serving-size").val(data.servingSize);
                $("#ingredient-name").val(data.ingredientName);
                $("#measuring-units").val(data.measuringUnits);
                $("#container-amount").val(data.containerAmount);
                $("#retail-price").val(data.retailPrice);
            });
    },
   
    saveIngredient: function()
    {
        $.get("rest/ingredients/get/name/" + $("#ingredient-name").val(), 
            function(data) 
            {
                ingredientEditor.postIngredient(data.ingredientId, $("#ingredient-name").val(), $("#serving-size").val(), $("#container-amount").val(), $("#measuring-units").val(), $("#retail-price").val());
            });
    },
    postIngredient: function(ingredientId, ingredientName, servingSize, containerAmount, measuringUnits, retailPrice)
    {
        if (ingredientId == 0 || ingredientId == undefined)
        {
            $.ajax(
            {
                type: "POST", 
                url: "rest/ingredients/createIngredient", 
                data: 
                JSON.stringify({
                    "ingredientName": ingredientName,
                    "servingSize": servingSize,
                    "containerAmount": containerAmount,
                    "measuringUnits": measuringUnits,
                    "retailPrice": retailPrice
                }),
                success:
                function(data)
                {
                    if (data.ingredientId == 0)
                    {
                        $("#sucess-status").show();
                        $("#sucess-status").val("Failed...");
                    }
                    else
                    {
                        $("#sucess-status").show();
                        $("#sucess-status").val("Success!");
                    }
                },
                dataType: "JSON",
                contentType: "application/json; charset=utf-8"
            });
        }
        else 
        {
            $.ajax(
            {
                type: "POST", 
                url: "rest/ingredients/modifyIngredient", 
                data: 
                JSON.stringify({
                    "ingredientId": ingredientId,
                    "ingredientName": ingredientName,
                    "servingSize": servingSize,
                    "containerAmount": containerAmount,
                    "measuringUnits": measuringUnits,
                    "retailPrice": retailPrice
                }),
                success:
                function(data)
                {
                    if (data.ingredientId == 0)
                    {
                        $("#sucess-status").show();
                        $("#sucess-status").val("Failed...");
                    }
                    else
                    {
                        $("#sucess-status").show();
                        $("#sucess-status").val("Success!");
                    }
                },
                dataType: "JSON",
                contentType: "application/json; charset=utf-8"
            });
        }
    }
};

