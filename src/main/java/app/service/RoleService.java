package app.service;

import app.model.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);

    List<Role> getRoles();

    void saveRole(Role role);
}