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
		
Maven
^^^^^

Get it via Maven::	
	
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
     
		