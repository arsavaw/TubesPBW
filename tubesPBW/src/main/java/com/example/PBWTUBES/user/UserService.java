package main.java.com.example.PBWTUBES.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean register(User user) {
        
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;  
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        try {
            userRepository.save(user);  
            return true;  
        } catch (Exception e) {
            return false;  
        }
    }

    public User login(String username, String password) {
        
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user; 
        } else {
            return null;  
        }
    }
}
