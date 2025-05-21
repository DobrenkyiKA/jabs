package com.kdob.jabs.task6.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class ServiceHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        if (isSomeServiceHealthy()) {
            return Health.up().withDetails(Map.of("External service ", "Healthy")).build();
        }
        return Health.down().withDetails(Map.of("External service ", "Is Not-Healthy")).build();
    }

    private boolean isSomeServiceHealthy() {
        return new Random().nextBoolean();

    }
}
