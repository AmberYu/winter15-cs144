<%@page import="edu.ucla.cs.cs144.Bid"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title><%=request.getAttribute("title") %></title>
		<link href="style.css" rel="stylesheet" type="text/css" />
	</head>
	<body onload="mapInit()">
	<div id="topPan">
    	<div id="logo"><img src="images/logo.gif" title="Ebay" alt="Ebay" width="200" height="220" border="0" /></div>
    	<ul>
    		<li class="current"><a href="index.html">Home</a></li>
    		<li><a href="keywordSearch.html">Keyword Search</a></li>
    		<li><a href="getItem.html">ItemID Search</a></li>
    	</ul>
    </div>
		
    <form name="get1" action="/eBay/item" method="GET">
			<table border="0" align="center">
                <tr><td align="center"><img alt="Ebay" src="ebay.png"/></td></tr>
				<tr>
					<td align="center" >
        				<div id="textbox" >
        					<h2>Auction Item Search </h2>
							<input type="text" name="itemID" placeholder = "Type in item id here...">
							<input type="submit" name="submit" value="Search">
						</div></td>
				</tr>
			</table>	
		</form>
		<div>
		<%String itemID = (String) request.getAttribute("itemID"); %>
		<%String address = ""; %>
		<% if(itemID.length() < 1) {%>
			<h2><%= request.getAttribute("Error")%></h2>
		<%}else
		{ %>
		<h3><center>Item Information</center></h3> 
		<table border="1" cellspacing="5px" width="800" align="center">
			<col width="300">
			<col width="500">
			<tr>
				<td valign="top">Item ID:</td><td><%=request.getAttribute("itemID") %></td>
			</tr>
			<tr>
				<td valign="top">Item Name:</td><td><%=request.getAttribute("Name") %></td>
			</tr>
			<tr>
				<td valign="top">Category:</td>
				<td><ul>
					<% String[] cates = (String[]) request.getAttribute("Categories"); %>
					<% for(int i=0; i<cates.length; i++) {%>
						<li><%=cates[i] %></li>
					<%} %>
					</ul></td>
			</tr>
			<tr>
				<td valign="top">Current Price:</td><td>$<%=request.getAttribute("Currently") %></td>
			</tr>
			<tr>
				<td valign="top">Buy Price:</td><td><%=request.getAttribute("Buy_Price") %></td>
			</tr>
			<tr>
				<td valign="top">First Bid:</td><td>$<%=request.getAttribute("First_Bid") %></td>
			</tr>
			<tr>
				<td valign="top">Number of Bids:</td><td><%=request.getAttribute("Number_of_Bids")%></td>
			</tr>
			<tr>
				<td valign="top">Location:</td><td><%=request.getAttribute("Location") %></td>
			</tr>
			<tr>
				<td valign="top">Country:</td><td><%=request.getAttribute("Country") %></td>
			</tr>
			<tr>
				<td valign="top">Started from:</td><td><%=request.getAttribute("Started") %></td>
			</tr>
			<tr>
				<td valign="top">End By:</td><td><%=request.getAttribute("Ends") %></td>
			</tr>
			<tr>
				<td valign="top">Seller:</td><td><%=request.getAttribute("Seller_ID")%></td>
			</tr>
			<tr>
				<td valign="top">Rating:</td><td><%=request.getAttribute("Seller_Rating")%></td>
			</tr>
			<tr>
				<td valign="top">Description:</td><td><%=request.getAttribute("Description")%></td>
			</tr>	
			<tr>
				<td valign="top">Bids</td>
				<% Bid[] bids = (Bid[]) request.getAttribute("Bids"); %>
				<%if(bids.length>=1){%>
				<td valign="top">

				<% for(int i=0; i<bids.length; i++) {%>
					<ul>
						<li>Bid<%=i+1 %>
							<ul>
								<% Bid b = bids[bids.length-i-1]; %>
								<li>Bidder: <%=b.getBidder() %></li>
								<li>Rating: <%=b.getBidder_rating() %></li>
								<li>Location: <%=b.getLocation() %></li>
								<li>Country: <%=b.getCountry() %></li>
								<li>Time: <%=b.getTime() %></li>
								<li>Amount: $<%=b.getAmount() %></li>
							</ul>								
						</li>
					</ul>
				<%} %>
				</td>
				<%} else{%>
				<td valign="top">No bids in this item!</td>
				<%}%>
			</tr>	
			
			<%
				String Longitude = (String)request.getAttribute("Longitude");
				String Latitude = (String)request.getAttribute("Latitude");
	
				if (!Longitude.equals("") && !Latitude.equals("")) {
					address = Latitude + " " + Longitude;
				} else {
					address = (String)request.getAttribute("Location");
				}
			%>
		</table>

		</div>
		<br>

		<h3><center>Item location on the map</center></h3>
		<div id="map-canvas" style="width:100%; height:50%;"></div>
		<script type="text/javascript"
			src="http://maps.google.com/maps/api/js?sensor=false">
		</script>
		<script type="text/javascript">
			function mapInit() 
			{
				var address = "<% out.print(address); %>"
				var geocoder = new google.maps.Geocoder();
				geocoder.geocode( {'address':address}, function(results, status) 
				{
					if (status == google.maps.GeocoderStatus.OK) 
					{
						var mapOptions = {zoom: 8, center: results[0].geometry.location}
						var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
						var marker = new google.maps.Marker({position: results[0].geometry.location, map: map});
					} else 
					{
						var mapOptions = {center: new google.maps.LatLng(0, 0), zoom: 1};
						var map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
					}
				});
			}
		</script>

		<%}%>
		
		<br><br>
<div id="footermainPan">
  <div id="footerPan">
    <div id="footerlogoPan"><img src="images/footerlogo.gif" title="Ebay" alt="Ebay" width="160" height="100" border="0" /></div>
    <ul>
      <li><a href="index.html">Home</a></li>
      <li><a href="keywordSearch.html">Keyword Search</a></li>
      <li><a href="getItem.html">ItemID Search</a></li>
    </ul>
    <p class="copyright">©Gunagli Wu, Jingzhi Yu, 2015</p>
  </div>
</div>
	</body>
</html>