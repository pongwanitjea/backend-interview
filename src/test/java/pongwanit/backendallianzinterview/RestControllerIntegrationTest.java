package pongwanit.backendallianzinterview;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import pongwanit.backendallianzinterview.config.EncoderConfig;
import pongwanit.backendallianzinterview.config.JwtAuthenticationEntryPoint;
import pongwanit.backendallianzinterview.config.WebSecurityConfig;
import pongwanit.backendallianzinterview.controller.EmployeeController;
import pongwanit.backendallianzinterview.controller.model.EmployeeDTO;
import pongwanit.backendallianzinterview.controller.model.JwtResponse;
import pongwanit.backendallianzinterview.mapper.EmployeeMapper;
import pongwanit.backendallianzinterview.mapper.EmployeeMapperImpl;
import pongwanit.backendallianzinterview.repository.EmployeeRepository;
import pongwanit.backendallianzinterview.repository.UserAuthRepository;
import pongwanit.backendallianzinterview.repository.model.EmployeeEntity;
import pongwanit.backendallianzinterview.repository.model.UserAuth;
import pongwanit.backendallianzinterview.services.JwtUserDetailsService;
import pongwanit.backendallianzinterview.utils.JwtTokenUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@ExtendWith(MockitoExtension.class)
public class RestControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private UserAuthRepository userAuthRepository;

    @SpyBean
    private JwtUserDetailsService jwtUserDetailsService;

    @SpyBean
    private JwtTokenUtil jwtTokenUtil;

    @SpyBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @SpyBean
    private EncoderConfig encoderConfig;

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @SpyBean
    private UserDetailsService jwtInMemoryUserDetailsService;


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @BeforeEach
    void setUp() {
        given(userAuthRepository.findByUsername("user1")).willReturn(new UserAuth("user1", "$2a$12$jjE5SH.qvYhv4pby/yrwhOlAYqE0kil1bxbqN5dwnlxBjAKmGHh62"));
    }

    @Test
    public void givenNoToken_thenUnauthorized() throws Exception {
        mvc.perform(get("/employees/")).andExpect(status().isUnauthorized());
    }

    @Test
    public void noUsernameInDatabase() throws Exception {
       mvc.perform(post("/authenticate").content(asJsonString(new UserAuth("notInDb", "abcd"))))
               .andExpect(status().isNotFound());
    }

    EmployeeMapper employeeMapper = new EmployeeMapperImpl();

    @Test
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        EmployeeEntity firstEmployeeEntity = new EmployeeEntity(1L, "fn", "ln", LocalDate.of(1990,10,1), "em@em.com");

        List<EmployeeEntity> allEmployeeDTOS = List.of(firstEmployeeEntity);
        given(employeeRepository.findAll()).willReturn(allEmployeeDTOS);

        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername("user1");
        final String token = jwtTokenUtil.generateToken(userDetails);

        mvc.perform(get("/employees/").header("Authorization", "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeFirstName", is(firstEmployeeEntity.getFirstName())))
                .andExpect(jsonPath("$[0].employeeLastName", is(firstEmployeeEntity.getLastName())))
                .andExpect(jsonPath("$[0].employeeFullName", is(firstEmployeeEntity.getFirstName()+" "+firstEmployeeEntity.getLastName())))
                .andExpect(jsonPath("$[0].employeeBirthDate", is(firstEmployeeEntity.getBirthDate().toString())))
                .andExpect(jsonPath("$[0].employeeId", is(firstEmployeeEntity.getId().intValue())))
                .andExpect(jsonPath("$[0].employeeEmail", is(firstEmployeeEntity.getEmail())));
    }

    @Test
    public void postEmployee_getEmployeeById_thenReturnEmployee() throws Exception {
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername("user1");
        final String token = jwtTokenUtil.generateToken(userDetails);
        String requestBody = "{\n" +
                "    \"employeeFirstName\": \"fn\",\n" +
                "    \"employeeLastName\": \"ln\",\n" +
                "    \"employeeBirthDate\": \"1994-10-30\",\n" +
                "    \"employeeEmail\": \"aaa@eee.com\"\n" +
                "}";

        mvc.perform(post("/employees/").header("Authorization", "Bearer "+ token).content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        EmployeeEntity employeeEntity = new EmployeeEntity(1L,"aaa","bbb",LocalDate.of(1990,10,1), "aaa@eee.com");
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employeeEntity));

        mvc.perform(get("/employees/1").header("Authorization", "Bearer "+ token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeFirstName", is("aaa")))
                .andExpect(jsonPath("$.employeeLastName", is("bbb")))
                .andExpect(jsonPath("$.employeeFullName", is("aaa bbb")))
                .andExpect(jsonPath("$.employeeBirthDate", is("1990-10-01")))
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.employeeEmail", is("aaa@eee.com")));
    }

    @Test
    public void putEmployeeByNotExistId_thenReturnNotFound() throws Exception {
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername("user1");
        final String token = jwtTokenUtil.generateToken(userDetails);
        String requestBody = "{\n" +
                "    \"employeeFirstName\": \"fn\",\n" +
                "    \"employeeLastName\": \"ln\",\n" +
                "    \"employeeBirthDate\": \"1994-10-30\",\n" +
                "    \"employeeEmail\": \"aaa@eee.com\"\n" +
                "}";
        mvc.perform(put("/employees/1").header("Authorization", "Bearer "+ token).content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void putEmployeeByExistId_updateIdAndFirstName_get() throws Exception {
        EmployeeEntity employeeEntity = new EmployeeEntity(1L,"aaa","bbb",LocalDate.of(1990,10,1), "aaa@eee.com");
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employeeEntity));

        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername("user1");
        final String token = jwtTokenUtil.generateToken(userDetails);
        String requestBody = "{\n" +
                "    \"employeeFirstName\": \"aaaNEW\",\n" +
                "    \"employeeLastName\": \"bbb\",\n" +
                "    \"employeeBirthDate\": \"1990-10-01\",\n" +
                "    \"employeeEmail\": \"aaa@eee.com\"\n" +
                "}";
        mvc.perform(put("/employees/1").header("Authorization", "Bearer "+ token).content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        EmployeeEntity employeeEntityUpdated = new EmployeeEntity(1L,"aaaNEW","bbb",LocalDate.of(1990,10,1), "aaa@eee.com");
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employeeEntityUpdated));

        mvc.perform(get("/employees/1").header("Authorization", "Bearer "+ token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeFirstName", is("aaaNEW")))
                .andExpect(jsonPath("$.employeeLastName", is("bbb")))
                .andExpect(jsonPath("$.employeeFullName", is("aaaNEW bbb")))
                .andExpect(jsonPath("$.employeeBirthDate", is("1990-10-01")))
                .andExpect(jsonPath("$.employeeId", is(1)))
                .andExpect(jsonPath("$.employeeEmail", is("aaa@eee.com")));
    }

    @Test
    public void deleteEmployeeByNotExistId_thenReturnNotFound() throws Exception {
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername("user1");
        final String token = jwtTokenUtil.generateToken(userDetails);
        mvc.perform(delete("/employees/1").header("Authorization", "Bearer "+ token).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteEmployeeByExistId_thenReturnSuccess() throws Exception {
        final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername("user1");
        final String token = jwtTokenUtil.generateToken(userDetails);
        EmployeeEntity employeeEntity = new EmployeeEntity(2L,"aaa","bbb",LocalDate.of(1990,10,1), "aaa@eee.com");
        given(employeeRepository.findById(2L)).willReturn(Optional.of(employeeEntity));
        mvc.perform(delete("/employees/2").header("Authorization", "Bearer "+ token).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        given(employeeRepository.findById(2L)).willReturn(null);

    }
}
