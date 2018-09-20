package vn.twotowers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private final Context fContext;
    private static final String DATABASE_NAME = "two_towers.db";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        fContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }


    //метод повзволяет получить все карты из xml файла с параметрами карт
    public ArrayList<Card> cards_from_xml ()
    {

        ArrayList <Card> cards = new ArrayList<Card>();

        ContentValues values = new ContentValues();

        Resources res = fContext.getResources();

        XmlResourceParser _xml = res.getXml(R.xml.cards);

        try
        {
            // Ищем конец документа
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Ищем теги record
                if ((eventType == XmlPullParser.START_TAG)
                        && (_xml.getName().equals("record")))
                {
                    // Тег Record найден, теперь получим его атрибуты и
                    // вставляем в таблицу

                    Card tmp = new Card();

                    int count = _xml.getAttributeCount();

                    tmp.title = _xml.getAttributeValue(null, "title");
                    tmp.path = _xml.getAttributeValue(null, "path");
                    tmp.my_tower_val = Integer.parseInt(_xml.getAttributeValue(null, "my_tower_val"));
                    tmp.my_wall_val = Integer.parseInt(_xml.getAttributeValue(null, "my_wall_val"));
                    tmp.enemy_tower_val = Integer.parseInt(_xml.getAttributeValue(null, "enemy_tower_val"));
                    tmp.enemy_wall_val = Integer.parseInt(_xml.getAttributeValue(null, "enemy_wall_val"));
                    tmp.throw_and_continue = Integer.parseInt(_xml.getAttributeValue(null, "throw_and_continue"));
                    tmp._continue = Integer.parseInt(_xml.getAttributeValue(null, "continue"));
                    tmp.end = Integer.parseInt(_xml.getAttributeValue(null, "end"));
                    tmp.cost_mana = Integer.parseInt(_xml.getAttributeValue(null, "cost_mana"));
                    tmp.cost_rock = Integer.parseInt(_xml.getAttributeValue(null, "cost_rock"));
                    tmp.cost_war = Integer.parseInt(_xml.getAttributeValue(null, "cost_war"));
                    tmp.my_mana_val = Integer.parseInt(_xml.getAttributeValue(null, "my_mana_val"));
                    tmp.my_rock_val = Integer.parseInt(_xml.getAttributeValue(null, "my_rock_val"));
                    tmp.my_war_val = Integer.parseInt(_xml.getAttributeValue(null, "my_war_val"));
                    tmp.enemy_mana_val = Integer.parseInt(_xml.getAttributeValue(null, "enemy_mana_val"));
                    tmp.enemy_rock_val = Integer.parseInt(_xml.getAttributeValue(null, "enemy_rock_val"));
                    tmp.enemy_war_val = Integer.parseInt(_xml.getAttributeValue(null, "enemy_war_val"));
                    tmp.my_mana_building = Integer.parseInt(_xml.getAttributeValue(null, "my_mana_building"));
                    tmp.my_rock_building = Integer.parseInt(_xml.getAttributeValue(null, "my_rock_building"));
                    tmp.my_war_building = Integer.parseInt(_xml.getAttributeValue(null, "my_war_building"));
                    tmp.enemy_mana_building = Integer.parseInt(_xml.getAttributeValue(null, "enemy_mana_building"));
                    tmp.enemy_rock_building = Integer.parseInt(_xml.getAttributeValue(null, "enemy_rock_building"));
                    tmp.enemy_war_building = Integer.parseInt(_xml.getAttributeValue(null, "enemy_war_building"));
                    cards.add(tmp);
                }
                eventType = _xml.next();
            }
        }
        // Catch errors
        catch (XmlPullParserException e) {
            Log.e("Test", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("Test", e.getMessage(), e);

        } finally {
            // Close the xml file
            _xml.close();
        }
        return cards;
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
