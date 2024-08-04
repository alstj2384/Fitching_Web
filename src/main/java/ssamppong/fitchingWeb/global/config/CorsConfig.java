package ssamppong.fitchingWeb.global.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CorsConfig {
    public static CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        ArrayList<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:8080");
        corsConfiguration.setAllowedOrigins(allowedOriginPatterns);

        ArrayList<String> allowedmethods = new ArrayList<>();
        allowedmethods.add("GET");
        allowedmethods.add("POST");
        allowedmethods.add("PUT");
        allowedmethods.add("DELETE");
        corsConfiguration.setAllowedOrigins(allowedmethods);

        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));

        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;


    }
}
