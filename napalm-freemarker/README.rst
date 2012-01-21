Napalm FreeMarker Plugin
========================

.. highlight:: java

Just include *NapalmFreeMarker.class* in the list of apps to 
run::

	@Service
	@Path("/")
	public class NapalmFreeMarkerTestApp {
	
		@Autowired
		private NapalmFreeMarker freemarker;
	
	    @GET()
	    @Path("/{user}")
	    @Produces(MediaType.TEXT_PLAIN)
	    public String hi(@PathParam("user") String user) {
	    	return freemarker("user.txt", "user", user, "ip", "0.0.0.0");
	    }
	    
	    public static void main(String[] args) {
	    	Napalm.run(8080, NapalmFreeMarkerTestApp.class, NapalmFreeMarker.class);
	    }
	}

	
and get your Napalm REST fix instantly:

	curl http://localhost:8080/johndoe
	
	Hi there johndoe
	Napalm says hi from 0.0.0.0	
	
	
Maven
^^^^^
::

    <dependency> 
            <groupId>napalm</groupId> 
            <artifactId>napalm-freemarker</artifactId> 
            <version>0.1-SNAPSHOT</version> 
    </dependency>

