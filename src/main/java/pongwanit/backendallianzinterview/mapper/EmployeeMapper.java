package pongwanit.backendallianzinterview.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pongwanit.backendallianzinterview.controller.model.EmployeeCreateUpdateDTO;
import pongwanit.backendallianzinterview.controller.model.EmployeeDTO;
import pongwanit.backendallianzinterview.repository.model.EmployeeEntity;

@Mapper
public interface EmployeeMapper {
    @Mapping(target="employeeId", source="entity.id")
    @Mapping(target="employeeFullName", source = "entity" ,qualifiedByName = "firstNameLastNameToFullName")
    @Mapping(target="employeeFirstName", source="entity.firstName")
    @Mapping(target="employeeLastName", source="entity.lastName")
    @Mapping(target="employeeBirthDate", source="entity.birthDate")
    @Mapping(target="employeeEmail", source="entity.email")
    EmployeeDTO employeeEntityToEmployeeDTO(EmployeeEntity entity);

    @Named("firstNameLastNameToFullName")
    default String firstNameLastNameToFullName(EmployeeEntity entity){
        return entity.getFirstName()+" "+entity.getLastName();
    }

    @Mapping(target="id", source="dto.employeeId")
    @Mapping(target="firstName", source="dto.employeeFirstName")
    @Mapping(target="lastName", source="dto.employeeLastName")
    @Mapping(target="birthDate", source="dto.employeeBirthDate")
    @Mapping(target="email", source="dto.employeeEmail")
    EmployeeEntity employeeDTOtoEmployeeEntity(EmployeeDTO dto);

    @Mapping(target="firstName", source="dto.employeeFirstName")
    @Mapping(target="lastName", source="dto.employeeLastName")
    @Mapping(target="birthDate", source="dto.employeeBirthDate")
    @Mapping(target="email", source="dto.employeeEmail")
    EmployeeEntity employeeCreateUpdateDTOtoEmployeeEntity(EmployeeCreateUpdateDTO dto);
}
