package com.onthewifi.casacalarota.MemoApp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.onthewifi.casacalarota.MemoApp.MainActivity.*;

public class DisplayMessageActivity extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner dropdown;
    Button submit;
    //public static String[] names;
    public static List<String> list_names;
    public final static String no_file = "(Select a file)";
    ArrayAdapter adapter;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        textView = (TextView) findViewById(R.id.textView);
        dropdown = (Spinner) findViewById(R.id.spinner1);

        Bundle b = getIntent().getExtras();
        String[] names = null;
        if (b != null) {
            names = b.getStringArray("names");
        }

        //test
        int i = 0;
        while (names[i] != null) {
            //System.out.println(names[i]);
            i++;
        }
        list_names = new LinkedList<>();
        list_names.add(no_file);
        for (int j = 0; j < i; j++) {
            list_names.add(names[j]);
        }
        Collections.sort(list_names,String.CASE_INSENSITIVE_ORDER);


        //TODO try get dropdownview Oncreate Activity

        //sempre vera
        //if (names != null){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list_names);
        //adapter = ArrayAdapter.createFromResource(this,,android.R.layout.simple_spinner_dropdown_item);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        //dropdown.setPressed(true);
           /* dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                   @Override
                                                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                       //System.out.println(parent.getItemAtPosition(position)+ " selecred");

                                                   }

                                                   @Override
                                                   public void onNothingSelected(AdapterView<?> parent) {

                                                   }
                                               });*/
        //addListenerOnButton(dropdown.getSelectedView());
        //addListenerOnSpinnerSelection();
        //}
    }

    @Override
    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        ReadFile();
    }

    @Override
    public void onNothingSelected(AdapterView parent) {

    }

    public void show_alert(View view){
        dropdown = (Spinner) findViewById(R.id.spinner1);
        String name_selected = String.valueOf(dropdown.getSelectedItem());
        if (name_selected == no_file){
            Delete_file();
        }
        else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage(getResources().getString(R.string.delete_alertdialog)+ name_selected +"?")
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            doOnTrueResult();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle(getResources().getString(R.string.careful_message))
                    .setIcon(R.mipmap.ic_launcher)
                    .create();
            myAlert.show();}
    }

    private void doOnTrueResult() {
        Delete_file();
    }

    public void Delete_file() {
        dropdown = (Spinner) findViewById(R.id.spinner1);
        //ListView listView = (ListView) dropdown.getAdapter();
        String name_selected = String.valueOf(dropdown.getSelectedItem());
        if (name_selected != no_file) {
            String file_name = name_selected.concat(user_file);
            File f = new File(getFilesDir(), file_name);
            f.delete();
            // ArrayAdapter adapter = (ArrayAdapter) dropdown.getAdapter();
            //
            int pos = dropdown.getSelectedItemPosition();
            list_names.remove(list_names.indexOf(name_selected));
            //if (pos>0)
               // dropdown.setSelection(pos-pos);
                dropdown.setSelection(0);
            adapter.notifyDataSetChanged();
            // adapter = (ListAdapter) dropdown.getAdapter();
            // adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1 , list_names);

        /*int pos = 0;
        Object t = adapter.getItem(pos);
        adapter.remove((String) t);*/
            Toast.makeText(getApplicationContext(), name_selected + " " + getResources().getString(R.string.deleted) + "\n", Toast.LENGTH_LONG).show();
            ReadFile();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_none) + "\n", Toast.LENGTH_LONG).show();
        }
    }


    public void edit_file(View view){
        dropdown = (Spinner) findViewById(R.id.spinner1);
        String file_name = String.valueOf(dropdown.getSelectedItem());
        if (file_name != no_file){
            String message = get_string_message();
            Intent intent = new Intent();
            intent.putExtra("file_name", file_name);
            intent.putExtra("message", message);
            setResult(RESULT_OK,intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_none), Toast.LENGTH_LONG).show();
        }
    }

    private String get_string_message(/*View view*/) {
        dropdown = (Spinner) findViewById(R.id.spinner1);
        StringBuffer stringBuffer = new StringBuffer();
            try {
                String Message;
                String file_name = String.valueOf(dropdown.getSelectedItem()).concat(user_file);
                FileInputStream fileInputStream = openFileInput(/*file_selectednames[0]*/file_name);
                InputStreamReader inputStreamReader = new InputStreamReader((fileInputStream));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                //String str[] = file_name.split(user_file);
               // file_name = str[0];
                //stringBuffer.append(file_name + "\n");
                //Toast.makeText(getApplicationContext(), getResources().getString(R.string.last_edit) + bufferedReader.readLine(), Toast.LENGTH_LONG).show();
                while ((Message = bufferedReader.readLine()) != null) {
                    stringBuffer.append(Message + "\n");
                }
            } catch (FileNotFoundException e) {
                //System.out.println(getResources().getString(R.string.error_message));
                //return getResources().getString(R.string.error_message);
                stringBuffer.append(getResources().getString(R.string.error_message) + "\n");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return stringBuffer.toString();
    }

    public void ReadFile(/*View view*/) {
        dropdown = (Spinner) findViewById(R.id.spinner1);
        if (String.valueOf(dropdown.getSelectedItem()) != no_file) {
            try {
                String Message;
                String file_name = String.valueOf(dropdown.getSelectedItem()).concat(user_file);
                FileInputStream fileInputStream = openFileInput(/*file_selectednames[0]*/file_name);
                InputStreamReader inputStreamReader = new InputStreamReader((fileInputStream));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                String str[] = file_name.split(user_file);
                file_name = str[0];
                //stringBuffer.append(file_name + "\n");
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.last_edit) + bufferedReader.readLine(), Toast.LENGTH_LONG).show();
                while ((Message = bufferedReader.readLine()) != null) {
                    stringBuffer.append(Message + "\n");
                }
                if (stringBuffer.length() > 0) {
                    textView.setMovementMethod(new ScrollingMovementMethod());
                    textView.setText(stringBuffer.toString());
                    textView.setVisibility(View.VISIBLE);
                }
            } catch (FileNotFoundException e) {
                //System.out.println(getResources().getString(R.string.error_message));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            textView.setText("");
            textView.setVisibility(View.VISIBLE);
        }
    }
}