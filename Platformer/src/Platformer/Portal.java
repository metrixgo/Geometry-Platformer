package Platformer;

import javax.swing.*;
import java.awt.*;

public class Portal {
    public int x, y, width, height, factorG, direction, stay;
    public boolean isAvailable, pressed;
    public Image image;

    public Portal(int X, int Y) {
        this(X,Y,0,0);
    }

    public Portal(int X, int Y, int G) {
        this(X,Y,G,0);
    }

    public Portal(int X, int Y, int G, int D) {
        x = X;
        y = Y;
        factorG = G;
        direction = D;
        isAvailable = true;
        if(G == 0){
            image = new ImageIcon(getClass().getResource("/Platformer/Pictures/slowPortal.png")).getImage();
            height = 100;
            width = 50;
        }
        else if(G == 1){
            image = new ImageIcon(getClass().getResource("/Platformer/Pictures/speedPortal.png")).getImage();
            height = 100;
            width = 80;
        }
        else if(G == 2){
            height = 130;
            width = 40;
            image = new ImageIcon(getClass().getResource("/Platformer/Pictures/gravityPortal.png")).getImage();
        }
        else{
            height = 130;
            width = 40;
            image = new ImageIcon(getClass().getResource("/Platformer/Pictures/gravityDeactivationPortal.png")).getImage();
        }
    }

    public void update(Player p){
        if(pressed){
            if(isCollidingPlayer(p)) isAvailable = false;
            else{
                pressed = false;
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
        if(Platformer.finalTrig == 2) y+=3;
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
