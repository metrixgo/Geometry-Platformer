package Platformer;

import javax.swing.*;
import java.awt.*;

public class Pad {
    public int x, y, width, height, direction, stay;
    public boolean isGravitational, isAvailable, touched;
    public Image image;

    public Pad(int X, int Y) {this(X,Y,false,0);}

    public Pad(int X, int Y, boolean G) {this(X,Y,G,0);}

    public Pad(int X, int Y, boolean G, int D) {
        x = X;
        y = Y;
        width = 50;
        height = 10;
        isGravitational = G;
        isAvailable = true;
        direction = D;
        if(G) image = new ImageIcon(getClass().getResource("/Platformer/Pictures/gravityPad.png")).getImage();
        else image = new ImageIcon(getClass().getResource("/Platformer/Pictures/jumpPad.png")).getImage();
    }

    public void update(Player p){
        if(touched){
            if(isCollidingPlayer(p)) isAvailable = false;
            else{
                touched = false;
                isAvailable = true;
            }
        }
    }

    public void show(Graphics g, boolean d) {
        if(d || stay>0){
            stay++;
            if(stay == 1) x+=5;
            else if(stay == 2) x-=10;
            else if(stay == 3) x+=5;
            else stay = 0;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        drawRotatedImage(g2, image, x, y, width, height, direction);
        g2.dispose();
    }

    private void drawRotatedImage(Graphics2D g2, Image img, int x, int y, int width, int height, double angle) {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g2.rotate(Math.toRadians(angle), centerX, centerY);
        g2.drawImage(img, x, y, width, height, null);
    }

    public boolean isCollidingPlayer(Player p) {
        return x<p.x+p.width && x+width>p.x && y<p.y+p.height && y+height>p.y;
    }

}
