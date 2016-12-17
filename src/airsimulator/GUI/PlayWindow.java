/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.*;

import airsimulator.model.Airplane;

/**
 *
 * @author Adam
 */
public class PlayWindow extends JFrame implements KeyListener {

    private final String STATE_ONEARTH = "Na zemi";
    private final String STATE_ONAIR = "Ve vzduchu";
    private final String STATE_FALL = "Pád";
    private final String STATE_CRASH = "Havárie";

    private final String PADDING = "   ";

    Timer timer, timer200, timer40;
    DateFormat dateFormat;
    Date realTime;
    Date playTime;
    Calendar cal;
    int second = 0;

    GamePanel gamePanel;
    InfoPanel bottomPanel;
    SidePanel sidePanel;

    JLabel realTimeValue, realTimeName, playTimeValue, playTimeName,
            horizontalSpeedValue, horizontalSpeedName, verticalSpeedValue,
            verticalSpeedName, altitudeValue, altitudeName, gradientValue,
            gradientName, gradientControlValue, gradientControlName,
            powerControlValue, powerControlName, stateValue, stateName;

    JMenuBar menuBar;
    JMenu file, help;
    JMenuItem newGame, continueGame, saveGame, saveGameAs, loadGame, loadGameAs,
            exit, viewHelp;
    JSlider powerControl, gradientControl;

    Airplane airplane;
    int powerKey;

    public PlayWindow() {
        super("Hrací okno");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLayout(new BorderLayout());

        timer = new Timer(1000, new TimeStep());
        timer200 = new Timer(200, new Animator());
        timer40 = new Timer(40, new Animator());
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
        timer40.start();

        this.menuBar = new JMenuBar();
        this.file = new JMenu("Soubor");
        this.newGame = new JMenuItem("Nová hra");
        this.continueGame = new JMenuItem("Pokračovat");
        this.saveGame = new JMenuItem("Uložit");
        this.saveGameAs = new JMenuItem("Uložit jako");
        this.loadGame = new JMenuItem("Načíst");
        this.loadGameAs = new JMenuItem("Načist jako");
        this.exit = new JMenuItem("Ukončit");
        this.help = new JMenu("Nápověda");
        this.viewHelp = new JMenuItem("Zobrazit nápovědu");
        this.viewHelp.addActionListener(new ViewHelp());

        this.file.add(this.newGame);
        this.file.add(this.continueGame);
        this.file.addSeparator();
        this.file.add(this.saveGame);
        this.file.add(this.saveGameAs);
        this.file.addSeparator();
        this.file.add(this.loadGame);
        this.file.add(this.loadGameAs);
        this.file.addSeparator();
        this.file.add(this.exit);
        this.help.add(this.viewHelp);

        this.menuBar.add(this.file);
        this.menuBar.add(this.help);

        this.gamePanel = new GamePanel();
        this.bottomPanel = new InfoPanel();
        this.sidePanel = new SidePanel();

        this.gamePanel.setBackground(Color.RED);
        this.bottomPanel.setBackground(Color.GRAY);
        //this.sidePanel.setBackground(Color.LIGHT_GRAY);

        super.setJMenuBar(this.menuBar);
        super.add(this.gamePanel, BorderLayout.CENTER);
        super.add(this.bottomPanel, BorderLayout.SOUTH);
        super.add(this.sidePanel, BorderLayout.EAST);

        super.setSize(700, 550);
        super.setLocation(dim.width / 2 - super.getSize().width / 2, dim.height / 2 - super.getSize().height / 2);

        super.setResizable(false);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.airplane = new Airplane();
        powerControl.addChangeListener(bottomPanel);
        gradientControl.addChangeListener(bottomPanel);
    }

    public int getHorizontalSpeed() {
        return this.airplane.getHorizontalSpeed();
    }

    public int getVerticalSpeed() {
        return this.airplane.getVerticalSpeed();
    }

    private class ViewHelp implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            HelpWindow helpWindow = new HelpWindow();
        }

    }

    private class TimeStep implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            second++;
            cal.setTime(playTime);
            cal.set(Calendar.SECOND, second);
            if (second == 60) {
                second = 0;
            }
            playTime = cal.getTime();

            realTime = new Date();

            realTimeValue.setText("  " + dateFormat.format(realTime));
            playTimeValue.setText("  " + dateFormat.format(playTime));
            realTimeValue.repaint();
            playTimeValue.repaint();
        }
    }

    private class Animator implements ActionListener {

        private int divider;

        @Override
        public void actionPerformed(ActionEvent e) {
            ++divider;

            if (divider % 5 == 0) {
                int value;
                if (powerKey == KeyEvent.VK_UP) {
                    value = powerControl.getValue();
                    value = (value + 10) / 10 * 10;
                    if (value > 100) {
                        value = 100;
                    }
                    powerControl.setValue(value);
                } else if (powerKey == KeyEvent.VK_DOWN) {
                    value = powerControl.getValue();
                    if (value == 0) {
                        value = -1;
                    } else {
                        value = (value - 10) / 10 * 10;
                        if (value < -1) {
                            value = -1;
                        }
                    }
                    powerControl.setValue(value);
                }

                airplane.simulationStep();

                // překleslení hodnot - každých 200 ms
                horizontalSpeedValue.setText(PADDING + airplane.getHorizontalSpeed() + " m/s");
                verticalSpeedValue.setText(PADDING + airplane.getVerticalSpeed() + " m/s");
                altitudeValue.setText(PADDING + airplane.getAltitude() + " m");
                gradientValue.setText(PADDING + airplane.getGradient() + "°");
                gradientControlValue.setText(PADDING + airplane.getControls().getGradient());
                powerControlValue.setText(PADDING + airplane.getControls().getPower());

            }

            // překreslení gamePanel - každých 40 ms
            gamePanel.repaint();
            // aktualizuje polozku stav;
            updateState();
            // aktualizuji se frantovi raficky
            bottomPanel.repaint();

        }

    }

    private void updateState() {
        if (this.airplane.getAltitude() == 0) {
            if (isCrash()) {
                stateValue.setText(STATE_CRASH);
            } else {
                stateValue.setText(STATE_ONEARTH);
            }
        } else if (this.airplane.getVerticalSpeed() < -200) {
            stateValue.setText(STATE_FALL);
        } else {
            stateValue.setText(STATE_ONAIR);
        }
    }

    private boolean isCrash() {
        return false;
    }

    private class SidePanel extends JPanel {

        public SidePanel() {
            super.setPreferredSize(new Dimension(200, 500));
            GridLayout gridLayout = new GridLayout(9, 2, 10, 10);
            super.setLayout(gridLayout);

            realTimeName = new JLabel(PADDING + "Reálný čas");
            realTimeValue = new JLabel();
            playTimeName = new JLabel(PADDING + "Herní čas");
            playTimeValue = new JLabel();
            playTimeName.setForeground(Color.WHITE);
            playTimeValue.setForeground(Color.WHITE);
            horizontalSpeedName = new JLabel(PADDING + "Rychlost letu");
            horizontalSpeedValue = new JLabel();
            verticalSpeedName = new JLabel(PADDING + "Rychlost stoupání");
            verticalSpeedValue = new JLabel();
            verticalSpeedName.setForeground(Color.WHITE);
            verticalSpeedValue.setForeground(Color.WHITE);
            altitudeName = new JLabel(PADDING + "Výška letu");
            altitudeValue = new JLabel();
            gradientName = new JLabel(PADDING + "Skon");
            gradientValue = new JLabel();
            gradientName.setForeground(Color.WHITE);
            gradientValue.setForeground(Color.WHITE);
            gradientControlName = new JLabel(PADDING + "Ovladač sklonu");
            gradientControlValue = new JLabel();  
            powerControlName = new JLabel(PADDING + "Ovladač tahu");
            powerControlValue = new JLabel();
            powerControlName.setForeground(Color.WHITE);
            powerControlValue.setForeground(Color.WHITE);
            stateName = new JLabel(PADDING + "Stav");
            stateValue = new JLabel();

            super.add(realTimeName);
            super.add(realTimeValue);
            super.add(playTimeName);
            super.add(playTimeValue);
            super.add(horizontalSpeedName);
            super.add(horizontalSpeedValue);
            super.add(verticalSpeedName);
            super.add(verticalSpeedValue);
            super.add(altitudeName);
            super.add(altitudeValue);
            super.add(gradientName);
            super.add(gradientValue);
            super.add(gradientControlName);
            super.add(gradientControlValue);
            super.add(powerControlName);
            super.add(powerControlValue);
            super.add(stateName);
            super.add(stateValue);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Graphics componentGraphics = getComponentGraphics(g);
            Graphics co = componentGraphics.create();

            g.setColor(Color.GRAY);
            g.fillRect(playTimeName.getLocation().x, playTimeName.getLocation().y, super.getSize().getSize().width, playTimeName.getHeight());
            g.fillRect(verticalSpeedName.getLocation().x, verticalSpeedName.getLocation().y, super.getSize().getSize().width, verticalSpeedName.getHeight());
            g.fillRect(gradientName.getLocation().x, gradientName.getLocation().y, super.getSize().getSize().width, gradientName.getHeight());
            g.fillRect(powerControlName.getLocation().x, powerControlName.getLocation().y, super.getSize().getSize().width, powerControlName.getHeight());

            co.create();
            super.paintChildren(co);
        }
    }

    private class GamePanel extends JPanel {

        private BufferedImage img, plane;
        int backgroundOffset;

        public GamePanel() {
            super.setPreferredSize(new Dimension(500, 380));

            try {
                img = ImageIO.read(new File("resources/background.jpg"));
                plane = ImageIO.read(new File("resources/airplane.png"));
            } catch (IOException ex) {
                Logger.getLogger(PlayWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            backgroundOffset += airplane.getHorizontalSpeed() / 5;
            if (backgroundOffset >= 3222) {
                backgroundOffset -= 3222;
            }
            Image subimage = img.getSubimage(backgroundOffset, 0, 500, 380);
            g.drawImage(subimage, 0, 0, 500, 380, null);

            int altitude = (2700 - airplane.getAltitude()) / 10;
            g.drawImage(plane, 30, altitude, 170, 70, null);
        }
    }

    private class InfoPanel extends JPanel implements ChangeListener {

        public InfoPanel() {
            super.setPreferredSize(new Dimension(500, 170));
            super.setBackground(Color.WHITE);
            super.setLayout(null);

            powerControl = new JSlider(SwingConstants.VERTICAL, -1, 100, 0);
            super.add(powerControl);
            powerControl.setBounds(180, 10, 50, 150);
            powerControl.setFocusable(false);

            gradientControl = new JSlider(SwingConstants.HORIZONTAL, -1, 1, 0);
            super.add(gradientControl);
            gradientControl.setBounds(240, 60, 70, 50);
            gradientControl.setFocusable(false);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            JSlider l = (JSlider) e.getSource();
            if (!l.getValueIsAdjusting()) {
                airplane.getControls().setPower(powerControl.getValue());
                airplane.getControls().setGradient(gradientControl.getValue());
            }

        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, 700, 550);
            try {
                int horizontalSpeed = getHorizontalSpeed() * 180;
                int verticalSpeed = getVerticalSpeed() * 180;
                horizontalSpeed /= 100;
                verticalSpeed /= 100;
                drawAirSpeedValue(horizontalSpeed, g);
                drawSomeSpeedValue(verticalSpeed, g);
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

            try {
                BufferedImage img = ImageIO.read(new File("resources/logo.jpg"));
                g.setColor(Color.WHITE);
                g.fillRect(495, 5, 190, 160);
                g.drawImage(img, 500, 10, 180, 150, null);
            } catch (IOException ex) {
            }
        }

        private void drawAirSpeedValue(int i, Graphics g) throws IOException {
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(img, 10, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);

            int x = 80;
            int y = 40;
            int w = 7;
            int h = 50;

            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x + w, rect1.y + h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }

        private void drawSomeSpeedValue(int i, Graphics g) throws IOException {
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(img, 330, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);

            int x = 400;
            int y = 40;
            int w = 7;
            int h = 50;

            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x + w, rect1.y + h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }

        private void drawAnySpeedValue(int i, Graphics g) throws IOException {
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(img, 170, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);

            int x = 240;
            int y = 40;
            int w = 7;
            int h = 50;

            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x + w, rect1.y + h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }

        private void drawControls(int i, Graphics g) throws IOException {
            BufferedImage img = ImageIO.read(new File("resources/airspeed.png"));
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(img, 170, 10, 150, 150, null);
            g2d.setColor(Color.WHITE);

            int x = 240;
            int y = 40;
            int w = 7;
            int h = 50;

            Rectangle rect1 = new Rectangle(x, y, w, h);
            g2d.rotate(Math.toRadians(i), rect1.x + w, rect1.y + h);
            g2d.setColor(Color.RED);
            g2d.fill(rect1);
            g2d.setColor(Color.WHITE);
            g2d.draw(rect1);
            g2d.dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT) {
            gradientControl.setValue(-1);
        } else if (code == KeyEvent.VK_RIGHT) {
            gradientControl.setValue(1);
        }

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
            powerKey = code;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT) {
            gradientControl.setValue(0);
        }

        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
            powerKey = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
