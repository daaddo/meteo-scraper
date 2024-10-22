package it.cascella.meteo_scraper.scraping.meteo;

public enum Clima {
    SERENO,
    NUVOLOSO,
    POCO_NUVOLOSO,
    NUBI_SPARSE,
    PIOGGIA,
    PIOGGIA_DEBOLE,
    PIOGGIA_E_SCHIARITE,
    NEBBIA,
    NEVE,
    PIOGGIA_MISTA_A_NEVE,
    TEMPORALE,
    TEMPORALE_E_SCHIARITE,
    GRANDINE,


    VENTO,

    TEMPORALE_FORTE,
    GRANDINE_FORTE,
    NEVE_FORTE,
    COPERTO,
    NEBBIA_FITTA;


    public static boolean isGonnaRain(Clima clima){
        return clima == PIOGGIA || clima == PIOGGIA_DEBOLE || clima == PIOGGIA_E_SCHIARITE || clima == PIOGGIA_MISTA_A_NEVE || clima == TEMPORALE ||
                clima == TEMPORALE_E_SCHIARITE || clima == GRANDINE || clima == TEMPORALE_FORTE || clima == GRANDINE_FORTE || clima == NEVE_FORTE;
    }
}
