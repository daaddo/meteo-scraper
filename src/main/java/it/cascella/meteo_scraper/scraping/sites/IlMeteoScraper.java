package it.cascella.meteo_scraper.scraping.sites;

import it.cascella.meteo_scraper.scraping.Scraper;
import it.cascella.meteo_scraper.scraping.meteo.Clima;
import it.cascella.meteo_scraper.scraping.meteo.GiornoOra;
import it.cascella.meteo_scraper.scraping.meteo.MeteoScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.*;


public class IlMeteoScraper extends MeteoScraper implements Scraper {
    private final String url = "https://www.ilmeteo.it/meteo/";
    private EnumMap<Clima,String> climaToString = new EnumMap<>(Clima.class);
    private String provincia;

    public IlMeteoScraper(String provincia) {
        this.provincia = provincia;
        climaToString.put(Clima.SERENO,"sereno");
        climaToString.put(Clima.NUVOLOSO,"nuvoloso");
        climaToString.put(Clima.VENTO,"poco nuvoloso");
        climaToString.put(Clima.TEMPORALE,"nubi sparse");
        climaToString.put(Clima.PIOGGIA,"pioggia");
        climaToString.put(Clima.PIOGGIA_DEBOLE,"pioggia debole");
        climaToString.put(Clima.PIOGGIA_E_SCHIARITE,"pioggia e schiarite");
        climaToString.put(Clima.NEBBIA,"nebbia");
        climaToString.put(Clima.NEVE,"neve");
        climaToString.put(Clima.PIOGGIA_MISTA_A_NEVE,"pioggia mista a neve");
    }
    private Clima findClima(String clima) {
        for (Map.Entry<Clima, String> clima1 : climaToString.entrySet()) {
            if (clima1.equals(clima)) {
                return clima1.getKey();
            }
        }
        return null;
    }
    @Override
    public void scrape() {
        try {
            //get the document and select the div with the infos about the weather, hour and day
            Document document = Jsoup.connect(url+this.provincia).get();
            Elements elements = document.select("div.meteo-dialog");


            //iterate over the elements, get the weather infos and adding
            for (Element element : elements) {


                String data = element.attr("data-time");
                String[] split = data.split("-");
                Integer giorno = Integer.parseInt(split[0]);
                Integer ora = Integer.parseInt(split[2]);


                Clima clima = findClima(element.select(".previ-descri").text());
                addGiornata(new GiornoOra(giorno,ora),clima);

                System.out.println("-------------------------------------------------");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        IlMeteoScraper ilMeteoScraper = new IlMeteoScraper("perugia");
        ilMeteoScraper.scrape();
    }


}
