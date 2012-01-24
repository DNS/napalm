Using step definitions from: '../../../../../napalm-core/src/test/python/features/steps'  #reuse same core steps.py

Feature: Napalm Freemarker support
    As a user I want to be able to access REST resources
    That serve pages generated via the Freemarker template engine
    
    Background:
        Given base URL 'http://localhost:8080'
    
    Scenario Outline: Base Velocity REST methods
        When send a GET to '<url>'
        Then expect HTTP <code>
        And expect <type> equivalent to '<content>'
        
        Examples:
            | url       	 | code  | type  | content   |
            | /user/jacekf   | 200   | text  | Hi there jacekf\nNapalm says hi from 0.0.0.0          |
            | /user/test     | 200   | text  | Hi there test\nNapalm says hi from 0.0.0.0            |  
            
    Scenario: Database schema dump
        When send a GET to '/db'
        Then expect HTTP 200
        And expect text equivalent to
        	"""
			Tables:
				CATALOGS
				COLLATIONS
				COLUMNS
				COLUMN_PRIVILEGES
				CONSTANTS
			
			Columns:
				ADMIN
				ALIAS_CATALOG
				ALIAS_NAME
				ALIAS_SCHEMA
				ASC_OR_DESC
			"""
        	
    