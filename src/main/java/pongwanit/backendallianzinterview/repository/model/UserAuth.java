package pongwanit.backendallianzinterview.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuth {
    @Id
    @Column(nullable = false, unique = true)
    private String username;

    private String password;
}