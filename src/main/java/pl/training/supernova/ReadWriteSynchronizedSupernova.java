package pl.training.supernova;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class ReadWriteSynchronizedSupernova implements Supernova<PersonRow, Long> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Supernova<PersonRow, Long> supernova;

    @Override
    public void insert(PersonRow row) {
        lock.writeLock().lock();
        try {
            supernova.insert(row);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Optional<PersonRow> getById(Long id) {
        Optional<PersonRow> result;
        lock.readLock().lock();
        try {
            result = supernova.getById(id);
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        supernova.close();
    }

}
