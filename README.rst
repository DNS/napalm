=================================
NAPALM : Death to Java complexity
=================================

.. image:: Napalm/raw/master/napalm.png

A Java web microframework, inspired by Sinatra (http://www.sinatrarb.com).

Jetty, Spring and JAX-RS all rolled into one super simple package.
All REST, all the time.

No *web.xml*. No *beans.xml*. **No XML, period.**

Grind this in your beat::
	
	@Service @Path("/") 
	public class NapalmTest  {

	    @GET
	    public String sayHi() {
	        return "hi";
	    }
	
	    public static void main( String[] args )
	    {
	        Napalm.run(8080,NapalmTest.class);
	    }
	    
	}
	
And blast it::

	== Napalm has taken the stage...
	>> Listening on 0.0.0.0:8080
	
Links
========	
	
:License:
	http://www.apache.org/licenses/LICENSE-2.0.html	
	
:Blog:	
	http://napalm4j.blogspot.com
	
:Forum:
	http://groups.google.com/group/napalm4j		
	
Database access
===============

Connecting to databases
^^^^^^^^^^^^^^^^^^^^^^^

Tired of fiddling with JNDI and datasources? No problem, just specify the JDBC connection string
in the '<url>,<user>,<password>' format (e.g. *"jdbc:mysql://localhost:3306/napalm,user,password"*)
and Napalm with auto-create a DataSource for you and wire it into the context::

	@Service @Path("/") @Produces(MediaType.TEXT_PLAIN)
	public class NapalmTestApp {
	
		@Resource(name = "db") private DataSource db;
	
		@GET @Path("/db") 
		public String getDbInfo() {
			return String.valueOf(new JdbcTemplate(db).queryForInt("SELECT COUNT(*) FROM INFORMATION_SCHEMA.CATALOGS"));
		}
	
		public static void main(String[] args) {
			Napalm.addResource("db", "jdbc:h2:mem:db1");
			Napalm.run(8080, NapalmTestApp.class);
		}
	}

Shortly we will add support for auto-loading resources from an external *napalm.yml* file.

Simplifying raw DB access via JDBC (without the need for JPA)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

JPA is a great ORM, but sometimes (especially for smaller apps) it's overkill. Napalm provides a way of easily integrating
raw SQL queries (similar to MyBatis, but without the XML verbosity).

In the classpath */sql* resources folder, e.g.:

	src/main/resources/sql
	
you can create any number of YAML files (*.yml) in the following format:
::
	
    allUsers: |
        SELECT *
        FROM USERS
  
    users:
       - SELECT * FROM USERS
       - byUsername: WHERE USERNAME = ?
       - byBirthDate: WHERE BIRTH_DATE = ?
       
This will get parsed by our SQL engine to create 4 separate named SQL queries:

* *allUsers: SELECT * FROM USERS*
* *users: SELECT * FROM USERS*
* *users.byUsername =  SELECT * FROM USERS WHERE USERNAME = ?*       
* *users.byBirthDate =  SELECT * FROM USERS WHERE WHERE BIRTH_DATE = ?*

The second hierarchical YAML format allows you to maintain a common SELECT (as the first string in the YAML list) and then just define
additional WHERE clauses for it. This way you need to update the SELECT only in one place in case of future maintenance and all the
*child* queries will inherit the change automatically.

Then you can just refer to those queries by name when passing them into a template engine (e.g. Freemarker in the example below):
::

	@Resource(name = "db") private DataSource db;
	@Autowired private NapalmFreeMarker freemarker;
	@Autowired private SqlQueryEngine sql;
	
	@GET() @Path("/db") @Produces(MediaType.TEXT_PLAIN)
	public String db(@PathParam("user") String user) {
		return freemarker.render("db.txt", sql.queryForList(db, "tables"), sql.queryForList(db, "columns"));
	}
	
Parallel query execution
^^^^^^^^^^^^^^^^^^^^^^^^

In the example above we see 2 separate SQL queries defined as data sources for the final template. Napalm will
automatically detect that and **parallelize those queries**. They will all run concurrently and their results
will be merged into the final output, in a simple *Map/Reduce* fashion.

You get dirt-simple concurrency and basic Map/Reduce without any additional fuss.	

.. note:: This is not SQL specific
   This parallel functionality is generic and not tied to SQL. In the future we plan to provide connectors for other
   data sources, e.g. JPA, NoSQL (MongoDB, CouchDB, etc.) and they will all benefit from the same built-in functionality.
        
Static resources
================

All static resources are placed in the classpath */static* folder, e.g.

	src/main/resources/static
	
If at least one of your initial JAX-RS classes is hooked up to the root context */*, e.g.

	@Path("/")
	
then all static resources will be served from the */static* context, e.g.

	http://localhost:8080/static/index.html
	
However, if all your initial JAX-RS classes are already hooked up to a child *non-root* context, e.g.	

	@Path("/services")
 	        
then the static servlet will be automatically configured to serve resources from the root */* instead, e.g.

	http://localhost:8080/index.html 	        
        
That way you can easily control if Napalm is your main app (e.g. by using one of our template plugins to serve the UI) 
or whether it is just a REST backend for an app served via static resources, e.g. a *jQuery* or *JavaScriptMVC* application.

Testing with BDD
================

Java is a great language for writing performant server-side applications.

It is however, a *horrible* language for writing integration unit tests (especially for REST apps, which are so easily testable). 
A terse, tight dynamic language like Python or Ruby is a much better choice for this. 

Once you experience BDD with tools such as Freshen, Lettuce or Cucumber, it is hard to go back to testing with JUnit again.

Hence, Napalm provides a simple convenience class *BddTester* that allows to launch BDD stories
from a single JUnit test (for better integration with existing build systems).

:Example unit test:
	https://github.com/jacek99/Napalm/blob/master/napalm/core/src/test/java/com/github/napalm/test/CoreTest.java
	
:Example BDD tests:
	https://github.com/jacek99/Napalm/blob/master/napalm/core/src/test/python/features/napalm.feature
	https://github.com/jacek99/Napalm/blob/master/napalm/core/src/test/python/features/steps.py
	
**JUnit = legacy**. The faster you try out BDD the better off you will be.	
     
Template Plugins
================
Napalm apps are supposed to be self-contained JARs run via a simple "java -jar myapp.jar" command,
with embedded Jetty serving the content.

Hence, all content is supposed to be embedded in the JAR and thus all templates are placed directly
in the classpath and not in an external folder.

If using Maven/Gradle/Buildr, you would put them all under the standard
	
	src/main/resources/templates
	
folder

Available plugins
^^^^^^^^^^^^^^^^^

* Velocity: https://github.com/jacek99/Napalm/tree/master/napalm/napalm-velocity
* Freemarker https://github.com/jacek99/Napalm/tree/master/napalm/napalm-freemarker
* JMustache (TODO)
* JHaml (TODO)
* Scalate (TODO)    

Maven
================

Get it via our custom Maven repo::	
	
    <dependency> 
            <groupId>napalm</groupId> 
            <artifactId>napalm</artifactId> 
            <version>0.1-SNAPSHOT</version> 
    </dependency>
    
    <repositories> 
        <repository> 
                <id>javabuilders</id> 
                <url>http://javabuilders.googlecode.com/svn/repo</url> 
        </repository> 
    </repositories>

TODO
====

Short-term development plans:

* integrate Spring Security
* auto-create REST services from database DDL definition (perfect for simple apps where Napalm is a REST backend for a Javascript app)
* JHaml integration (if possible)

Planned integration with non-Java tools:

* CoffeeScript (with node.js installed): http://jashkenas.github.com/coffee-script/
* Pyjamas : http://pyjs.org/
		