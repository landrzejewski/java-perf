package pl.training.supernova.kapibara;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class KapibaraService {
  public static final Path FILE_PATH = Path.of("kapibara.txt");
  public static final Path FILE_PATH_TEMP = Path.of("kapibara_temp.txt");

  public void create(Kapibara kapibara) throws IOException {
    Files.write(FILE_PATH, kapibara.toString().getBytes(), StandardOpenOption.APPEND);
  }

  public Kapibara get(long id) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH.toFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.startsWith(id + Kapibara.DELIMITER)) {
          String[] values = line.split(Kapibara.DELIMITER);
          return new Kapibara(Integer.valueOf(values[0]), values[1], values[2], Integer.valueOf(values[3]), Boolean.valueOf(values[4]));
        }
      }
    }
    return null;
  }

  public void update(Kapibara kapibara) throws IOException {
    File inputFile = FILE_PATH.toFile();
    File tempFile = FILE_PATH_TEMP.toFile();

    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.startsWith(kapibara.getId() + Kapibara.DELIMITER)) {
        writer.write(kapibara.toString());
      } else {
        writer.write(line + System.getProperty("line.separator"));
      }
    }
    writer.close();
    reader.close();
    inputFile.delete();
    tempFile.renameTo(inputFile);
  }

  public void delete(int id) throws IOException {
    File inputFile = FILE_PATH.toFile();
    File tempFile = FILE_PATH_TEMP.toFile();

    BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.startsWith(id + Kapibara.DELIMITER)) {
        continue;
      }
      writer.write(line + System.getProperty("line.separator"));
    }
    writer.close();
    reader.close();
    inputFile.delete();
    tempFile.renameTo(inputFile);
  }
}
