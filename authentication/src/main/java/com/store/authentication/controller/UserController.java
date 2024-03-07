package com.store.authentication.controller;

import com.store.authentication.domain.User;
import com.store.authentication.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController extends GenericController<User> {
    public UserController(UserService service) {
        super(service);
    }
}
