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
        if (ore <0){
            throw new IllegalArgumentException("Trying to compute in the past");
        }
        //inizializzazione del calendario
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        HashMap<GiornoOra, Clima> giornoOraClimaHashMap = new HashMap<GiornoOra, Clima>();

        int counter = 0;
        while(counter < ore){
            if(currentHour+counter<24){
                GiornoOra giornoOra = new GiornoOra(currentDay, currentHour+counter);
                if(toCheck.containsKey(giornoOra)) {
                    //check if
                    Optional<Clima> clima = checkSingleChanges(giornoOra, toCheck.get(giornoOra));

                    clima.ifPresent(clima1 -> giornoOraClimaHashMap.put(giornoOra, clima1));
                }
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

    private GiornoOra getPreviousHour(GiornoOra giornoOra, int hourBefore){
        int hour = giornoOra.ora();
        int day = giornoOra.giorno();
        if (hour == 0){
            return new GiornoOra(day-1, 24-hourBefore);
        }
        return new GiornoOra(day, hour-hourBefore);
    }

    private GiornoOra getNextHour(GiornoOra giornoOra, int hourAfter){
        int hour = giornoOra.ora();
        int day = giornoOra.giorno();
        if (hour == 23){
            return new GiornoOra(day+1, 0);
        }
        return new GiornoOra(day, hour+1);
    }

    private Optional<Clima> checkSingleChanges(GiornoOra giornoOraToCheck, Clima climaToCheck){

        if (oraClima.containsKey(giornoOraToCheck)) {
            Clima clima = oraClima.get(giornoOraToCheck);
            if (clima != climaToCheck && climaToCheck != null) {
                return Optional.of(climaToCheck);
            }
        }
        else{

            GiornoOra previusHour = getPreviousHour(giornoOraToCheck , 1);
            GiornoOra nextHour = getNextHour(giornoOraToCheck, 1);
            GiornoOra previusPreviousHour = getPreviousHour(giornoOraToCheck,2);
            GiornoOra nextNextHour = getNextHour(giornoOraToCheck,2);
            //questo if controlla se la ora successiva e l ora precedente sono presenti nella mappa, se presenti e almeno uno dei due clima è uguale a quello passato
            //in argomento aggiunge l ora, se non sono presenti fa lo stesso controllo per le due ore successive
            //necessita un refactor
            if ((climaToCheck == oraClima.get(previusHour) || climaToCheck == oraClima.get(nextHour)) ||
                    (oraClima.get(previusHour) == null && oraClima.get(nextHour) == null &&
                            (climaToCheck == oraClima.get(previusPreviousHour) || climaToCheck == oraClima.get(nextNextHour)))) {
                addOra(giornoOraToCheck, climaToCheck);
            }
            else{
                addOra(giornoOraToCheck, climaToCheck);
                return Optional.of(climaToCheck);
            }
        }
        return Optional.empty();
    }


    private boolean isNextOrPreviousEquals(GiornoOra giornoOra, Clima clima, int hours){
        GiornoOra previusHour = getPreviousHour(giornoOra , hours);
        GiornoOra nextHour = getNextHour(giornoOra, hours);
        return oraClima.get(previusHour) == clima || clima == oraClima.get(nextHour);
    }




    @Override
    public String toString() {
        return oraClima.toString();
    }

}
