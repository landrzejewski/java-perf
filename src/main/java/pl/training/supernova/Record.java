package pl.training.supernova;

import java.nio.charset.Charset;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface Record {

    int START_POSITION = 0;
    byte EMPTY_VALUE = 0xa;
    byte TRUE_VALUE = 0xb;
    byte FALSE_VALUE = 0xc;
    Charset CHARSET = UTF_8;

    byte[] toBytes();

    void fromBytes(byte[] bytes);

    default byte[] toField(byte[] data, int size) {
        var bytes = new byte[size];
        System.arraycopy(data, START_POSITION, bytes, START_POSITION, data.length);
        Arrays.fill(bytes, data.length, size, EMPTY_VALUE);
        return bytes;
    }

    default byte[] toField(boolean data) {
        return new byte[]{data ? TRUE_VALUE : FALSE_VALUE};
    }

    default String getString(byte[] bytes) {
        var endIndex = START_POSITION;
        for (var index = bytes.length - 1; index >= START_POSITION; index--) {
            if (bytes[index] != EMPTY_VALUE) {
                endIndex = index + 1;
                break;
            }
        }
        return new String(Arrays.copyOfRange(bytes, 0, endIndex), CHARSET);
    }

    default boolean getBoolean(byte[] bytes) {
        return bytes[0] == TRUE_VALUE;
    }

    long getSize();

}
