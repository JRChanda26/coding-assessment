package com.interview.interview_service.config;
 
// package com.interview.interview_service.config;
 
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
// import java.util.List;
 
// @Configuration
// public class WebConfig implements WebMvcConfigurer {
 
//     @Value("#{'${cors.origin.urls}'.split(',')}")
//     private List<String> originUrls; // Correct way to inject a list from properties
 
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("*")
//                 .allowedOrigins(originUrls.toArray(new String[0])) // Convert List to String array
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
//     }
// }
 
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
// import java.util.List;
 
// @Configuration
// public class WebConfig implements WebMvcConfigurer {
 
//     @Value("#{'${cors.origin.urls}'.split(',')}")
//     private List<String> originUrls; // Correct way to inject a list from properties
 
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("*") // Convert List to String array
//                 .allowedMethods("*")
//                 .allowCredentials(true)
//                 .allowedHeaders("*");
//     }
 
// }
 
 
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
 
import java.util.List;
 
@Configuration
public class CorsConfig {
 
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
       
        // Use specific allowed origins instead of "*"
        config.setAllowedOrigins(List.of("https://aippoint.ai/aippoint-userinterface/", "https://aippoint.ai/"));
       
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true); // Enables credentials (cookies, authorization headers)
       
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
 