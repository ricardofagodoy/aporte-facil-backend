package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.repository.CotacaoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class CotacaoRepositoryImpl implements CotacaoRepository {

    private final String cotacoesAcoesUrl;
    private final String cotacoesFiisUrl;
    private final Map<String, Double> cotacoes;

    public CotacaoRepositoryImpl(@Value("${cotacoes.acoes.url}") String cotacoesAcoesUrl,
                                 @Value("${cotacoes.fiis.url}") String cotacoesFiisUrl) {
        this.cotacoesAcoesUrl = cotacoesAcoesUrl;
        this.cotacoesFiisUrl = cotacoesFiisUrl;
        this.cotacoes = new HashMap<>();

        // Populate cotações when system boots up
        this.updateCotacoesAcoes();
        this.updateCotacoesFiis();
    }

    @Override
    public Double getCotacao(String ticker) {
        return this.cotacoes.getOrDefault(ticker, 0.0);
    }

    @Override
    public Set<String> getAvailableTickers() {
        return this.cotacoes.keySet();
    }

    @Scheduled(cron = "* */15 10-17 * * MON-FRI")
    private void updateCotacoesAcoes() {
        System.out.println("Updating cotacoes acoes at " + new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date()));
        this.callApi(this.cotacoesAcoesUrl);
    }

    @Scheduled(cron = "* */15 10-17 * * MON-FRI")
    private void updateCotacoesFiis() {
        System.out.println("Updating cotacoes FIIs at " + new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date()));
        this.callApi(this.cotacoesFiisUrl);
    }

    private void callApi(String url) {

        Scanner input = null;

        try {
            URL rowdata = new URL(url);
            URLConnection data = rowdata.openConnection();
            data.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            input = new Scanner(data.getInputStream());

            // Skip header
            if (input.hasNext())
                input.nextLine();

            while (input.hasNextLine())
                processCotacaoLine(input.nextLine());

        } catch (Exception e) {
            System.out.print(e.getMessage());
        } finally {
            if (input != null)
                input.close();
        }
    }

    private void processCotacaoLine(String line) {

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("pt"));

            String[] tokens = line.split(";");

            if (tokens.length > 1)
                this.cotacoes.put(tokens[0], format.parse(tokens[1]).doubleValue());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}