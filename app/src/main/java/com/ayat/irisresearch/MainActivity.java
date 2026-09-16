package com.ayat.irisresearch;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.content.Intent;

public class MainActivity extends Activity {
    private EditText nameInput, dateInput, timeInput;
    private AutoCompleteTextView cityInput;
    private TextView resultText;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameInput=findViewById(R.id.nameInput); dateInput=findViewById(R.id.dateInput); timeInput=findViewById(R.id.timeInput);
        cityInput=findViewById(R.id.cityInput); resultText=findViewById(R.id.resultText);
        ArrayAdapter<CityDatabase.City> adapter=new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,CityDatabase.all());
        cityInput.setAdapter(adapter);
        findViewById(R.id.analyzeButton).setOnClickListener(v -> analyze());
        nameInput.requestFocus();
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(nameInput,InputMethodManager.SHOW_IMPLICIT);
    }
    private void analyze() {
        try {
            String name=nameInput.getText().toString().trim(), jalali=dateInput.getText().toString().trim(), time=timeInput.getText().toString().trim();
            CityDatabase.City city=CityDatabase.find(cityInput.getText().toString().trim());
            if(name.isEmpty()||jalali.isEmpty()||time.isEmpty()||city==null) throw new IllegalArgumentException("نام، تاریخ، ساعت و شهر را وارد کنید.");
            String greg=JalaliDateConverter.toGregorian(jalali);
            int lifePath=NumerologyEngine.lifePath(greg);
            Intent intent=new Intent(this,ChartActivity.class);
            intent.putExtra("date",greg); intent.putExtra("time",time.length()==5?time+":00":time);
            startActivity(intent);
            resultText.setText("اطلاعات تولد\n\nنام: "+name+"\nتاریخ شمسی: "+jalali+"\nتاریخ میلادی: "+greg+"\nساعت: "+time+"\nشهر: "+city.name+"\nعرض جغرافیایی: "+city.latitude+"\nطول جغرافیایی: "+city.longitude+"\nمنطقه زمانی: "+city.timezone+"\n\nعدد مسیر زندگی: "+lifePath);
        } catch(Exception e) { resultText.setText("خطا: "+e.getMessage()); }
    }
}
