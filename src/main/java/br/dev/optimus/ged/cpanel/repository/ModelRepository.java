package br.dev.optimus.ged.cpanel.repository;

import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;

public interface ModelRepository<M, R, ID> {
    public Uni<Boolean> existsById(ID id);

    public Uni<M> store(@Valid R request);

    public Uni<M> update(ID id, @Valid R request);

    public Uni<M> show(ID id);

    public Uni<Object> destroy(ID id);
}
