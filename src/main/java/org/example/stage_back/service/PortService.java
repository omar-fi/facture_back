package org.example.stage_back.service;

import lombok.RequiredArgsConstructor;
import org.example.stage_back.entities.Port;
import org.example.stage_back.repository.PortRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PortService {

    private final PortRepository portRepository;

    public Port findById(Long portId) {
        return portRepository.findById(portId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Port non trouvé"));
    }
}
