<%-- 
    Document   : header
    Created on : Mar 17, 2015, 10:58:31 PM
    Author     : mstark
--%>

<%@page import="edu.unl.csce361.group4.cookbook.User"%>
<%

    User user = (User) session.getAttribute("userInformation");
    
%>

<div id="header">
    <span id="cookbook-logo">
     <a href="index.jsp">CookBook</a>
    </span>
    <div class="nav">
        <span>
            New
        </span>
        <span>
            Top
        </span>
        <span>
            Favorites
        </span>
    </div>
    <div id="right-container">
        <input type="text" id="search-query" placeholder="Search" />
        <span id="search-submit">
            <img src="_img/search.png" width="25" height="25" />
        </span>
        <span id="user-info">
            <% if(user == null) { %>
            <a href="login_page.html">Login</a>
            <% 
                } else {
            %>
            <span id="user-settings-button"><%= user.getFullName() %></span>
            <%
                }
            %>
        </span>
    </div>
    <%
        if(user != null) {
    %>
    <div id="settings-menu">
        <ul>
            <li>
                <a href="recipeEditPage.jsp?recipe_id=0">Create Recipe</a>
            </li>
            <li>
                <a href="recipeEditPage.jsp?recipe_id=${recipeId}">Edit Recipe</a>
            </li>
            <li>
                <a href="rest/users/logout">Logout</a>
            </li>
        </ul>
    </div>
    <%
        }
    %>
</div>