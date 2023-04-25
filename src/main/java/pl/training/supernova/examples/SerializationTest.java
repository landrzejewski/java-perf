package pl.training.supernova.examples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

public class SerializationTest {

    private static final String OUTPUT_FILE = "data.ext";

    public static void main(String[] args) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            var person = Person.builder()
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .age(21)
                    .birthDate(LocalDate.now().minus(21, YEARS))
                    .hasAccount(true)
                    .build();
            objectOutputStream.writeObject(person);
        }
    }

}
