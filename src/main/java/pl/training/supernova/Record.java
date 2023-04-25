package pl.training.supernova;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public interface Record {

    int START_POSITION = 0;
    byte EMPTY_VALUE = 0xa;
    byte TRUE_VALUE = 0xb;
    byte FALSE_VALUE = 0xc;

    byte[] toBytes();

    void fromBytes(byte[] bytes);

    default byte[] toField(byte[] data, int size) {
        var bytes = new byte[size];
        System.arraycopy(data, START_POSITION, bytes, START_POSITION, data.length);
        Arrays.fill(bytes, data.length, size, EMPTY_VALUE);
        return bytes;
    }

    default byte[] toField(boolean value) {
        return new byte[] { value ? TRUE_VALUE : FALSE_VALUE };
    }

    default String getString(byte[] bytes) {
        var targetIndex = START_POSITION;
        for (var index = bytes.length - 1; index >= START_POSITION; index--) {
            if (bytes[index] != EMPTY_VALUE) {
                targetIndex = index;
                break;
            }
        }
        System.out.println(targetIndex);
        return new String(Arrays.copyOfRange(bytes, 0, bytes.length - targetIndex), UTF_8);
    }

    default boolean getBoolean(byte[] value) {
        return value[0] == TRUE_VALUE;
    }

    default long getSize() {
        return toBytes().length;
    }

}
