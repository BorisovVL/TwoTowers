package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;

import java.util.ArrayList;

public class Player
{
    ArrayList<Card> cards = new ArrayList<Card>();

    Wall wall;
    Tower tower;

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
            card.drawCard(canvas);
    }

    //Tower tower;
    //Wall wall;

}