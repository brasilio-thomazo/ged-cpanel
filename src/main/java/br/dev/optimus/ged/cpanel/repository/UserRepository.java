package br.dev.optimus.ged.cpanel.repository;

import java.util.NoSuchElementException;
import java.util.UUID;

import br.dev.optimus.ged.cpanel.exception.UniqueException;
import br.dev.optimus.ged.cpanel.model.User;
import br.dev.optimus.ged.cpanel.request.UserRequest;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID>, ModelRepository<User, UserRequest, UUID> {
    @Override
    public Uni<Boolean> existsById(UUID id) {
        return count("id", id).map(count -> count > 0);
    }

    public Uni<User> findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public Uni<Boolean> existsByUsername(String username) {
        return count("username", username).map(count -> count > 0);
    }

    public Uni<Boolean> existsByUsernameAndNotId(String username, UUID id) {
        return count("username = ?1 and id != ?2", username, id).map(count -> count > 0);
    }

    public Uni<User> findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public Uni<Boolean> existsByEmail(String email) {
        return count("email", email).map(count -> count > 0);
    }

    public Uni<Boolean> existsByEmailAndNotId(String email, UUID id) {
        return count("email = ?1 and id != ?2", email, id).map(count -> count > 0);
    }

    @Override
    public Uni<User> store(UserRequest request) {
        return existsByUsername(request.username())
                .flatMap(exists -> {
                    if (exists) {
                        return Uni.createFrom().failure(new UniqueException("Username already exists", "username"));
                    }
                    return existsByEmail(request.email());
                })
                .flatMap(exists -> {
                    if (exists) {
                        return Uni.createFrom().failure(new UniqueException("Email already exists", "email"));
                    }
                    return persistAndFlush(request.toUser());
                });
    }

    @Override
    public Uni<User> update(UUID id, UserRequest request) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("User not found"));
            }
            return existsByUsernameAndNotId(request.username(), id).flatMap(existsUsername -> {
                if (existsUsername) {
                    return Uni.createFrom().failure(new UniqueException("Username already exists", "username"));
                }
                return existsByEmailAndNotId(request.email(), id).flatMap(emailExists -> {
                    if (emailExists) {
                        return Uni.createFrom().failure(new UniqueException("Email already exists", "email"));
                    }
                    return findById(id).flatMap(this::persistAndFlush);
                });
            });
        });
    }

    @Override
    public Uni<User> show(UUID id) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("User not found"));
            }
            return findById(id);
        });
    }

    @Override
    public Uni<Object> destroy(UUID id) {
        return existsById(id).flatMap(exists -> {
            if (!exists) {
                return Uni.createFrom().failure(new NoSuchElementException("User not found"));
            }
            return deleteById(id);
        });
    }
}
