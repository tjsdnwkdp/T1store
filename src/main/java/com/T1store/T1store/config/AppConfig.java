package com.T1store.T1store.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    String apiKey = "3542251585645048";
    String secretKey = "eZ105wNyE5bfelaZysmngAYp2JaCXjf1FNFjUV3x4NbWPXsXU8wnfKr2lH1tqteKDKJyhJf81caw8hko";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }


}
