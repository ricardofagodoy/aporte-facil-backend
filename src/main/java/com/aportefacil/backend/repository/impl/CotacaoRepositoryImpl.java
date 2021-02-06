package com.aportefacil.backend.repository.impl;

import com.aportefacil.backend.model.InfoAtivo;
import com.aportefacil.backend.model.TipoAtivo;
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
    private final Map<String, InfoAtivo> cotacoes;

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
    public InfoAtivo getInfoAtivo(String ticker) {
        return this.cotacoes.getOrDefault(ticker, null);
    }

    @Override
    public Set<String> getAvailableTickers() {
        return this.cotacoes.keySet();
    }

    @Scheduled(cron = "0 */15 10-17 * * MON-FRI")
    private void updateCotacoesAcoes() {
        System.out.println("Updating cotacoes acoes at " + new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date()));
        List<String> lines = this.callApi(this.cotacoesAcoesUrl);

        lines.forEach(this::processAcaoLine);
    }

    @Scheduled(cron = "0 */15 10-17 * * MON-FRI")
    private void updateCotacoesFiis() {
        System.out.println("Updating cotacoes FIIs at " + new SimpleDateFormat("dd/MM HH:mm:ss").format(new Date()));
        List<String> lines = this.callApi(this.cotacoesFiisUrl);

        lines.forEach(this::processFIILine);
    }

    private List<String> callApi(String url) {

        Scanner input = null;

        List<String> resultLines = new ArrayList<>();

        try {
            URL rowdata = new URL(url);
            URLConnection data = rowdata.openConnection();
            data.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");

            input = new Scanner(data.getInputStream());

            // Skip header
            if (input.hasNext())
                input.nextLine();

            while (input.hasNextLine()) {
                resultLines.add(input.nextLine());
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        } finally {
            if (input != null)
                input.close();
        }

        return resultLines;
    }

    private void processAcaoLine(String line) {

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("pt"));

            String[] tokens = line.split(";");

            if (tokens.length > 1) {
                String ticker = tokens[0];
                String cotacao = tokens[1];
                String dy = tokens[2].isBlank() ? "0,00" : tokens[2];
                String pl = tokens[3].isBlank() ? "0,00" : tokens[3];

                this.cotacoes.put(ticker, new InfoAtivo(format.parse(cotacao).doubleValue(),
                        TipoAtivo.ACAO, null, format.parse(dy).doubleValue(), format.parse(pl).doubleValue()));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void processFIILine(String line) {

        try {
            NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag("pt"));

            String[] tokens = line.split(";");

            if (tokens.length > 1) {
                String ticker = tokens[0];
                String cotacao = tokens[1];
                String dy = tokens[2].isBlank() ? "0,00" : tokens[2];
                String pvp = tokens[3].isBlank() ? "0,00" : tokens[3];

                this.cotacoes.put(ticker, new InfoAtivo(format.parse(cotacao).doubleValue(),
                        TipoAtivo.FII, format.parse(pvp).doubleValue(), format.parse(dy).doubleValue(), null));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}