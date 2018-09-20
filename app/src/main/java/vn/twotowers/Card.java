package vn.twotowers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Card
{
    //переменные определяющие свойства эти же перменные определены в xml

    String title;               //уникальное название карты
    String path;                //путь до img с картой
    int my_tower_val;           //сколько единиц прибавится к моей башне
    int my_wall_val;            //сколько единиц прибавится к моей стене
    int enemy_tower_val;        //сколько единиц отнимется у башни врага
    int enemy_wall_val;         //сколько единиц отнимется у стены врага
    int throw_and_continue;     //1 если после этой карты нужно скинуть карту и продолжить ход
    int _continue;              //1 если можно после этой карты просто продолжить ход
    int end;                    //1 если после этой карты ничего нельзя сделать
    int cost_mana;              //стоимость данной карты в мане
    int cost_rock;              //стоимость данной карты в руде
    int cost_war;               //стоимость данной карты в войсках
    int my_mana_val;            //сколько единиц прибавится к моей мане
    int my_rock_val;            //сколько единиц прибавится к моей руде
    int my_war_val;             //сколько единиц прибавится к моим войскам
    int enemy_mana_val;         //сколько единиц маны отнимется у врага
    int enemy_rock_val;         //сколько единиц руды отнимется к врага
    int enemy_war_val;          //сколько единиц войск отнимется у врага
    int my_mana_building;
    int my_rock_building;
    int my_war_building;
    int enemy_mana_building;
    int enemy_rock_building;
    int enemy_war_building;


    Paint paint = new Paint(Color.BLACK);
    Boolean is_fictive_card = false;
    Boolean is_flying_card = false;
    Boolean is_back_card = false;        //это карта которая возвращается из колоды
    Boolean is_card_to_deck = false;     //это карта которую нужно сбросить
    Boolean is_card_to_table = false;    //это карта которая должна полететь на стол,
    Boolean is_my_card = false;

    Bitmap card;
    public float dx = 0;
    public float dy = 0;

    public float to_x;
    public float to_y;
    public int id;

    float left;
    float right;
    float top;
    float bottom; ///!!! bottom всегда больше чем top!!!



    ///константы подобраны опытным путем профессиональными каскадерами, просьба не повторять в домашних условиях!!!
    public static int width = (int) (MainActivity.width / 6.7); // ширина карты относительно текущего размера экрана
    public static int height = (int)(MainActivity.height / 2.5); // высота карты относительно высоты экрана


    //----------Methods block-----------//

    ///TODO добавить exception'ы на условие top < bottom left > right

    public Card ()
    {

    }

    public Card (Card card)
    {
        this.left = card.left;
        this.top = card.top;
        ///TODO: добавить exception если не установлены параметры card.right и card.bottom
        this.right = left + width;
        this.bottom = top + height;
        this.dx = card.dx;
        this.dy = card.dy;
        this.card = card.card;
        this.is_fictive_card = card.is_fictive_card;
        this.is_my_card = card.is_my_card;
        this.title = card.title;
        this.path = card.path;
        this.my_tower_val = card.my_tower_val;
        this.my_wall_val = card.my_wall_val;
        this.enemy_tower_val = card.enemy_tower_val;
        this.enemy_wall_val = card.enemy_wall_val;
        this.throw_and_continue = card.throw_and_continue;
        this._continue = card._continue;
        this.end = card.end;
        this.cost_mana = card.cost_mana;
        this.cost_rock = card.cost_rock;
        this.cost_war = card.cost_war;
        this.my_mana_val = card.my_mana_val;
        this.my_rock_val = card.my_rock_val;
        this.my_war_val = card.my_war_val;
        this.enemy_mana_val = card.enemy_mana_val;
        this.enemy_rock_val = card.enemy_rock_val;
        this.enemy_war_val = card.enemy_war_val;
    }

    public Card (float left, float top)
    {
        this.left = left;
        this.top = top;
        this.right = left + width;
        this.bottom = top + height;
    }

    public void setup_card_img(int res)
    {
        card = BitmapFactory.decodeResource(GameProcess.myResources, res);
        card = Bitmap.createScaledBitmap(card, width, height, false);
    }

    ///Рисует карту из левого верхнего угла
    ///Высота и ширина заданы в классе
    public void drawCard(Canvas canvas)
    {
        if (is_fictive_card)
            return;
        if (card == null)
            setup_card_img(R.drawable.sample);
        canvas.drawBitmap(this.card, this.left, this.top, this.paint);

        if (throw_and_continue == 1)
        {
            Paint pp = new Paint();
            pp.setColor(Color.BLUE);
            canvas.drawCircle(left, top, 10, pp);
        }
    }

    //принадлежит ли точка карте
   public boolean is_point_inside (float x, float y)
   {
        return left <= x && x <= right && top <= y && y <= bottom;
   }

    public void move_on_vector(float x, float y)
    {
        this.left += x;
        this.right += x;
        this.top += y;
        this.bottom += y;
    }

    public boolean is_card_on_point (float x, float y)
    {
        return left - 3 <= x && x <= left + 3 && top - 3 <= y && y <= top + 3;
    }

} 