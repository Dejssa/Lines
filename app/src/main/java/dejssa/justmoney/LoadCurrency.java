package dejssa.justmoney;

import android.support.design.widget.Snackbar;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Алексей on 11.09.2017.
 */

public class LoadCurrency implements Runnable{
    private MainActivity activity;

    private ArrayList<Currency> currencies = new ArrayList<>();

    private String[] currency_codes;

    boolean next_by = true;

    public LoadCurrency(MainActivity activity, String[] currency_codes) {
        this.activity = activity;
        this.currency_codes = currency_codes;
    }

    @Override
    public void run() {
        boolean good[] = {true,true};

        for(int i = 0; i < currency_codes.length; i++){
            Currency currency = new Currency(currency_codes[i]);
            try {

                next_by = true;
                connectToPage("http://www.nbrb.by/API/ExRates/Rates/" + currency_codes[i] + "?ParamMode=2", currency);

            } catch (IOException | JSONException e) {

                good[0] = false;
                e.printStackTrace();

            }
            if(good[0]) {
                try {
                    if (currency_codes[i].equals("PLN")) {
                        currency.setValuePL("1.0000");
                    } else
                        connectToPage("http://api.nbp.pl/api/exchangerates/rates/a/" + currency_codes[i] + "?format=json", currency);
                } catch (IOException | JSONException e) {
                    good[1] = false;
                    e.printStackTrace();
                }
            }
            if(good[0] && good[1])
                currencies.add(currency);
        }

        update();

    }

    private URL prepareURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setDoOutput(true);
        urlConnection.connect();
        return url;
    }

    private void connectToPage(String urlString, Currency currency) throws IOException, JSONException {

        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(   prepareURL (urlString).openStream()    ));

        StringBuilder report = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            report.append(line + "\n");
        }

        bufferedReader.close();

        String jsonString = report.toString();
        System.out.println("JSON: " + jsonString);
        parse(new JSONObject(jsonString), currency);
    }

    private void parse(JSONObject object, Currency currency) throws JSONException{

        if(next_by)parseBy(object, currency);
        else parsePl(object, currency);
    }

    private void parseBy(JSONObject container, Currency currency) throws JSONException{
        Log.v("Parsed value : BY" ,  container.toString());

        String value = container.getString("Cur_OfficialRate");

        currency.setValueBY(value);

        next_by = false;


    }

    private void parsePl(JSONObject container, Currency currency)  throws JSONException{
        Log.v("Parsed value : PL" ,  container.toString());

        JSONArray array = container.getJSONArray("rates");
        JSONObject obj = array.getJSONObject(0);

        String value = obj.getString("mid");

        currency.setValuePL(value);
    }

    private void update(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.loadResult(currencies);

                String result = currencies.size() == currency_codes.length ? "Updated" : "Something went wrong. Half of date were missed";

                Snackbar.make(activity.getMainScreen(), result,Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
