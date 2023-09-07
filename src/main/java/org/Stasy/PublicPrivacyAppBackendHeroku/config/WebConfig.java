package org.Stasy.PublicPrivacyAppBackendHeroku.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        /** this is for email confirmation**/

        registry.addMapping("/subscribe")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("Origin", "Content-Type", "Accept");


        registry.addMapping("/collaborator/resetPassword1")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/resetPassword2")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/resetPassword3")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/resetPassword4")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");



        registry.addMapping("/collaborator/login")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/logout")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken")
                .exposedHeaders("dashboardToken");

        registry.addMapping("/collaborator/register")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/dashboard")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/resetPassword")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/collaborator/deleteAccount")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/forum/opinions/**")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");

        registry.addMapping("/forum/opinions/**")
                .allowedOrigins("https://public-privacy.xyz") // Update from allowedOriginPatterns to allowedOrigins
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Cookie","dashboardtoken","logintoken");

        registry.addMapping("/forum/opinions/add")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken","logintoken");

        registry.addMapping("/forum/opinions/edit/{id}")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS","PUT")
                .allowedHeaders("Origin", "Content-Type", "Accept","Authorization","Cookie","dashboardtoken", "logintoken");


        /** this is for  drafts**/

        registry.addMapping("/forum/drafts/**")
                .allowedOrigins("https://public-privacy.xyz") // Update from allowedOriginPatterns to allowedOrigins
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Cookie", "dashboardtoken", "logintoken");

        registry.addMapping("/forum/drafts/add")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Cookie", "dashboardtoken", "logintoken");

        registry.addMapping("/forum/drafts/edit/{id}")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Cookie", "dashboardtoken", "logintoken");

        registry.addMapping("/forum/drafts/delete/{id}")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Cookie", "dashboardtoken", "logintoken");


        registry.addMapping("/api/coordinates-with-info")
                .allowedOriginPatterns("https://public-privacy.xyz")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization", "Cookie", "dashboardtoken", "logintoken");
    }

}
