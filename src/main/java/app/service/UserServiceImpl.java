package app.service;

import app.model.User;
import app.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleService roleService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> showAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public List<User> showUsersWithUserRole() {
        List<User> onlyUsers = showAllUsers().stream()
                .filter(user -> !(user.getRole().contains(roleService.getRoleByName("ROLE_ADMIN"))))
                .collect(Collectors.toList());

        return onlyUsers;
    }

    @Override
    public User showUser(Integer id) {
        User user = userRepository.findById(id).get();
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        if (user.getPassword() != "") {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(userRepository.findById(user.getId()).get().getPassword());
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User findUserById(Integer id) {
        User user = null;
        Optional<User> optional = userRepository.findById(id);
        if(optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
