package pl.lodz.p.edu.grs.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.edu.grs.model.User;
import pl.lodz.p.edu.grs.service.UserService;

@RestController
@RequestMapping(value = "api/user")
@Api(value = "api/user", description = "Endpoints for user management")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "10") Integer size,
                               @RequestParam(defaultValue = "0") Integer page) {
        return userService.findAll(new PageRequest(page, size));
    }
}
