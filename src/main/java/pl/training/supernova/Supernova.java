package pl.training.supernova;

public class Supernova {

    public static void main(String[] args) {
        var person = new PersonRecord(1, "Jan", "Kowalski", 30, true);
        var bytes = person.toBytes();
        var testPerson = new PersonRecord(0, "", "", 0, false);
        testPerson.fromBytes(bytes);
        System.out.println(testPerson);


    }

}
