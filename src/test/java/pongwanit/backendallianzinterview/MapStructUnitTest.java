package pongwanit.backendallianzinterview;


import org.junit.jupiter.api.Test;
import pongwanit.backendallianzinterview.controller.model.EmployeeCreateUpdateDTO;
import pongwanit.backendallianzinterview.controller.model.EmployeeDTO;
import pongwanit.backendallianzinterview.mapper.EmployeeMapper;
import pongwanit.backendallianzinterview.mapper.EmployeeMapperImpl;
import pongwanit.backendallianzinterview.repository.model.EmployeeEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapStructUnitTest {
    EmployeeMapper employeeMapper = new EmployeeMapperImpl();
    @Test
    public void givenEmployeeEntity_whenMapsToEmployeeDTO_thenCorrect() {
        EmployeeEntity entity = new EmployeeEntity(1L, "aaa", "bbb", LocalDate.of(1980,1,5), "ddd");
        EmployeeDTO dto = employeeMapper.employeeEntityToEmployeeDTO(entity);

        assertEquals(entity.getId(), dto.getEmployeeId());
        assertEquals(entity.getFirstName(), dto.getEmployeeFirstName());
        assertEquals(entity.getLastName(), dto.getEmployeeLastName());
        assertEquals(entity.getFirstName()+" "+entity.getLastName(), dto.getEmployeeFullName());
        assertEquals(entity.getBirthDate(), dto.getEmployeeBirthDate());
        assertEquals(entity.getEmail(), dto.getEmployeeEmail());
    }


    @Test
    public void givenEmployeeCreateUpdateDTO_whenMapsToEmployeeEntity_thenCorrect() {
        EmployeeCreateUpdateDTO dto = new EmployeeCreateUpdateDTO("aaa", "bbb", "aaa bbb", LocalDate.of(1980,1,5), "ddd");
        EmployeeEntity entity = employeeMapper.employeeCreateUpdateDTOtoEmployeeEntity(dto);

        assertEquals(entity.getFirstName(), dto.getEmployeeFirstName());
        assertEquals(entity.getLastName(), dto.getEmployeeLastName());
        assertEquals(entity.getFirstName()+" "+entity.getLastName(), dto.getEmployeeFullName());
        assertEquals(entity.getBirthDate(), dto.getEmployeeBirthDate());
        assertEquals(entity.getEmail(), dto.getEmployeeEmail());
    }

}
