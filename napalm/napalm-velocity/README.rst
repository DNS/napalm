Napalm Velocity Plugin
======================

Just include *NapalmVelocityEngine.class* in the list of apps to run::

	@Service
	@Path("/")
	public class NapalmVelocityTest {
	
		@Autowired
		private NapalmVelocityEngine velocity;
	
		@GET
		@Produces(MediaType.TEXT_PLAIN)
		public String hi() {
			return velocity.render("NapalmVelocityTest.vm", ImmutableMap.of("ip", "0.0.0.0"));
		}
	
		public static void main(String[] args) {
			Napalm.run(8080, NapalmVelocityTest.class, NapalmVelocityEngine.class);
		}
	}
	
	
Maven
^^^^^
::

    <dependency> 
            <groupId>napalm</groupId> 
            <artifactId>napalm-velocity</artifactId> 
            <version>0.1-SNAPSHOT</version> 
    </dependency>

