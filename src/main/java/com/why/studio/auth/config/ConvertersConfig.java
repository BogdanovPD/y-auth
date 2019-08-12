package com.why.studio.auth.config;

import com.why.studio.auth.converters.UserDtoToUserEntityConverter;
import com.why.studio.auth.converters.UserEntityToUserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
public class ConvertersConfig {

    private final UserDtoToUserEntityConverter userDtoToUserEntityConverter;
    private final UserEntityToUserDtoConverter userEntityToUserDtoConverter;

    @Bean
    public ConversionService yConversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(new HashSet<>(Arrays.asList(
                userDtoToUserEntityConverter,
                userEntityToUserDtoConverter
        )));
        bean.afterPropertiesSet();
        return bean.getObject();
    }

}
