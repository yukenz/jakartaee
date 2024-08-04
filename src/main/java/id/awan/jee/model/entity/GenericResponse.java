package id.awan.jee.model.entity;

import jakarta.ws.rs.core.Response;

public record GenericResponse(
        String statusCode,
        String statusMessage,
        Object data
) {


    public static Response sysErr(Throwable e) {
        GenericResponse entity = new GenericResponse("01", e.getMessage(), null);
        return Response.serverError().entity(entity).build();
    }

    public static Response ok(Object data) {
        GenericResponse entity = new GenericResponse("00", "success", data);
        return Response.ok().entity(entity).build();
    }
}
