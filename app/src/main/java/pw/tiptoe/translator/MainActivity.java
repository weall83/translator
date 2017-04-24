package pw.tiptoe.translator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import pw.tiptoe.translator.fragments.FavoritesFragment;
import pw.tiptoe.translator.fragments.HistoryFragment;
import pw.tiptoe.translator.fragments.TranslateFragment;

import static pw.tiptoe.translator.R.id.navigation_favorites;
import static pw.tiptoe.translator.R.id.navigation_history;
import static pw.tiptoe.translator.R.id.navigation_translate;

public class MainActivity extends AppCompatActivity {

    //private TextView mTextMessage;
    FragmentManager fm = getSupportFragmentManager();
    Fragment fragment = fm.findFragmentById(R.id.content);

    Fragment fragment_translate = fm.findFragmentById(R.id.content);
    Fragment fragment_favorites = fm.findFragmentById(R.id.content);
    Fragment fragment_history = fm.findFragmentById(R.id.content);


    private static final String TAG = "translator";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case navigation_translate:
                    //if (fragment_translate==null) fragment_translate = new TranslateFragment();
                    fm.beginTransaction()
                            .replace(R.id.content, fragment_translate)
                            .commit();

                   // mGlobal.setBotNavId(navigation_translate);



                    //mTextMessage.setText(R.string.title_home);
                    return true;
                case navigation_history:

                        if (fragment_history==null)fragment_history = new HistoryFragment();
                        fm.beginTransaction()
                                .replace(R.id.content, fragment_history)
                                .commit();
                    //mGlobal.setBotNavId(1);
                   // mGlobal.setBotNavId(R.id.navigation_history);

                    //Log.e(TAG,"on create, mBotNavId frag="+mGlobal.getBotNavId());


                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case navigation_favorites:

                        if (fragment_favorites==null)fragment_favorites = new FavoritesFragment();
                        fm.beginTransaction()
                                .replace(R.id.content, fragment_favorites)
                                .commit();
                    //mGlobal.setBotNavId(R.id.navigation_favorites);


                        //mTextMessage.setText(R.string.title_notifications);
                        return true;

            }
            return false;



        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        fragment_translate = new TranslateFragment();
        fm.beginTransaction()
                .replace(R.id.content, fragment_translate)
                .commit();



        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);





    }


}
