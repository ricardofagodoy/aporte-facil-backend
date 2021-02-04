package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.helper.SSLHelper;
import com.aportefacil.backend.repository.TaxaTesouroRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public class TaxaTesouroRepositoryImpl implements TaxaTesouroRepository {

    private final Logger logger = LoggerFactory.getLogger(TaxaTesouroRepository.class);

    private final String REFERER_HEADER = "Referer";
    private final String USER_AGENT_HEADER = "User-Agent";

    private final String taxaTesouroUrl;
    private final String taxaTesouroRefererHeader;
    private final String userAgentHeader;
    private final String taxaTesouroAttributeName;
    private Double taxaIpca = 0.0;

    private RestTemplate restTemplate;

    @Autowired
    public TaxaTesouroRepositoryImpl(@Value("${cotacoes.taxa.tesouro.url}") String taxaTesouroUrl,
                                     @Value("${cotacoes.taxa.tesouro.refererHeader}") String taxaTesouroRefererHeader,
                                     @Value("${cotacoes.header.userAgentHeader}") String userAgentHeader,
                                     @Value("${cotacoes.taxa.tesouro.attributeName}") String taxaTesouroAttributeName,
                                     RestTemplate restTemplate) throws KeyManagementException, NoSuchAlgorithmException {
        this.taxaTesouroUrl = taxaTesouroUrl;
        this.taxaTesouroRefererHeader = taxaTesouroRefererHeader;
        this.userAgentHeader = userAgentHeader;
        this.taxaTesouroAttributeName = taxaTesouroAttributeName;
        this.restTemplate = restTemplate;
        SSLHelper.turnOffSslChecking();

        // Populate taxa when system boots up
        this.updateTesouroTaxa();
    }

    public Double getTaxaIpca() {
        return taxaIpca;
    }

    @Scheduled(cron = "0 */30 10-17 * * MON-FRI")
    private void updateTesouroTaxa() {
        logger.info("Updating tesouro taxa at " + new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date()));
        try {
            taxaIpca = this.callApi(taxaTesouroUrl);
        } catch (JsonProcessingException e) {
            logger.error("Erro ao tentar processar o request JSON na chamada da API de taxa do tesouro.");
        }
    }

    private Double callApi(String url) throws JsonProcessingException {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(REFERER_HEADER, taxaTesouroRefererHeader);
        headers.set(USER_AGENT_HEADER, userAgentHeader);

        final var entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode root = mapper.readTree(response.getBody());

        return root.findPath(taxaTesouroAttributeName).asDouble();
    }
}