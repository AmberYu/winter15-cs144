<html>
	<% 
		String[] itemIDs = (String[])request.getAttribute("itemIDs");
		String[] itemNames = (String[])request.getAttribute("itemNames");
		String content = (String)request.getAttribute("content");
		int numResultsToSkip = (Integer)request.getAttribute("numResultsToSkip");
		int numResultsToReturn = (Integer)request.getAttribute("numResultsToReturn");
	%>
	<head>
		<title><%=request.getAttribute("title") %></title>
		<script type="text/javascript" src="autosuggest.js"></script>
		<link rel="stylesheet" type="text/css" href="autosuggest.css" />
		<script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("search_input"));        
            }
        </script>
	</head>
	<body>
		<form name="search1" action="/eBay/search" method="GET">
			<table border="0" align="center" style="padding-top:50px">
                <tr><td align="center"><img alt="Ebay" src="ebay.png"/></td></tr>
				<tr>
					<td align="center" >
        				<div id="search_slide" >
							<input type="text" name="content" id="search_input" value="" />
							<input type="hidden" name="numResultsToSkip" value="0">
							<input type="hidden" name="numResultsToReturn" value="20">
							<input type="submit" name="submit" id="search_submit" value="Search">
						</div>
					</td>
				</tr>
			</table>	
		</form>

		<!-- <div>The total number of results is: <%=itemIDs.length %></div> -->
		<ul id="list">
			<% for (int i=0; i<itemIDs.length; i++) {%>
				<li><a href="item?itemID=<%=itemIDs[i] %>"><%=itemIDs[i] %></a>&nbsp;&nbsp;&nbsp;<%=itemNames[i] %></li>	
			<%} %>
		</ul>

		<%
		if (itemIDs.length > 0)
		{
		%>
		<table>
			<tr>
				<td>
					<%
					if (numResultsToSkip > 0)
					{
						int newSkip = numResultsToSkip - numResultsToReturn;
						if (newSkip < 0)
							newSkip = 0;
					%>
					<a href="search?content=<%= content %>&amp;numResultsToSkip=<%= newSkip %>&amp;numResultsToReturn=<%= numResultsToReturn %>"> Previous </a>
					<%}%>
				</td>
				<td>
					<%
					if (itemIDs.length != 0 && numResultsToReturn <= itemIDs.length && (numResultsToReturn > 0 || numResultsToSkip > 0))
					{
					%>
					<a href="search?content=<%= content %>&amp;numResultsToSkip=<%= numResultsToSkip + numResultsToReturn %>&amp;numResultsToReturn=<%= numResultsToReturn %>"> Next </a>
					<%}%>
				</td>
			</tr>
		</table>
		<%
		} else 
		{
			if (numResultsToSkip > 0)
			{
				int newSkip = numResultsToSkip - numResultsToReturn;
				if (newSkip < 0)
					newSkip = 0;
		%>
		<a class="next_prev" href="search?content=<%= content%>&numResultsToSkip=<%= newSkip%>&numResultsToReturn=<%= numResultsToReturn%>"> Previous </a>
 		<% }
 		} %>

	</body>	
</html>