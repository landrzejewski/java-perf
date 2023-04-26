package pl.training.supernova;

import lombok.SneakyThrows;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class Supernova<R extends Row<I>, I> implements AutoCloseable {

    private final Map<I, Long> primaryIndex;
    private final R row;
    private final RandomAccessFile randomAccessFile;

    @SneakyThrows
    public Supernova(Path filePath, R row, Map<I, Long> primaryIndex) {
        this.randomAccessFile = new RandomAccessFile(filePath.toFile(), "rw");
        this.row = row;
        this.primaryIndex = primaryIndex;
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

    @SneakyThrows
    public void insert(R row) {
        var id = row.getId();
        checkIdExistence(id);
        var position = getEndPosition();
        primaryIndex.put(id, position);
        addRow(position, row);
    }

    @SneakyThrows
    public Optional<R> getById(I id) {
        return getRowPosition(id).map(this::getRow);
    }

    private void addRow(long position, R row) throws IOException {
        randomAccessFile.seek(position);
        randomAccessFile.write(row.toBytes());
    }

    private void checkIdExistence(I id) {
        if (primaryIndex.containsKey(id)) {
            throw new DuplicatedKeyException();
        }
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
