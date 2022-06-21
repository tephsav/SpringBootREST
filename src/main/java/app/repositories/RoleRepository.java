package app.repositories;

import app.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    @Query("select r from Role r where r.role = :role")
    Role getRoleByName(String role);
}
