package vn.twotowers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameProcess extends View
{

    ArrayList<Card> cards_on_table = new ArrayList<Card>();
    ArrayList<Card> deck = new ArrayList<Card>();
    ArrayList<Card> cards = new ArrayList<>();
    Queue<Card> cards_in_process = new LinkedList<Card>();

    DatabaseHelper db;
    public Card back_card;

    float resource_board_scale = 12;
    public boolean start_step = true;
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

    float x = -1000, y = -1000; ///точка касания

    public GameProcess(Context context)
    {
        super(context);
        db = new DatabaseHelper(context);
        cards = db.cards_from_xml();
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
            //create_cards(enemy);

            Card tmp = new Card((float) (canvas.getWidth() / 2.0) - card_to_card_dist - Card.width * (float)1.5, top);
            tmp.setup_card_img(R.drawable.back);
            for (int i = 0; i < 4; i++)
            {
                Card now = new Card(tmp);
                deck.add(now);
                tmp.top += (float)Card.height / 15;
            }
        }

        //while (check_winner(canvas)){invalidate();}
        canvas_static_view(canvas);
        me.show_cards(canvas);
        me.tower.draw(canvas);
        me.wall.draw(canvas);
        enemy.wall.draw(canvas);
        enemy.tower.draw(canvas);
        print_cards_on_table(canvas);


        ///сюда вынесена обработка перелетов карт, иначе пздц как сложно это отслеживат в коде
        if (!cards_in_process.isEmpty())
        {
            ///двинем карту в направлении полета
            cards_in_process.peek().move_on_vector(cards_in_process.peek().dx, cards_in_process.peek().dy);
            cards_in_process.peek().drawCard(canvas);

            ///карта закончила свое движение, значит больше ее двигать не стоит, удалим ее из очереди
            if (cards_in_process.peek().is_card_on_point(cards_in_process.peek().to_x, cards_in_process.peek().to_y))
            {
                ///проверяем что дальше сделать с этой картой
                if (cards_in_process.peek().is_back_card) /// если это карта из колоды
                {
                    ///если мой ход то кладем карту себе иначе сопернику
                    if (cards_in_process.peek().is_my_card)
                    {
                        me.cards.set(cards_in_process.peek().id, cards_in_process.peek());
                        me.cards.get(cards_in_process.peek().id).is_fictive_card = false;
                    }
                    else
                        enemy.cards.set(cards_in_process.peek().id, cards_in_process.peek());
                }
                else if (cards_in_process.peek().is_card_to_table)
                {
                    cards_on_table.add(cards_in_process.peek());
                    change_values_by_card(cards_in_process.peek());
                }

                cards_in_process.remove();
            }
        }
        else if(check_winner(canvas)) /// игра закончилась
        {
            invalidate();
        }
        else if (is_my_step) /// мой ход
        {
            my_step(canvas);
        }
        else /// ход соперника
        {
            enemy_step(canvas);
        }


        timer++;

        invalidate();
    }


    ///начинаем наш ход
    public void my_step(Canvas canvas)
    {
        if (start_step)
        {
            cards_on_table.clear();
            start_step = false;
        }
        // проверяем пришлось ли нажатие на карту
        for (Card card : me.cards)
        {
            ///обозначем что сейчас будет двигаться одна из карт
            ///также помечаем что за карта
            if (card.is_point_inside(x, y)
                    && !card.is_fictive_card
                    && cards_in_process.isEmpty())
            {
                Card tmp = new Card(card);

                if (me.need_throw_card) /// если мне нужно скинуть карту то выбраная карта полетит в колоду, а не на стол
                {
                    Card card_to_deck = new Card(card);
                    card_to_deck.to_x = deck.get(deck.size() - 1).left;
                    card_to_deck.to_y = deck.get(deck.size() - 1).top;
                    card_to_deck.dx = (card_to_deck.to_x - card_to_deck.left) / (float)100.0;
                    card_to_deck.dy = (card_to_deck.to_y - card_to_deck.top) / (float)100.0;
                    card_to_deck.id = card.id;
                    card_to_deck.setup_card_img(R.drawable.back);
                    cards_in_process.add(card_to_deck);
                    me.need_throw_card = false;
                }
                else
                {
                    if (tmp.throw_and_continue == 1)
                    {
                        me.need_throw_card = true;
                    }
                    else if (tmp.end == 1)
                    {
                        start_step = true;
                        is_my_step = false;
                    }

                    add_card_on_table(tmp);
                }

                /// объявляем карту которая вернется нам вместо той которую мы сейчас выбрали
                back_card = new Card(get_random_card());
                back_card.left = deck.get(deck.size() - 1).left;
                back_card.right = deck.get(deck.size() - 1).right;
                back_card.top = deck.get(deck.size() - 1).top;
                back_card.bottom = deck.get(deck.size() - 1).bottom;
                back_card.is_flying_card = true;
                back_card.dx = (card.left - deck.get(deck.size() - 1).left) / 100;
                back_card.dy = (card.top - deck.get(deck.size() - 1).top) / 100;
                back_card.to_x = card.left;
                back_card.to_y = card.top;
                back_card.id = card.id;
                back_card.is_back_card = true;
                back_card.is_fictive_card = false;
                back_card.is_my_card = true;


                cards_in_process.add(back_card);

                card.is_fictive_card = true;

                invalidate();
            }
        }

        x = -1000; y = -1000; //сбросим координаты после хода
        invalidate();
    }


    ///пока что противник играет примитивно, просто выбирает 2-3 рандомные карты
    public void enemy_step(Canvas canvas)
    {
        if (start_step)
        {
            cards_on_table.clear();
            start_step = false;
        }

        ///выбираем рандомную карту для хода
        Random rnd = new Random(System.currentTimeMillis());
        int ind = rnd.nextInt((int)enemy.cards.size());

        ///если противнику нужно скинуть карту, то выбранную карту мы тупо скинем
        if (enemy.need_throw_card)
        {
            Card card_to_deck = new Card(enemy.cards.get(ind));
            card_to_deck.to_x = deck.get(deck.size() - 1).left;
            card_to_deck.to_y = deck.get(deck.size() - 1).top;
            card_to_deck.left = MainActivity.width / 2 + Card.width;
            card_to_deck.top = -Card.height;
            card_to_deck.dx = (card_to_deck.to_x - card_to_deck.left) / (float)100.0;
            card_to_deck.dy = (card_to_deck.to_y - card_to_deck.top) / (float)100.0;
            card_to_deck.setup_card_img(R.drawable.back);
            cards_in_process.add(card_to_deck);
            enemy.need_throw_card = false;
        }
        else
        {
            if (enemy.cards.get(ind).throw_and_continue == 1)
            {
                enemy.need_throw_card = true;
            }
            else if (enemy.cards.get(ind).end == 1)
            {
                start_step = true;
                is_my_step = true;
            }

            Card tmp = new Card (enemy.cards.get(ind));
            add_card_on_table(tmp);
        }

        /// объявляем карту которая вернется нам вместо той которую мы сейчас выбрали
        back_card = new Card(get_random_card());
        back_card.left = deck.get(deck.size() - 1).left;
        back_card.right = deck.get(deck.size() - 1).right;
        back_card.top = deck.get(deck.size() - 1).top;
        back_card.bottom = deck.get(deck.size() - 1).bottom;
        back_card.is_flying_card = true;
        back_card.to_x = MainActivity.width / 2 + Card.width;
        back_card.to_y = -Card.height * (float)1.5;
        back_card.dx = (back_card.to_x - deck.get(deck.size() - 1).left) / 100;
        back_card.dy = (back_card.to_y - deck.get(deck.size() - 1).top) / 100;
        back_card.is_back_card = true;
        back_card.id = ind;

        cards_in_process.add(back_card);

        //enemy.cards.remove(ind);


        x = -1000; y = -1000; //сбросим координаты после хода
        invalidate();
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


    ///Раздаем себе рандомные 6 карт из колоды
    public void create_cards (Player player)
    {
        float x = MainActivity.width / 2 - (Card.width) * 3 - 2 * card_to_card_dist - card_to_card_dist / 2;
        float y = MainActivity.height - Card.height - bottom;

        for (int i = 0; i < 6; i++)
        {

            Card now = new Card(get_random_card());
            now.is_fictive_card = false;
            String mDrawableName = now.title; // название карты
            int resID = getResources().getIdentifier(mDrawableName , "drawable", MainActivity.PACKAGE_NAME);
            now.setup_card_img(resID);
            now.id = i;
            now.left = x; now.right = now.left + Card.width;
            now.top = y;  now.bottom = now.top + Card.height;
            now.is_my_card = true;
            player.Add_card(now);
            x += Card.width + card_to_card_dist;
        }

        for (int i = 0; i < 6; i++)
        {
            Card now = new Card(get_random_card());
            now.is_fictive_card = false;
            String mDrawableName = now.title; // название карты
            int resID = getResources().getIdentifier(mDrawableName , "drawable", MainActivity.PACKAGE_NAME);
            now.setup_card_img(resID);
            now.id = i;
            now.left = MainActivity.width / 2 + Card.width; now.right = now.left + Card.width;
            now.top = -Card.height;  now.bottom = now.top + Card.height;
            now.is_my_card = false;
            enemy.Add_card(now);
        }

        invalidate();
    }


    public boolean onTouchEvent(MotionEvent event)
        {

        ///пока что здесь только обработка нажатия на карту
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            x = (int)event.getX();
            y = (int)event.getY();
        }
        return true;
    }

    ///процедура которая размещает карту, которой мы походили на столе
    void add_card_on_table (Card card)
    {
        float x, y;

        if (cards_on_table.size() % 2 == 0)
            x = (float)MainActivity.width / 2 - (float)Card.width / 2;
        else
            x = (float)MainActivity.width / 2 + (float)Card.width / 2 + card_to_card_dist;

        y = top + ((int)cards_on_table.size() / 2) * (float)Card.height / 10;

        card.to_x = x;
        card.to_y = y;
        card.dx = (float) ((x - card.left) / 100.0);
        card.dy = (float) ((y - card.top) / 100.0);
        card.is_card_to_table = true;
        cards_in_process.add(card);
    }

    ///позволяет получить рандомную карты из колоды
    ///TODO нужно обработать что мы можем взять карту из колоды, колода не пустая и тд
    Card get_random_card ()
    {
        Card res = new Card();
        Random rnd = new Random(System.currentTimeMillis());
        int ind = rnd.nextInt((int)cards.size());
        res = cards.get(ind);
        cards.remove(ind);
        return res;
    }


    void print_cards_on_table (Canvas canvas)
    {
        for (Card card : cards_on_table)
            card.drawCard(canvas);
    }


    void change_values_by_card (Card card)
    {
        if (card.is_my_card)
        {
            me.tower.change_height(card.my_tower_val);
            me.wall.change_height(card.my_wall_val);
            me.mana += card.my_mana_val;
            me.rock += card.my_rock_val;
            me.war  += card.my_war_val;
            me.mana -= card.cost_mana;
            me.rock -= card.cost_rock;
            me.war  -= card.cost_war;
            me.mana_building += card.my_mana_building;
            me.rock_building += card.my_rock_building;
            me.war_building +=  card.my_war_building;

            enemy.tower.change_height(card.enemy_tower_val);
            enemy.wall.change_height(card.enemy_wall_val);
            enemy.mana +=          card.my_mana_val;
            enemy.rock +=          card.enemy_rock_val;
            enemy.war  +=          card.enemy_war_val;
            enemy.mana_building += card.enemy_mana_building;
            enemy.rock_building += card.enemy_rock_building;
            enemy.war_building +=  card.enemy_war_building;


        }
        else
        {
            enemy.tower.change_height(card.my_tower_val);
            enemy.wall.change_height(card.my_wall_val);
            enemy.mana += card.my_mana_val;
            enemy.rock += card.my_rock_val;
            enemy.war  += card.my_war_val;
            enemy.mana -= card.cost_mana;
            enemy.rock -= card.cost_rock;
            enemy.war  -= card.cost_war;
            enemy.mana_building += card.my_mana_building;
            enemy.rock_building += card.my_rock_building;
            enemy.war_building +=  card.my_war_building;

            me.tower.change_height(card.enemy_tower_val);
            me.wall.change_height(card.enemy_wall_val);
            me.mana +=          card.enemy_mana_val;
            me.rock +=          card.enemy_rock_val;
            me.war  +=          card.enemy_war_val;
            me.mana_building += card.enemy_mana_building;
            me.rock_building += card.enemy_rock_building;
            me.war_building +=  card.enemy_war_building;

        }
    }


    ///здесь собрано все, что особо не изменяется рамки, колода, значки и тд.
    void canvas_static_view (Canvas canvas)
    {
        //вывод колоды
        for (Card card : deck)
            card.drawCard(canvas);

        float third_part = (canvas.getHeight() / (float)2.0) / (float)3.0;

        Paint p = new Paint();
        p.setColor(Color.BLACK);

        ///Вывод иконок ресурсов в левой половине
        canvas.drawText("Mine", 0, 10, p);
        canvas.drawText("Rock: " + String.valueOf(me.rock), 0, 20, p);
        canvas.drawText("Mine: " + String.valueOf(me.rock_building), 0, 40, p);
        canvas.drawText("Mana", 0, third_part, p);
        canvas.drawText("Mana: " + String.valueOf(me.mana), 0, third_part + 20, p);
        canvas.drawText("Mine: " + String.valueOf(me.mana_building), 0, third_part + 40, p);
        canvas.drawText("Barracks", 0, 2 * third_part, p);
        canvas.drawText("Wars: " + String.valueOf(me.war), 0, 2 * third_part + 20, p);
        canvas.drawText("Barracks: " + String.valueOf(me.war), 0, 2 * third_part + 40, p);

        ///Вывод иконок ресурсов в правой половине
        canvas.drawText("Mine", MainActivity.width  - MainActivity.width / resource_board_scale, 10, p);
        canvas.drawText("Rock: " + String.valueOf(enemy.rock), MainActivity.width  - MainActivity.width / resource_board_scale, 20, p);
        canvas.drawText("Mana", MainActivity.width  - MainActivity.width / resource_board_scale, third_part, p);
        canvas.drawText("Mana: " + String.valueOf(enemy.mana), MainActivity.width  - MainActivity.width / resource_board_scale, third_part + 20, p);
        canvas.drawText("Barracks", MainActivity.width  - MainActivity.width / resource_board_scale, 2 * third_part, p);
        canvas.drawText("Wars: " + String.valueOf(enemy.war), MainActivity.width  - MainActivity.width / resource_board_scale, 2 * third_part + 20, p);
    }


    boolean check_winner (Canvas canvas) {

        /// случай когда я победил
        if (me.tower.loc_height >= Tower.height)
        {
            Paint p = new Paint();
            p.setColor(Color.BLACK);

            float x = MainActivity.width / (float) 2.0;
            float y = MainActivity.height / (float) 2.0;
            float len = (float) 200.0;
            canvas.drawRect(x - len, y - len, x + len, y + len, p);
            p.setColor(Color.YELLOW);
            p.setTextSize(20);
            canvas.drawText("Вы победили!", x, y - len * (float)0.5, p);
            return true;
        }
        else if (enemy.tower.loc_height >= Tower.height) ///победил соперник
        {
            Paint p = new Paint();
            p.setColor(Color.BLACK);

            float x = MainActivity.width / (float) 2.0;
            float y = MainActivity.height / (float) 2.0;
            float len = (float) 200.0;
            canvas.drawRect(x - len, y - len, x + len, y + len, p);
            p.setColor(Color.YELLOW);
            p.setTextSize(20);
            canvas.drawText("Вы проиграли!", x, y - len * (float)0.5, p);
            return true;
        }
        else
        {
            return false;
        }
    }
}
