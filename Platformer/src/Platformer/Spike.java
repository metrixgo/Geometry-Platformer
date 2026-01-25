package Platformer;

import javax.swing.*;
import java.awt.*;

public class Spike {
    public int x, y, width, height, step, direction, stay;
    public Image image;

    public Spike(int X, int Y) {
        this(X,Y,0,50,50);
    }

    public Spike(int X, int Y, int D) {
        this(X,Y,D,50,50);
    }

    public Spike(int X, int Y, int D, int H) {
        this(X,Y,D,H,50);
    }

    public Spike(int X, int Y, int D, int H, int W) {
        x = X;
        y = Y;
        direction = D;
        height = H;
        width = W;
        image = new ImageIcon(getClass().getResource("/Platformer/Pictures/spike.png")).getImage();
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

}
