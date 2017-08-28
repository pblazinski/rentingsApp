package pl.lodz.p.edu.grs.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.edu.grs.model.user.User;
import pl.lodz.p.edu.grs.service.UserService;

import javax.validation.Valid;

@RestController
@Api(value = "api/user", description = "Endpoints for user management")
@RequestMapping(value = "api/users")
public class UserController {
    //TODO handle not found for update method and remove
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation("Register new user")
    public User registerUser(@RequestBody @Valid final RegisterUserDTO registerUserDTO) {
        return userService.registerUser(registerUserDTO);
    }

    @GetMapping
    @ApiOperation("Get users page")
    public Page<User> getUsers(@PageableDefault final Pageable pageable) {
        return userService.findAll(pageable);
    }

    @GetMapping("/{userId}")
    @ApiOperation("Get user by id")
    public User getUserById(@PathVariable final long userId) {
        return userService.findOne(userId);
    }

    @PutMapping("{id}/email")
    @ApiOperation("Update user email")
    @PreAuthorize("@userModifyPermissionResolver.hasAuthorityToModifyUser(principal, #id)")
    public HttpEntity updateEmail(@PathVariable final long id,
                                  @RequestBody @Valid final UpdateUserEmailDto updateUserEmailDto) {
        User user = userService.updateEmail(id, updateUserEmailDto.getEmail());

        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}/password")
    @ApiOperation("Update user password")
    @PreAuthorize("@userModifyPermissionResolver.hasAuthorityToModifyUser(principal, #id) ")
    public HttpEntity updatePassword(@PathVariable final long id,
                                     @RequestBody @Valid final UpdateUserPasswordDto updateUserPasswordDto) {
        User user = userService.updatePassword(id, updateUserPasswordDto.getPassword());

        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}/names")
    @ApiOperation("Update user names")
    @PreAuthorize("@userModifyPermissionResolver.hasAuthorityToModifyUser(principal, #id)")
    public HttpEntity updateNames(@PathVariable final long id,
                                  @RequestBody @Valid final UpdateUserNamesDto updateUserNamesDto) {
        User user = userService.updateNames(id, updateUserNamesDto.getFirstName(), updateUserNamesDto.getLastName());

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Remove user")
    @PreAuthorize("@userModifyPermissionResolver.hasAuthorityToModifyUser(principal, #id)")
    public HttpEntity removeUser(@PathVariable final long id) {
        userService.remove(id);

        return ResponseEntity.ok().build();
    }

}
