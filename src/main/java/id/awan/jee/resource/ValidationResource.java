package id.awan.jee.resource;

import id.awan.jee.interceptor.annotation.SafeHttpInvoke;
import id.awan.jee.model.entity.Address;
import id.awan.jee.model.entity.GenericResponse;
import id.awan.jee.model.entity.UsernamePasswordCredential;
import id.awan.jee.service.ValidationService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/validation")
@SafeHttpInvoke
public class ValidationResource {


    @Inject
    private ValidationService validationService;


    @POST
    @Path("/address")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response valdiationAddress(
            Address address
    ) {


        String violationMessages = validationService.getViolationMessages(address);

        if (violationMessages != null) {
            GenericResponse payload = new GenericResponse("400", violationMessages, null);
            return Response.status(Response.Status.BAD_REQUEST).entity(payload).build();
        }

        return Response.ok().entity("Address Valid").build();
    }

    @POST
    @Path("/basic-auth")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response basicAuthentication(
            UsernamePasswordCredential usernamePasswordCredential
    ) {

        String violationMessages = validationService.getViolationMessages(usernamePasswordCredential);

        if (violationMessages != null) {
            GenericResponse payload = new GenericResponse("400", violationMessages, null);
            return Response.status(Response.Status.BAD_REQUEST).entity(payload).build();
        }

        return Response.ok().entity("Credential Valid").build();
    }
}