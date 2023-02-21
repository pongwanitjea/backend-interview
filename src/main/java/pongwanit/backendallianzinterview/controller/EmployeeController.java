package pongwanit.backendallianzinterview.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pongwanit.backendallianzinterview.controller.model.EmployeeCreateUpdateDTO;
import pongwanit.backendallianzinterview.controller.model.EmployeeDTO;
import pongwanit.backendallianzinterview.mapper.EmployeeMapper;
import pongwanit.backendallianzinterview.mapper.EmployeeMapperImpl;
import pongwanit.backendallianzinterview.repository.EmployeeRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;
    EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    @GetMapping("/")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        var employeeEntityList = employeeRepository.findAll();
        var employeeDTOList = new ArrayList<EmployeeDTO>();
        employeeEntityList.forEach(employeeEntity -> {
            EmployeeDTO res = employeeMapper.employeeEntityToEmployeeDTO(employeeEntity);
            employeeDTOList.add(res);
        });
        return ResponseEntity.ok(employeeDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id){
        if(employeeRepository.findById(id).isPresent()){
            return ResponseEntity.ok(employeeMapper.employeeEntityToEmployeeDTO(employeeRepository.findById(id).get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional(rollbackOn = {Exception.class})
    @PostMapping("/")
    public ResponseEntity<String> postEmployee(@RequestBody EmployeeCreateUpdateDTO employeeCreateUpdateDTO){
        employeeRepository.save(employeeMapper.employeeCreateUpdateDTOtoEmployeeEntity(employeeCreateUpdateDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Transactional(rollbackOn = {Exception.class})
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeCreateUpdateDTO employeeDTO){
        var updateEmployee = employeeRepository.findById(id);
        if (updateEmployee.isPresent()){
            var toBeUpdatedEmployee = employeeMapper.employeeCreateUpdateDTOtoEmployeeEntity(employeeDTO);
            toBeUpdatedEmployee.setId(id);
            employeeRepository.save(toBeUpdatedEmployee);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional(rollbackOn = {Exception.class})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable Long id){
        var findEmployee = employeeRepository.findById(id);
        if (findEmployee.isPresent()){
            employeeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
