package Platformer;

import javax.swing.*;
import java.awt.*;

public class Gear {
    public int x, y, width, height, rotation, stay;
    public Image image;

    public Gear(int X, int Y){
        this(X,Y,80);
    }

    public Gear(int X, int Y, int S) {
        x = X;
        y = Y;
        width = S;
        height = S;
        rotation = 0;
        image = new ImageIcon(getClass().getResource("/Platformer/Pictures/gear.png")).getImage();
    }


    public void show(Graphics g, boolean d) {
        if(d || stay>0){
            stay++;
            if(stay == 1) x+=5;
            else if(stay == 2) x-=10;
            else if(stay == 3) x+=5;
            else stay = 0;
        }
        if(Platformer.finalTrig == 2) y-=4;
        Graphics2D g2 = (Graphics2D) g.create();
        drawRotatedImage(g2, image, x, y, width, height, rotation);
        g2.dispose();
        rotation+=2;
    }

    private void drawRotatedImage(Graphics2D g2, Image img, int x, int y, int width, int height, double angle) {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g2.rotate(Math.toRadians(angle), centerX, centerY);
        g2.drawImage(img, x, y, width, height, null);
    }

}
