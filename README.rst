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
	
:Forum:
	http://groups.google.com/group/napalm4j		
	

Connecting to databases
=======================

Tired of fiddling with JNDI and datasources? No problem, just specify the JDBC connection string
in the '<url>,<user>,<password>' format (e.g. *"jdbc:mysql://localhost:3306/napalm,user,password"*)
and Napalm with auto-create a DataSource for you and wire it into the context::

	@Service
	@Path("/")
	@Produces(MediaType.TEXT_PLAIN)
	public class NapalmTestApp {
	
		@Resource(name = "db")
		private DataSource db;
	
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
* Freemarker (TODO)
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

* add common config file (napalm.yml) to allow externalizing things like ports, DB connections, etc.
* add easy creation of JNDI datasources and resources via napalm.yml
* easy integration of JPA / Hibernate with full @Transactional support pre-configured
* integrate Spring Security
* JHaml template plugin (HAML being the most cutting edge template technology right now)

Long-term development plans:

* allow auto-creation of REST services for JPA entities, similar to some of the libraries avaliable for Python Django

		