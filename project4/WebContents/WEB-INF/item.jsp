<%@page import="edu.ucla.cs.cs144.Bid"%>
<html>
	<head>
		<title><%=request.getAttribute("title") %></title>
	</head>
	<body onload="mapInit()">
		<form name="get1" action="/eBay/item" method="GET">
			<table border="0" align="center" style="padding-top:50px">
                <tr><td align="center"><img alt="Ebay" src="ebay.png"/></td></tr>
				<tr>
					<td align="center" >
        				<div id="textbox" >
							<input type="text" name="itemID" >
							<input type="submit" name="submit" value="Search">
						</div></td>
				</tr>
			</table>	
		</form>
		<table border="1" cellspacing="5px" width="800">
			<col width="300">
			<col width="500">
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
				<td valign="top">Bids</td>
				<td valign="top">
				<% Bid[] bids = (Bid[]) request.getAttribute("Bids"); %>
				<% for(int i=0; i<bids.length; i++) {%>
					<ul>
						<li>Bid<%=i %>
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
			</tr>	

			<tr>
				<td valign="top">Location:</td><td><%=request.getAttribute("Location") %></td>
			</tr>
			<tr>
				<td valign="top">Country:</td><td><%=request.getAttribute("Country") %></td>
			</tr>
			<tr>
				<td valign="top">Latitude/Longitude:</td><td><%=request.getAttribute("Latitude") %>, <%=request.getAttribute("Longitude") %></td>
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
			<%
				String Longitude = (String)request.getAttribute("Longitude");
				String Latitude = (String)request.getAttribute("Latitude");
				String address = "";
				if (!Longitude.equals("") && !Latitude.equals("")) {
					address = Latitude + " " + Longitude;
				} else {
					address = (String)request.getAttribute("Location");
				}
			%>

		</table>
		<div id="map-canvas" style="width:50%; height:50%"></div>
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
	</body>
</html>