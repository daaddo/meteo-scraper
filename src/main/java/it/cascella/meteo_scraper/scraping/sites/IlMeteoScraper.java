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
    private Map<String,Clima> stringToClima = new HashMap<>();

    private String provincia;

    public IlMeteoScraper(String provincia) {
        this.provincia = provincia;
        stringToClima.put("sereno", Clima.SERENO);
        stringToClima.put("nuvoloso", Clima.NUVOLOSO);
        stringToClima.put("coperto", Clima.COPERTO);
        stringToClima.put("poco nuvoloso", Clima.POCO_NUVOLOSO);
        stringToClima.put("nubi sparse", Clima.NUBI_SPARSE);
        stringToClima.put("pioggia", Clima.PIOGGIA);
        stringToClima.put("pioggia debole", Clima.PIOGGIA_DEBOLE);
        stringToClima.put("pioggia e schiarite", Clima.PIOGGIA_E_SCHIARITE);
        stringToClima.put("nebbia", Clima.NEBBIA);
        stringToClima.put("neve", Clima.NEVE);
        stringToClima.put("pioggia mista a neve", Clima.PIOGGIA_MISTA_A_NEVE);
        stringToClima.put("temporale e schiarite", Clima.TEMPORALE_E_SCHIARITE);
    }

    //get the key from the value;

    @Override
    public void scrape() {
        try {
            //get the document and select the div with the infos about the weather, hour and day
            Document document = Jsoup.connect(url+this.provincia).get();
            Elements elements = document.select("div.meteo-dialog");


            //iterate over the elements, get the weather infos and adding
            for (Element element : elements) {

                //get the data-time attribute and split it to get the day and the hour
                String data = element.attr("data-time");
                String[] split = data.split("-");
                Integer giorno = Integer.parseInt(split[0]);
                Integer ora = Integer.parseInt(split[2]);
                System.out.println("a "+provincia+" alle "+ora+" il tempo sarà: "+element.select(".previ-descri").text());
                //add the weather infos
                addOra(new GiornoOra(giorno,ora),stringToClima.get(element.select(".previ-descri").text()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        IlMeteoScraper ilMeteoScraper = new IlMeteoScraper("perugia");
        ilMeteoScraper.scrape();
        System.out.println(ilMeteoScraper.toString());
    }




}
