package com.jwick.continental.deathagreement.controller.v1.business.betobject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/continental/betobject")
public class BetObjectBusinessController {

    @Autowired private BetObjectBusinessService betObjectBusinessService;

    @PostMapping
    public ResponseEntity createBetObject(@RequestBody @Valid BetObjectRequest request) {
        final UUID processId = UUID.randomUUID();
        return ResponseEntity.ok().body(betObjectBusinessService.execute(processId,request));
    }
}
