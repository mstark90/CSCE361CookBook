<%-- 
    Document   : recipeEditPage
    Created on : Mar 18, 2015, 2:46:07 PM
    Author     : Greenwood
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CookBook</title>
        <jsp:include page="WEB-INF/jsp/css_ref.jsp" />
        <jsp:include page="WEB-INF/jsp/js_ref.jsp" />
        <script type="text/javascript">
            $(document).ready(function() {
                recipeEditor.getRecipe(<%= request.getParameter("recipeId") %>);
                $("#saveStatus").hide();
                $("#reciPage-link").attr("href", "recipage.jsp?recipeId=" + <%= request.getParameter("recipeId") %>);
                $("#ingrPage-link").attr("href", "ingredientEdit.jsp");
            });
        </script>
    </head>
    <body>
        <jsp:include page="WEB-INF/jsp/header.jsp" />

        <div id="recipe">
            <textarea rows="1" cols="30" id="recipe-name"></textarea>
            <div id="recipe-img-container">
                <img id="recipe-image" src="" alt="IMG NOT FOUND" />
                <textarea rows="1" cols="50" id="recipe-image-url"></textarea>
            </div>
            <div id="tab-view-container">
                <ul class="tabs">
                    <li><a href="#ingredients-container">Ingredients</a></li>
                    <li><a href="#description-container">Description</a></li>
                    <li><a href="#nutrition-container">Nutrition</a></li>
                </ul>
                <div class="tabcontents">
                    <div id="ingredients-container">
                        <ul id="ingredients">
                           
                        </ul>
                        <button onclick="recipeEditor.addIngredient()">+</button>
                    </div>
                    <textarea id="description-container" cols="25" rows="5">
                    
                    </textarea>                   
                    <div id="nutrition-container">Cannot be edited at this time</div>
                </div>
                <textarea cols="25" rows="1" id="category-container"></textarea>
                <div id="button-container">
                    <button id="save-button" onclick="recipeEditor.saveRecipe(<%= request.getParameter("recipeId") %>)">Save</button><br><br>
                    <button id="post-button" onclick="recipeEditor.postRecipe(<%= request.getParameter("recipeId") %>)">Post</button>
                </div>
                <div id="saveStatus">Failed to save</div>
            </div>
            
        </div>
        <div id="bottom-links">
            <a id="reciPage-link" href="">Recipe Page</a>   
            <a id="ingrPage-link" href="">Ingredient Edit Page</a>  
        </div>
        
        <jsp:include page="WEB-INF/jsp/footer.jsp" />

    </body>
</html>
