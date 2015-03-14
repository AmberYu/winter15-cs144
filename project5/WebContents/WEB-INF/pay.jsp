<% String secureURL = "https://" + request.getServerName() + ":8443" + request.getContextPath() + "/confirm"; %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <meta charset="utf-8">
        <title>Credit Card Input Page</title>
        <link href="style.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="payment_style.css" />
    </head>
	<body>
		<div id="topPan">
        <div id="logo"><img src="images/logo.gif" title="Ebay" alt="Ebay" width="200" height="220" border="0" /></div>
        <ul>
            <li class="current"><a href="index.html">Home</a></li>
            <li><a href="keywordSearch.html">Keyword Search</a></li>
            <li><a href="getItem.html">ItemID Search</a></li>
        </ul>
    </div>

    <div class="container">
    <section id="content">
        <form method="post" action="<%= secureURL %>">
            <h1>Information Check</h1>
        <h4>Item ID: <%= request.getAttribute("itemID") %></h4>
        <h4>Item Name: <%= request.getAttribute("Name") %></h4>
        <h4>Buy Price: $<%= request.getAttribute("Buy_Price") %></h4>
        
        <input type="hidden" name="id" value="<%= request.getAttribute("itemID") %>" />
        <input type="hidden" name="action" value="confirm" />
        <div>
                <input type="text" placeholder="Enter Credit Card #:" id="password" name="creditCardNumber" />
        </div>
        
        <div><input type="submit" value="Pay" /></div>
    </form>
	   </section><!-- content -->
</div><!-- container -->
		
	<div id="footermainPan">
  <div id="footerPan">
    <div id="footerlogoPan"><img src="images/footerlogo.gif" title="Ebay" alt="Ebay" width="160" height="100" border="0" /></div>
    <ul>
      <li><a href="index.html">Home</a></li>
      <li><a href="keywordSearch.html">Keyword Search</a></li>
      <li><a href="getItem.html">ItemID Search</a></li>
    </ul>
    <p class="copyright">Copyright &copy; 2015 Gunagli Wu, Jingzhi Yu</p>
  </div>
</div>
	</body>
</html>