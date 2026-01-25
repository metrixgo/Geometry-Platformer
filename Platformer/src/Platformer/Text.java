package Platformer;

import java.awt.*;

public class Text {
    public int x, y, size;
    public String s;

    public Text(String S){
        this(S,400,60,40);
    }

    public Text(String S, int X, int Y){
        this(S,X,Y,40);
    }

    public Text(String S, int X, int Y, int Si){
        s = S;
        x = X;
        y = Y;
        size = Si;
    }

    public void show(Graphics g) {
        g.setFont(new Font("Papyrus", Font.BOLD, size));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int w=fm.stringWidth(s);
        g.drawString(s, x-w/2, y);
    }

}
