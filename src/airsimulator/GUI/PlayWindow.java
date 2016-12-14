/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import airsimulator.model.Airplane;

/**
 *
 * @author Adam
 */
public class PlayWindow extends JFrame {
    
    Timer timer, timer200;
    DateFormat dateFormat;
    Date realTime;
    Date playTime;
    Calendar cal;
    int second = 0;

    GamePanel gamePanel;
    InfoPanel infoPanel;
    SidePanel sidePanel;

    JLabel valueA, valueB, valueC, valueD, valueE, valueF, valueG, valueH, 
            valueI;

    JMenuBar menuBar;
    JMenu file;
    JMenuItem newGame, continueGame, saveGame, saveGameAs, loadGame, loadGameAs,
            exit;
    
    Airplane airplane;
   

    public PlayWindow() {
        super("Hrací okno");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLayout(new BorderLayout());

        timer = new Timer(1000, new TimeStep());
        timer200 = new Timer(200, new Animator());
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        realTime = new Date();
        playTime = new Date();
        cal = Calendar.getInstance();  
        cal.setTime(playTime);  
        cal.set(Calendar.HOUR_OF_DAY, 0);  
        cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, second);   
        playTime = cal.getTime();
        timer.start();
        timer200.start();

        this.menuBar = new JMenuBar();
        this.file = new JMenu("Soubor");
        this.newGame = new JMenuItem("Nová hra");
        this.continueGame = new JMenuItem("Pokračovat");
        this.saveGame = new JMenuItem("Uložit");
        this.saveGameAs = new JMenuItem("Uložit jako");
        this.loadGame = new JMenuItem("Načíst");
        this.loadGameAs = new JMenuItem("Načist jako");
        this.exit = new JMenuItem("Ukončit");

        this.file.add(this.newGame );
        this.file.add(this.continueGame);
        this.file.addSeparator();
        this.file.add(this.saveGame);
        this.file.add(this.saveGameAs);
        this.file.addSeparator();
        this.file.add(this.loadGame);
        this.file.add(this.loadGameAs);
        this.file.addSeparator();
        this.file.add(this.exit);

        this.menuBar.add(this.file);

        this.gamePanel = new GamePanel();
        this.infoPanel = new InfoPanel();
        this.sidePanel = new SidePanel();

        this.gamePanel.setBackground(Color.RED);
        this.infoPanel.setBackground(Color.GRAY);
        this.sidePanel.setBackground(Color.LIGHT_GRAY);

        super.setJMenuBar(this.menuBar);
        super.add(this.gamePanel, BorderLayout.CENTER);
        super.add(this.infoPanel, BorderLayout.SOUTH);
        super.add(this.sidePanel, BorderLayout.EAST);

        super.setSize(700, 550);
        super.setLocation(dim.width / 2 - super.getSize().width / 2, dim.height / 2 - super.getSize().height / 2);

        super.setResizable(false);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        airplane = new Airplane();
    }
    
    public int getAirSpeedValue(){
        return 40;
    }

    private class TimeStep implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            second ++;
            cal.setTime(playTime);  
            cal.set(Calendar.SECOND, second);  
            playTime = cal.getTime();
            
            realTime = new Date();
            
            valueA.setText("  "+dateFormat.format(realTime));
            valueB.setText("  "+dateFormat.format(playTime));
            valueA.repaint();
            valueB.repaint();
        }
    }
    
    private class Animator implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            airplane.simulationStep();
            
            // překleslení hodnot
            valueC.setText("" + airplane.getVerticalSpeed());
            valueD.setText("" + airplane.getHorizontalSpeed());
            valueE.setText("" + airplane.getGradient());
            valueF.setText("" + airplane.getAltitude());
            valueG.setText("" + airplane.getControls().getGradient());
            valueH.setText("" + airplane.getControls().getPower());
            
        }
    }

    private class SidePanel extends JPanel {

        public SidePanel() {
            super.setPreferredSize(new Dimension(200, 500));
            super.setLayout(new GridLayout(9, 2, 10, 10));
            valueA = new JLabel("  ");
            valueB = new JLabel("  ");
            valueC = new JLabel("  135.30");
            valueD = new JLabel("  110.50");
            valueE = new JLabel("  13.90");
            valueF = new JLabel("  14.6");
            valueG = new JLabel("  619");
            valueH = new JLabel("  0600");
            valueI = new JLabel("  0000");

            super.add(new JLabel("Reálný čas", SwingConstants.CENTER));
            super.add(valueA);
            super.add(new JLabel("Herní čas", SwingConstants.CENTER));
            super.add(valueB);
            super.add(new JLabel("Rychlost letu", SwingConstants.CENTER));
            super.add(valueC);
            super.add(new JLabel("Rychlost stoupání", SwingConstants.CENTER));
            super.add(valueD);
            super.add(new JLabel("Výška letu", SwingConstants.CENTER));
            super.add(valueE);
            super.add(new JLabel("Náklon", SwingConstants.CENTER));
            super.add(valueF);
            super.add(new JLabel("Nastavený sklon", SwingConstants.CENTER));
            super.add(valueG);
            super.add(new JLabel("Tah letadla", SwingConstants.CENTER));
            super.add(valueH);
            super.add(new JLabel("-", SwingConstants.CENTER));
            super.add(valueI);


            
            for (Component component : super.getComponents()) {
                if (component instanceof JLabel) {
                    JLabel item = (JLabel) component;
                    item.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                }
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }
    }

    private class GamePanel extends JPanel {

        public GamePanel() {
            super.setPreferredSize(new Dimension(500, 380));
        }
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            BufferedImage img; 
            try {
                img = ImageIO.read(new File("resources/background.jpg"));
                Image subimage = img.getSubimage(0, 0, 500, 380); 
                g.drawImage(subimage, 0, 0, 500, 380, null);
                
                Image airplane = ImageIO.read(new File("resources/airplane.png"));
                g.drawImage(airplane, 50, 120, 170, 70, null);
            } catch (IOException ex) {
                Logger.getLogger(PlayWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class InfoPanel extends JPanel {

        public InfoPanel() {
            super.setPreferredSize(new Dimension(500, 170));
            super.setBackground(Color.WHITE);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.BLACK);
            g.drawRect(0,0,700,550);
            try {
                int x = getAirSpeedValue()*180;
                x /= 100;
                drawAirSpeedValue(x,g);
                drawAnySpeedValue(x,g);
                drawSomeSpeedValue(x,g);
            } catch (IOException ex) {
            }

            try {    
                BufferedImage img = ImageIO.read(new File("resources/logo.jpg"));
                g.setColor(Color.WHITE);
                g.fillRect(495, 5, 190, 160);
                g.drawImage(img, 500, 10, 180, 150, null);
            } catch (IOException ex) {
            }
        }
        
        private void drawAirSpeedValue(int i, Graphics g) throws IOException{
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D)g.create();
            g2d.drawImage(img, 10, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);
           
            int x = 80;
            int y = 40;
            int w = 7;
            int h = 50;
            
            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x+w, rect1.y+h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }
        
         private void drawSomeSpeedValue(int i, Graphics g) throws IOException{
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D)g.create();
            g2d.drawImage(img, 330, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);
           
            int x = 400;
            int y = 40;
            int w = 7;
            int h = 50;
            
            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x+w, rect1.y+h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }
        
         private void drawAnySpeedValue(int i, Graphics g) throws IOException{
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D)g.create();
            g2d.drawImage(img, 170, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);
           
            int x = 240;
            int y = 40;
            int w = 7;
            int h = 50;
            
            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x+w, rect1.y+h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }
    }
}
