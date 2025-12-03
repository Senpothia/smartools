/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Michel
 */
public class PortSupplier implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
       
         Connecteur.portName = e.getActionCommand();
    }
    
}
