package id.awan.jee.resource;

import id.awan.jee.event.publisher.CredentialPublisher;
import id.awan.jee.interceptor.annotation.SafeHttpInvoke;
import id.awan.jee.model.entity.GenericResponse;
import id.awan.jee.model.entity.UsernamePasswordCredential;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/credential")
@SafeHttpInvoke
public class CredentialResource {


    @Inject
    private CredentialPublisher credentialPublisher;


    @POST
    @Path("/basic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUsernamePasswordCredential(
            UsernamePasswordCredential credential
    ) {
        credentialPublisher.publish(credential);
        return GenericResponse.ok("Credential Added");
    }

}