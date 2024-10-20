package com.server.InvestiMate.common.client.openai.header;

import feign.RequestInterceptor;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantHeaderConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        Dotenv dotenv = Dotenv.load();
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"));
            
            requestTemplate.header("OpenAI-Beta", "assistants=v2");
        };
    }
}
