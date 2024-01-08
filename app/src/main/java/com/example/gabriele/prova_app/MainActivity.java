package com.example.gabriele.prova_app;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    EditText editText;
    EditText filename;
    TextView textView;
    Boolean backpressed;
    Boolean go_load;
    //private int overwrite; //0 nothing, 1 true, -1 false
    public static final String user_file = ".txt_user";
    public static final String default_name = "default_name";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_message);
        filename = (EditText) findViewById(R.id.file_name);
        backpressed = false;
        go_load = false;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    protected String get_real_file_name(String name){
        String new_name = name;
        File f = new File(getFilesDir(),name+user_file);
        if (!f.exists()) {
            //provaSystem.out.println(name+" esiste");
            return name.concat(user_file);
        }
        else {
            int i = 1;
            new_name = new_name.concat("(" + Integer.toString(i) + ")");
            f = new File (getFilesDir(),new_name + user_file);
            //System.out.println(new_name);
            while (f.exists()) {
                int i_length =(int) Math.log10(i) + 3;
                new_name = new_name.substring(0,new_name.length()-i_length);
                //System.out.println("after splititing " + new_name + Integer.toString(i));
                i = i+1;
                new_name = new_name.concat("(" + Integer.toString(i) + ")");
                //System.out.println("final " + new_name + " " + Integer.toString(i));
                f = new File (getFilesDir(),new_name + user_file);
            }
            return new_name.concat(user_file);
        }
    }

    //TODO names available(starting with letter)
    //TODO (SAVE in GoogleDrive)
    public void SaveFile(View view) {
        String file_name = filename.getText().toString();
        String Message = editText.getText().toString();
        if ((!file_name.isEmpty()) && (!Message.isEmpty())) {

            if (file_name_exists(file_name)) {
                showAlert(file_name);
            } else {
                save_this_file(file_name+user_file);
            }
        }else {
            if (Message.isEmpty()){
                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage(getResources().getString(R.string.empty_message))
                    .setPositiveButton(getResources().getString(R.string.button_edit), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //doOnTrueResult();
                        }
                    })
                    .setTitle(getResources().getString(R.string.careful_message))
                    .setIcon(R.mipmap.ic_launcher)
                    .create();
            myAlert.show();}
            else{
                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                myAlert.setMessage(getResources().getString(R.string.no_file_name))
                        .setPositiveButton(getResources().getString(R.string.button_edit), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //doOnTrueResult();
                            }
                        })
                        .setTitle(getResources().getString(R.string.careful_message))
                        .setIcon(R.mipmap.ic_launcher)
                        .create();
                myAlert.show();}
        }
    }

    private void save_this_file(String file_name) {
        String Message = editText.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ITALIAN);
        Date date = new Date();
        Message = dateFormat.format(date).toString().concat("\n" + Message);
        File f = new File (getFilesDir(),file_name);
        Boolean overwrite = f.exists();
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
            fileOutputStream.write(Message.getBytes());
            fileOutputStream.close();
            String[] tmp = file_name.split(user_file);
            if (overwrite)
                Toast.makeText(getApplicationContext(), tmp[0] + ": " + getResources().getString(R.string.overwrite_message), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), tmp[0] + ": " + getResources().getString(R.string.saved_message), Toast.LENGTH_LONG).show();
            editText.setText("");
            filename.setText("");
            if (backpressed){
                onBackPressed();
            }
            else{
                if (go_load){
                    ReadFile(findViewById (R.id.load_file));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   private void showAlert(String name) {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        String default_name = get_real_file_name(name);
        default_name = default_name.substring(0,default_name.length()-user_file.length());
        myAlert.setMessage(name + getResources().getString(R.string.save_alertdialog))
                .setPositiveButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //doOnTrueResult();
                    }
                })
                .setNeutralButton(getResources().getString(R.string.default_save)+" "+default_name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        doOnTrueResult();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        doOnFalseResult();
                    }
                })
                .setTitle(getResources().getString(R.string.careful_message))
                .setIcon(R.mipmap.ic_launcher)
                .create();
        myAlert.show();
    }

    private void doOnFalseResult() {
        save_this_file(filename.getText().toString()+user_file);
    }

    private void doOnTrueResult() {
        String file_name = get_real_file_name(filename.getText().toString());
        save_this_file(file_name);
    }

    private boolean file_name_exists(String name) {
        String new_name = name;
        File f = new File(getFilesDir(),name+user_file);
        return f.exists();
    }

    public boolean file_available() {

        //System.out.println("tutto bene fin qui");
        File f = new File(String.valueOf(getFilesDir()));
        File file_list[] = f.listFiles();
        int how_many_file = file_list.length;
        String[] names = new String[how_many_file];
        for (int i = 1; i < (how_many_file); i++) {

            //System.out.println("tutto bene fin qui");
            if (file_list[i].getName().contains(user_file)) {
                //System.out.println("tutto bene fin qui"+file_list[i].getName());
                String tmp[] = file_list[i].getName().split(user_file);
                //System.out.println("trovata"+tmp[0]);
                if (tmp.length <= 1) {
                    //System.out.println("trovata"+tmp[0]);
                    return true;
                }

            }
        }
        //System.out.println("non trovata");
        return false;
    }

    public void get_string_names(int how_many_file, File file_list[], String names[]) {
        int c = 0;
        for (int i = 1; i < (how_many_file); i++) {
            if (file_list[i].getName().contains(user_file)) {
                String tmp[] = file_list[i].getName().split(user_file);
                if (tmp.length <= 1) {
                    names[c] = tmp[0];
                    c++;
                }
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                Bundle b = data.getExtras();
                String file_name = b.getString("file_name");
                String message = b.getString("message");
                filename.setText(file_name);
                String[] tmp = message.split("\r\n|\r|\n",2);
                String last_edit = tmp[0];
                message = tmp[1];
                editText.setText(message);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.last_edit) + last_edit, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void ReadFile(View view) {
        String Message = editText.getText().toString();
        backpressed = false;
        go_load = true;
        if (!Message.isEmpty()) {
            showAlert_messageUnsaved();
        } else {
            if (file_available()) {
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                File f = new File(String.valueOf(getFilesDir()));
                File file_list[] = f.listFiles();
                String[] names = new String[file_list.length];
                get_string_names(file_list.length, file_list, names);
                Bundle b = new Bundle();
                b.putStringArray("names", names);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
                //finish();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_dir), Toast.LENGTH_LONG).show();
            }
        }
        go_load = false;
    }

    private void showAlert_messageUnsaved() {
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(getResources().getString(R.string.messageunsaved_alertdialog))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goOnLoad();
                    }
                })
                .setNeutralButton(getResources().getString(R.string.button_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        goOnSave();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //doOnFalseResult();
                    }
                })
                .setTitle(getResources().getString(R.string.careful_message))
                .setIcon(R.mipmap.ic_launcher)
                .create();
        myAlert.show();
    }

    private void goOnSave() {
        String name = filename.getText().toString();
        if (name.isEmpty()){
            save_this_file(get_real_file_name(default_name));
        }
        else{
            SaveFile(findViewById(R.id.save_file));
        }
    }

    public void goOnLoad() {
        textView = (EditText) findViewById(R.id.edit_message);
        filename = (EditText) findViewById(R.id.file_name);
        filename.setText("");
        textView.setText("");
        if (backpressed){
            finish();
        }
        else
            ReadFile(findViewById(R.id.load_file));
    }

    @Override
    public void onBackPressed() {
        String Message = editText.getText().toString();
        String Name = filename.getText().toString();
        backpressed = true;
        if ((!Message.isEmpty())||(!Name.isEmpty())) {
            showAlert_messageUnsaved();
        }
        else{
            if (go_load)
                ReadFile(findViewById(R.id.load_file));
            else
                finish();
        }
       //super.onBackPressed();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   /* public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // do: Define a title for the content shown.
                // do: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }*/
};
