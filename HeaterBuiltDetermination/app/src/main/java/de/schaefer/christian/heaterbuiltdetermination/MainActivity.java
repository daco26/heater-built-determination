package de.schaefer.christian.heaterbuiltdetermination;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private TextView tvInputSerial;
    private TextView tvYear, tvSerial, tvManufacturer;

    private Button btnShow;
    private String manufacturer;

    private LinearLayout result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.manufacturer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        tvInputSerial = (TextView) findViewById(R.id.inputSerial);

        tvManufacturer = (TextView) findViewById(R.id.result_hersteller);
        tvSerial = (TextView) findViewById(R.id.result_seriennummer);
        tvYear = (TextView) findViewById(R.id.result_baujahr);

        btnShow = (Button) findViewById(R.id.button);

        result = (LinearLayout) findViewById(R.id.result);
        result.setVisibility(View.GONE);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(manufacturer, tvInputSerial.getText().toString());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(!parent.getItemAtPosition(pos).toString().equals("Bitte Hersteller auswählen")){
            manufacturer = parent.getItemAtPosition(pos).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setResult(String manufacturer, String serialNumber){

        String year = findOutYear(manufacturer, serialNumber);

        tvManufacturer.setText(manufacturer);
        tvSerial.setText(serialNumber);
        tvYear.setText(year);

        result.setVisibility(View.VISIBLE);
    }

    private String findOutYear(String manufacturer, String serialNumber){

        String year = "";

        if(manufacturer==null) return "";

        switch(manufacturer){
            case "Alphainotec":
                year = "fehlt";
                break;
            case "Buderus":
                if(serialNumber.matches("\\d+-\\d{2}-\\d{4}-\\d+")){
                    String[] parts = serialNumber.split("-");
                    String number = String.valueOf(parts[2].charAt(0));
                    year = "xxx"+number+" (das Jahrzehnt muss ggf. abgeschätzt werden)";
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "MAN/MHG":
                if(serialNumber.matches("\\d{4}.{11}")){
                    String y = serialNumber.substring(0,2);
                    String m = serialNumber.substring(2,4);
                    int yInt = Integer.valueOf(serialNumber.substring(0,2));
                    if(yInt>=0 && yInt<=16){
                        year = "20"+y+"/"+m;
                    }else if(yInt>16){
                        year = "19"+y+"/"+m;
                    }
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Schäfer":
                if(serialNumber.matches("\\d{7}-\\d{2}-\\d{6}")){
                    String y = serialNumber.substring(0,2);
                    int yInt = Integer.valueOf(serialNumber.substring(0, 2));
                    if(yInt>=0 && yInt<=16){
                        year = "20"+y;
                    }else if(yInt>16){
                        year = "19"+y;
                    }
                } else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Sieger":
                if(serialNumber.matches("\\d{9}-\\d{2}-\\d{4}-\\d{3}")){
                    String[] parts = serialNumber.split("-");
                    String number = String.valueOf(parts[2].charAt(0));
                    year = "xxx"+number+" (das Jahrzehnt muss ggf. abgeschätzt werden)";
                } else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Stiebel-Eltron":
                if(serialNumber.matches("\\d{6}-\\d{4}")){
                    String[] parts = serialNumber.split("-");
                    int number = Integer.valueOf(parts[1].substring(0, 2));
                    int yearTwoDigits = number+25;
                    if(yearTwoDigits>=100){
                        String lastTwoDigits = String.valueOf(yearTwoDigits);
                        year = "20"+lastTwoDigits.substring(lastTwoDigits.length()-2, lastTwoDigits.length());
                    }else{
                        year = "19"+yearTwoDigits;
                    }
                } else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Vaillant":
                if(serialNumber.matches("\\d{2} \\d{4}.*")){
                    // with space
                    String numberYear = serialNumber.substring(3, 5);
                    String numberWeek = serialNumber.substring(5, 7);
                    year = "xx"+numberYear+", Fertigungswoche "+numberWeek;
                }
                else if(serialNumber.matches("\\d{2}\\d{4}.*")){
                    // without space
                    String numberYear = serialNumber.substring(2, 4);
                    String numberWeek = serialNumber.substring(4, 6);
                    year = "xx"+numberYear+", Fertigungswoche "+numberWeek;
                }
                else if(serialNumber.matches("\\d{2}-.*")){
                int number = Integer.valueOf(serialNumber.substring(0,2));
                    year = "xx"+number;
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            default:
                year = "Muss erst noch programmiert werden :)";
                break;
        }

        return year;
    }
}
