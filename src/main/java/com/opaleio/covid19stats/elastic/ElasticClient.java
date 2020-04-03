package com.opaleio.covid19stats.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opaleio.covid19stats.elastic.model.ElasticIndex;
import org.apache.tomcat.util.codec.binary.Base64;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class ElasticClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticClient.class);

    private final String url;
    private final String user;
    private final String password;
    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper objectMapper;

    public ElasticClient(@Value("${elasticsearch.host}") String url, @Value("${elasticsearch.user}") String user, @Value("${elasticsearch.password}") String password,
                         RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    public void bulkInsert(List<ElasticIndex> documents, int nbElementsToInsertInBulk, String elasticIndex) {
        int nbTotalElementsToInsert = documents.size();
        int lowerBound = 0;
        int upperBound;
        LOGGER.info("{} elements to insert to {}", nbTotalElementsToInsert, url);

        while(lowerBound <= nbTotalElementsToInsert) {
            upperBound = getUpperBound(lowerBound, nbElementsToInsertInBulk, nbTotalElementsToInsert);
            LOGGER.debug("Inserting elements from {} to {}", lowerBound, upperBound);

            List<ElasticIndex> documentsToInsert = extractDocumentsBucket(documents, lowerBound, upperBound);
            launchBulkInsertQuery(documentsToInsert, elasticIndex);
            lowerBound += nbElementsToInsertInBulk;
        }
    }

    private List<ElasticIndex> extractDocumentsBucket(List<ElasticIndex> documents, int lowerBound, int upperBound) {
        return documents.subList(lowerBound, upperBound + 1);
    }

    private void launchBulkInsertQuery(List<ElasticIndex> documents, String index) {
        BulkRequest bulkRequest = new BulkRequest();

        documents.forEach(document -> {
            IndexRequest indexRequest = new IndexRequest(index).
                    source(objectMapper.convertValue(document, Map.class));
            bulkRequest.add(indexRequest);
        });

        try {
            restHighLevelClient.bulk(bulkRequest, buildRequestOptions());
        } catch(IOException e) {
            LOGGER.error("Error occurred when calling elastic host :", e);
        }
    }

    private int getUpperBound(int lowerBound, int nbElementsToCompute, int nbTotalElements) {
        int upperBound = lowerBound + nbElementsToCompute;
        return Math.min(nbTotalElements, upperBound) -1;
    }

    private RequestOptions buildRequestOptions() {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Authorization", createBasicAuthHeader(user, password));
        builder.setHttpAsyncResponseConsumerFactory(
            new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(1024 * 1024 * 100));
        return builder.build();
    }

    private String createBasicAuthHeader(String user, String password) {
        String auth = user + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(StandardCharsets.US_ASCII));
        return "Basic " + new String(encodedAuth);
    }
}
