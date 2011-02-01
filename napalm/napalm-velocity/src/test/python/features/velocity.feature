Feature: Napalm Velocity support
    As a user I want to be able to access REST resources
    That serve pages generated via the Apache Velocity template engine
    
    Background:
        Given base URL 'http://localhost:8080'
    
    Scenario Outline: Base Velocity REST methods
        When send a GET to '<url>'
        Then expect HTTP <code>
        And expect <type> equivalent to '<content>'
        
        Examples:
            | url       | code  | type  | content   |
            | /jacekf   | 200   | text  | Hi there jacekf\nNapalm says hi from 0.0.0.0          |
            | /test     | 200   | text  | Hi there test\nNapalm says hi from 0.0.0.0            |  
