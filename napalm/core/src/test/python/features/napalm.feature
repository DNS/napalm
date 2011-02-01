Feature: Napalm Core functionality
	A user should be able to access REST resources
	served by a Napalm application
	
	Background:
		Given base URL 'http://localhost:8080'
	
	Scenario Outline: Base REST methods
		When send a GET to '<url>'
		Then expect HTTP <code>
		And expect <type> equivalent to '<content>'
		
		Examples:
 			| url 	    | code  | type  | content  	|
			| /			| 200	| text	| hi 		| #plain text
			| /user		| 200	| JSON	| {"napalmTestUser":[{"id":"0","name":"User 0"},{"id":"1","name":"User 1"},{"id":"2","name":"User 2"}]}	| #JSON payload			 	
			