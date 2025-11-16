package challenge.concurrency.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
 
    public record User(String name) {}

    public User findUser(String name) {
        return new User(name);
    }
}