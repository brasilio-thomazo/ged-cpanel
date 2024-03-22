package br.dev.optimus.ged.cpanel.repository;

import java.util.HashSet;
import java.util.NoSuchElementException;

import br.dev.optimus.ged.cpanel.exception.UniqueException;
import br.dev.optimus.ged.cpanel.model.Group;
import br.dev.optimus.ged.cpanel.request.GroupRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@ApplicationScoped
public class GroupRepository implements PanacheRepositoryBase<Group, Long>, ModelRepository<Group, GroupRequest, Long> {
    @Inject
    RoleRepository roleRepository;

    @Override
    public Uni<Boolean> existsById(Long id) {
        return count("id", id).map(count -> count > 0);
    }

    public Uni<Group> findByName(String name) {
        return find("name", name).firstResult();
    }

    public Uni<Boolean> existsByName(String name) {
        return count("name", name).map(count -> count > 0);
    }

    @Override
    public Uni<Group> store(@Valid GroupRequest request) {
        return existsByName(request.name()).flatMap(exists -> {
            if (exists) {
                return Uni.createFrom().failure(new UniqueException("Name already exists", "name"));
            }
            var group = request.toGroup();
            if (request.roles() != null) {
                return roleRepository.find("id in ?1", request.roles()).list().flatMap(roleList -> {
                    group.getRoles().addAll(roleList);
                    return persistAndFlush(group);
                });
            }
            return persistAndFlush(group);
        });
    }

    @Override
    public Uni<Group> update(Long id, @Valid GroupRequest request) {
        return existsById(id).flatMap(idCheck -> {
            if (!idCheck) {
                return Uni.createFrom().failure(new NoSuchElementException("Group not found"));
            }
            return existsByName(request.name()).flatMap(nameCheck -> {
                if (nameCheck) {
                    return Uni.createFrom().failure(new UniqueException("Name already exists", "name"));
                }
                return findById(id).flatMap(group -> {
                    group.setName(request.name());
                    group.setDescription(request.description());
                    group.getRoles().clear();
                    if (request.roles() != null) {
                        return roleRepository.find("id in ?1", new HashSet<>(request.roles())).list()
                                .flatMap(roleList -> {
                                    group.getRoles().addAll(roleList);
                                    return persistAndFlush(group);
                                });
                    }
                    return persistAndFlush(group);
                });
            });
        });
    }

    @Override
    public Uni<Group> show(Long id) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("Group not found"));
            }
            return findById(id);
        });
    }

    @Override
    public Uni<Object> destroy(Long id) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("Group not found"));
            }
            return deleteById(id);
        });
    }

}
