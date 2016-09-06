package com.countrypicker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Created by deanwild on 06/09/16.
 */
public class LocaleExtractor {

    class CountryList extends ArrayList<Country> {

        public boolean containsCountry(Country country) {
            Iterator<Country> it = iterator();

            while (it.hasNext()) {
                Country next = it.next();
                if (next.getCode().equalsIgnoreCase(country.getCode())) {
                    return true;
                }
            }

            return false;
        }
    }

    public List<Country> getAllCountries() {


        Locale[] locales = Locale.getAvailableLocales();
        CountryList countries = new CountryList();


        for (Locale locale : locales) {
            try {
                String iso = " ";// locale.getISO3Country();
                String code = locale.getCountry();
                String name = locale.getDisplayCountry();

                if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {

                    Country country = new Country(name, code, iso);

                    if (!countries.containsCountry(country)) {
                        countries.add(country);
                    }
                }

            } catch (MissingResourceException e) {
                //do nothing
            }

        }


        return countries;

    }
}
