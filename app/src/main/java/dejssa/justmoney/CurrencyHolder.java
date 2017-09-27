package dejssa.justmoney;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CurrencyHolder extends RecyclerView.ViewHolder{

    private TextView Name;
    private TextView BY;
    private TextView PL;

    public CurrencyHolder(View itemView, MainActivity activity) {
        super(itemView);

        Name =  itemView.findViewById(R.id.names);
        BY = itemView.findViewById(R.id.nbrb);
        PL = itemView.findViewById(R.id.nbp);

        activity.setFont(Name);
        activity.setFont(BY);
        activity.setFont(PL);
    }

    public void UpdateUI(Currency currency){
        Name.setText(currency.getName());
        BY.setText(currency.getValueBY());
        PL.setText(currency.getValuePL());
    }
}
