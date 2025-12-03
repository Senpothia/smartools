/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.awt.Color;
import javax.swing.JLabel;

public class Voyant {

    private JLabel indicateur;
    private int statut;  // ; 0=masqué; 1=attente; 2=processing; 3=ok; 4=nok
    private int ordre = 0;

    public Voyant(JLabel indicateur, int statut, int ordre) {

        this.indicateur = indicateur;
        this.statut = statut;
        this.ordre = 0;
    }

    public JLabel getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(JLabel indicateur) {
        this.indicateur = indicateur;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public void masquer(boolean mono) {

        if (!mono) {
            indicateur.setVisible(true);
            indicateur.setBackground(new Color(50, 131, 168));
            indicateur.setForeground(new Color(50, 131, 168));
            indicateur.setOpaque(true);
            statut = 0;
        } else {

            indicateur.setVisible(true);
            indicateur.setBackground(new Color(140, 3, 252));
            indicateur.setForeground(new Color(140, 3, 252));
            indicateur.setOpaque(true);
            statut = 0;
        }
    }

    public void ok(Boolean ok) {

        indicateur.setVisible(true);
        if (!ok) {

            indicateur.setBackground(Color.RED);
            indicateur.setForeground(Color.RED);
            statut = 3;
        } else {

            indicateur.setBackground(Color.GREEN);
            indicateur.setForeground(Color.GREEN);
            statut = 4;
        }

        indicateur.setOpaque(true);
    }

    public void processing(Boolean processing) {

        indicateur.setVisible(true);
        if (!processing) {

            indicateur.setBackground(Color.GRAY);
            indicateur.setForeground(Color.GRAY);
            //indicateur.setText("  " + this.ordre);
            statut = 1;

        } else {

            indicateur.setBackground(Color.YELLOW);
            indicateur.setForeground(Color.YELLOW);
            statut = 2;
        }
        indicateur.setOpaque(true);
    }

    void supprimer() {

        indicateur.setVisible(false);
    }

}
