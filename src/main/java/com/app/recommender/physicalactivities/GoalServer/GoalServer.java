package com.app.recommender.physicalactivities.GoalServer;

import com.app.recommender.diet.DietController;
import com.app.recommender.diet.Persistence.DietRepository;
import com.app.recommender.physicalactivities.ResourceRdfServer.PhysicalActivitiesServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.ConnectionFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@EnableDiscoveryClient
@SpringBootApplication
@EnableJms
@EnableMongoRepositories(basePackageClasses = GoalRepository.class)
@ComponentScan(value = {"com.app.recommender.physicalactivities.GoalServer", "com.app.recommender.diet"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern="com.app.recommender.diet.DietController"))
public class GoalServer {

    public static void main(String[] args) {
        System.setProperty("spring.config.name", "goal-server");
        SpringApplication.run(GoalServer.class, args);
    }

    @Bean
    public JmsListenerContainerFactory<?> goalFactory(ConnectionFactory connectionFactory,
                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        // This provides all boot's default to this factory, including the message converter
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverterGoals() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        ObjectMapper mapper = new ObjectMapper();

        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        mapper.registerModule(timeModule);
        converter.setObjectMapper(mapper);
        return converter;
    }
}
