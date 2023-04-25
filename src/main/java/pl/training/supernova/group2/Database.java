package pl.training.supernova.group2;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

public class Database {
  public static final int LINE_SIZE = 100;
  public static final String FILENAME = "db";

  public static void createRow(Row row) throws IOException {
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILENAME, "rw")) {
      randomAccessFile.seek(randomAccessFile.length());
      randomAccessFile.writeLong(row.getId());
      randomAccessFile.writeBytes(StringUtils.rightPad(row.getFirstName(), 50));
      randomAccessFile.writeBytes(StringUtils.rightPad(row.getLastName(), 70));
      randomAccessFile.writeInt(row.getAge());
      randomAccessFile.writeBoolean(row.isActive());
      System.out.println(randomAccessFile.getFilePointer());
    }
  }

  public static Row getRow(long id) throws IOException {
    byte[] firstNameBytes = new byte[50];
    byte[] lastNameBytes = new byte[70];
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILENAME, "r")) {
      for (int i=0; i<randomAccessFile.length(); i+=133) {
        if (randomAccessFile.readLong() == id) {
          Row row = new Row();
          row.setId(id);
          randomAccessFile.read(firstNameBytes);
          row.setFirstName(new String(firstNameBytes).trim());
          randomAccessFile.read(lastNameBytes);
          row.setLastName(new String(lastNameBytes).trim());
          row.setAge(randomAccessFile.readInt());
          row.setActive(randomAccessFile.readBoolean());
          return row;
        } else {
          randomAccessFile.seek(i+133);
        }
      }
    }
    return null;
  }

  public static List<Row> getAll() throws IOException {
    byte[] firstNameBytes = new byte[50];
    byte[] lastNameBytes = new byte[70];
    List<Row> list = new ArrayList<>();
    try (RandomAccessFile randomAccessFile = new RandomAccessFile(FILENAME, "r")) {
    for (int i=0; i<randomAccessFile.length(); i+=133) {
      Row row = new Row();
      row.setId(randomAccessFile.readLong());
      randomAccessFile.read(firstNameBytes);
      row.setFirstName(new String(firstNameBytes).trim());
      randomAccessFile.read(lastNameBytes);
      row.setLastName(new String(lastNameBytes).trim());
      row.setAge(randomAccessFile.readInt());
      row.setActive(randomAccessFile.readBoolean());
      list.add(row);
    }
    }
    return list;
  }

  public static void main(String[] args) throws IOException {
    Random random = new Random();
    Row row = Row.builder().id(123).firstName(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString()).age(random.nextInt()).isActive(random.nextBoolean()).build();
    createRow(row);
    row = Row.builder().id(234).firstName(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString()).age(random.nextInt()).isActive(random.nextBoolean()).build();
    createRow(row);
    row = Row.builder().id(456).firstName(UUID.randomUUID().toString()).lastName(UUID.randomUUID().toString()).age(random.nextInt()).isActive(random.nextBoolean()).build();
    createRow(row);
//    System.out.println(getAll());
    System.out.println(getRow(234L).toString());

    //1 w, 1m - 100 x CRUD
  }


}
