package pl.training.supernova.group2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Row {
  private long id;
  private String firstName; //50
  private String lastName; //70
  private int age;
  private boolean isActive;

  public void setFirstName(String firstName) {

    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}

