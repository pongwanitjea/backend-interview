package pongwanit.backendallianzinterview.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pongwanit.backendallianzinterview.repository.model.EmployeeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> {
    List<EmployeeEntity> findAll();
    Optional<EmployeeEntity> findById(Long id);

}
