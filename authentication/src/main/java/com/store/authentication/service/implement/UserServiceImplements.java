package com.store.authentication.service.implement;

import com.store.authentication.domain.User;
import com.store.authentication.repository.UserRepository;
import com.store.authentication.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplements extends GenericServiceImplements<User, Long, UserRepository>  implements UserService {
    public UserServiceImplements(UserRepository repository) {
        super(repository);
    }
}
