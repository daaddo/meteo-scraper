package it.cascella.meteo_scraper.scraping.meteo;

import it.cascella.meteo_scraper.scraping.Scraper;

import java.util.*;
import java.util.function.Consumer;

public abstract class MeteoScraper implements Scraper {

    private Map<GiornoOra,Clima> oraClima = new HashMap<>();

    public Map<GiornoOra, Clima> getOraClima() {
        return oraClima;
    }
    public void setOraClima(Map<GiornoOra, Clima> oraClima) {
        this.oraClima = oraClima;
    }
    public void addOra(GiornoOra ora, Clima clima){
        oraClima.put(ora,clima);
    }
    public void setOra(GiornoOra ora, Clima clima) {
        oraClima.put(ora, clima);
    }

    public void deleteOra(GiornoOra ora) {
        oraClima.remove(ora);
    }
    public Map<GiornoOra,Clima> checkChanges(int ore, Map<GiornoOra,Clima> toCheck){

        //inizializzazione del calendario
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        HashMap<GiornoOra, Clima> giornoOraClimaHashMap = new HashMap<GiornoOra, Clima>();

        int counter = 0;
        while(counter < ore){
            if(currentHour+counter<24){
                GiornoOra giornoOra = new GiornoOra(currentDay, currentHour+counter);
                Optional<Clima> clima = checkSingleChanges(giornoOra, toCheck.get(giornoOra));
                clima.ifPresent(clima1 -> giornoOraClimaHashMap.put(giornoOra, clima1));

            }else{
                currentHour = 0;
                currentDay++;
                if (currentDay > calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
                    currentDay =1;
                }
            }
            counter++;
        }
        return giornoOraClimaHashMap;
    }
    private Optional<Clima> checkSingleChanges(GiornoOra giornoOraToCheck, Clima climaToCheck){
        Clima clima = oraClima.get(giornoOraToCheck);
        if (clima != climaToCheck){
            return Optional.of(climaToCheck);
        }
        return Optional.empty();
    }
    @Override
    public String toString() {
        return "MeteoScraper{" +
                "meteoGiornate=" + oraClima.toString() +
                '}';
    }

}
