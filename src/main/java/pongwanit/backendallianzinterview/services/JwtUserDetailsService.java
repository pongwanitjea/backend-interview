package pongwanit.backendallianzinterview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pongwanit.backendallianzinterview.repository.UserAuthRepository;
import pongwanit.backendallianzinterview.repository.model.UserAuth;
import pongwanit.backendallianzinterview.services.model.MyUserPrincipal;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAuth user = userAuthRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }

}