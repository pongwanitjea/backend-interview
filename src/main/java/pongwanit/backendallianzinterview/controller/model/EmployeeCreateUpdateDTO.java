package pongwanit.backendallianzinterview.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateUpdateDTO implements Serializable {

    private String employeeFirstName;
    private String employeeLastName;
    private String employeeFullName;
    private LocalDate employeeBirthDate;
    private String employeeEmail;

}
