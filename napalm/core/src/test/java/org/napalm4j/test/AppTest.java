package org.napalm4j.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import org.napalm4j.Napalm;
import org.napalm4j.NapalmApplication;

/**
 * Unit test for simple App.
 */
@Path("/")
public class AppTest implements NapalmApplication {

    @GET
    public String get() {
        return "hi";
    }

    public static void main( String[] args )
    {
        new Napalm().run(new AppTest(),8080);
    }
    
}
