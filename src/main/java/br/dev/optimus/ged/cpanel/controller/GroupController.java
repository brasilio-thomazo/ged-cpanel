package br.dev.optimus.ged.cpanel.controller;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import br.dev.optimus.ged.cpanel.model.Group;
import br.dev.optimus.ged.cpanel.repository.GroupRepository;
import br.dev.optimus.ged.cpanel.request.GroupRequest;
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
@Path("/api/groups")
public class GroupController implements ModelController<Group, GroupRequest, Long> {
    @Inject
    GroupRepository groupRepository;

    @Override
    @GET
    public Uni<List<Group>> index() {
        return groupRepository.listAll();
    }

    @Override
    @POST
    @WithTransaction
    public Uni<RestResponse<Group>> store(GroupRequest request) {
        return groupRepository
                .store(request)
                .map(group -> RestResponse.status(RestResponse.Status.CREATED, group));
    }

    @Override
    @GET
    @Path("/{id}")
    public Uni<Group> show(Long id) {
        return groupRepository.show(id);
    }

    @Override
    @PUT
    @Path("/{id}")
    @WithTransaction
    public Uni<Group> update(Long id, GroupRequest request) {
        return groupRepository.update(id, request);
    }

    @Override
    @DELETE
    @Path("/{id}")
    @WithTransaction
    public Uni<RestResponse<Object>> destroy(Long id) {
        return groupRepository
                .destroy(id)
                .map(response -> RestResponse.status(RestResponse.Status.NO_CONTENT, response));
    }

}
