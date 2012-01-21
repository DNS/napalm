<#macro layout>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--

	terrafirma1.0 by nodethirtythree design
	http://www.nodethirtythree.com

-->
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=iso-8859-1" />
<title>Napalm Examples: Invoicing App</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link rel="stylesheet" type="text/css" href="static/default.css" />
<link rel="stylesheet" type="text/css" href="static/tablesorter.css" />
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.0/jquery.min.js"></script>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.9/jquery-ui.min.js"></script>
<script type="text/javascript" src="static/js/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="static/js/jquery.tablesorter.pager.js"></script>
</head>
<body>

<div id="outer">

<div id="inner">

<div id="header">
<h1><span>Invoicing Application</span></h1>
<h2>by Napalm</h2>
</div>

<div id="splash"></div>

<div id="menu">
<ul>
	<li class="first"><a href="/">Home</a></li>
	<li><a href="/customer">Customers</a></li>
	<li><a href="/product">Products</a></li>
	<li><a href="/order">Orders</a></li>
	<li><a href="/invoice">Invoices</a></li>
	<li><a href="/report">Reports</a></li>
</ul>

<div id="date">Served by Napalm</div>
</div>


<div id="primarycontent"><!-- primary content start --> <#nested>

</div>

<!-- primary content end --></div>

<div id="secondarycontent"><!-- secondary content start -->

<h3>User</h3>
<div class="content">
<p>Not logged in</p>
</div>

<h3>Topics</h3>
<div class="content">
<ul class="linklist">
	<li class="first"><a href="https://github.com/jacek99/Napalm">Napalm
	Documentation</a></li>
	<li><a
		href="http://napalm4j.blogspot.com/2011/02/connecting-to-database-with-napalm.html">Connecting
	to databases using Napalm</a></li>
</ul>
</div>

<!-- secondary content end --></div>

<div id="footer">&copy; Napalm Invocing App. All rights reserved.
Design by <a href="http://www.nodethirtythree.com/">NodeThirtyThree</a>.

</div>

</div>

</div>

</body>
</html>
</#macro>
