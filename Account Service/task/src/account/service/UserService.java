package account.service;

import account.model.User;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user){
        return userRepository.save(user);
    }
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
