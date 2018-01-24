package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Card
{
    Paint paint = new Paint(Color.BLACK);
    int left;
    int right;
    int top;
    int bottom;

    public String name = "Card name"; // Название карты
    public String description = "Description";  //описание карты
    public String type = new String(); // тип карты: руда, мана, отряды
    int MyTowerVal;     // значение на которое следует изменить мою башню
    int MyWallVal;      // значение на которое следует изменить мою стену
    int EnemyTowerVal;  //значение на которое следует изменить вражескую башню
    int EnemyWallVal;   //значение накоторое следует изменить вражескую стену

    public static int width = MainActivity.width / 7;
    public static int height = (int)((double)MainActivity.height / 2.8);


    //------Methods block------//

    ///Рисует карту из левого верхнего угла
    ///Высота и ширина заданы в классе
    public void drawCard(int x, int y, Canvas canvas)
    {
        left = x; right = x + width; top = y; bottom = y + height;

        ///название на карте
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLUE);
        canvas.drawText(name, left + 5, top + 5, paint);

        ///рисуем основу для карты
        Rect rect = new Rect(x, y , x + width, y + height);
        canvas.drawRect(rect, paint);



    }

    ///рисует карту из левого верхнего угла x, y
    ///canvas - полотно на котором будем рисовать
    ///width - ширина карты
    ///height - высота карты
    public void drawCard (int x, int y, int width, int height, Canvas canvas)
    {
        left = x; right = x + width; top = y; bottom = y + height;
        Rect rect = new Rect(x, y , x + width, y + height);
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

   public boolean is_point_inside (int x, int y)
   {
        return left <= x && x <= right && top <= y && y <= bottom;
   }

   public void change_color()
   {
       paint.setColor(Color.RED);
   }

} 