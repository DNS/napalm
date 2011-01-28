package org.napalm4j.test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.napalm4j.Napalm;
import org.springframework.stereotype.Service;

/**
 * Unit test for simple App.
 */
@Service @Path("/") 
public class NapalmTest  {

    @GET 
    public String get() {
        return "hi";
    }

    public static void main( String[] args )
    {
        Napalm.run(8080,NapalmTest.class);
    }
    
}
