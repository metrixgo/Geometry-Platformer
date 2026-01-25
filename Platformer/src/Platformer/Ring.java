package Platformer;

import javax.swing.*;
import java.awt.*;

public class Ring {
    public int x, y, width, height, factorG, stay, rotation;
    public boolean isAvailable, pressed;
    public Image image, decor;

    public Ring(int X, int Y) {
        this(X,Y,0);
    }

    public Ring(int X, int Y, int G) {
        x = X;
        y = Y;
        width = 30;
        height = 30;
        factorG = G;
        isAvailable = true;
        if(G == 0) image = new ImageIcon(getClass().getResource("/Platformer/Pictures/jumpRing.png")).getImage();
        else if(G == 1) image = new ImageIcon(getClass().getResource("/Platformer/Pictures/gravityRing.png")).getImage();
        else image = new ImageIcon(getClass().getResource("/Platformer/Pictures/delayedGravityRing.png")).getImage();
        decor = new ImageIcon(getClass().getResource("/Platformer/Pictures/ringDecor.png")).getImage();
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
        g.drawImage(image, x, y, width, height, null);
        Graphics2D g2 = (Graphics2D) g.create();
        drawRotatedImage(g2, decor, x-2, y-2, width+4, height+4, rotation);
        g2.dispose();
        rotation+=2;
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
