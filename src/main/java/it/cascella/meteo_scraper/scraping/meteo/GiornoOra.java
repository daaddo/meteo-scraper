package it.cascella.meteo_scraper.scraping.meteo;

public record GiornoOra(Integer giorno, Integer ora) {
    public GiornoOra {
        if (giorno < 1 || giorno > 31) {
            throw new IllegalArgumentException("giorno deve essere compreso tra 1 e 31");
        }
        if (ora < 0 || ora > 23) {
            throw new IllegalArgumentException("ora deve essere compreso tra 0 e 23");
        }
    }
}
