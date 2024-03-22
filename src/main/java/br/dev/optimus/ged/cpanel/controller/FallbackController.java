package br.dev.optimus.ged.cpanel.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jboss.resteasy.reactive.RestResponse;

import br.dev.optimus.ged.cpanel.exception.UniqueException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class FallbackController implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        return mapException(exception);
    }

    private Response mapException(Exception e) {
        if (e instanceof UniqueException ex) {
            var fallback = new Fallback(ex.getMessage(), Map.of(ex.getField(), List.of(ex.getMessage())));
            return RestResponse.status(Response.Status.CONFLICT, fallback).toResponse();
        }
        if (e instanceof NoSuchElementException) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    }

    public record Fallback(
            String message,
            Map<String, List<String>> errors) {
    }

}
