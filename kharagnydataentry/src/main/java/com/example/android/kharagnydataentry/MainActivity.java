package com.example.android.kharagnydataentry;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DatabaseReference storesRef = FirebaseDatabase.getInstance().getReference().child("stores");
        final DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference().child("categories");
        final DatabaseReference priceGroupRef = FirebaseDatabase.getInstance().getReference().child("price_group");
        final DatabaseReference areaRef = FirebaseDatabase.getInstance().getReference().child("area");

        final EditText nameEdtTxt = (EditText)findViewById(R.id.editText);
        final EditText addressEdtTxt = (EditText)findViewById(R.id.editText2);
        final AutoCompleteTextView areaAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        final EditText emailEdtTxt = (EditText)findViewById(R.id.editText3);
        final EditText phone1EdtTxt = (EditText)findViewById(R.id.editText4);
        final EditText phone2EdtTxt = (EditText)findViewById(R.id.editText5);
        final EditText phone3EdtTxt = (EditText)findViewById(R.id.editText6);
        final EditText opensEdtTxt = (EditText)findViewById(R.id.timePickerTraditional);
        final EditText closesEdtTxt = (EditText)findViewById(R.id.timePicker2Traditional);
        final RadioGroup priceRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        final EditText descriptionEdtTxt = (EditText)findViewById(R.id.editText9);
        Button eshtaButt = (Button)findViewById(R.id.button);

        //categories checkboxes
        final CheckBox[] checkBoxes = new CheckBox[]{
                (CheckBox)findViewById(R.id.sports_chk_bx),
                (CheckBox)findViewById(R.id.food_chk_bx),
                (CheckBox)findViewById(R.id.hot_deals_chk_bx),
                (CheckBox)findViewById(R.id.featured_places_chk_bx),
                (CheckBox)findViewById(R.id.newly_added_chk_bx),
                (CheckBox)findViewById(R.id.chill_chk_bx)
        };

        ArrayAdapter<String> areaArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        areaArrayAdapter.add("zamalek");
        areaArrayAdapter.add("5th settlement");
        areaArrayAdapter.add("downtown");
        areaAutoCompleteTextView.setAdapter(areaArrayAdapter);
        areaAutoCompleteTextView.setThreshold(2);


        eshtaButt.setOnClickListener(new View.OnClickListener()
        {
            @Override
                public void onClick(View view)
                {
                    String name, address, area, email, phone1, phone2, phone3, openTime, closeTime, priceGroup = "", description, categories = "";
                    try
                    {
                        name = nameEdtTxt.getText().toString();
                        address = addressEdtTxt.getText().toString();
                        email = emailEdtTxt.getText().toString();
                        phone1 = phone1EdtTxt.getText().toString();
                        phone2 = (phone2EdtTxt.getText() == null) ? "" : phone2EdtTxt.getText().toString();
                        phone3 = (phone3EdtTxt.getText() == null) ? "" : phone3EdtTxt.getText().toString();
                        description = descriptionEdtTxt.getText().toString();
                        area = areaAutoCompleteTextView.getText().toString();
                        openTime = opensEdtTxt.getText().toString();
                        closeTime = closesEdtTxt.getText().toString();

                        for (CheckBox checkBox : checkBoxes)
                            if (checkBox.isChecked())
                                categories += checkBox.getText() + "|";

                    switch (priceRadioGroup.getCheckedRadioButtonId())
                    {
                        case R.id.s_rdo_butt:
                            priceGroup = "1";
                            break;
                        case R.id.ss_rdo_butt:
                            priceGroup = "2";
                            break;
                        case R.id.sss_rdo_butt:
                            priceGroup = "3";
                            break;
                        default:
                            break;
                    }
                }
                catch (NullPointerException nullE)
                {
                    popAnAlertDialogUp("Error", "You probably didn't fill all of the fields");
                    return;
                }

                HashMap<String, Object> store = new HashMap<>();
                store.put("name", name);
                store.put("address", address);
                store.put("area", area);
                store.put("email", email);
                store.put("phone1", phone1);
                store.put("phone2", phone2);
                store.put("phone3", phone3);
                store.put("openTime", openTime);
                store.put("closeTime", closeTime);
                store.put("priceGroup", priceGroup);
                store.put("description", description);
                store.put("categories", categories);

                //final copies from the variables to be used inside of the callbacks
                final String finalPhone1 = phone1, finalPriceGroup = priceGroup, finalArea = area;

                //phone1 is the primary key
                HashMap<String, Object> tempMap = new HashMap<>();
                tempMap.put(phone1, store);
                //update stores
                storesRef.updateChildren(tempMap, new DatabaseReference.CompletionListener()
                {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                    {
                        //update prices
                        if (databaseError == null)
                        {
                            DatabaseReference priceRef = priceGroupRef.child(finalPriceGroup);
                            HashMap<String,Object> priceHashMap = new HashMap<>();
                            priceHashMap.put(finalPhone1, ".");
                            priceRef.updateChildren(priceHashMap, new DatabaseReference.CompletionListener()
                            {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                                {
                                    //update areas
                                    if (databaseError == null)
                                    {
                                        DatabaseReference areaRefChild = areaRef.child(finalArea);
                                        HashMap<String,Object> areaMap = new HashMap<>();
                                        areaMap.put(finalPhone1, ".");
                                        areaRefChild.updateChildren(areaMap, new DatabaseReference.CompletionListener()
                                        {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                                            {
                                                //update categories
                                                if (databaseError == null)
                                                {
                                                    for (CheckBox checkBox : checkBoxes)
                                                    {
                                                        if (!checkBox.isChecked())
                                                            continue;
                                                        DatabaseReference categoryRefChild = categoriesRef.child(checkBox.getText().toString());
                                                        HashMap<String,Object> categoriesMap = new HashMap<>();
                                                        categoriesMap.put(finalPhone1, ".");
                                                        categoryRefChild.updateChildren(categoriesMap, new DatabaseReference.CompletionListener()
                                                        {
                                                            @Override
                                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference)
                                                            {
                                                                if (databaseError == null)
                                                                    popAnAlertDialogUp("Hey!", "Done");
                                                                else
                                                                    popAnAlertDialogUp("Error", databaseError.toString());
                                                            }
                                                        });
                                                    }
                                                }
                                                else
                                                    popAnAlertDialogUp("Error", databaseError.toString());
                                            }
                                        });
                                    }
                                    else
                                        popAnAlertDialogUp("Error", databaseError.toString());
                                }
                            });
                        }
                        else //if failed
                            popAnAlertDialogUp("Error", databaseError.toString());
                    }
                });
            }
        });
    }
/*

 */
    void popAnAlertDialogUp(Object title, Object msg)
    {
        if (title == null)
            title = "";
        if (msg == null)
            msg = "";

        new AlertDialog.Builder(this)
                .setTitle(title.toString())
                .setMessage(msg.toString())
                .show();
    }
}
