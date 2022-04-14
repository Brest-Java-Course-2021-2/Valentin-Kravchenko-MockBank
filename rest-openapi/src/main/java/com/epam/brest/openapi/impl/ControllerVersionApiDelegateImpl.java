package com.epam.brest.openapi.impl;

import com.epam.brest.model.Version;
import com.epam.brest.openapi.api.ControllerVersionApiDelegate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.epam.brest.openapi.constant.OpenApiConstant.VERSION;

@Service
public class ControllerVersionApiDelegateImpl implements ControllerVersionApiDelegate {

    @Value("${controller.version}")
    private String version;

    @Override
    public ResponseEntity<Version> getVersion() {
        return ResponseEntity.ok(new Version().version(version));
    }

}
