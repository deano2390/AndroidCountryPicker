package com.countrypicker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CountryPicker extends DialogFragment {

    static final java.lang.String ARG_REMOVE_OPTION = "ARG_REMOVE_OPTION";
    static final String ARG_TITLE = "ARG_TITLE";
    /**
     * View components
     */
    private EditText searchEditText;
    private ListView countryListView;

    /**
     * Adapter for the listview
     */
    private CountryListAdapter adapter;

    /**
     * Hold all countries, sorted by country name
     */
    List<Country> allCountriesList;

    /**
     * Hold countries that matched user query
     */
    private static List<Country> selectedCountriesList;

    /**
     * Listener to which country user selected
     */
    private CountryPickerListener listener;
    String removeOption;

    /**
     * Set listener
     *
     * @param listener
     */
    public void setListener(CountryPickerListener listener) {
        this.listener = listener;
    }


    /**
     * Convenient function to get currency code from country code currency code
     * is in English locale
     *
     * @param countryCode
     * @return
     */
    public static Currency getCurrencyCode(String countryCode) {
        try {
            return Currency.getInstance(new Locale("en", countryCode));
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Get all countries with code and name from res/raw/countries.json
     *
     * @return
     */
    static List<Country> getAllCountries(String removeOption) {

        List<Country> allCountriesList = new ArrayList<Country>();

        try {
            allCountriesList = new LocaleExtractor().getAllCountries();

            // Sort the all countries list based on country name
            Collections.sort(allCountriesList, new Comparator<Country>() {
                @Override
                public int compare(Country lhs, Country rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            String userLocale = Locale.getDefault().getCountry();

            if (userLocale != null && userLocale.length() > 0) {
                int indexOfUserLocale = -1;

                for (int i = 0; i < allCountriesList.size(); i++) {
                    Country country = allCountriesList.get(i);
                    if (country.getCode() != null && country.getCode().equals(userLocale)) {
                        indexOfUserLocale = i;
                        break;
                    }
                }

                if (indexOfUserLocale > -1)
                    Collections.swap(allCountriesList, 0, indexOfUserLocale);

                if (removeOption != null) {
                    Country removeCountry = new Country();
                    removeCountry.setName(removeOption);
                    allCountriesList.add(0, removeCountry);
                }

            }

            // Initialize selected countries with all countries
            selectedCountriesList = new ArrayList<Country>();
            selectedCountriesList.addAll(allCountriesList);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return allCountriesList;
    }

    public static HashMap getCountriesMap() {
        List<Country> allCountriesList = getAllCountries(null);

        HashMap<String, Country> map = new HashMap();


        for (Country country : allCountriesList) {
            map.put(country.getCode(), country);
        }

        return map;
    }


    /**
     * To support show as dialog
     *
     * @param dialogTitle
     * @param removeOption
     * @return
     */
    public static CountryPicker newInstance(String dialogTitle, String removeOption) {
        CountryPicker picker = new CountryPicker();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, dialogTitle);
        bundle.putString(ARG_REMOVE_OPTION, removeOption);
        picker.setArguments(bundle);
        return picker;
    }

    /**
     * Create view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.country_picker, null);

        // Set dialog title if show as dialog
        Bundle args = getArguments();
        if (args != null) {
            removeOption = args.getString(ARG_REMOVE_OPTION);
            String dialogTitle = args.getString(ARG_TITLE);
            getDialog().setTitle(dialogTitle);

            int width = getResources().getDimensionPixelSize(
                    R.dimen.cp_dialog_width);
            int height = getResources().getDimensionPixelSize(
                    R.dimen.cp_dialog_height);
            getDialog().getWindow().setLayout(width, height);
        }

        // Get countries from the system
        getAllCountries(removeOption);

        // Get view components
        searchEditText = (EditText) view
                .findViewById(R.id.country_picker_search);
        countryListView = (ListView) view
                .findViewById(R.id.country_picker_listview);

        // Set adapter
        adapter = new CountryListAdapter(getActivity(), selectedCountriesList);
        countryListView.setAdapter(adapter);

        // Inform listener
        countryListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    Country country = selectedCountriesList.get(position);
                    listener.onSelectCountry(country.getName(),
                            country.getCode());
                }
            }
        });

        // Search for which countries matched user query
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return true;
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        return view;
    }

    /**
     * Search allCountriesList contains text and put result into
     * selectedCountriesList
     *
     * @param text
     */
    @SuppressLint("DefaultLocale")
    private void search(String text) {
        selectedCountriesList.clear();

        for (Country country : allCountriesList) {

            if (country.getName().toLowerCase(Locale.ENGLISH)
                    .startsWith(text.toLowerCase())) {
                selectedCountriesList.add(country);
            }
        }

        if (selectedCountriesList.size() <= 0) {
            for (Country country : allCountriesList) {

                if (country.getName().toLowerCase(Locale.ENGLISH)
                        .contains(text.toLowerCase())) {
                    selectedCountriesList.add(country);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

}
