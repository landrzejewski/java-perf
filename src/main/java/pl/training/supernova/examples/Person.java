package pl.training.supernova.examples;

import lombok.Builder;
import lombok.Data;

import java.io.*;
import java.time.LocalDate;

@Builder
@Data
public class Person implements /*Serializable*/ Externalizable {

    private String firstName;
    private String lastName;
    private int age;
    private transient boolean hasAccount;
    private LocalDate birthDate;


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(firstName);
        out.writeUTF(lastName);
        out.writeInt(age);
        out.writeObject(birthDate);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        firstName = in.readUTF();
        lastName = in.readUTF();
        age = in.readInt();
        birthDate = (LocalDate) in.readObject();
    }

}
