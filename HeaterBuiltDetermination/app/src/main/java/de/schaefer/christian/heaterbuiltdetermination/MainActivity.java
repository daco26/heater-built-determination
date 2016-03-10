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
                    String y = serialNumber.substring(0, 2);
                    String m = serialNumber.substring(2, 4);
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
            case "Viessmann":
                serialNumber = serialNumber.replaceAll("\\s+", ""); //remove potential whitespaces
                if(serialNumber.matches("\\d{8,}")){
                    String number = serialNumber.substring(7,8);
                    year = "xxx"+number+" (das Jahrzehnt muss ggf. abgeschätzt werden)";
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Wolf":
                serialNumber = serialNumber.replaceAll("\\s+", ""); //remove potential whitespaces
                if(serialNumber.matches("\\d{4,}")){
                    String number = serialNumber.substring(0,1)+serialNumber.substring(3,4);
                    year = "xx"+number;
                }
                else{
                    year = "Keine gültige Seriennummer, im Moment werden nur Wandgeräte und keine Kessel unterstützt";
                }
                break;
            case "Brötje":
                if(serialNumber.matches("\\d{10}")){
                    //alt
                    String number = serialNumber.substring(0,2);
                    year = "xx"+number;
                }else if(serialNumber.matches("\\d{7}")){
                    String number = serialNumber.substring(3,4);
                    year = "xxx"+number+" (das Jahrzehnt muss ggf. abgeschätzt werden)";
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "DeDietrich":
                if(serialNumber.matches("\\d{2,}")){
                    String number = serialNumber.substring(0,2);
                    year = "xx"+number;
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Geminox":
                if(serialNumber.matches("\\d{4,}")){
                    String number = serialNumber.substring(2,4);
                    year = "xx"+number;
                }
                else{
                    year = "Keine gültige Seriennummer";
                }
                break;
            case "Junkers":
                if(serialNumber.matches("\\d{3}")){
                    year = checkYearForJunkers(serialNumber);
                }
                else{
                    year = "Keine gültige Seriennummer, älter als 1964 oder neuer als 2015";
                }
                break;
            default:
                year = "Muss erst noch programmiert werden :)";
                break;
        }

        return year;
    }

    /**
     * values copied from http://www.gosler.de/bjunkers.htm
     * @param threeDigits serialnumber
     * @return respective year
     */
    private String checkYearForJunkers(String threeDigits){
        String number = threeDigits.substring(0,2);

        String year="";

        if(number.equals("40")||number.equals("41")) year = "1964";
        if(number.equals("50")||number.equals("51")) year = "1965";
        if(number.equals("60")||number.equals("61")) year = "1966";
        if(number.equals("70")||number.equals("81")) year = "1967";
        if(number.equals("80")||number.equals("81")) year = "1968";
        if(number.equals("90")||number.equals("91")) year = "1969";
        if(number.equals("02")||number.equals("03")) year = "1970";
        if(number.equals("12")||number.equals("13")) year = "1971";
        if(number.equals("22")||number.equals("23")) year = "1972";
        if(number.equals("32")||number.equals("33")) year = "1973";
        if(number.equals("42")||number.equals("43")) year = "1974";
        if(number.equals("52")||number.equals("53")) year = "1975";
        if(number.equals("62")||number.equals("63")) year = "1976";

        if(number.equals("72")||number.equals("73")) year = "1977";
        if(number.equals("82")||number.equals("83")) year = "1978";
        if(number.equals("92")||number.equals("93")) year = "1979";
        if(number.equals("04")||number.equals("05")) year = "1980";
        if(number.equals("14")||number.equals("15")) year = "1981";
        if(number.equals("24")||number.equals("25")) year = "1982";
        if(number.equals("34")||number.equals("35")) year = "1983";
        if(number.equals("44")||number.equals("45")) year = "1984";
        if(number.equals("54")||number.equals("55")) year = "1985";
        if(number.equals("64")||number.equals("65")) year = "1986";
        if(number.equals("74")||number.equals("75")) year = "1987";
        if(number.equals("84")||number.equals("85")) year = "1988";
        if(number.equals("94")||number.equals("95")) year = "1989";

        if(number.equals("06")||number.equals("07")) year = "1990";
        if(number.equals("16")||number.equals("17")) year = "1991";
        if(number.equals("26")||number.equals("27")) year = "1992";
        if(number.equals("36")||number.equals("37")) year = "1993";
        if(number.equals("46")||number.equals("47")) year = "1994";
        if(number.equals("56")||number.equals("57")) year = "1995";
        if(number.equals("66")||number.equals("67")) year = "1996";
        if(number.equals("76")||number.equals("77")) year = "1997";
        if(number.equals("86")||number.equals("87")) year = "1998";
        if(number.equals("96")||number.equals("97")) year = "1999";
        if(number.equals("08")||number.equals("09")) year = "2000";
        if(number.equals("18")||number.equals("19")) year = "2001";
        if(number.equals("28")||number.equals("29")) year = "2002";

        if(number.equals("38")||number.equals("39")) year = "2003";
        if(number.equals("48")||number.equals("49")) year = "2004";
        if(number.equals("58")||number.equals("59")) year = "2005";
        if(number.equals("68")||number.equals("69")) year = "2006";
        if(number.equals("78")||number.equals("79")) year = "2007";
        if(number.equals("88")||number.equals("89")) year = "2008";
        if(number.equals("98")||number.equals("99")) year = "2009";

        if(number.equals("00")||number.equals("01")) year = "2010";
        if(number.equals("10")||number.equals("11")) year = "2011";
        if(number.equals("20")||number.equals("21")) year = "2012";
        if(number.equals("30")||number.equals("31")) year = "2013";

        if(threeDigits.equals("417")) year = "2014";
        if(threeDigits.equals("418")) year = "2014";
        if(threeDigits.equals("419")) year = "2014";
        if(threeDigits.equals("420")) year = "2014";
        if(threeDigits.equals("453")) year = "2014";
        if(threeDigits.equals("454")) year = "2014";
        if(threeDigits.equals("455")) year = "2014";
        if(threeDigits.equals("456")) year = "2014";
        if(threeDigits.equals("457")) year = "2014";
        if(threeDigits.equals("458")) year = "2014";
        if(threeDigits.equals("459")) year = "2014";
        if(threeDigits.equals("460")) year = "2014";

        if(threeDigits.equals("517")) year = "2015";
        if(threeDigits.equals("518")) year = "2015";
        if(threeDigits.equals("519")) year = "2015";
        if(threeDigits.equals("520")) year = "2015";
        if(threeDigits.equals("553")) year = "2015";
        if(threeDigits.equals("554")) year = "2015";
        if(threeDigits.equals("555")) year = "2015";
        if(threeDigits.equals("556")) year = "2015";
        if(threeDigits.equals("557")) year = "2015";
        if(threeDigits.equals("558")) year = "2015";
        if(threeDigits.equals("559")) year = "2015";
        if(threeDigits.equals("560")) year = "2015";

        return year;
    }
}
