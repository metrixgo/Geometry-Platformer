package Platformer;

import javax.swing.*;
import java.awt.*;

public class Block {
    public int x, y, width, height;
    public int stay;
    public Image image;

    public Block(int X, int Y) {
        this(X,Y,50,50);
    }

    public Block(int X, int Y, int H) {
        this(X,Y,H,50);
    }

    public Block(int X, int Y, int H, int W) {
        x = X;
        y = Y;
        height = H;
        width = W;
        image = new ImageIcon(getClass().getResource("/Platformer/Pictures/block.png")).getImage();
    }

    public void show(Graphics g, boolean d) {
        if(d || stay>0){
            stay++;
            if(stay == 1) x-=5;
            else if(stay == 2) x+=10;
            else if(stay == 3) x-=5;
            else stay = 0;
        }
        g.drawImage(image, x, y, width, height, null);
    }

}
