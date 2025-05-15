package com.kdob.jabs.task3.controller;

import com.kdob.jabs.task3.dto.TokenRequest;
import com.kdob.jabs.task3.dto.TokenResponse;
import com.kdob.jabs.task3.facade.TokenFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    private final TokenFacade tokenFacade;

    public TokenController(final TokenFacade tokenFacade) {
        this.tokenFacade = tokenFacade;
    }

    @PostMapping("/token")
    public TokenResponse getToken(@RequestBody final TokenRequest tokenRequest) {
        return tokenFacade.getToken(tokenRequest);
    }
}
