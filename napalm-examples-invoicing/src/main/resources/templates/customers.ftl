<#import "layout.ftl" as layout>
<@layout.layout>
<script>
$(document).ready(function() 
    { 
        $("#myTable").tablesorter(); 
    } 
);     
</script>

<div class="header">
<h3>Customers</h3>
</div>
<div class="content">

<table id="myTable" class="tablesorter"> 
<thead> 
<tr> 
    <th>Last Name</th> 
    <th>First Name</th> 
    <th>Email</th> 
    <th>State</th> 
</tr> 
</thead> 
<tbody> 
<#list customers as customer>
<tr> 
    <td>${customer.firstName}</td> 
    <td>${customer.lastName}</td> 
    <td>${customer.email}</td> 
    <td>${customer.state}</td> 
</tr> 
</#list>
</tbody> 
</table> 

</div>
</@layout.layout>