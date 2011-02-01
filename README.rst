=================================
NAPALM : Death to Java complexity
=================================

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
	
Overview
========	
	
:License:
	Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0.html	
	
:Forum:
	http://groups.google.com/group/napalm4j		
	
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

		