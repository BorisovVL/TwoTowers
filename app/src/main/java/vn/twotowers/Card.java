package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Card
{


    //------Methods block------//
    ///рисует карту из левого верхнего угла x, y
    ///canvas - полотно на котором будем рисовать
    ///width - ширина карты
    ///height - высота карты
    public void drawCard (int x, int y, int width, int height, Canvas canvas)
    {
        Rect rect = new Rect(x, y , x + width, y + height);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(rect, paint);
    }
    ///рисует карту с центром в x,y
    ///len - длину стороны карты
   /* public void drawCard (int x, int y, int len, Canvas canvas)
    {
        Rect rect = new Rect(x - len / 2, y - len / 2, x + side / 2, y + side / 2);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawRect(rect, paint);
    }
    */

} 