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
        User admin = new User("admin", "adminLastName", 30, "sashka19.97@mail.ru", "admin", roleAdminSet);
        userService.createUser(admin);

        Set<Role> roleAdminUserSet = new HashSet<>();
        roleAdminUserSet.add(roleAdmin);
        roleAdminUserSet.add(roleUser);
        User adminUser = new User("adminUser", "adminUserLastName", 50, "necromant45z@gmail.com", "admin", roleAdminUserSet);
        userService.createUser(adminUser);

        Set<Role> roleUserSet1 = new HashSet<>();
        roleUserSet1.add(roleUser);
        User user1 = new User("user1", "userLastName1", 20, "shpet97@mail.ru", "user", roleUserSet1);
        userService.createUser(user1);

        Set<Role> roleUserSet2 = new HashSet<>();
        roleUserSet2.add(roleUser);
        User user2 = new User("user2", "userLastName2", 25, "user@mail.ru", "user", roleUserSet2);
        userService.createUser(user2);
    }
}