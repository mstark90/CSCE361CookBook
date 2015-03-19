<%-- 
    Document   : index
    Created on : Mar 18, 2015, 2:46:07 PM
    Author     : Abak
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<link rel="stylesheet" href="_css/default.css" />
<link rel="stylesheet" href="_css/tabcontent.css" />
<script src="_js/tabcontent.js" type="text/javascript"></script>
</head>
<body>
	<jsp:include page="WEB-INF/jsp/header.jsp" />

	<div id="recipe-list">
		<h1>DOUBLE-DARK CHICKEN NOODLE SOUP</h1>
                <img src="_img/51264240.jpg" alt="CHICKEN NOODLE SOUP"
			style="width: 304px; height: 228px">
		<ul class="tabs">
			<li><a href="#view1">INGREDIENTS</a></li>
			<li><a href="#view2">PREPARATION</a></li>
			<li><a href="#view3">Nutrition</a></li>
		</ul>
		<div class="tabcontents">
			<div id="view1">
				<ul>
					<li>1 leek</li>
					<li>Stems from 1/2 bunch parsley</li>
					<li>6 sprigs thyme</li>
					<li>2 bay leaves</li>
					<li>1 tablespoon vegetable oil</li>
					<li>3 pounds chicken wings</li>
					<li>1 onion, unpeeled, quartered</li>
				</ul>
			</div>
			<div id="view2">Add onion, carrots, celery, garlic, and
				reserved leek to pot. Cook, turning occasionally, until browned,
				10–12 minutes. Add tomato paste; cook, stirring, until slightly
				darkened, about 3 minutes. Add 1/2 cup water, scraping up browned
				bits. Add wings, leek bundle, peppercorns, and 12 cups water; bring
				to a boil. Reduce heat; simmer, skimming occasionally, until stock
				is deep amber and chicken is falling off the bone, 1 1/2–2 hours.
				Remove chicken and vegetables. Strain stock into a clean large heavy
				pot.</div>
			<div id="view3">Calories 615 - Fat 31 g - Fiber 6 g</div>
		</div>

	</div>

	<jsp:include page="WEB-INF/jsp/footer.jsp" />

</body>
</html>
