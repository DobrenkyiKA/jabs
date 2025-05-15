package com.kdob.jabs.task3.servcie;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    public JwtTokenService(final JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String getToken(final Authentication authentication) {
        final JwtClaimsSet claims = createJwtClaimsSet(authentication);
        return getTokenValue(claims);
    }

    private static JwtClaimsSet createJwtClaimsSet(final Authentication authentication) {
        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", getScope(authentication))
                .build();
    }

    private static String getScope(final Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
    }

    private String getTokenValue(final JwtClaimsSet claims) {
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
