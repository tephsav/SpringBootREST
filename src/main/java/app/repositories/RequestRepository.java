package app.repositories;

import app.model.Request;
import app.model.User;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Integer> {
}
