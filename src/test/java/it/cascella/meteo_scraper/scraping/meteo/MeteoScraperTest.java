package it.cascella.meteo_scraper.scraping.meteo;

import it.cascella.meteo_scraper.scraping.sites.IlMeteoScraper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MeteoScraperTest {
    private MeteoScraper meteoScraper = new IlMeteoScraper("Perugia");
    private Map<GiornoOra, Clima> toCheck;
    @BeforeEach
    void setUp() {


        toCheck = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // Adding some sample data to the map
        toCheck.put(new GiornoOra(currentDay, currentHour), Clima.COPERTO);
        toCheck.put(new GiornoOra(currentDay, currentHour + 1), Clima.SERENO);

        meteoScraper.setOraClima(toCheck);
    }

    @Test
    void checkChanges() {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        Map<GiornoOra, Clima> giornoOraClimaHashMap = new HashMap<>();
        giornoOraClimaHashMap.put(new GiornoOra(currentDay, currentHour), Clima.COPERTO);
        giornoOraClimaHashMap.put(new GiornoOra(currentDay, currentHour+ 1), Clima.COPERTO);

        Map<GiornoOra, Clima> giornoOraClimaMap = meteoScraper.checkChanges(2, giornoOraClimaHashMap);

        assertNotNull(giornoOraClimaMap);
        assertEquals(1, giornoOraClimaMap.size());
        HashMap<GiornoOra, Clima> giornoOraClimaHashMap1 = new HashMap<>();
        giornoOraClimaHashMap1.put(new GiornoOra(currentDay, currentHour+1), Clima.COPERTO);
        assertEquals(giornoOraClimaMap, giornoOraClimaHashMap1);

        meteoScraper.setOraClima(new HashMap<>());
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour), Clima.COPERTO);
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour+1), Clima.COPERTO);
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour+2), Clima.COPERTO);
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour+4), Clima.COPERTO);
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour+5), Clima.COPERTO);

        giornoOraClimaHashMap.clear();
        giornoOraClimaHashMap.put(new GiornoOra(currentDay, currentHour+3), Clima.COPERTO);

        giornoOraClimaMap.clear();
        giornoOraClimaMap = meteoScraper.checkChanges(8, giornoOraClimaHashMap);
        assertEquals(0, giornoOraClimaMap.size());

        meteoScraper.setOraClima(new HashMap<>());
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour), Clima.COPERTO);
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour+1), Clima.COPERTO);
        meteoScraper.addOra(new GiornoOra(currentDay, currentHour+5), Clima.COPERTO);

        giornoOraClimaHashMap.clear();
        giornoOraClimaHashMap.put(new GiornoOra(currentDay, currentHour+3), Clima.COPERTO);
        assertEquals(0, giornoOraClimaMap.size());
    }
}