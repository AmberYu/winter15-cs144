<!DOCTYPE html>
<html>
	<% 
		String[] itemIDs = (String[])request.getAttribute("itemIDs");
		String[] itemNames = (String[])request.getAttribute("itemNames");
		String q = (String)request.getAttribute("q");
		int numResultsToSkip = (Integer)request.getAttribute("numResultsToSkip");
		int numResultsToReturn = (Integer)request.getAttribute("numResultsToReturn");
	%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title><%=request.getAttribute("title") %></title>
		<link href="style.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="autosuggest.js"></script>
		<link rel="stylesheet" type="text/css" href="autosuggest.css" />
		<script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("search_input"));        
            }
        </script>
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
    <tr><td align="center"><img alt="Ebay" src="ebay.png"/></td></tr>
    <div id="middlePan">
  	<div id="middletopPan">

		<form name="search1" action="/eBay/search" method="GET">
			<label>Keyword Search </label>
			<input type="text" name="q" id="search_input" value="" placeholder = "Type in keywords here..."/>
			<input type="hidden" name="numResultsToSkip" value="0">
			<input type="hidden" name="numResultsToReturn" value="20">
			<input type="submit" name="submit" id="search_submit" class="button" value="Go" />
		</form>
		</div>
    	</div>

		<!-- <div>The total number of results is: <%=itemIDs.length %></div> -->
		<% if (itemIDs.length == 0) {%>
			<h3><%= request.getAttribute("Error")%></h3>
		<%} else {%>
			<h3> Keyword search result: </h3>
		<%}%>

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
					<a href="search?q=<%= q %>&amp;numResultsToSkip=<%= newSkip %>&amp;numResultsToReturn=<%= numResultsToReturn %>"> Previous </a>
					<%}%>
				</td>
				<td>
					<%
					if (itemIDs.length != 0 && numResultsToReturn <= itemIDs.length && (numResultsToReturn > 0 || numResultsToSkip > 0))
					{
					%>
					<a href="search?q=<%= q %>&amp;numResultsToSkip=<%= numResultsToSkip + numResultsToReturn %>&amp;numResultsToReturn=<%= numResultsToReturn %>"> Next </a>
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
		<a class="next_prev" href="search?q=<%= q%>&numResultsToSkip=<%= newSkip%>&numResultsToReturn=<%= numResultsToReturn%>"> Previous </a>
 		<% }
 		} %>
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