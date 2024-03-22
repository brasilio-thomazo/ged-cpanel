package br.dev.optimus.ged.cpanel.controller;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;
import io.smallrye.mutiny.Uni;

public interface ModelController<T, R, ID> {
    public Uni<List<T>> index();

    public Uni<RestResponse<T>> store(R request);

    public Uni<T> show(ID id);

    public Uni<T> update(ID id, R request);

    public Uni<RestResponse<Object>> destroy(ID id);
}
