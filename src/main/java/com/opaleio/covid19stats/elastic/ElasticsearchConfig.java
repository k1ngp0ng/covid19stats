package com.opaleio.covid19stats.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String elasticHost;

    @Value("${elasticsearch.port}")
    private int elasticPort;

    @Value("${elasticsearch.scheme}")
    private String scheme;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {

        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(new HttpHost(elasticHost, elasticPort, scheme))
        );

        return client;
    }
}
