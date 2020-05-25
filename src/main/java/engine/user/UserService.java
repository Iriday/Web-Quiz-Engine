package engine.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public void registerUser(User user) {
        if (userRepo.existsById(user.getUsername()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        userRepo.save(user);
    }
}
