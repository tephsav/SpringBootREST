package app.config;

import app.model.Role;
import app.model.User;
import app.service.RoleService;
import app.service.UserService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DbInit {

    private final UserService userService;
    private final RoleService roleService;

    public DbInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void createUsers() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        roleService.saveRole(roleAdmin);
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleUser);

        Set<Role> roleAdminSet = new HashSet<>();
        roleAdminSet.add(roleAdmin);
        User admin = new User("admin", "adminLastName", 30, "admin@mail.ru", "admin", roleAdminSet);
        userService.createUser(admin);

        Set<Role> roleUserSet = new HashSet<>();
        roleUserSet.add(roleUser);
        User user = new User("user", "userLastName", 20, "user@mail.ru", "user", roleUserSet);
        userService.createUser(user);

        Set<Role> roleAdminUserSet = new HashSet<>();
        roleAdminUserSet.add(roleUser);
        roleAdminUserSet.add(roleAdmin);
        User adminUser = new User("adminUser", "adminUserLastName", 45, "adminUser@mail.ru", "adminUser", roleAdminUserSet);
        userService.createUser(adminUser);
    }
}
