package app.controller;

import app.model.Role;
import app.model.User;
import app.service.RoleService;
import app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRestCotroller {

    private final UserService userService;
    private final RoleService roleService;

    public UserRestCotroller(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        return new ResponseEntity<>(userService.showAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(new ArrayList<>(roleService.getAllRoles()), HttpStatus.OK);
    }

    @GetMapping("/users/only_users")
    public ResponseEntity<List<User>> showUsersWithUserRole() {
        return new ResponseEntity<>(userService.showUsersWithUserRole(), HttpStatus.OK);
    }

    @PostMapping("/users/accept/{id}")
    public ResponseEntity<?> acceptAdminRequest(@PathVariable("id") Integer id) {
        User user = userService.findUserById(id);
        Set<Role> roles = user.getRole();
        roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        user.setRole(roles);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/users/decline/{id}")
    public ResponseEntity<?> declineAdminRequest(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> showUser(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(userService.showUser(id), HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<User> update(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
