package vn.twotowers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GameProcess extends View
{

    public static Canvas canva;
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
            canva = canvas;
            create_cards(me);
        }

        me.show_cards(canvas);
        timer++;
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
            for (Card card : me.cards)
            {
                if (card.is_point_inside(x, y))
                    card.change();
            }

            invalidate();
        }
        return true;
    }

}
