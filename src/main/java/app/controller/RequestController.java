package app.controller;

import app.model.Request;
import app.model.Role;
import app.model.User;
import app.service.EmailServiceImpl;
import app.service.RequestService;
import app.service.RoleService;
import app.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RequestController {

    private final RequestService requestService;
    private final UserService userService;
    private final RoleService roleService;
    private final EmailServiceImpl emailService;

    public RequestController(RequestService requestService, UserService userService, RoleService roleService, EmailServiceImpl emailService) {
        this.requestService = requestService;
        this.userService = userService;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    @GetMapping("/requests")
    public ResponseEntity<List<Request>> showRequests() {
        return new ResponseEntity<>(requestService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/requests")
    public ResponseEntity<?> createRequest(@RequestBody Request request) {
        requestService.save(request);

        List<User> adminUsers = userService.showAllUsers().stream()
                .filter(user -> (user.getRole().contains(roleService.getRoleByName("ROLE_ADMIN"))))
                .collect(Collectors.toList());

        for (User user : adminUsers) {
            emailService.sendSimpleEmail(user.getEmail(), "Заявка на нового админа", "Новый user хочет стать админом");
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/request/accept/{id}")
    public ResponseEntity<?> acceptAdminRequest(@PathVariable("id") Integer id) {
        Optional<Request> request = requestService.getRequestById(id);
        User user = request.get().getUser();
        Set<Role> roles = user.getRole();
        roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        user.setRole(roles);
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/request/decline/{id}")
    public ResponseEntity<?> declineAdminRequest(@PathVariable("id") Integer id) {
        requestService.deleteRequestById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}