package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tower
{
    public static float height = MainActivity.height / 2;
    public static float width = MainActivity.width / 10;
    public float left;
    public float bottom;
    Paint p = new Paint();

    public Tower ()
    {
        p.setColor(Color.RED);
    }

    public Tower (float left, float bottom)
    {
        p.setColor(Color.RED);
        this.left = left;
        this.bottom = bottom;
    }

    public void draw (Canvas canvas)
    {


        canvas.drawRect(left, bottom - height, left + width, bottom, p);
    }
    public void draw (Canvas canvas, float left, float bottom)
    {
        canvas.drawRect(left, bottom - height, left + width, bottom, p);
    }


} 