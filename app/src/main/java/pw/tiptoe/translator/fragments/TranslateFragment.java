package pw.tiptoe.translator.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import pw.tiptoe.translator.R;
import pw.tiptoe.translator.database.HistoryContract.HistoryEntry;


public class TranslateFragment extends Fragment {
    private static final String API_KEY = "trnsl.1.1.20170320T232028Z.e0db359129615fb2.c2f2c551b6257b6f80fdb824a9c9a6fb32075f53";
    private static final String YA_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
    private static final String COPYRIGHT_COMMENT = "Переведено сервисом «Яндекс.Переводчик» http://translate.yandex.ru/";

    private final int TRIGGER_SERACH = 1;

    private final long SEARCH_TRIGGER_DELAY_IN_MS = 1000;


    private EditText translateTextInput;
    private Button buttonTranslate;
    private TextView translatedText;
    private Button buttonDeleteInputText;
    private Spinner spinnerLangFrom;
    private Spinner spinnerLangTo;
    private String stringLangFrom = "ru";
    private String stringLangTo = "en";
    private ImageButton buttonChangeLang;

    private ToggleButton bookmarkToggleButton;

    private String chooseBookmark = "0";

    static final String[] LANG_SHORT_ARRAY = {"en","ar","el","it","es","zh","ko","de","no","fa",
            "pl","pt","uk","ru","fr","sv","ja"};

    static final String[] LANG_SHORT_ARRAY_FULL = {"english","arabian","hellenic","italian","spanish",
            "chinese","korean","german","norwegian","persian",
            "polish","portuguese","ukrainian","russian","french",
            "swedish","japanese"};

    static final int START_LANG_FROM = 13;
    static final int START_LANG_TO = 0;





    public TranslateFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        translateTextInput = (EditText) view.findViewById(R.id.translateTextInput);
        buttonTranslate = (Button) view.findViewById(R.id.buttonTranslate);
        translatedText = (TextView) view.findViewById(R.id.translatedText);

        buttonChangeLang = (ImageButton) view.findViewById(R.id.buttonChangeLang);

        bookmarkToggleButton= (ToggleButton) view.findViewById(R.id.toggle_bookmark);


        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        buttonTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable textObjectFromEdit = translateTextInput.getText();
                String newString = textObjectFromEdit.toString();
                if(!newString.equals("")) {
                    BGTask task = new BGTask();
                    task.execute(String.valueOf(translateTextInput.getText()));
                }
            }
        });





        buttonChangeLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = spinnerLangFrom.getSelectedItemPosition();
                spinnerLangFrom.setSelection(spinnerLangTo.getSelectedItemPosition());
                spinnerLangTo.setSelection(current);
            }
        });


        bookmarkToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookmarkToggleButton.isChecked()){
                    chooseBookmark = "1";
                }else{
                    chooseBookmark = "0";
                }
            }
        });

        setupLanguageFromSpiner(view);
        setupLanguageToSpiner(view);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        translateTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                /* Перед отправкой текста на перевод нужно проверить не пустой ли он. */

                String textInput = translateTextInput.getText().toString();
                if(!textInput.trim().isEmpty()) {
                    handler.removeMessages(TRIGGER_SERACH);
                    handler.sendEmptyMessageDelayed(TRIGGER_SERACH, SEARCH_TRIGGER_DELAY_IN_MS);
                }
            }


        });
    }






    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == TRIGGER_SERACH) {
                BGTask task = new BGTask();
                task.execute(String.valueOf(translateTextInput.getText()));
            }
        }
    };


    private class BGTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... translatingTextArray) {
            String output = null;
            for (String text : translatingTextArray) {
                try {
                    output = getOutputFromUrl(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return output;
        }

        private String getOutputFromUrl(String text) throws IOException {
            String translated;

            URL urlObj = new URL(YA_URL + API_KEY);
            HttpsURLConnection connection = (HttpsURLConnection)urlObj.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes("&text=" + URLEncoder.encode(text, "UTF-8") + "&lang=" + stringLangFrom + '-' + stringLangTo);

            InputStream response = connection.getInputStream();
            String jsonString = new Scanner(response).nextLine();

            int start = jsonString.indexOf("[");
            int end = jsonString.indexOf("]");
            translated = jsonString.substring(start + 2, end - 1);
            connection.disconnect();

            return translated;
        }

        @Override
        protected void onPostExecute(String output) {
            translatedText.setText(output);

            /* Передаем данные в контент провайдер */

            ContentValues values = new ContentValues();
            values.put(HistoryEntry.COLUMN_TEXT_INPUT, translateTextInput.getText().toString());
            values.put(HistoryEntry.COLUMN_TEXT_TRANSLATED, output);
            values.put(HistoryEntry.COLUMN_LANGUAGES_FROM_TO, stringLangFrom + '-' + stringLangTo);
            values.put(HistoryEntry.COLUMN_BOOKMARK, chooseBookmark);

            /* Внимание, здесь может выпасть NPE
            * TODO: пофиксить это дело
            */
            Context context = getActivity().getApplicationContext();
            if(context != null) {
                try {
                    context.getContentResolver().insert(HistoryEntry.CONTENT_URI, values);
                } catch (Exception e) {
                    Log.v("TranslateFragment", "ALARM IN onPostExecute method" + e);
                }
            }

        }
        @Override
        protected void onPreExecute() {
            translatedText.setText(R.string.connection);
        }
    }

    private void setupLanguageFromSpiner(View view){
        ArrayAdapter<String> adapterSpinnerFrom = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, LANG_SHORT_ARRAY_FULL);
        adapterSpinnerFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLangFrom = (Spinner) view.findViewById(R.id.spinnerLangFrom);
        spinnerLangFrom.setAdapter(adapterSpinnerFrom);
        spinnerLangFrom.setSelection(START_LANG_FROM);
        spinnerLangFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringLangFrom = LANG_SHORT_ARRAY[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void setupLanguageToSpiner(View view){
        ArrayAdapter<String> adapterSpinnerTo = new ArrayAdapter<>(this.getActivity(),
                android.R.layout.simple_spinner_item, LANG_SHORT_ARRAY_FULL);
        adapterSpinnerTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLangTo = (Spinner)view.findViewById(R.id.spinnerLangTo);
        spinnerLangTo.setAdapter(adapterSpinnerTo);
        spinnerLangTo.setSelection(START_LANG_TO);
        spinnerLangTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stringLangTo = LANG_SHORT_ARRAY[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}