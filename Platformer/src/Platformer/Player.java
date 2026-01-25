package Platformer;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static Platformer.Platformer.finalTrig;
import static Platformer.Platformer.player;


public class Player {
    public int x, y, width, height, velocityX, velocityY;
    public boolean onGround;
    public int gravity = 1;
    public int stay, respawnTime;
    public int moveSpeed = 8;
    public int jumpStrength = 15;
    public Image image;
    public Clip death;


    public Player(int X, int Y) {
        x = X;
        y = Y;
        width = 50;
        height = 50;
        velocityX = 0;
        velocityY = 0;
        onGround = false;
        respawnTime = 20;
        image = new ImageIcon(getClass().getResource("/Platformer/Pictures/player.png")).getImage();
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/Platformer/Sounds/explode.wav"));
            death = AudioSystem.getClip();
            death.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void update(ArrayList<Gear> gears, ArrayList<Block> blocks, ArrayList<Spike> spikes, ArrayList<Ring> rings, ArrayList<Pad> pads, ArrayList<Portal> speedPortals, boolean left, boolean right, boolean up) {
        if(stay > 0) return;

        for(int i=speedPortals.size()-1;i>=0;i--){
            Portal s = speedPortals.get(i);
            if(isCollidingSpeedPortal(s)){
                if(s.factorG == 0) moveSpeed = 8;
                else if(s.factorG == 1) moveSpeed = 12;
                else if(s.factorG == 2){
                    gravity = -1;
                    if(velocityY > 10) velocityY = 10;
                }
                else{
                    gravity = 1;
                    if(velocityY < -10) velocityY = -10;
                }
            }
        }

        if(left) player.moveLeft();
        else player.stopLeft();
        if(right) player.moveRight();
        else player.stopRight();

        player.jump(up,rings,pads);

        if(checkHorizontalCollision(velocityX, blocks)) velocityX = 0;
        else x += velocityX;

        if (x >= 800) {
            Platformer.level++;
            Platformer.flg = false;
        }
        if (x < 0) x = 0;

        velocityY += gravity;
        if(checkVerticalCollision(velocityY, blocks)) {
            if(velocityY>0&&gravity>0 || velocityY<0&&gravity<0) onGround = true;
            velocityY = 0;
        } else {
            y += velocityY;
            onGround = false;
        }
        if(finalTrig != 1){
        for(Gear g:gears){
            if(isCollidingGear(g)){
                die();
                return ;
            }
        }
        for(Spike s:spikes){
            if(isCollidingSpike(s)){
                die();
                return ;
            }
        }
        if(y>750||y<-200) die();
        }
    }


    public boolean checkHorizontalCollision(int v, ArrayList<Block> blocks){
        for(Block b:blocks){
            if(!(y<b.y+b.height && y+height>b.y)) continue;
            if(v>0 && x+width+v>b.x && x<b.x+b.width) {
                x=b.x-width;
                return true;
            }
            if(v<0 && x+v<b.x+b.width && x+width>b.x) {
                x=b.x+b.width;
                return true;
            }
        }
        return false;
    }


    public boolean checkVerticalCollision(int v, ArrayList<Block> blocks){
        for(Block b:blocks){
            if(!(x<b.x+b.width&&x+width>b.x)) continue;
            if(v>0 && y+height+v>b.y-5 && y<b.y+b.height){
                y=b.y-height;
                return true;
            }
            if(v<0 && y+v<b.y+b.height+5 && y+height>b.y) {
                y=b.y+b.height;
                return true;
            }
        }
        return false;
    }


    public void show(Graphics g) {
        if(stay > 0){
            if(++stay > respawnTime + 10){
                stay = 0;
                x = Platformer.respawnX;
                y = Platformer.respawnY;
                moveSpeed = Platformer.respawnS;
            }
            else if(stay > 10){
                drawCircle(g, Platformer.respawnX+height/2,Platformer.respawnY+height/2,2*respawnTime+20-2*stay);
            }
        }
        else g.drawImage(image,x,y,width,height,null);
    }


    public void drawCircle(Graphics g, int x, int y, int r) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(10));
        g2.drawOval(x-r,y-r,2*r,2*r);
        g2.dispose();
    }


    public void moveLeft() {velocityX = -moveSpeed;}
    public void moveRight() {velocityX = moveSpeed;}
    public void stopLeft() {if(velocityX<0) velocityX = 0;}
    public void stopRight() {if(velocityX>0) velocityX = 0;}

    public void jump(boolean up,ArrayList<Ring> rings,ArrayList<Pad> pads) {
        for(Pad p:pads){
            if(isCollidingPad(p) && p.isAvailable){
                velocityY = (int)(-1.6*jumpStrength*gravity);
                p.touched = true;
                if(p.isGravitational) gravity*=-1;
                return ;
            }
        }
        if(!up) return ;
        int temp=0;
        for(Ring r:rings){
            if(isCollidingRing(r) && r.isAvailable){
                r.pressed = true;
                onGround = true;
                temp = r.factorG;
                break;
            }
        }
        if(onGround) {
            if(temp == 2) gravity*=-1;
            velocityY = -jumpStrength*gravity;
            if(temp == 1) gravity*=-1;
            onGround = false;
        }
    }


    public boolean isCollidingGear(Gear g) {
        return x<g.x+g.width-15 && x+width>g.x+15 && y<g.y+g.height-15 && y+height>g.y+15;
    }
    public boolean isCollidingSpike(Spike s) {
        return x<s.x+s.width-16 && x+width>s.x+16 && y<s.y+s.height-12 && y+height>s.y+12;
    }
    public boolean isCollidingRing(Ring r) {
        return x<r.x+r.width && x+width>r.x && y<r.y+r.height && y+height>r.y;
    }
    public boolean isCollidingPad(Pad p) {
        return x<p.x+p.width && x+width>p.x && y<p.y+p.height && y+height>p.y;
    }
    public boolean isCollidingSpeedPortal(Portal s) {
        if(s.direction == 0 || s.direction == 180) return x<s.x+s.width && x+width>s.x && y<s.y+s.height && y+height>s.y;
        else return x<s.x-s.height/2+width/2+s.height && x+width>s.x-s.height/2+width/2 && y<s.y+s.height/2-s.width/2+s.width && y+height>s.y+s.height/2-s.width/2;
    }


    public void die(){
        velocityX = 0;
        velocityY = 0;
        stay = 1;
        gravity = Platformer.respawnG;
        Platformer.deathTrig = true;
        death.setFramePosition(0);
        death.start();
    }


}



