/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Adam
 */
public class HelpWindow extends JFrame{
    
    JLabel text;
    JLabel nadpis;
    JButton storno;
    
    public HelpWindow(){
        super("Nápověda");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLayout(null);
        
        this.nadpis = new JLabel("<html><h2>Nápověda</h2></html>");
        this.text = new JLabel();
        this.storno = new JButton("Zavřít");
        this.storno.addActionListener(new Close());
        
        try {
            BufferedReader br = new BufferedReader(new FileReader("napoveda.html"));
            String originalText, line;
            while((line = br.readLine()) != null){
                originalText = this.text.getText();
                this.text.setText(originalText + line+ "<br>");
            }
        } catch (IOException ex ) {
           this.text.setText("Momentálně není nápověda k dizpozici\n");
        }
        
        this.nadpis.setSize(500,50);
        this.nadpis.setLocation(10,10);
        
        this.text.setSize(450,300);
        this.text.setLocation(10,0);
        
        this.storno.setSize(100, 30);
        this.storno.setLocation(350,420);
        
        super.add(this.storno);
        super.add(this.nadpis);
        super.add(this.text);
         
        super.setSize(500, 500);
        super.setLocation(dim.width/2-super.getSize().width/2, dim.height/2-super.getSize().height/2);
        
        super.setResizable(false);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class Close implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    } 
}
