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
    int x, y, k = 1;

    public GameProcess(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        drawGrid(canvas);
        Player pl = new Player();
        pl.Add_card(new Card());
        pl.Add_card(new Card());
        pl.Add_card(new Card());
        pl.Add_card(new Card());
        pl.Add_card(new Card());
        pl.Add_card(new Card());
        Print_cards(pl, canvas);
        //Card c = new Card();
        //c.drawCard(x, y, 50, 50, canvas);
        //Start(canvas);
        //invalidate();
    }

    // отрисовка вспомогательной сетки
    public void drawGrid (Canvas canvas)
    {
        canvas.drawLine(MainActivity.width / 2, 0, MainActivity.width / 2, MainActivity.height, new Paint(Color.RED));
        canvas.drawLine(0, MainActivity.height / 2, MainActivity.width, MainActivity.height / 2, new Paint(Color.RED));
    }

    ///Выводим карты игрока (думаю что наши карты, карты соперника выводить нет смысла)
    public void Print_cards (Player player, Canvas canvas)
    {
        int x = MainActivity.width / 2 - (Card.width) * 3 - 20 * 2 - 10;
        int y = MainActivity.height - Card.height - 20;

        for (Card card : player.cards)
        {
            card.drawCard(x, y, canvas);
            x += 90;
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
            invalidate();
        }
        return true;
    }

}
