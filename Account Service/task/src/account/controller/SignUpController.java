package account.controller;

import account.model.User;
import account.service.UserService;
import account.util.UserExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class SignUpController {
    static Logger LOGGER = Logger.getLogger(SignUpController.class.getName());
    @Autowired
    UserService userService;


    private PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();;

    @PostMapping("/api/auth/signup")
    public Map<String, String> signUp(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getName();
        String lname = user.getLastname();
        if ( email ==null || email.isEmpty() || email.isBlank() || email.indexOf("@acme.com",email.length()-"@acme.com".length()) ==-1) {
            LOGGER.info("Email Issue: " + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (name==null || name.isEmpty() || user.getName().isBlank()) {
            LOGGER.info("Name Issue: " + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (lname ==null || lname.isEmpty() || lname.isBlank()) {
            LOGGER.info("last Name Issue: " + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if (password ==null || password.isEmpty() || password.isBlank()) {
            LOGGER.info("Password Issue: " + user);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        User retrievedUser = userService.getUserByEmail(email);
        //check if user is registered
        if(retrievedUser !=null){
            throw new UserExistException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        LOGGER.info(" Password encoded: " + user);
        user = userService.register(user);

        LOGGER.info(" User signup successfully: " + user);

        Map<String, String> m = new HashMap<>();
        m.put("id", String.valueOf(user.getId()));
        m.put("name", user.getName());
        m.put("lastname", user.getLastname());
        m.put("email", user.getEmail());
        return m;
    }
    @GetMapping("api/empl/payment")
    public Map<String, String> getPayment(@AuthenticationPrincipal UserDetails details){
       if(details ==null){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
       }
       User user = userService.getUserByEmail(details.getUsername());

        Map<String, String> m = new HashMap<>();
        m.put("id", String.valueOf(user.getId()));
        m.put("name", user.getName());
        m.put("lastname", user.getLastname());
        m.put("email", user.getEmail());
        return m;
    }
}
