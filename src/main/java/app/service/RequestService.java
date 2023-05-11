package app.service;

import app.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    Optional<Request> getRequestById(Integer id);

    void saveRequest(Request request);

    void deleteRequestById(Integer id);

    List<Request> getRequests();
}
