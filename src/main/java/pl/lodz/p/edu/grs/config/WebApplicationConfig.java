package pl.lodz.p.edu.grs.config;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pl.lodz.p.edu.grs.serializer.LocalDateDeserializer;
import pl.lodz.p.edu.grs.serializer.LocalDateSerializer;
import pl.lodz.p.edu.grs.serializer.LocalDateTimeDeserializer;
import pl.lodz.p.edu.grs.serializer.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class WebApplicationConfig
        extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
        builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
        builder.serializerByType(LocalDate.class, new LocalDateSerializer());
        builder.deserializerByType(LocalDate.class, new LocalDateDeserializer());

        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

}