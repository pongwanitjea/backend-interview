package pongwanit.backendallianzinterview.controller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse implements Serializable {

    private final String jwtToken;

}
