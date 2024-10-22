package it.cascella.meteo_scraper.scraping.meteo;

import it.cascella.meteo_scraper.scraping.Scraper;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class MeteoScraper implements Scraper {

    private Map<GiornoOra,Clima> oraClima = new HashMap<>();

    public void setOraClima(Map<GiornoOra, Clima> oraClima) {
        this.oraClima = oraClima;
    }
    public void addOra(GiornoOra ora, Clima clima){
        oraClima.put(ora,clima);
    }
    public void setOra(GiornoOra ora, Clima clima) {
        oraClima.put(ora, clima);
    }

    public void deleteOraClima(GiornoOra ora) {
        oraClima.remove(ora);
    }
    public void clearOraClima(){
        oraClima.clear();
    }
    public Optional<Clima> isItGoingToRain(int hours){
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < hours; i++) {
            GiornoOra giornoOra= new GiornoOra(calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.HOUR_OF_DAY)+i);
            if (oraClima.containsKey(giornoOra) && Clima.isGonnaRain(oraClima.get(giornoOra))){
                return Optional.of(oraClima.get(giornoOra));
            }
        }
        return Optional.empty();
    }
    public Map<GiornoOra,Clima> checkChanges(int ore, Map<GiornoOra,Clima> toCheck){
        if (ore <0){
            throw new IllegalArgumentException("Trying to compute in the past");
        }
        //inizializzazione del calendario
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        HashMap<GiornoOra, Clima> giornoOraClimaHashMap = new HashMap<>();

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
        return new GiornoOra(day, hour+hourAfter);
    }

    private Optional<Clima> checkSingleChanges(GiornoOra giornoOraToCheck, Clima climaToCheck){

        if (oraClima.containsKey(giornoOraToCheck)) {
            Clima clima = oraClima.get(giornoOraToCheck);
            if (clima != climaToCheck && climaToCheck != null) {
                return Optional.of(climaToCheck);
            }
        }
        else{
            //questo if controlla se la ora successiva e l ora precedente sono presenti nella mappa, se presenti e almeno uno dei due clima è uguale a quello passato
            //in argomento aggiunge l ora, se non sono presenti fa lo stesso controllo per le due ore successive
            if (isNextOrPreviousEquals(giornoOraToCheck,climaToCheck,1)){
                addOra(giornoOraToCheck, climaToCheck);
            }else if (isNextOrPreviousNull(giornoOraToCheck,1) && isNextOrPreviousEquals(giornoOraToCheck,climaToCheck,2)) {
                addOra(giornoOraToCheck, climaToCheck);
            } else{
                addOra(giornoOraToCheck, climaToCheck);
                return Optional.of(climaToCheck);
            }
        }
        return Optional.empty();
    }

    private boolean isNextOrPreviousNull(GiornoOra giornoOra, int hours){
        GiornoOra previusHour = getPreviousHour(giornoOra , hours);
        GiornoOra nextHour = getNextHour(giornoOra, hours);
        return oraClima.get(previusHour)==null || oraClima.get(nextHour)==null;
    }

    private boolean isNextOrPreviousEquals(GiornoOra giornoOra, Clima clima, int hours){
        if (clima == null){
            throw new IllegalArgumentException("clima null");
        }
        GiornoOra previusHour = getPreviousHour(giornoOra , hours);
        GiornoOra nextHour = getNextHour(giornoOra, hours);
        return clima == oraClima.get(previusHour) || clima == oraClima.get(nextHour);
    }




    @Override
    public String toString() {
        return oraClima.toString();
    }

    public static void main(String[] args) {
        System.out.println(Calendar.DAY_OF_MONTH);
    }
}
