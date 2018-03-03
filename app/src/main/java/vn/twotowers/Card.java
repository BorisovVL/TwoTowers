package vn.twotowers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Card
{
    Paint paint = new Paint(Color.BLACK);
    Boolean is_fictive_card = false;
    Boolean is_flying_card = false;
    Bitmap card;
    public float dx = 0;
    public float dy = 0;

    public float to_x;
    public float to_y;

    float left;
    float right;
    float top;
    float bottom;


    public static int width = MainActivity.width / 7; // ширина карты относительно текущего размера экрана
    public static int height = (int)((float)MainActivity.height / 2.8); // высота карты относительно высоты экрана


    //----------Methods block-----------//

    public Card (Card card)
    {
        this.left = card.left;
        this.top = card.top;
        this.dx = card.dx;
        this.dy = card.dy;
        this.card = card.card;
        this.is_fictive_card = card.is_fictive_card;
    }

    public Card (float left, float top)
    {
        this.left = left;
        this.top = top;
        this.right = left + width;
        this.bottom = top + height;
    }

    public void setup_card_img(int res)
    {
        card = BitmapFactory.decodeResource(GameProcess.myResources, res);
        card = Bitmap.createScaledBitmap(card, width, height, false);
    }

    ///Рисует карту из левого верхнего угла
    ///Высота и ширина заданы в классе
    public void drawCard(Canvas canvas)
    {
        if (is_fictive_card)
            return;

        if (card == null)
            setup_card_img(R.drawable.sample_card);
        canvas.drawBitmap(this.card, this.left, this.top, this.paint);
    }

    //принадлежит ли точка карте
   public boolean is_point_inside (float x, float y)
   {
        return left <= x && x <= right && top <= y && y <= bottom;
   }

    public void move_on_vector(float x, float y)
    {
        this.left += x;
        this.top += y;
    }

    public boolean is_card_on_point (float x, float y)
    {
        return left - 3 <= x && x <= left + 3 && top - 3 <= y && y <= top + 3;
    }

} 