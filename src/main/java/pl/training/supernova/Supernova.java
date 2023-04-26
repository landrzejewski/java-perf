package pl.training.supernova;

import lombok.SneakyThrows;

import java.util.Optional;

public interface Supernova<R extends Row<I>, I> extends AutoCloseable {

    @SneakyThrows
    void insert(R row);

    @SneakyThrows
    Optional<R> getById(I id);

}
