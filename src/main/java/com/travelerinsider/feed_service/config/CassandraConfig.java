package com.travelerinsider.feed_service.config;

import com.datastax.oss.driver.api.core.CqlSession;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
@Slf4j
@Observed
public class CassandraConfig {

    @Value("${scylla.contact-points}")
    private String contactPoints;

    @Value("${scylla.datacenter}")
    private String datacenter;

    @Value("${scylla.keyspace}")
    private String keyspace;

    @Bean
    public CqlSession session() {
        log.info("Creating CqlSession");
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress(contactPoints.split(":")[0],
                                                       Integer.parseInt(contactPoints.split(":")[1])))
                .withLocalDatacenter(datacenter)
                .withKeyspace(keyspace)
                .build();
    }
}
