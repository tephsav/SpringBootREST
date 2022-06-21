package app.service;

import app.model.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);

    Role getRoleById(Integer id);

    List<Role> getAllRoles();

    void saveRole(Role role);
}
