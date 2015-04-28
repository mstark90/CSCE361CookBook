<%-- 
    Document   : header
    Created on : Mar 17, 2015, 10:58:31 PM
    Author     : mstark
--%>

<%@page import="edu.unl.csce361.group4.cookbook.User"%>

<div id="header">
    <span id="cookbook-logo">
     <a href="index.jsp">CookBook</a>
    </span>
    <div id="right-container">
        <input type="text" id="search-query" placeholder="Search" />
        <span id="search-submit">
            <img src="_img/search.png" width="25" height="25" />
        </span>
        <span id="user-info">
            <span id="user-settings-button">Menu</span>
        </span>
    </div>
    <div id="settings-menu">
        <ul>
            <li>
                <a href="recipeEditPage.jsp?recipeId=0">Create Recipe</a>
            </li>
            <% if(request.getParameter("recipeId") != null) { %>
            <li>
                <a href="recipeEditPage.jsp?recipeId=<%= request.getParameter("recipeId") %>">Edit Recipe</a>
            </li>
            <% } %>
        </ul>
    </div>
</div>