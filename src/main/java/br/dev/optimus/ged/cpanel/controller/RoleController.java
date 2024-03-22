package br.dev.optimus.ged.cpanel.controller;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import br.dev.optimus.ged.cpanel.model.Role;
import br.dev.optimus.ged.cpanel.repository.RoleRepository;
import br.dev.optimus.ged.cpanel.request.RoleRequest;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

@ApplicationScoped
@Path("/api/roles")
public class RoleController implements ModelController<Role, RoleRequest, Long> {
    @Inject
    RoleRepository roleRepository;

    @Override
    @GET
    public Uni<List<Role>> index() {
        return roleRepository.listAll();
    }

    @Override
    @POST
    public Uni<RestResponse<Role>> store(RoleRequest request) {
        return roleRepository.store(request).map(role -> RestResponse.status(RestResponse.Status.CREATED, role));
    }

    @Override
    @GET
    @Path("/{id}")
    public Uni<Role> show(Long id) {
        return roleRepository.show(id);
    }

    @Override
    @PUT
    @Path("/{id}")
    public Uni<Role> update(Long id, RoleRequest request) {
        return roleRepository.update(id, request);
    }

    @Override
    @DELETE
    @Path("/{id}")
    public Uni<RestResponse<Object>> destroy(Long id) {
        return roleRepository
                .destroy(id)
                .map(response -> RestResponse.status(RestResponse.Status.NO_CONTENT, response));
    }

}
