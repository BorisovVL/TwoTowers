package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;

import java.util.ArrayList;

public class Player
{
    ArrayList<Card> cards = null;

    public void Add_card(Card card)
    {
        cards.add(card);
    }

    public void Print_cards (Canvas canvas)
    {
        int x = MainActivity.height / 2;
        int y = 10;

        for (Card card : cards)
        {
            card.drawCard(x, y, 30, 50, canvas);
        }
    }


    //Tower tower;
    //Wall wall;

}