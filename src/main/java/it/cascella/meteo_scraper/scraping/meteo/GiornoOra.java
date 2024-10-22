package it.cascella.meteo_scraper.scraping.meteo;

public record GiornoOra(Integer giorno, Integer ora) {
    public GiornoOra {
        if (ora < 0 || ora > 23) {
            while(ora <0){
                giorno--;
                ora += 24;
            }
            while(ora > 23){
                giorno++;
                ora -= 24;
            }
        }
        if (giorno < 1 || giorno > 31) {
            if (giorno >31) {
                giorno -= 31;
            }
            if (giorno <1){
                giorno += 31;
            }
        }
    }
}
