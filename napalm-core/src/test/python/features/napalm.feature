Feature: Napalm Core functionality
	A user should be able to access REST resources
	served by a Napalm application
	
	Scenario Outline: REST at root with dynamic content
		Given base URL 'http://localhost:8080'
		When send a GET to '<url>'
		Then expect HTTP <code>
		And expect <type> equivalent to '<content>'
		
		Examples:
 			| url 	    | code  | type  | content  	|
			| /         | 200	| text	| hi 		| #plain text
			| /user     | 200	| JSON	| {"napalmTestUser":[{"id":"0","name":"User 0"},{"id":"1","name":"User 1"},{"id":"2","name":"User 2"}]}	| #JSON payload
			| /db       | 200	| text	| 1 		| #test the DB connection
		
	Scenario Outline: Static at root with /services dynamic content
		Given base URL 'http://localhost:8081/services'
		When send a GET to '<url>'
		Then expect HTTP <code>
		And expect <type> equivalent to '<content>'
		
		Examples:
 			| url 	    | code  | type  | content  	|
			| /         | 200	| text	| hi 		| #plain text
			| /user     | 200	| JSON	| {"napalmTestUser":[{"id":"0","name":"User 0"},{"id":"1","name":"User 1"},{"id":"2","name":"User 2"}]}	| #JSON payload
			| /db       | 200	| text	| 1 		| #test the DB connection		
			
	Scenario: REST at root with static content
		Given base URL 'http://localhost:8080'
		When send a GET to '/static/'
		Then expect HTTP 200
		And expect text equivalent to
		    """
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Napalm Static</title>
</head>
<body>
Napalm Static
</body>
</html>
		    """
		    	
	Scenario: Static at root with static content
		Given base URL 'http://localhost:8081'
		When send a GET to '/'
		Then expect HTTP 200
		And expect text equivalent to
		    """
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Napalm Static</title>
</head>
<body>
Napalm Static
</body>
</html>
		    """		    	
				 	
			