package pw.tiptoe.translator.database;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import pw.tiptoe.translator.R;

import static pw.tiptoe.translator.database.HistoryContract.HistoryEntry.COLUMN_BOOKMARK;
import static pw.tiptoe.translator.database.HistoryContract.HistoryEntry.COLUMN_LANGUAGES_FROM_TO;
import static pw.tiptoe.translator.database.HistoryContract.HistoryEntry.COLUMN_TEXT_INPUT;
import static pw.tiptoe.translator.database.HistoryContract.HistoryEntry.COLUMN_TEXT_TRANSLATED;


public class HistoryCursorAdapter extends CursorAdapter implements Filterable {

    public HistoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * Метод для привязки всех данных к заданному виду, например, для установки текста в TextView.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Находим поля, в которые будем подставлять свои данные в списке
        TextView textInputTextView = (TextView) view.findViewById(R.id.textInput);
        TextView translatedTextTextView = (TextView) view.findViewById(R.id.textTranslated);
        TextView languagesTextView = (TextView) view.findViewById(R.id.languages);
        TextView bookmarkTextView = (TextView) view.findViewById(R.id.bookmark);
        ImageView bookmarkColor = (ImageView) view.findViewById(R.id.bookmarkPic);

        // Находим индексы колонок в курсоре
        if (cursor != null) {
            int textInputColumnIndex = cursor.getColumnIndex(COLUMN_TEXT_INPUT);
            int translatedTextColumnIndex = cursor.getColumnIndex(COLUMN_TEXT_TRANSLATED);
            int languagesColumnIndex = cursor.getColumnIndex(COLUMN_LANGUAGES_FROM_TO);
            int bookmarkColumnIndex = cursor.getColumnIndex(COLUMN_BOOKMARK);

            // Читаем данные из курсора для текущей записи
            String textInput = cursor.getString(textInputColumnIndex);
            String translatedText = cursor.getString(translatedTextColumnIndex);
            String languages = cursor.getString(languagesColumnIndex);
            String bookmark = cursor.getString(bookmarkColumnIndex);

            // Обновляем текстовые поля, подставляя в них данные для текущей записи
            textInputTextView.setText(textInput);
            translatedTextTextView.setText(translatedText);
            languagesTextView.setText(languages.toUpperCase());
//            bookmarkTextView.setText(bookmark);

            if (bookmark.equals("1")){
                bookmarkColor.setColorFilter(ContextCompat.getColor(context,R.color.bookmarkYes));
            } else {
                bookmarkColor.setColorFilter(ContextCompat.getColor(context,R.color.bookmarkNo));
            }
        }
    }

}
