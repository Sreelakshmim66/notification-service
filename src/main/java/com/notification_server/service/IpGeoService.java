package com.notification_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

/**
 * Resolves geographic location from a client IP address.
 *
 * Uses the free ipapi.co API: GET https://ipapi.co/{ip}/json/
 * No API key required for up to 1 000 requests/day.
 *
 * Returns null fields gracefully if the lookup fails or the IP is
 * a private/loopback address (127.0.0.1, ::1, 192.168.x.x etc.)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IpGeoService {

    private final WebClient.Builder webClientBuilder;

    @Value("${app.ip-geo-url}")
    private String ipGeoBaseUrl;

    /** Returns a two-element array [country, city], either value may be null */
    public String[] resolve(String ip) {
        if (ip == null || ip.isBlank() || isPrivateIp(ip)) {
            log.debug("Skipping geo-lookup for private/blank IP: {}", ip);
            return new String[]{null, null};
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = webClientBuilder
                    .baseUrl(ipGeoBaseUrl)
                    .build()
                    .get()
                    .uri("/{ip}/json/", ip)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .timeout(Duration.ofSeconds(3))
                    .block();

            if (result == null || result.containsKey("error")) {
                log.warn("IP geo-lookup returned error for {}: {}", ip,
                        result != null ? result.get("reason") : "null response");
                return new String[]{null, null};
            }

            String country = (String) result.get("country_name");
            String city    = (String) result.get("city");
            log.info("IP {} resolved to {}, {}", ip, city, country);
            return new String[]{country, city};

        } catch (Exception e) {
            log.warn("IP geo-lookup failed for {}: {}", ip, e.getMessage());
            return new String[]{null, null};
        }
    }

    private boolean isPrivateIp(String ip) {
        return ip.startsWith("127.")
                || ip.startsWith("192.168.")
                || ip.startsWith("10.")
                || ip.startsWith("172.")
                || ip.equals("0:0:0:0:0:0:0:1")
                || ip.equals("::1")
                || ip.equals("localhost");
    }
}
