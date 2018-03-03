package vn.twotowers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameProcess extends View
{

    ArrayList<Card> flying_card = new ArrayList<Card>();
    ArrayList<Card> deck = new ArrayList<Card>();

    public float xx = MainActivity.width / 2 - Card.width / 2;
    public float card_speed = 50;
    public int card_to_card_dist = 20;
    public int timer = 0;
    public int bottom = 10; // расстояние от низа экрана
    public int top = 10; // расстояние сверху экрана
    public int left = 5; // расстояние слева экрана
    public int right = 5; //расстояние справа экрана
    public Player me = new Player();
    public Player enemy = new Player();
    public static Resources myResources ;


    int x, y;

    public GameProcess(Context context)
    {
        super(context);
        myResources = getResources();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawGrid(canvas);
        if (timer == 0)
        {
            create_cards(me);
            Card tmp = new Card((float) (canvas.getWidth() / 2.0) - card_to_card_dist - Card.width * (float)1.5, top);
            tmp.setup_card_img(R.drawable.back);
            for (int i = 0; i < 4; i++)
            {
                Card now = new Card(tmp);
                deck.add(now);
                tmp.top += 7;
            }
        }
        else
            my_step(canvas);

        for (Card card : deck)
            card.drawCard(canvas);

        me.show_cards(canvas);

        timer++;
        invalidate();
    }

    public void my_step(Canvas canvas)
    {
        ///тут у нас 2 варианта, либо мы ждем пока карта ляжет на стол
        ///либо выбираем карту


        for (Card card : flying_card)
            if (card.is_flying_card == false)
                card.drawCard(canvas);

        ///карта еще летит
        if (flying_card.isEmpty() == false)
        {
            Card now = flying_card.get((int)flying_card.size() - 1);
            if (now.is_flying_card && !now.is_card_on_point(now.to_x, now.to_y))
            {
                now.move_on_vector(now.dx, now.dy);
                now.drawCard(canvas);
            }
            else if (now.is_card_on_point(now.to_x, now.to_y))
            {
                now.is_flying_card = false;
            }
        }
        else /// выбираем карту или делаем какие-то другие действия
        {

        }
        //invalidate();
    }

    public void enemy_step()
    {

    }


    // отрисовка вспомогательной сетки
    public void drawGrid (Canvas canvas)
    {
        // две линии, делящие экран по середине
        canvas.drawLine(MainActivity.width / 2, 0, MainActivity.width / 2, MainActivity.height, new Paint(Color.RED));
        canvas.drawLine(0, MainActivity.height / 2, MainActivity.width, MainActivity.height / 2, new Paint(Color.RED));

        // линия отделяющая карты
        canvas.drawLine(0, MainActivity.height - (Card.height + bottom + 10), MainActivity.width, MainActivity.height - (Card.height + bottom + 10), new Paint(Color.CYAN));

        // две линии по бокам отделяющие инфморацию о ресурсах
        canvas.drawLine(MainActivity.width / 8, 0, MainActivity.width / 8, MainActivity.height, new Paint(Color.GRAY));
        canvas.drawLine(MainActivity.width - MainActivity.width / 8, 0, MainActivity.width - MainActivity.width / 8, MainActivity.height, new Paint(Color.GRAY));
    }

    ///Выводим карты игрока (думаю что наши карты, карты соперника выводить нет смысла)
    public void create_cards (Player player)
    {
        int x = MainActivity.width / 2 - (Card.width) * 3 - 2 * card_to_card_dist - card_to_card_dist / 2;
        int y = MainActivity.height - Card.height - bottom;

        for (int i = 0; i < 6; i++)
        {
            player.Add_card(new Card(x, y));
            x += Card.width + card_to_card_dist;
        }
        invalidate();
    }

    public void Start(Canvas canvas)
    {

    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            x = (int) event.getX();
            y = (int)event.getY();

            // проверяем пришлось ли нажатие на карту
            int cnt = 0;
            for (Card card : me.cards)
            {
                ///обозначем что сейчас будет двигаться одна из карт
                ///также помечаем что за карта
                if (card.is_point_inside(x, y) && card.is_fictive_card == false
                && (flying_card.size() == 0 || flying_card.get(flying_card.size() - 1).is_flying_card == false))
                {
                    Card tmp = new Card(card);
                    card.is_fictive_card = true;

                    float x, y;

                    if (flying_card.size() % 2 == 0)
                        x = MainActivity.width / 2 - Card.width / 2;
                    else
                        x = MainActivity.width / 2 + Card.width / 2 + card_to_card_dist;

                    y = top + ((int)flying_card.size() / 2) * Card.height / 20;

                    tmp.to_x = x;
                    tmp.to_y = y;
                    tmp.dx = (float) ((x - card.left) / 100.0);
                    tmp.dy = (float) ((y - card.top) / 100.0);
                    tmp.is_flying_card = true;
                    tmp.setup_card_img(R.drawable.sample_card);
                    flying_card.add(tmp);
                    invalidate();
                }
                cnt++;
            }

            invalidate();
        }
        return true;
    }

}
