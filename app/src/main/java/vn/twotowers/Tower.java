package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Tower
{
    public static float height = MainActivity.height / 2; ///максимальная высоат башни
    public static float width = MainActivity.width / 10;  ///ширина башни
    public float parts = 50; ///высота башни в игровых единицах
    public float loc_height = height * (float)0.5;
    public float loc_width = width;
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
        canvas.drawRect(left, bottom - loc_height, left + loc_width, bottom, p);
    }
    public void draw (Canvas canvas, float left, float bottom)
    {
        canvas.drawRect(left, bottom - loc_height, left + loc_width, bottom, p);
    }

    void change_height (int val)
    {
        float one = height / parts;
        loc_height += val * one;
    }



} 