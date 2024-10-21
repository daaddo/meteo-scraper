package it.cascella.meteo_scraper.scraping.meteo;

import it.cascella.meteo_scraper.scraping.Scraper;
import it.cascella.meteo_scraper.scraping.meteo.Giornata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MeteoScraper implements Scraper {
    private LinkedList<Giornata> meteoGiornate;

    public List<Giornata> getMeteoGiornate() {
        return Collections.unmodifiableList(meteoGiornate);
    }
    public void setMeteoGiornate(LinkedList<Giornata> meteoGiornate) {
        this.meteoGiornate = meteoGiornate;
    }
}
