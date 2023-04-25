package pl.training.supernova;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;

import static java.nio.charset.StandardCharsets.UTF_8;

@Data
@AllArgsConstructor
public class PersonRecord implements Record {

    private static final int RECORD_SIZE = 8 + 50 + 70 + 4 + 1;

    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private boolean isActive;

    @Override
    public byte[] toBytes() {
        var buffer = ByteBuffer.allocate(RECORD_SIZE);
        buffer.putLong(id);
        buffer.put(toField(firstName.getBytes(UTF_8), 50));
        buffer.put(toField(lastName.getBytes(UTF_8), 70));
        buffer.putInt(age);
        buffer.put(toField(isActive));
        return buffer.array();
    }

    @Override
    public void fromBytes(byte[] bytes) {
        var buffer = ByteBuffer.wrap(bytes);
        id = buffer.getLong();
        firstName = getString(readBytes(buffer, 50));
        lastName = getString(readBytes(buffer, 70));
        age = buffer.getInt();
        isActive = getBoolean(readBytes(buffer, 1));
    }

    private byte[] readBytes(ByteBuffer buffer, int size) {
        var value = new byte[size];
        buffer.get(value);
        return value;
    }

    @Override
    public long getSize() {
        return RECORD_SIZE;
    }

}
