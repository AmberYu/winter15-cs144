<html>
     <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <meta charset="utf-8">
        <title>Confirmation</title>
        <link href="style.css" rel="stylesheet" type="text/css" />
        <link rel="stylesheet" type="text/css" href="payment_style.css" />
    </head>
	<body>
        <div id="topPan">
        <div id="logo"><img src="images/logo.gif" title="Ebay" alt="Ebay" width="200" height="220" border="0" /></div>
        <ul>
            <li class="current"><a href="http://localhost:1448/eBay/index.html">Home</a></li>
            <li><a href="http://localhost:1448/eBay/keywordSearch.html">Keyword Search</a></li>
            <li><a href="http://localhost:1448/eBay/getItem.html">ItemID Search</a></li>
        </ul>
    </div>
     <div class="container">
    <section id="content">
		<h1>Thanks for shopping with us</h1>
		
    <h4>Item ID: <%= request.getAttribute("itemID") %></h4> 
    <h4>Item Name: <%= request.getAttribute("Name") %></h4> 
   	<h4>Buy Price: $<%= request.getAttribute("Buy_Price") %></h4> 
    <h4>Credit Card: <%= request.getAttribute("creditCardNumber") %></h4>
    <h4>Time: <%= request.getAttribute("currentTimeStamp") %></h4>
    	
    </section><!-- content -->
</div><!-- container -->
        
    <div id="footermainPan">
  <div id="footerPan">
    <div id="footerlogoPan"><img src="images/footerlogo.gif" title="Ebay" alt="Ebay" width="160" height="100" border="0" /></div>
    <ul>
      <li><a href="http://localhost:1448/eBay/index.html">Home</a></li>
      <li><a href="http://localhost:1448/eBay/keywordSearch.html">Keyword Search</a></li>
      <li><a href="http://localhost:1448/eBay/getItem.html">ItemID Search</a></li>
    </ul>
    <p class="copyright">Copyright &copy; 2015 Gunagli Wu, Jingzhi Yu</p>
  </div>
</div>
		
	</body>
</html>