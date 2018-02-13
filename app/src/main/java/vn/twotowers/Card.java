package vn.twotowers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

public class Card
{
    Paint paint = new Paint(Color.BLACK);
    Bitmap card;
    int left;
    int right;
    int top;
    int bottom;
    boolean f = false;

    public String name = "Card name"; // Название карты
    public String description = "Description";  //описание карты
    public String type = new String(); // тип карты: руда, мана, отряды
    int MyTowerVal;     // значение на которое следует изменить мою башню
    int MyWallVal;      // значение на которое следует изменить мою стену
    int EnemyTowerVal;  //значение на которое следует изменить вражескую башню
    int EnemyWallVal;   //значение накоторое следует изменить вражескую стену

    public static int width = MainActivity.width / 7; // ширина карты относительно текущего размера экрана
    public static int height = (int)((double)MainActivity.height / 2.8); // высота карты относительно высоты экрана


    //----------Methods block-----------//


    public Card (int left, int top)
    {
        this.left = left;
        this.top = top;
        this.right = left + width;
        this.bottom = top + height;
    }

    ///Рисует карту из левого верхнего угла
    ///Высота и ширина заданы в классе
    public void drawCard(Canvas canvas)
    {
        if (!f)
            card = BitmapFactory.decodeResource(GameProcess.myResources, R.drawable.sample_card);
        else
            card = BitmapFactory.decodeResource(GameProcess.myResources, R.drawable.sample2);

        card = Bitmap.createScaledBitmap(card, width, height, false);
        canvas.drawBitmap(card, left, top, paint);
    }

    //принадлежит ли точка карте
   public boolean is_point_inside (int x, int y)
   {
        return left <= x && x <= right && top <= y && y <= bottom;
   }

    // ненужная хуйня просто чтобы изменить картинку на карте
    public void change ()
    {
        f = !f;
    }
    public void move_to_point( )
   {
       //paint.setColor(Color.RED);
   }

} 