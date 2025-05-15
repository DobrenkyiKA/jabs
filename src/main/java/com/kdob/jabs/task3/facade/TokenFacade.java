package com.kdob.jabs.task3.facade;

import com.kdob.jabs.task3.dto.TokenRequest;
import com.kdob.jabs.task3.dto.TokenResponse;
import com.kdob.jabs.task3.servcie.AuthenticationService;
import com.kdob.jabs.task3.servcie.JwtTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class TokenFacade {

    private final JwtTokenService jwtTokenService;
    private final AuthenticationService authenticationService;

    public TokenFacade(final JwtTokenService jwtTokenService, final AuthenticationService authenticationService) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationService = authenticationService;
    }

    public TokenResponse getToken(final TokenRequest tokenRequest) {
        final Authentication authentication = authenticationService.getAuthentication(tokenRequest.username(), tokenRequest.password());
        final String token = jwtTokenService.getToken(authentication);
        return new TokenResponse(token);
    }
}
