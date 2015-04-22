<%-- 
    Document   : ingredientEdit
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
                ingredientEditor.getIngredient(<%= request.getParameter("name") %>);
                $("#sucess-status").hide();
            });
        </script>
    </head>
    <body>
        <jsp:include page="WEB-INF/jsp/header.jsp" />
        
        <br>
        <br>
        <br>
        <ul id="add-ingredient-container">
            <li id="sucess-status"></li>
            <li><div>Serving Size</div><input type="text" id="serving-size"></input></li>
            <li><div>Ingredient Name</div><input type="text" id="ingredient-name"></input></li>
            <li><div>Measuring Units</div><input type="text" id="measuring-units"></input></li>
            <li><div>Container Amount</div><input type="text" id="container-amount"></input></li>
            <li><div>Retail Price</div><input type="text" id="retail-price"></input></li>
            <button id="add-button" onclick="ingredientEditor.saveIngredient()">Save</button>
        </ul>
        
        <jsp:include page="WEB-INF/jsp/footer.jsp" />

    </body>
</html>
