package br.dev.optimus.ged.cpanel.repository;

import java.util.NoSuchElementException;

import br.dev.optimus.ged.cpanel.exception.UniqueException;
import br.dev.optimus.ged.cpanel.model.Role;
import br.dev.optimus.ged.cpanel.request.RoleRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepositoryBase<Role, Long>, ModelRepository<Role, RoleRequest, Long> {

    @Override
    public Uni<Boolean> existsById(Long id) {
        return count("id", id).map(count -> count > 0);
    }

    public Uni<Role> findByPermission(String permission) {
        return find("permission", permission).firstResult();
    }

    public Uni<Boolean> existsByPermission(String permission) {
        return count("permission", permission).map(count -> count > 0);
    }

    public Uni<Boolean> existsByPermissionAndNotId(String permission, Long id) {
        return count("permission = ?1 and id != ?2", permission, id).map(count -> count > 0);
    }

    @Override
    public Uni<Role> store(RoleRequest request) {
        return existsByPermission(request.permission())
                .flatMap(exists -> {
                    if (exists) {
                        return Uni.createFrom().failure(new UniqueException("Permission already exists", "permission"));
                    }
                    return persistAndFlush(request.toRole());
                });
    }

    @Override
    public Uni<Role> update(Long id, RoleRequest request) {
        return existsById(id).flatMap(idCheck -> {
            if (!idCheck) {
                return Uni.createFrom().failure(new NoSuchElementException("Role not found"));
            }
            return existsByPermissionAndNotId(request.permission(), id).flatMap(permissionCheck -> {
                if (permissionCheck) {
                    return Uni.createFrom().failure(new UniqueException("Permission already exists", "permission"));
                }
                return findById(id).flatMap(this::persistAndFlush);
            });
        });
    }

    @Override
    public Uni<Role> show(Long id) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("Role not found"));
            }
            return findById(id);
        });
    }

    @Override
    public Uni<Object> destroy(Long id) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("Role not found"));
            }
            return deleteById(id);
        });
    }

}
