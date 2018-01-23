package vn.twotowers;

import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;

import java.util.ArrayList;

public class Player
{
    ArrayList<Card> cards = new ArrayList<Card>();

    public Player()
    {
    }

    public void Add_card(Card card)
    {
        cards.add(card);
    }

    //Tower tower;
    //Wall wall;

}