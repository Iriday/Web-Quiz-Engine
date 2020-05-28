package engine.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        if (userRepo.existsById(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");

        UserUtils.checkPassword(user.getPassword())
                .ifPresent((errMessage) -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errMessage);
                });

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
