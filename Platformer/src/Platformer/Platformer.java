package Platformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class Platformer extends JPanel implements ActionListener, KeyListener {
    static JFrame frame;
    static Timer timer;
    static Player player = new Player(0,500);
    static ArrayList<Gear> gears = new ArrayList<>();
    static ArrayList<Block> blocks = new ArrayList<>();
    static ArrayList<Spike> spikes = new ArrayList<>();
    static ArrayList<Ring> rings = new ArrayList<>();
    static ArrayList<Pad> pads = new ArrayList<>();
    static ArrayList<Text> texts = new ArrayList<>();
    static ArrayList<Portal> portals = new ArrayList<>();
    static Background background = new Background();
    static int level = 1,lastLevel = 30, stay, ticks;
    static int respawnX,respawnY,respawnG,respawnS, finalTrig;
    static boolean flg, deathTrig, cheatFlg;
    static boolean leftPressed,rightPressed,upPressed;

    public Platformer() {
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(20, this);
        timer.start();
        background.play();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        background.show(g,deathTrig);
        player.show(g);
        for(Gear gear:gears) gear.show(g,deathTrig);
        for(Spike s:spikes) s.show(g,deathTrig);
        for(Block b:blocks) b.show(g,deathTrig);
        for(Ring r:rings) r.show(g,deathTrig);
        for(Pad p:pads) p.show(g,deathTrig);
        for(Portal sp: portals) sp.show(g,deathTrig);
        for(Text t:texts) t.show(g);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(!flg) generateLevel(level);
        if(finalTrig == 2){
            if(deathTrig){
                frame.dispose();
                System.exit(0);
            }
            ticks++;
            if(ticks == 1) background.finalPlay();
            if(ticks<=1200 && ticks%100==0){
                int temp;
                for(int i=0;i<4;i++){
                    temp=(int)(Math.random()*16);
                    spikes.add(new Spike(temp*50,-50,180));
                }
            }
            if(ticks<=1200 && (ticks%200==0)){
                int temp;
                temp=(int)(Math.random()*701);
                gears.add(new Gear(temp,700,100));
            }
            if(ticks>=1250 && ticks%50==0){
                int temp;
                for(int i=0;i<3;i++){
                    temp=(int)(Math.random()*16);
                    spikes.add(new Spike(temp*50,-50,180));
                }
            }
            if(ticks>=1250 && ticks%70==0){
                int temp;
                temp=(int)(Math.random()*751);
                gears.add(new Gear(temp-5,800,120));
                portals.add(new Portal(temp+20,-50,2,270));
            }

            if(ticks == 2190){
                for(int i=0;i<16;i++) spikes.add(new Spike(i*50,-50,180));
            }

            if(ticks == 2341){
                finalTrig = 1;
                spikes.clear();
                gears.clear();
                portals.clear();
                blocks.clear();
                for(int i=0;i<16;i++) blocks.add(new Block(i*50,550));
                player.gravity = 1;
            }
        }
        player.update(gears,blocks,spikes,rings,pads, portals,leftPressed,rightPressed,upPressed);
        for(Pad p:pads) p.update(player);
        for(Ring r:rings) r.update(player);
        repaint();
        if(deathTrig){
            if(stay>0){
                stay = 0;
                deathTrig = false;
            }
            else stay++;
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) leftPressed = true;
        if (key == KeyEvent.VK_D) rightPressed = true;
        if (key == KeyEvent.VK_SPACE) upPressed = true;
        if (key == KeyEvent.VK_C){
            if(level < lastLevel) {
                Platformer.level++;
                Platformer.flg=false;
                cheatFlg = true;
            }
        }
        if (key == KeyEvent.VK_Z){
            if(level > 1 && level < lastLevel) {
                Platformer.level--;
                Platformer.flg=false;
                cheatFlg = true;
            }
        }
        if (key == KeyEvent.VK_R) player.die();
    }



    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) leftPressed = false;
        if (key == KeyEvent.VK_D) rightPressed = false;
        if (key == KeyEvent.VK_SPACE) upPressed = false;
    }


    @Override
    public void keyTyped(KeyEvent e) {}




    public void generateLevel(int x){
        flg=true;
        blocks.clear();
        gears.clear();
        spikes.clear();
        rings.clear();
        pads.clear();
        texts.clear();
        portals.clear();
        respawnX=0;
        respawnY=500;
        respawnG=1;
        respawnS=8;
        if(x==1){
            texts.add(new Text("Geometry Platformer"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
        }
        else if(x==2){
            texts.add(new Text("A Small Wall"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            blocks.add(new Block(350,500));
            blocks.add(new Block(350,450));
        }
        else if(x==3){
            texts.add(new Text("More Walls"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=1;i<=2;i++) blocks.add(new Block(100,550-i*50));
            for(int i=1;i<=3;i++) blocks.add(new Block(300,550-i*50));
            for(int i=1;i<=4;i++) blocks.add(new Block(500,550-i*50));
            for(int i=1;i<=5;i++) blocks.add(new Block(700,550-i*50));
        }
        else if(x==4){
            texts.add(new Text("A View to Death"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            spikes.add(new Spike(100,500));
            blocks.add(new Block(200,400));
            spikes.add(new Spike(200,450,180));
            for(int i=0;i<3;i++) spikes.add(new Spike(300+i*50,500,0));
            for(int i=0;i<3;i++) blocks.add(new Block(500+i*50,400));
            for(int i=0;i<3;i++) spikes.add(new Spike(500+i*50,450,180));
            for(int i=0;i<2;i++) spikes.add(new Spike(700+i*50,500));
        }
        else if(x==5){
            texts.add(new Text("More Hazards"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            blocks.add(new Block(50,500));
            blocks.add(new Block(50,450));
            for(int i=1;i<=4;i++) blocks.add(new Block(100,550-i*50));
            for(int i=1;i<=5;i++) blocks.add(new Block(350,550-i*50));
            for(int i=1;i<=6;i++) blocks.add(new Block(600,550-i*50));
            for(int i=0;i<3;i++) gears.add(new Gear(150+i*60,510));
            for(int i=0;i<3;i++) gears.add(new Gear(400+i*60,510));
            for(int i=0;i<2;i++) gears.add(new Gear(650+i*60,510));
        }
        else if(x==6){
            texts.add(new Text("Smaller Slabs"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=3;i<=10;i++) blocks.add(new Block(650,i*50));
            blocks.add(new Block(650,125,25));
            blocks.add(new Block(250,300,25));
            blocks.add(new Block(150,200,25));
            blocks.add(new Block(350,450,25));
            blocks.add(new Block(150,450,25));
            blocks.add(new Block(250,400,25));
            blocks.add(new Block(450,350,25));
            blocks.add(new Block(550,250,25));
            blocks.add(new Block(350,150,25));
            for(int i=0;i<4;i++) gears.add(new Gear(395+i*60,510));
            for(int i=0;i<8;i++) spikes.add(new Spike(700,150+i*50,90));
            for(int i=4;i<=14;i++) blocks.add(new Block(800,550-i*50));
        }
        else if(x==7){
            texts.add(new Text("Up!"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=2;i<=10;i++) blocks.add(new Block(650,i*50));
            for(int i=-1;i<10;i++) blocks.add(new Block(500,i*50));
            for(int i=2;i<=10;i++) blocks.add(new Block(400,i*50));
            for(int i=-1;i<10;i++) blocks.add(new Block(250,i*50));
            for(int i=2;i<=10;i++) blocks.add(new Block(150,i*50));
            for(int i=2;i<=5;i++) blocks.add(new Block(100,i*100,15));
            for(int i=2;i<=5;i++) blocks.add(new Block(350,i*100,15));
            for(int i=2;i<=5;i++) blocks.add(new Block(600,i*100,15));
            blocks.add(new Block(700,100));
            blocks.add(new Block(750,100));
        }
        else if(x==8){
            respawnY = 50;
            texts.add(new Text("Down...",400,55));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=-1;i<8;i++) blocks.add(new Block(750,i*50));
            for(int i=0;i<13;i++) blocks.add(new Block(i*50,100));
            for(int i=2;i<16;i++) blocks.add(new Block(i*50,350));
            for(int i=1;i<=3;i++) gears.add(new Gear(200*i-50,60));
            for(int i=1;i<=3;i++) spikes.add(new Spike(100+i*50,300));
            for(int i=1;i<=3;i++) spikes.add(new Spike(300+i*50,300));
            for(int i=1;i<=3;i++) spikes.add(new Spike(500+i*50,300));
            for(int i=1;i<=3;i++) gears.add(new Gear(200*i,510));
        }
        else if(x==9){
            respawnY = 470;
            texts.add(new Text("Deadly Zone",550,60));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,570,0,30));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,600));
            blocks.add(new Block(0,520,25));
            blocks.add(new Block(100,420,25));
            spikes.add(new Spike(100,445,180,40));
            blocks.add(new Block(0,320,25));
            spikes.add(new Spike(0,345,180,40));
            blocks.add(new Block(100,220,25));
            spikes.add(new Spike(100,245,180,40));
            blocks.add(new Block(0,120,25));
            spikes.add(new Spike(0,145,180,40));
            blocks.add(new Block(100,20,25));
            spikes.add(new Spike(100,45,180,40));
            for(int i=1;i<=7;i++) gears.add(new Gear(200,i*60));
            for(int i=0;i<10;i++) blocks.add(new Block(300+i*50,520,25));
            for(int i=0;i<11;i++) blocks.add(new Block(350,420-i*50));
            for(int i=0;i<11;i++) spikes.add(new Spike(320,440-i*50,270,10));
            for(int i=0;i<9;i++) blocks.add(new Block(700,470-i*50));
            blocks.add(new Block(650,420,25));
            gears.add(new Gear(500,460));
            gears.add(new Gear(600,230));
            blocks.add(new Block(400,395,25));
            blocks.add(new Block(400,300,25));
            for(int i=1;i<=5;i++) blocks.add(new Block(400+i*50,200,25));
            for(int i=0;i<3;i++) spikes.add(new Spike(500+i*50,150));
            blocks.add(new Block(650,150));
            for(int i=4;i<12;i++) blocks.add(new Block(800,470-i*50));
        }
        else if(x==10){
            texts.add(new Text("Bouncing Cube"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            pads.add(new Pad(100,540));
            for(int i=0;i<4;i++) blocks.add(new Block(225,500-i*50));
            spikes.add(new Spike(225,300));
            pads.add(new Pad(350,540));
            for(int i=0;i<4;i++) blocks.add(new Block(475,500-i*50));
            spikes.add(new Spike(475,300));
            pads.add(new Pad(600,540));
            for(int i=0;i<4;i++) blocks.add(new Block(725,500-i*50));
            spikes.add(new Spike(725,300));
        }
        else if(x==11){
            texts.add(new Text("Squares and Circles"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            rings.add(new Ring(135,405));
            for(int i=0;i<4;i++) blocks.add(new Block(225,500-i*50));
            for(int i=0;i<3;i++) gears.add(new Gear(270+i*70,510));
            spikes.add(new Spike(225,320,0,30));
            rings.add(new Ring(385,405));
            for(int i=0;i<4;i++) blocks.add(new Block(475,500-i*50));
            for(int i=0;i<3;i++) gears.add(new Gear(520+i*70,510));
            spikes.add(new Spike(475,320,0,30));
            rings.add(new Ring(635,405));
            for(int i=0;i<4;i++) blocks.add(new Block(725,500-i*50));
            spikes.add(new Spike(725,320,0,30));
        }
        else if(x==12){
            texts.add(new Text("Pros and Cons"));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=0;i<14;i++) blocks.add(new Block(i*50,200));
            for(int i=0;i<14;i++) blocks.add(new Block(i*50,150));
            for(int i=0;i<14;i++) spikes.add(new Spike(i*50,250,180));
            for(int i=0;i<=4;i++) pads.add(new Pad(50+i*150,540));
            for(int i=0;i<=4;i++) blocks.add(new Block(750,500-i*50));
        }
        else if(x==13){
            respawnY = 250;
            texts.add(new Text("The Great Leap"));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,570,0,30));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,600));
            for(int i=0;i<3;i++) blocks.add(new Block(50*i,300));
            pads.add(new Pad(100,290));
            gears.add(new Gear(220,180,260));
            for(int i=0;i<3;i++) blocks.add(new Block(750,500-i*50));
            for(int i=0;i<3;i++) spikes.add(new Spike(700,500-i*50,270));
            for(int i=5;i<9;i++) blocks.add(new Block(750,500-i*50));
            for(int i=5;i<9;i++) spikes.add(new Spike(700,500-i*50,270));
            rings.add(new Ring(600,420));
        }
        else if(x==14){
            respawnY = 350;
            texts.add(new Text("Through the Spikes",330,80));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,580,0,20));
            for(int i=0;i<16;i++) {
                if(i%2==0) spikes.add(new Spike(50*i,0,180,20));
                else spikes.add(new Spike(50*i,0,180,50));
            }
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,600));
            for(int i=0;i<2;i++) blocks.add(new Block(50*i,400));
            for(int i=0;i<3;i++) rings.add(new Ring(210,300-100*i));
            for(int i=0;i<6;i++) blocks.add(new Block(300,450-i*50));
            spikes.add(new Spike(300,150));
            rings.add(new Ring(430,120));
            gears.add(new Gear(410,180));
            for(int i=1;i<9;i++) blocks.add(new Block(600,450-i*50));
            for(int i=1;i<9;i++) spikes.add(new Spike(550,450-i*50,270));
            for(int i=1;i<=3;i++) blocks.add(new Block(600+50*i,400));
            for(int i=0;i<=3;i++) spikes.add(new Spike(600+50*i,450,180));
            for(int i=1;i<=3;i++) rings.add(new Ring(600+50*i,500));
            for(int i=0;i<=5;i++) blocks.add(new Block(500+i*50,550,25));
        }
        else if(x==15){
            texts.add(new Text("Gravity Reverse",400,300));
            for(int i=0;i<5;i++) blocks.add(new Block(50*i,550));
            for(int i=5;i<16;i++) spikes.add(new Spike(50*i,570,0,30));
            for(int i=5;i<16;i++) blocks.add(new Block(50*i,600));
            for(int i=0;i<7;i++) blocks.add(new Block(50*i,0));
            for(int i=7;i<16;i++) spikes.add(new Spike(50*i,0,180,30));
            for(int i=7;i<16;i++) blocks.add(new Block(50*i,-50));
            portals.add(new Portal(200,340,2));
            rings.add(new Ring(400,200));
            rings.add(new Ring(550,200));
            rings.add(new Ring(700,200));
            blocks.add(new Block(300,50));
        }
        else if(x==16){
            respawnY = 50;
            respawnG = -1;
            texts.add(new Text("Choose Wisely",530,70));
            for(int i=0;i<6;i++) blocks.add(new Block(50*i,0));
            for(int i=6;i<16;i++) spikes.add(new Spike(50*i,0,180,30));
            pads.add(new Pad(50,50,true,180));
            pads.add(new Pad(120,50,false,180));
            pads.add(new Pad(190,50,true,180));
            for(int i=0;i<15;i++) spikes.add(new Spike(50*i,570,0,30));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,600));
            for(int i=1;i<=5;i++) blocks.add(new Block(250,50*i));
            rings.add(new Ring(400,380,1));
            rings.add(new Ring(450,530,1));
            gears.add(new Gear(435,250));
            gears.add(new Gear(500,470));
            gears.add(new Gear(650,300));
            rings.add(new Ring(500,180,1));
            rings.add(new Ring(550,330,1));
            rings.add(new Ring(600,80,1));
            rings.add(new Ring(650,230,1));
            rings.add(new Ring(700,50,1));
            rings.add(new Ring(750,200,1));
            for(int i=3;i<=10;i++) blocks.add(new Block(800,550-i*50));
        }
        else if(x==17){
            texts.add(new Text("Reach the Top",600,450));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,0));
            for(int i=0;i<8;i++) blocks.add(new Block(750,500-50*i));
            spikes.add(new Spike(750,100));
            rings.add(new Ring(500,480));
            rings.add(new Ring(450,280));
            blocks.add(new Block(350,400));
            gears.add(new Gear(300,280));
            blocks.add(new Block(150,430));
            blocks.add(new Block(0,410,25));
            pads.add(new Pad(0,400));
            blocks.add(new Block(700,360,25));
            pads.add(new Pad(700,350));
            gears.add(new Gear(550,180));
            for(int i=0;i<=4;i++) rings.add(new Ring(220+80*i,70));
            portals.add(new Portal(700,15,2,270));
            gears.add(new Gear(-20,200));
        }
        else if(x==18){
            respawnG = -1;
            respawnY = 50;
            texts.add(new Text("Inverted World", 400,330));
            blocks.add(new Block(0,0));
            for(int i=1;i<16;i++) spikes.add(new Spike(50*i,0,180));
            for(int i=0;i<9;i++) blocks.add(new Block(800,50*i));
            for(int i=0;i<3;i++) rings.add(new Ring(235+200*i,30));
            portals.add(new Portal(710,160,3,90));
            for(int i=0;i<13;i++) blocks.add(new Block(50*i,200));
            for(int i=2;i<16;i++) blocks.add(new Block(50*i,400));
            for(int i=1;i<=4;i++) spikes.add(new Spike(150*i,250,180,25));
            for(int i=1;i<=4;i++) spikes.add(new Spike(75+150*i,375,0,25));
            rings.add(new Ring(20,410,2));
            for(int i=0;i<4;i++) pads.add(new Pad(200+150*i,450,true,180));
        }
        else if(x==19){
            respawnG = -1;
            respawnY = 450;
            texts.add(new Text("ZigZagging",390,500));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,580,0,20));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,0,180,20));
            blocks.add(new Block(0,400));
            for(int i=0;i<4;i++) rings.add(new Ring(100+185*i,200,2));
            for(int i=0;i<4;i++) gears.add(new Gear(165+185*i,185,75));
            for(int i=0;i<3;i++) gears.add(new Gear(730,385+60*i));
            for(int i=1;i<8;i++) blocks.add(new Block(193,210+i*50,50,20));
            for(int i=1;i<5;i++) blocks.add(new Block(378,190-i*50,50,20));
            for(int i=1;i<8;i++) blocks.add(new Block(563,210+i*50,50,20));
            for(int i=1;i<5;i++) blocks.add(new Block(748,190-i*50,50,20));
        }
        else if(x==20){
            respawnG = -1;
            respawnX = 100;
            respawnY = 50;
            texts.add(new Text("Find the Way"));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,580,0,20));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,0,180,20));
            for(int i=0;i<3;i++) blocks.add(new Block(100+i*50,25,25));
            rings.add(new Ring(100,420,0));
            rings.add(new Ring(120,150,0));
            rings.add(new Ring(200,300,2));
            rings.add(new Ring(300,220,1));
            rings.add(new Ring(350,450,0));
            rings.add(new Ring(450,400,2));
            rings.add(new Ring(450,500,0));
            rings.add(new Ring(500,180,1));
            rings.add(new Ring(600,350,0));
            rings.add(new Ring(650,250,2));
            rings.add(new Ring(700,480,1));
            gears.add(new Gear(150,450));
            gears.add(new Gear(200,200));
            gears.add(new Gear(300,100));
            gears.add(new Gear(400,300));
            gears.add(new Gear(500,250));
            gears.add(new Gear(650,320));
            gears.add(new Gear(700,400));
        }
        else if(x==21){
            texts.add(new Text("Speed Up!"));
            for(int i=0;i<5;i++) blocks.add(new Block(i*50,550));
            for(int i=5;i<12;i++) spikes.add(new Spike(i*50,550));
            for(int i=12;i<16;i++) blocks.add(new Block(i*50,550));
            portals.add(new Portal(250,400,1));
        }
        else if(x==22){
            respawnS = 12;
            texts.add(new Text("Scattered Field"));
            for(int i=2;i<=12;i++) blocks.add(new Block(800, i*50));
            for(int i=0;i<3;i++) blocks.add(new Block(i*30,550,25,30));
            for(int i=0;i<3;i++) blocks.add(new Block(350+i*30,500,25,30));
            for(int i=0;i<3;i++) blocks.add(new Block(650+i*30,450,25,30));
            for(int i=0;i<3;i++) blocks.add(new Block(350+i*30,350,25,30));
            for(int i=0;i<3;i++) blocks.add(new Block(50+i*30,250,25,30));
            for(int i=0;i<3;i++) blocks.add(new Block(300+i*30,150,25,30));
            for(int i=0;i<9;i++) blocks.add(new Block(550+i*30,100,25,30));
            portals.add(new Portal(650,0,0,180));
        }
        else if(x==23){
            respawnY = 50;
            texts.add(new Text("Accelerate and Decelerate",420,460));
            for(int i=-1;i<=8;i++) blocks.add(new Block(800, i*50));
            for(int i=0;i<14;i++) blocks.add(new Block(i*50,100,25));
            for(int i=2;i<=4;i++) spikes.add(new Spike(i*50,50));
            portals.add(new Portal(270,0,1));
            for(int i=7;i<=11;i++) spikes.add(new Spike(i*50,50));
            portals.add(new Portal(725,80,0,90));
            for(int i=2;i<=11;i++) spikes.add(new Spike(i*50,125,180,20));
            for(int i=0;i<14;i++) blocks.add(new Block(750-i*50, 300,25));
            portals.add(new Portal(10,300,1,90));
            for(int i=1;i<=4;i++) blocks.add(new Block(i*50, 525,25));
            for(int i=0;i<3;i++) gears.add(new Gear(150+i*200,250));
            rings.add(new Ring(270,200));
            rings.add(new Ring(470,200));
            for(int i=0;i<3;i++) blocks.add(new Block(750-i*50, 550,25));
        }
        else if(x==24){
            respawnS = 12;
            texts.add(new Text("Precision in Speed",400,90));
            for(int i=0;i<10;i++) blocks.add(new Block(800, i*50));
            for(int i=0;i<16;i++) blocks.add(new Block(i*50,550));
            for(int i=0;i<16;i++) spikes.add(new Spike(i*50,0,180));
            for(int i=1;i<15;i++) if(i!=6) spikes.add(new Spike(i*50,500));
            pads.add(new Pad(300,540,true));
            rings.add(new Ring(180,380));
            gears.add(new Gear(165,140,220));
            gears.add(new Gear(380,430,150));
            rings.add(new Ring(425,200));
            gears.add(new Gear(520,140,150));
            gears.add(new Gear(520,370,130));
            rings.add(new Ring(720,200,1));
        }
        else if(x==25){
            respawnS = 12;
            texts.add(new Text("Cycles!",250,40));
            blocks.add(new Block(0,550));
            portals.add(new Portal(720,60,0));
            for(int i=0;i<3;i++) portals.add(new Portal(120+i*240,460,2));
            for(int i=0;i<3;i++) portals.add(new Portal(240+i*240,40,3));
            for(int j=0;j<3;j++){
                for(int i=0;i<10;i++) blocks.add(new Block(130+240*j,400-i*50,50,20));
                spikes.add(new Spike(125+240*j,450,180,20,30));
                for(int i=0;i<10;i++) spikes.add(new Spike(95+240*j,415-i*50,270,20));
                for(int i=0;i<9;i++) blocks.add(new Block(250+240*j,180+i*50,50,20));
                spikes.add(new Spike(245+240*j,160,0,20,30));
                for(int i=0;i<10;i++) spikes.add(new Spike(215+240*j,195+i*50,270,20));
            }
        }
        else if(x==26){
            background.stop();
            texts.add(new Text("Congratulations! You win all the levels.",400,250));
            texts.add(new Text("Retry my game or beat it again!",400,450));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
        }
        else if(x==27){
            texts.add(new Text("You win all the levels.",400,250));
            texts.add(new Text("Retry game or beat it!",400,450));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=5;i<8;i++) spikes.add(new Spike(50*i,500));
            for(int i=10;i<13;i++) spikes.add(new Spike(50*i,500));
        }
        else if(x==28){
            texts.add(new Text("You win all level",400,250));
            texts.add(new Text("Retr gam r bet it!",400,450));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            portals.add(new Portal(200,450,1));
            for(int i=0;i<6;i++) spikes.add(new Spike(300+45*i,500));
        }
        else if(x==29){
            respawnS = 12;
            texts.add(new Text("You wi al lvl",400,250));
            texts.add(new Text("Ret ga r et it!",400,450));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=2;i<14;i++) spikes.add(new Spike(50*i,500));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,0,180));
            for(int i=0;i<3;i++) rings.add(new Ring(100+300*i,400,1));
            for(int i=0;i<2;i++) rings.add(new Ring(250+300*i,100,1));
            for(int i=0;i<10;i++) blocks.add(new Block(800,50*i));
        }
        else if(x==30){
            finalTrig = 1;
            respawnS = 12;
            texts.add(new Text("You wi l l",400,250));
            texts.add(new Text("Re g r et it",400,450));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=0;i<16;i++) spikes.add(new Spike(50*i,500));
        }
        else if(x==31){
            finalTrig = 2;
            respawnS = 12;
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=0;i<12;i++) blocks.add(new Block(800,50*i));
        }
        else if(x==32){
            finalTrig = 1;
            respawnS = 12;
            texts.add(new Text("Thanks for playing my game (for real :D)!!!",400,100));
            texts.add(new Text("Your Reward is...",400,200));
            texts.add(new Text("veryCoolObjects.add(new VeryCoolObject(400,500))",400,300,30));
            for(int i=0;i<16;i++) blocks.add(new Block(50*i,550));
            for(int i=0;i<12;i++) blocks.add(new Block(800,50*i));
        }
        player.x=respawnX;
        if(cheatFlg){
            player.y = respawnY;
            player.moveSpeed = respawnS;
            player.gravity = respawnG;
            cheatFlg = false;
        }
    }


    public static void main(String[] args){
        frame = new JFrame("GeometryPlatformer");
        Platformer game = new Platformer();
        frame.add(game);
        frame.setSize(813, 635);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}




