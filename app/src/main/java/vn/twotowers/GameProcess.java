package vn.twotowers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class GameProcess extends View
{

    ArrayList<Card> flying_card = new ArrayList<Card>();
    ArrayList<Card> deck = new ArrayList<Card>();

    public Card back_card;

    public float xx = MainActivity.width / 2 - Card.width / 2;
    public float card_speed = 50;
    private boolean is_my_step = true;
    public int card_to_card_dist = 20;
    public int timer = 0;
    public int bottom = 10; // расстояние от низа экрана
    public int top = 10; // расстояние сверху экрана
    public int left = 5; // расстояние слева экрана
    public int right = 5; //расстояние справа экрана
    public Player me = new Player();
    public Player enemy = new Player();
    public static Resources myResources ;


    int x, y;

    public GameProcess(Context context)
    {
        super(context);
        myResources = getResources();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        draw_grid(canvas);

        ///начальное распределение карт и прочие настройки
        if (timer == 0)
        {
            float delta = 11;
            enemy.tower = new Tower(MainActivity.width - MainActivity.width / delta - Tower.width, MainActivity.height / 2 + top + bottom);
            me.tower = new Tower(MainActivity.width / delta, MainActivity.height / 2 + top + bottom);

            enemy.wall = new Wall (enemy.tower.left - Wall.width - 5, enemy.tower.bottom);
            me.wall = new Wall (me.tower.left + me.tower.width + 5, me.tower.bottom);

            create_cards(me);
            create_cards(enemy);

            Card tmp = new Card((float) (canvas.getWidth() / 2.0) - card_to_card_dist - Card.width * (float)1.5, top);
            tmp.setup_card_img(R.drawable.back);
            for (int i = 0; i < 4; i++)
            {
                Card now = new Card(tmp);
                deck.add(now);
                tmp.top += (float)Card.height / 15;
            }
        }

        for (Card card : deck)
            card.drawCard(canvas);

        me.show_cards(canvas);
        me.tower.draw(canvas);
        me.wall.draw(canvas);
        enemy.wall.draw(canvas);
        enemy.tower.draw(canvas);

        if (is_my_step)
            my_step(canvas);
        else
            enemy_step(canvas);

        timer++;
        invalidate();
    }

    public void my_step(Canvas canvas)
    {
        ///тут у нас 2 варианта, либо мы ждем пока карта ляжет на стол
        ///либо выбираем карту

        boolean have_fly = false;

        ///пробегаем по всем нашим картам, которыми мы ходили
        for (Card card : flying_card)
            ///если эта карта не летит, прост выведем ее
            if (card.is_flying_card == false)
                card.drawCard(canvas);
            ///если карта летит, значит нужно ее сдвинуть в сторону точки назначение и вывести
            else
            {
                have_fly = true;
                /// проверяем что карта не на своем месте
                if (!card.is_card_on_point(card.to_x, card.to_y))
                {
                    card.move_on_vector(card.dx, card.dy);
                    card.drawCard(canvas);
                }
                else
                {
                    card.is_flying_card = false;
                }
            }


        if (flying_card.size() == 3 && have_fly == false)
        {
            flying_card.clear();
            is_my_step = false;
        }

        ///проверяем на полет карту которая идет к нам из колоды
        if (back_card != null && back_card.is_flying_card)
        {
            if (back_card.is_card_on_point(back_card.to_x, back_card.to_y))
            {
                back_card.is_flying_card = false;
                me.cards.set(back_card.id, back_card);
            }
            else
            {
                back_card.move_on_vector(back_card.dx, back_card.dy);
                back_card.drawCard(canvas);
            }
        }

        //invalidate();
    }


    ///пока что противник играет примитивно, просто выбирает 2-3 рандомные карты
    public void enemy_step(Canvas canvas)
    {
        /// тут почти аналогичные действия что и в методе my_step
        /// единственное карты будут вылетать из-за экрана, улетать будут туда же
        boolean have_fly = false;
        for (Card card : flying_card)
            if (card.is_flying_card == false)
                card.drawCard(canvas);
            else
            {
                if (!card.is_card_on_point(card.to_x, card.to_y))
                {
                    card.move_on_vector(card.dx, card.dy);
                    card.drawCard(canvas);
                }
                else
                {
                    card.is_flying_card = false;
                }
                have_fly = true;
            }


        ///сейчас летящих карт нет, значит либо еще одну кладем на стол, либо заканчиваем ход
        if(have_fly == false)
        {
            if (flying_card.size() == 2)
            {
                flying_card.clear();
                is_my_step = true;
                return;
            }
            else
            {
                Card tmp = new Card(canvas.getWidth() / 2 + Card.width, - Card.height);
                add_card_to_flying_card(tmp);
            }
        }
    }


    // отрисовка вспомогательной сетки
    public void draw_grid (Canvas canvas)
    {
        // две линии, делящие экран по середине
        canvas.drawLine(MainActivity.width / 2, 0, MainActivity.width / 2, MainActivity.height, new Paint(Color.RED));
        canvas.drawLine(0, MainActivity.height / 2, MainActivity.width, MainActivity.height / 2, new Paint(Color.RED));

        // линия отделяющая карты
        canvas.drawLine(0, MainActivity.height - (Card.height + bottom + 10), MainActivity.width, MainActivity.height - (Card.height + bottom + 10), new Paint(Color.CYAN));


        float f = 12;

        // две линии по бокам отделяющие инфморацию о ресурсах
        canvas.drawLine(MainActivity.width / f, 0, MainActivity.width / f, MainActivity.height, new Paint(Color.GRAY));
        canvas.drawLine(MainActivity.width - MainActivity.width / f, 0, MainActivity.width - MainActivity.width / f, MainActivity.height, new Paint(Color.GRAY));
    }


    ///Выводим карты игрока (думаю что наши карты, карты соперника выводить нет смысла)
    public void create_cards (Player player)
    {
        float x = MainActivity.width / 2 - (Card.width) * 3 - 2 * card_to_card_dist - card_to_card_dist / 2;
        float y = MainActivity.height - Card.height - bottom;

        for (int i = 0; i < 6; i++)
        {
            Card now = new Card (x, y);
            now.id = i;
            player.Add_card(now);
            x += Card.width + card_to_card_dist;
        }
        invalidate();
    }

    public void Start(Canvas canvas)
    {

    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN && is_my_step == true)
        {
            x = (int) event.getX();
            y = (int)event.getY();

            // проверяем пришлось ли нажатие на карту
            int cnt = 0;
            for (Card card : me.cards)
            {
                ///обозначем что сейчас будет двигаться одна из карт
                ///также помечаем что за карта
                if (card.is_point_inside(x, y) && card.is_fictive_card == false
                && (flying_card.size() == 0 || flying_card.get(flying_card.size() - 1).is_flying_card == false))
                {
                    Card tmp = new Card(card);
                    card.is_fictive_card = true;


                    /// объявляем карту которая вернется нам вместо той которую мы сейчас выбрали
                    back_card = new Card((float) (MainActivity.width / 2.0) - card_to_card_dist - Card.width * (float)1.5, top + 3 * (float)Card.height / 15);
                    back_card.is_flying_card = true;
                    back_card.dx = (card.left - back_card.left) / 100;
                    back_card.dy = (card.top - back_card.top) / 100;
                    back_card.to_x = card.left;
                    back_card.to_y = card.top;
                    back_card.id = card.id;
                    back_card.setup_card_img(R.drawable.sample2);


                    add_card_to_flying_card(tmp);

                    invalidate();
                }
                cnt++;
            }

            invalidate();
        }
        return true;
    }

    void add_card_to_flying_card (Card card)
    {
        float x, y;

        if (flying_card.size() % 2 == 0)
            x = (float)MainActivity.width / 2 - (float)Card.width / 2;
        else
            x = (float)MainActivity.width / 2 + (float)Card.width / 2 + card_to_card_dist;

        y = top + ((int)flying_card.size() / 2) * (float)Card.height / 10;

        card.to_x = x;
        card.to_y = y;
        card.dx = (float) ((x - card.left) / 100.0);
        card.dy = (float) ((y - card.top) / 100.0);
        card.is_flying_card = true;

        flying_card.add(card);
    }


}
