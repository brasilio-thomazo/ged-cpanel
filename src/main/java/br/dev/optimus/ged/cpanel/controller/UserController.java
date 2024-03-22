package br.dev.optimus.ged.cpanel.controller;

import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse;

import br.dev.optimus.ged.cpanel.model.User;
import br.dev.optimus.ged.cpanel.repository.UserRepository;
import br.dev.optimus.ged.cpanel.request.UserRequest;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/api/users")
public class UserController implements ModelController<User, UserRequest, UUID> {

    @Inject
    UserRepository userRepository;

    @Override
    @GET
    public Uni<List<User>> index() {
        return userRepository.listAll();
    }

    @Override
    @WithTransaction
    @POST
    public Uni<RestResponse<User>> store(UserRequest request) {
        return userRepository.store(request).map(user -> RestResponse.status(RestResponse.Status.CREATED, user));
    }

    @Override
    @GET
    @Path("/{id}")
    public Uni<User> show(UUID id) {
        return userRepository.show(id);
    }

    @Override
    @WithTransaction
    @PUT
    @Path("/{id}")
    public Uni<User> update(UUID id, UserRequest request) {
        return userRepository.update(id, request);
    }

    @Override
    @WithTransaction
    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Object>> destroy(UUID id) {
        return userRepository.destroy(id).map(v -> RestResponse.status(RestResponse.Status.NO_CONTENT, v));
    }

}
