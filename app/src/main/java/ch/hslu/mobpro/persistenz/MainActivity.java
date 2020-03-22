package ch.hslu.mobpro.persistenz;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    TextView section1 = null;
    private final String ONRESUME_1 = "MainActivity.onResume() wurde seit der Installation dieser App ";
    private final String ONRESUME_2 = " mal aufgerufen.";
    private final String COUNTER_KEY = "RESUMECOUNTER";
    private final String FILENAME ="myTextFile.txt";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        section1 = (TextView) findViewById(R.id.text_section1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final int newResumeCount = preferences.getInt(COUNTER_KEY, 0) + 1;
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(COUNTER_KEY, newResumeCount);
        editor.apply();
        section1.setText(ONRESUME_1 + newResumeCount + ONRESUME_2);
    }

    public void startAppPreferenceActivity(View button){
        Intent view = new Intent(this,AppPreferenceActivity.class);
        startActivity(view);
    }

    private void getDataFromPrefferenceActivity(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String sweetenerType = preferences.getString("teaSweetener", "natural");
        Boolean sweetener = preferences.getBoolean("teaWithSugar", true);
        String preferredTea = preferences.getString("teaPreferred", "Lipton/Pfefferminztee");

        String fertigerString = "Ich trinke am liebsten "+ preferredTea ;
        if (sweetener){
            int resID = getResources().getIdentifier(sweetenerType, "string", getPackageName());
            String stringSweetenerType = getResources().getString(resID);
            fertigerString = fertigerString + " mit "+ stringSweetenerType + " gesüsst";

        }

        TextView textView6 = (TextView) findViewById(R.id.textView6);
        textView6.setText(fertigerString);

    }

    public void setDefaultPreference(View buttone){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("teaSweetener", "natural");
        editor.putBoolean("teaWithSugar", true);
        editor.putString("teaPreferred", "Lipton/Pfefferminztee");
        editor.commit();

        getDataFromPrefferenceActivity();
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean loadExternalFileWithPermissions(String text) {
        int grant = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (grant != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 24);
            return false;
        } else {
            return writeToFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 24:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission " + permissions[0] + " denied", Toast.LENGTH_SHORT).show();
                } else {
                    loadFile();
                }
                break;

        }
    }

    public void writeString(View v) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (!checkBox.isChecked()) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = openFileOutput(FILENAME, MODE_PRIVATE);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(text);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                System.out.println("Written to local storage: " + text);
                Toast.makeText(this,"Written to local storage",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
//                fileOutputStream.close();
            }
        } else {
            loadExternalFileWithPermissions(text);
        }
    }

    public boolean writeToFile() {
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        System.out.println("writeToFile() mit Text "+text);

        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/performance");
            dir.mkdir();
            File file = new File(dir, FILENAME);
            FileOutputStream os = new FileOutputStream(file);
            String data = text;
            os.write(data.getBytes());
            os.close();
            Toast.makeText(this,"Written to sdcard storage",Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }finally {
        }
    }

    public void loadFile() {
        EditText editText = (EditText) findViewById(R.id.editText);
        String text = editText.getText().toString();
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/performance");
            dir.mkdir();
            File file = new File(dir, FILENAME);
            FileOutputStream os = new FileOutputStream(file);
            String data = text;
            os.write(data.getBytes());
            os.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void readString(View button) {
        EditText editText = (EditText) findViewById(R.id.editText);
        CheckBox chkbx = (CheckBox) findViewById(R.id.checkBox);

        if (!chkbx.isChecked()) { // lokal lesen
            ArrayList<String> lines = new ArrayList<>();
            try {
                FileInputStream fin = openFileInput(FILENAME);
                if (fin != null) {
                    InputStreamReader inputStreamReader = new InputStreamReader(fin);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while (( line = bufferedReader.readLine() ) != null) {
                        lines.add(line);
                    }
                    fin.close();
                }
                if (lines.size()>0){
                    editText.setText(lines.get(0));
                } else {
                    System.out.println("NOTHING TO READ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else { // extern lesen
            int grant = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (grant == PackageManager.PERMISSION_GRANTED) {
                try {


                    File sdcard = Environment.getExternalStorageDirectory();
                    File dir = new File(sdcard.getAbsolutePath()+"/performance/");
                    dir.mkdir();
                    File file = new File(dir, FILENAME);
                    FileInputStream fileIn = new FileInputStream(file);
                    InputStreamReader InputRead = new InputStreamReader(fileIn);

                    char[] inputBuffer = new char[100];
                    String s = "";
                    int charRead;

                    while ((charRead = InputRead.read(inputBuffer)) > 0) {
                        String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                        s += readstring;
                    }
                    InputRead.close();

                    editText.setText(s);
                    System.out.println("Read from sdcard storage: " + s);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 24);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ch.hslu.mobpro.persistenz/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
        getDataFromPrefferenceActivity();

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://ch.hslu.mobpro.persistenz/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
