package pl.training.supernova.kapibara;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kapibara {
  public static final String DELIMITER = ";";
  private long id;
  private String firstName;
  private String lastName;
  private int age;
  private boolean isActive;

  public String toString() {
    return new StringBuilder(id + "").append(DELIMITER)
      .append(firstName).append(DELIMITER)
      .append(lastName).append(DELIMITER)
      .append(age).append(DELIMITER)
      .append(isActive).append("\n").toString();
  }
}
