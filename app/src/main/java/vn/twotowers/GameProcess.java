package vn.twotowers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class GameProcess extends View
{
    int x, y, k = 1;
    int timer = 0;
    public GameProcess(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        Card c = new Card();
        c.drawCard(x, y, 50, 50, canvas);
        //Start(canvas);
        //invalidate();
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
