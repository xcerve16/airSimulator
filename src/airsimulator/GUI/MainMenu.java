/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Adam
 */
public class MainMenu extends JFrame{
    
    GridBagConstraints gridBagConstraints;
    GridBagConstraints mainBagConstraints;
            
    JPanel mainPanel;
    
    Platno platno;
    
    JButton spustit_aplikaci;
    JButton napoveda;
    JButton ukoncit;
    
    public MainMenu(){
        super("Úvodní nabídka");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLayout(null);
        
        this.platno = new Platno();
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new GridBagLayout());
        
        this.gridBagConstraints = new GridBagConstraints();
        this.gridBagConstraints.insets = new Insets(5, 0, 5, 0);
        this.gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        this.gridBagConstraints.fill = GridBagConstraints.BOTH;
        
        this.spustit_aplikaci = new JButton("Spustit aplikaci");
        this.spustit_aplikaci.addActionListener(new PlaySimulation());
        this.napoveda = new JButton("Nápověda");
        this.napoveda.addActionListener(new ViewHelp());
        this.ukoncit = new JButton("Ukončit");
        this.ukoncit.addActionListener(new Exit());
        
        this.mainPanel.add(this.spustit_aplikaci, this.gridBagConstraints);
        this.mainPanel.add(this.napoveda, this.gridBagConstraints);
        this.mainPanel.add(this.ukoncit, this.gridBagConstraints);
        
        this.platno.setLocation(0,0);
        this.platno.setSize(500,500);
        
        this.mainPanel.setLocation(300,200);
        this.mainPanel.setSize(150,100);
        
        super.add(this.platno);
        super.add(this.mainPanel, this.mainBagConstraints);
                
        super.setSize(505, 500);
        super.setLocation(dim.width/2-super.getSize().width/2, dim.height/2-super.getSize().height/2);

        super.setResizable(false);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class Platno extends JPanel{

        @Override
        public void paint(Graphics g){
            BufferedImage img;
            try {
                img = ImageIO.read(new File("resources/uvodniMenu.jpg"));
                g.drawImage(img, 0, 0, 505, 505, null);

            } catch (IOException ex) {
                //System.err.println("Cann´t input image read in main menu.");
            }
            spustit_aplikaci.repaint();
            napoveda.repaint();
            ukoncit.repaint();
        }
    }

    private class ViewHelp implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            HelpWindow helpWindow = new HelpWindow();
        }
    }

    private class Exit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object[] opt = {"Ano","Ne"};
            int answer = JOptionPane.showOptionDialog(null,"Opravdu chcete ukončit aplikaci?", 
                    "Ukončení aplikace",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null,opt, opt[0]);
            if( answer == 0){
                System.exit(1);
            }
        }
    }

    private class PlaySimulation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PlayWindow playWindow = new PlayWindow();
            dispose();
        }
    }
    
}
