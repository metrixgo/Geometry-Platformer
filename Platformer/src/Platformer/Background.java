package Platformer;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Background {
    public Clip bgm, finalBgm;
    public Image image, imageD, imageW;
    public int stay;

    public Background() {
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/Platformer/Sounds/bgm.wav"));
            bgm = AudioSystem.getClip();
            bgm.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/Platformer/Sounds/finalBgm.wav"));
            finalBgm = AudioSystem.getClip();
            finalBgm.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        image = new ImageIcon(getClass().getResource("/Platformer/Pictures/background.png")).getImage();
        imageD = new ImageIcon(getClass().getResource("/Platformer/Pictures/background_dead.png")).getImage();
    }

    public void show(Graphics g, boolean d) {
        if(Platformer.ticks > 2340) g.drawImage(image, 0, 0, null);
        else if(Platformer.ticks > 1250) g.drawImage(imageD, 0, 0, null);
        else if(d || stay>0 || Platformer.ticks % 100 == 99){
            stay++;
            if(stay <= 5) g.drawImage(imageD, 0, 0, null);
            else stay = 0;
        }
        if(stay == 0 && Platformer.ticks <= 1250) g.drawImage(image, 0, 0, null);
    }

    public void play() {bgm.loop(Clip.LOOP_CONTINUOUSLY);}
    public void finalPlay() {finalBgm.start();}
    public void stop() {bgm.stop();}
}
