<html>
	<% String[] itemIDs = (String[])request.getAttribute("itemIDs"); %>
	<% String[] itemNames = (String[])request.getAttribute("itemNames"); %>
	<head>
		<title><%=request.getAttribute("title") %></title>
	</head>
	<body>
		<form name="search1" action="/search" method="GET">
			<table border="0" align="center" style="padding-top:50px">
                <tr><td align="center"><img alt="Ebay" src="ebay.png"/></td></tr>
				<tr>
					<td align="center" >
        				<div id="textbox" >
							<input type="text" name="content" >
							<input type="hidden" name="numResultsToSkip" value="0">
							<input type="hidden" name="numResultsToReturn" value="20">
							<input type="submit" name="submit" value="Search">
						</div>
					</td>

				</tr>
			</table>	
		</form>	
			<div>The total number of results is: <%=itemIDs.length %></div>
			<ul id="list">
			<% for (int i=0; i<itemIDs.length; i++) {%>
				<li><%=itemIDs[i] %>&nbsp;&nbsp;&nbsp;<%=itemNames[i] %></li>	
			<%} %>
			</ul>
	</body>	
</html>