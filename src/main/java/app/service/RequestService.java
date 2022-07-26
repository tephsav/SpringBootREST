package app.service;

import app.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestService {
    public Optional<Request> getRequestById(Integer id);

    public List<Request> findAll();

    public void save(Request request);

    public void deleteRequestById(Integer id);
}
