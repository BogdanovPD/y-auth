package com.why.studio.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties("security")
public class SecurityProperties {

    private JwtProperties jwt;

    @Getter
    @Setter
    public static class JwtProperties {

        private Resource keyStore;
        private String keyStorePassword;
        private String keyPairAlias;
        private String keyPairPassword;

    }

}
