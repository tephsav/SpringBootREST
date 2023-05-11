package app.service;

import app.model.Request;
import app.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    public RequestServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Optional<Request> getRequestById(Integer id) {
        return requestRepository.findById(id);
    }

    @Override
    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    @Override
    public void deleteRequestById(Integer id) {
        requestRepository.deleteById(id);
    }

    @Override
    public List<Request> getRequests() {
        return (List<Request>) requestRepository.findAll();
    }
}