package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Wall
{
    public static float height = MainActivity.height / 2;
    public static float width = MainActivity.width / 18;
    public float left;
    public float bottom;
    Paint p = new Paint();

    public Wall ()
    {
        p.setColor(Color.BLUE);
    }

    public Wall (float left, float bottom)
    {
        p.setColor(Color.BLUE);
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