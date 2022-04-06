package com.epam.brest.openapi.impl;

import com.epam.brest.openapi.api.ControllerVersionApiDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ControllerVersionApiDelegateImpl implements ControllerVersionApiDelegate {

    @Value("${controller.version}")
    private String version;

    @Override
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(version);
    }

}
