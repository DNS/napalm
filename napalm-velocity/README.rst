Napalm Velocity Plugin
======================

.. highlight:: java

Just include *NapalmVelocity.class* in the list of apps to 
run::

	@Service
	@Path("/")
	public class NapalmVelocityTest {
	
		@Autowired
		private NapalmVelocity velocity;
	
	    @GET()
	    @Path("/{user}")
	    @Produces(MediaType.TEXT_PLAIN)
	    public String hi(@PathParam("user") String user) {
	    	return velocity.render("NapalmVelocityTest.vm", "user", user, "ip", "0.0.0.0");
	    }
	    
	    public static void main(String[] args) {
	    	Napalm.run(8080, NapalmVelocityTest.class, NapalmVelocity.class);
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
            <artifactId>napalm-velocity</artifactId> 
            <version>0.1-SNAPSHOT</version> 
    </dependency>

