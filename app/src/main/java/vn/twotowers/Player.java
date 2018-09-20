package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;

import java.util.ArrayList;

public class Player
{
    ArrayList<Card> cards = new ArrayList<Card>();


    Boolean need_throw_card = false; ///должен ли я скинуть карту
    Wall wall;
    Tower tower;
    int mana = 10;
    int war = 10;
    int rock = 10;
    int mana_building = 3;
    int rock_building = 3;
    int war_building = 3;

    public Player()
    {
    }

    public void Add_card(Card card)
    {
        cards.add(card);
    }

    public void show_cards (Canvas canvas)
    {
        for (Card card : cards)
            if (!card.is_fictive_card)
                card.drawCard(canvas);
            else {
                System.out.print("Hidden card");
            }
    }

}