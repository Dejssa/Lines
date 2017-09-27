package dejssa.justmoney;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private String[] currency_codes = {"USD", "EUR", "RUB", "PLN"};
    private final String PREF_STORE = "CURR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindInterface();
        restore();

        //startUpdate();
    }

    private void restore(){
        SharedPreferences save = getSharedPreferences(PREF_STORE, MODE_PRIVATE);
        ArrayList<Currency> currencies = new ArrayList<>();
        for(String names : currency_codes){
            String[] val = save.getString(names, "null null").split(" ");
            if(val[0].equals("null") && val[1].equals("null")){
                continue;
            }
            Currency currency = new Currency(names);
            currency.setValueBY(val[0]);
            currency.setValuePL(val[1]);
            currencies.add(currency);
        }
        loadResult(currencies);
        Snackbar.make(getMainScreen(), "Previous data were restored", Snackbar.LENGTH_LONG).show();
    }

    private void store(ArrayList<Currency> currencies){
        SharedPreferences.Editor preference = getSharedPreferences(PREF_STORE, MODE_PRIVATE).edit();
        for(Currency currency : currencies){
            preference.putString(currency.getName(), currency.getValueBY() + " " + currency.getValuePL());
        }
        preference.apply();
    }

    private void bindInterface(){
        TextView[] titles = {(TextView) findViewById(R.id.names), (TextView) findViewById(R.id.nbrb), (TextView) findViewById(R.id.nbp)};
        for(TextView view : titles)
            setFont(view);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.update);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdate();
            }
        });

    }

    public void loadResult(ArrayList<Currency> currencies){

        for(Currency currency : currencies){
            Log.v("RESULT", currency.getName() + " " + currency.getValuePL() + " " + currency.getValueBY());
        }

        store(currencies);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.result_screen);
        CurrencyAdapter currencyAdapter = new CurrencyAdapter(currencies);
        recyclerView.setAdapter(currencyAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        hideProgressBar(true);
    }

    private void startUpdate(){

        hideProgressBar(false);

        LoadCurrency loadCurrency = new LoadCurrency(this, currency_codes);
        Thread s = new Thread(loadCurrency);
        s.start();

    }


    public RelativeLayout getMainScreen(){
        return (RelativeLayout) findViewById(R.id.main_screen);
    }

    public void setFont(TextView textView){
        AssetManager manager = this.getApplicationContext().getAssets();
        Typeface typeface = Typeface.createFromAsset(manager, "fonts/akrobat_semi_bold.otf");
        textView.setTypeface(typeface);
    }

    class CurrencyAdapter extends RecyclerView.Adapter<CurrencyHolder>{

        ArrayList<Currency> currencies = new ArrayList<>();

        CurrencyAdapter(ArrayList<Currency> list) {
            this.currencies = list;
        }

        @Override
        public CurrencyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_row,parent,false);
            return new CurrencyHolder(view, MainActivity.this);
        }

        @Override
        public void onBindViewHolder(CurrencyHolder holder, int position) {
            Currency currency = currencies.get(position);
            holder.UpdateUI(currency);
        }

        @Override
        public int getItemCount() {
            return currencies.size();
        }
    }

    private void hideProgressBar(Boolean hide){
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
    }



}

