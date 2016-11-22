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
import java.awt.GridLayout;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Adam
 */
public class PlayWindow extends JFrame {

    GamePanel gamePanel;
    InfoPanel infoPanel;
    SidePanel sidePanel;

    JLabel valueA, valueB, valueC, valueD, valueE, valueF;

    JMenuBar menuBar;
    JMenu file;
    JMenuItem newGame, continueGame, saveGame, saveGameAs, loadGame, loadGameAs,
            exit;
   

    public PlayWindow() {
        super("Hrací okno");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLayout(new BorderLayout());

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
        this.infoPanel.setBackground(Color.BLUE);
        this.sidePanel.setBackground(Color.CYAN);

        super.setJMenuBar(this.menuBar);
        super.add(this.gamePanel, BorderLayout.CENTER);
        super.add(this.infoPanel, BorderLayout.SOUTH);
        super.add(this.sidePanel, BorderLayout.EAST);

        super.setSize(700, 500);
        super.setLocation(dim.width / 2 - super.getSize().width / 2, dim.height / 2 - super.getSize().height / 2);

        super.setResizable(false);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class SidePanel extends JPanel {

        public SidePanel() {
            super.setPreferredSize(new Dimension(200, 500));
            super.setLayout(new GridLayout(6, 2, 10, 10));

            valueA = new JLabel("  A");
            valueB = new JLabel("  B");
            valueC = new JLabel("  C");
            valueD = new JLabel("  D");
            valueE = new JLabel("  E");
            valueF = new JLabel("  F");

            super.add(new JLabel("Čislo A:", SwingConstants.CENTER));
            super.add(valueA);
            super.add(new JLabel("Čislo B:", SwingConstants.CENTER));
            super.add(valueB);
            super.add(new JLabel("Čislo C:", SwingConstants.CENTER));
            super.add(valueC);
            super.add(new JLabel("Čislo D:", SwingConstants.CENTER));
            super.add(valueD);
            super.add(new JLabel("Čislo E:", SwingConstants.CENTER));
            super.add(valueE);
            super.add(new JLabel(" Čislo F:", SwingConstants.CENTER));
            super.add(valueF);

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
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 500, 380);
        }
    }

    private class InfoPanel extends JPanel {

        public InfoPanel() {
            super.setPreferredSize(new Dimension(500, 120));
            super.setBackground(Color.WHITE);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.BLACK);
            g.fillOval(50, 10, 100, 100);
            g.setColor(Color.WHITE);
            g.fillRect(225, 10, 50, 100);
            g.setColor(Color.GREEN);
            g.fillOval(350, 10, 100, 100);
            g.setColor(Color.WHITE);
            g.fillRect(505, 10, 180, 100);
        }
    }
}
