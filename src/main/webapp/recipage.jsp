<%-- 
    Document   : index
    Created on : Mar 18, 2015, 2:46:07 PM
    Author     : Abak
--%>

<%@page import="edu.unl.csce361.group4.cookbook.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    User user = (User) session.getAttribute("userInformation");
    
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CookBook</title>
        <jsp:include page="WEB-INF/jsp/css_ref.jsp" />
        <jsp:include page="WEB-INF/jsp/js_ref.jsp" />
        <script type="text/javascript">
            $(document).ready(function() {
                recipeloader.getRecipe(<%= request.getParameter("recipeId") %>);
                $("#reciEditPage-link").attr("href", "recipeEditPage.jsp?recipeId=" + <%= request.getParameter("recipeId") %>);
            });
        </script>
    </head>
    <body>
        <jsp:include page="WEB-INF/jsp/header.jsp" />

        <div id="recipe">
            <h1 id="recipe-name"></h1>
            <div id="recipe-img-container">
                <img id="recipe-image" src="" alt="CHICKEN NOODLE SOUP" />
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
                    </div>
                    <div id="description-container">
                    </div>
                    <div id="nutrition-container">Coming soon</div>
                </div>
            </div>
        </div>
        <div id="bottom-links">
            <a id="reciEditPage-link" href="">Recipe Edit Page</a>
        </div>

        <jsp:include page="WEB-INF/jsp/footer.jsp" />

    </body>
</html>
