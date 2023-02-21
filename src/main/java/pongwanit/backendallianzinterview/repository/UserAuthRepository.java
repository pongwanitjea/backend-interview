package pongwanit.backendallianzinterview.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pongwanit.backendallianzinterview.repository.model.UserAuth;

@Repository
public interface UserAuthRepository extends CrudRepository<UserAuth, String> {
    UserAuth findByUsername(String username);
}
