package pl.training.supernova;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class FlatFileSupernova<R extends Row<I>, I> implements Supernova<R, I> {

    private final LinkedHashMap<I, R> cache;
    private final Map<I, Long> primaryIndex;
    private final R row;
    private final RandomAccessFile randomAccessFile;

    @SneakyThrows
    public FlatFileSupernova(Path filePath, R row, Map<I, Long> primaryIndex, int cacheSize) {
        this.randomAccessFile = new RandomAccessFile(filePath.toFile(), "rw");
        this.row = row;
        this.primaryIndex = primaryIndex;
        cache = new LinkedHashMap<>(cacheSize, 0.75f, true) {

            @Override
            protected boolean removeEldestEntry(Map.Entry<I, R> eldest) {
                return size() > cacheSize;
            }

        };
        init();
    }

    @SneakyThrows
    private void init() {
        if (randomAccessFile.length() != 0) {
            long position = 0;
            while (position <= randomAccessFile.length()) {
                var currentRow = getRow(position);
                primaryIndex.put(currentRow.getId(), position);
                position += row.getSize();
            }
        }
    }

    @Override
    @SneakyThrows
    public void insert(R row) {
        var id = row.getId();
        var cachedPosition = primaryIndex.get(id);
        var position = cachedPosition != null ? cachedPosition : getEndPosition();
        primaryIndex.put(id, position);
        addRow(position, row);
        cache.put(id, row);
    }

    @Override
    @SneakyThrows
    public Optional<R> getById(I id) {
        if (cache.containsKey(id)) {
            return Optional.of(cache.get(id));
        }
        var result = getRowPosition(id).map(this::getRow);
        result.ifPresent(selectedRow -> cache.put(id, selectedRow));
        return result;
    }

    private void addRow(long position, R row) throws IOException {
        randomAccessFile.seek(position);
        randomAccessFile.write(row.toBytes());
    }

    @SuppressWarnings("unchecked")
    private R getRow(long position) {
        var bytes = readBytes(position, row.getSize());
        return (R) row.fromBytes(bytes);
    }

    private Optional<Long> getRowPosition(I id) {
        return Optional.ofNullable(primaryIndex.get(id));
    }

    @SneakyThrows
    private byte[] readBytes(long position, int size) {
        var bytes = new byte[size];
        randomAccessFile.seek(position);
        randomAccessFile.read(bytes);
        return bytes;
    }

    @SneakyThrows
    private long getEndPosition() {
        return randomAccessFile.length();
    }

    @Override
    public void close() throws Exception {
        randomAccessFile.close();
    }

}
