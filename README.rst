======
NAPALM
======
------------------------
Death to Java complexity
------------------------

A Java web-microframework, inspired by _Sinatra http://sinatrarb.com

Jetty, Spring and Jersey all rolled into one super simple package.
No web.xml. No beans.xml. No XML. Period.

Grind this in your beat::
	
	@Service @Path("/") 
	public class NapalmTest  {

	    @GET @Produces(MediaType.TEXT_PLAIN)
	    public String get() {
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
		
