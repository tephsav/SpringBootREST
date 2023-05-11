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
    public ResponseEntity<List<Request>> getRequests() {
        return new ResponseEntity<>(requestService.getRequests(), HttpStatus.OK);
    }

    @PostMapping("/requests/{idUser}")
    public ResponseEntity<?> createRequest(@PathVariable("idUser") Integer idUser) {
        User user = userService.findUserById(idUser);
        Request request = new Request();
        request.setUser(user);
        requestService.saveRequest(request);

        List<User> adminUsers = userService.getOnlyAdmins();
        for (User admin : adminUsers) {
            emailService.sendSimpleEmail(
                    admin.getEmail(),
                    "Заявка на нового админа", String.format("Новый user (%s) хочет стать админом", user.getEmail()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/requests/accept/{id}")
    public ResponseEntity<?> acceptAdminRequest(@PathVariable("id") Integer id) {
        Optional<Request> request = requestService.getRequestById(id);
        User user = request.get().getUser();
        Set<Role> roles = user.getRoles();
        roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        user.setRoles(roles);
        userService.updateUser(user);
        requestService.deleteRequestById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/requests/decline/{id}")
    public ResponseEntity<?> declineAdminRequest(@PathVariable("id") Integer id) {
        requestService.deleteRequestById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}