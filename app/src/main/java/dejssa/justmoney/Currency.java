package dejssa.justmoney;

/**
 * Created by Алексей on 11.09.2017.
 */

public class Currency {
    private String name;
    private String valueBY;
    private String valuePL;

    public Currency(String name) {
        this.name = name;
    }

    public void setValueBY(String valueBY) {
        this.valueBY = valueBY;
    }

    public void setValuePL(String valuePL) {
        this.valuePL = valuePL;
    }

    public String getName() {
        return name;
    }

    public String getValueBY() {
        return valueBY;
    }

    public String getValuePL() {
        return valuePL;
    }
}
