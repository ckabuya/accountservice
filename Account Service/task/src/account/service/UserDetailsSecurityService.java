package account.service;

import account.model.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserDetailsSecurityService implements UserDetailsService {
    private static final Logger LOGGER= Logger.getLogger(UserDetailsSecurityService.class.getName());
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Loading user for authentication " + username.toLowerCase()); //temp

        Optional<User> optUser = Optional.ofNullable(userService.getUserByEmail(username.toLowerCase()));

        if(optUser.isEmpty()){
            LOGGER.info("No user found from the database: " + optUser); //tem
            throw new UsernameNotFoundException("Not found " +username);
        }

        User user = optUser.get();
        UserDetails userDetail=org.springframework.security.core.userdetails.User.withUsername(user.getEmail().toLowerCase()).password(user.getPassword()).authorities("USER").build();
        LOGGER.info("User Details from the database: " + userDetail); //tem
        return userDetail;
    }
}
