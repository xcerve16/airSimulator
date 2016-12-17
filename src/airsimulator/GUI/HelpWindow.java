/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

/**
 *
 * @author Adam
 */
public class HelpWindow extends JFrame {

    JTextArea text;
    JLabel nadpis;
    JButton storno;

    public HelpWindow() {
        super("Nápověda");
        super.setLayout(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        this.text = new JTextArea();
        this.storno = new JButton("Zavřít");
        this.storno.addActionListener(new Close());

        text.setOpaque(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(false);

        this.nadpis = new JLabel("<html><h2>Nápověda k leteckému simulátoru</h2></html>");

        try {

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader("napoveda.html"));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");

            }
            text.setText(sb.toString());

        } catch (IOException ex) {

            
            this.text.setText("Momentálně není nápověda k dizpozici\n");
        }

        this.nadpis.setSize(500, 50);
        this.nadpis.setLocation(40, 20);

        this.text.setSize(500, 400);
        this.text.setLocation(40, 60);

        this.storno.setSize(100, 30);
        this.storno.setLocation(470, 500);

        super.add(this.nadpis);
        super.add(this.text);
        super.add(this.storno);

        super.setSize(600, 600);
        super.setLocation(dim.width / 2 - super.getSize().width / 2, dim.height / 2 - super.getSize().height / 2);

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
