package com.landongnet.gateway.common.controller;

import com.landongnet.gateway.common.provider.MySwaggerResourceProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;

/**
 * @author snake
 * @description
 * @since 2023/8/19 14:52
 */
@RestController
@RequestMapping("/swagger-resources")
@RequiredArgsConstructor
public class SwaggerResourceController {

    private final MySwaggerResourceProvider swaggerResourceProvider;

    @RequestMapping
    public ResponseEntity<List<SwaggerResource>> swaggerResources() {
        return new ResponseEntity<>(swaggerResourceProvider.get(), HttpStatus.OK);
    }
}
