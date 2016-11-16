/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airsimulator.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author Adam
 */
public class PlayWindow extends JFrame{
    
    public PlayWindow(){
        super("Hrací okno");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        super.setLocation(dim.width/2-super.getSize().width/2, dim.height/2-super.getSize().height/2);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
}
