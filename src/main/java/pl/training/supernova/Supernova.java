package pl.training.supernova;

public class Supernova {

    public static void main(String[] args) {
        var person = PersonRecord.builder()
                .id(1)
                .firstName("Jan")
                .lastName("Kowalski")
                .age(30)
                .isActive(true)
                .build();

        var bytes = person.toBytes();
        var testPerson = new PersonRecord();
        testPerson.fromBytes(bytes);
        System.out.println(testPerson);
    }

}
