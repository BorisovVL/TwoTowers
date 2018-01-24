package vn.twotowers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class GameProcess extends View
{

    public int card_to_card_dist = 20;
    public int timer = 0;
    public int bottom = 10; // расстояние от низа экрана
    public int top = 10; // расстояине сверху экрана
    public int left = 5; // расстояине слева экрана
    public int right = 5; //расстояние справа экрана
    public Player me = new Player();
    public Player enemy = new Player();

    int x, y;

    public GameProcess(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawGrid(canvas);
        if (timer == 0)
        {
            me.Add_card(new Card());
            me.Add_card(new Card());
            me.Add_card(new Card());
            me.Add_card(new Card());
            me.Add_card(new Card());
            me.Add_card(new Card());
        }
        Print_cards(me, canvas);
        timer++;
        //Card c = new Card();
        //c.drawCard(x, y, 50, 50, canvas);
        //Start(canvas);
        //invalidate();
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
    public void Print_cards (Player player, Canvas canvas)
    {
        int x = MainActivity.width / 2 - (Card.width) * 3 - 2 * card_to_card_dist - card_to_card_dist / 2;
        int y = MainActivity.height - Card.height - bottom;

        for (Card card : player.cards)
        {
            card.drawCard(x, y, canvas);
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
                    card.change_color();
            }

            invalidate();
        }
        return true;
    }

}
