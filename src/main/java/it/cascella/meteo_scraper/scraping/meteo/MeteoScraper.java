package it.cascella.meteo_scraper.scraping.meteo;

import it.cascella.meteo_scraper.scraping.Scraper;

public abstract class MeteoScraper implements Scraper {
    private Giornate meteoGiornate = new Giornate();

    public Giornate getMeteoGiornate() {
        return meteoGiornate;
    }
    public void setMeteoGiornate(Giornate meteoGiornate) {
        this.meteoGiornate = meteoGiornate;
    }
    public void addGiornata(GiornoOra ora, Clima clima) {
        meteoGiornate.setOra(ora,clima);
    }
    public void deleteGiornata(GiornoOra ora) {
        meteoGiornate.getOraClima().remove(ora);
    }
}
