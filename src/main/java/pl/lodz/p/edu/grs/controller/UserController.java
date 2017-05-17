package pl.lodz.p.edu.grs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.service.UserService;

@RestController
@RequestMapping(value = "api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){this.userService=userService;}

}
