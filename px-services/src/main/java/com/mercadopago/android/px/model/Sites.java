package com.mercadopago.android.px.model;

import android.support.annotation.NonNull;
import java.util.Arrays;

/**
 * Utility class for sites.
 */
public final class Sites {

    public static final Site ARGENTINA =
         Site.createWith("MLA", "ARS", "https://www.mercadopago.com.ar/ayuda/terminos-y-condiciones_299", false);
    public static final Site BRASIL =
        Site.createWith("MLB", "BRL", "https://www.mercadopago.com.br/ajuda/termos-e-condicoes_300",false);
    public static final Site CHILE =
        Site.createWith("MLC", "CLP", "https://www.mercadopago.cl/ayuda/terminos-y-condiciones_299", false);
    public static final Site MEXICO =
        Site.createWith("MLM", "MXN", "https://www.mercadopago.com.mx/ayuda/terminos-y-condiciones_715", false);
    public static final Site COLOMBIA =
        Site.createWith("MCO", "COP", "https://www.mercadopago.com.co/ayuda/terminos-y-condiciones_299",true);
    public static final Site VENEZUELA =
        Site.createWith("MLV", "VES", "https://www.mercadopago.com.ve/ayuda/terminos-y-condiciones_299", false);
    public static final Site USA = Site.createWith("USA", "USD", "", false);
    public static final Site PERU =
        Site.createWith("MPE", "PEN", "https://www.mercadopago.com.pe/ayuda/terminos-condiciones-uso_2483", false);
    public static final Site URUGUAY =
        Site.createWith("MLU", "UYU", "https://www.mercadopago.com.uy/ayuda/terminos-y-condiciones-uy_2834", false);


    private static final Iterable<Site> SITES = Arrays.asList(
        ARGENTINA,
        BRASIL,
        CHILE,
        MEXICO,
        COLOMBIA,
        VENEZUELA,
        USA,
        PERU,
        URUGUAY
    );

    private Sites() {
    }

    /**
     * Given a site id returns a {@link Site} or throws.
     *
     * @param siteId searched site id.
     * @return site
     * @throws IllegalArgumentException if it does not exists.
     */
    @NonNull
    public static Site getById(@NonNull final String siteId) throws IllegalArgumentException {
        for (final Site site : SITES) {
            if (site.getId().equals(siteId)) {
                return site;
            }
        }
        throw new IllegalArgumentException("There is no site for that id");
    }
}
