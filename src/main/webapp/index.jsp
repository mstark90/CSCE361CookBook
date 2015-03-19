<%-- 
    Document   : index
    Created on : Mar 18, 2015, 2:46:07 PM
    Author     : Sirous
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
                recipeloader.getRecipes();
            })
        </script>
    </head>
    <body>
        <jsp:include page="WEB-INF/jsp/header.jsp" />

        <div id="recipe-list">
            
        </div>

        <jsp:include page="WEB-INF/jsp/footer.jsp" />

    </body>
</html>
