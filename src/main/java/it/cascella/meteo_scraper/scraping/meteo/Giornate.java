package it.cascella.meteo_scraper.scraping.meteo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class Giornate {
    private Map<GiornoOra,Clima> oraClima = new HashMap<>();
    public Map<GiornoOra, Clima> getOraClima() {
        return oraClima;
    }
    public void setOraClima(Map<GiornoOra, Clima> oraClima) {
        this.oraClima = oraClima;
    }
    public void setOra(GiornoOra ora, Clima clima) {
        oraClima.put(ora, clima);
    }
    @Override
    public String toString() {
        return "Giornata{" +
                "oraClima=" + oraClima.toString() +
                '}';
    }
}
