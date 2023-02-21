package pongwanit.backendallianzinterview.controller.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class JwtRequest implements Serializable {

    private String username;
    private String password;
}