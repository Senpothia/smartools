/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.util.ArrayList;
import javax.swing.JLabel;

public class Ligne {

    private ArrayList<Voyant> voyants;
    private boolean visible;
    private int numero;

    public Ligne(ArrayList<Voyant> voyants, Boolean visible, int numero) {

        this.voyants = voyants;
        this.visible = visible;
        this.numero = numero;
    }

    public Ligne(ArrayList<JLabel> indicateurs, boolean visible, int numero) {

        ArrayList<Voyant> listeVoyants = new ArrayList<Voyant>();
        for (JLabel i : indicateurs) {

            Voyant v = new Voyant(i, 0, 0);
            listeVoyants.add(v);
        }
        this.voyants = listeVoyants;
        this.visible = visible;
        this.numero = numero;

    }

    Ligne() {

    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    

    public void supprimer() {

        for (Voyant v : voyants) {

            v.supprimer();
        }

    }

    public ArrayList<Voyant> getVoyants() {
        return voyants;
    }

    public void setVoyants(ArrayList<Voyant> voyants) {
        this.voyants = voyants;
    }

    public void initialiser(int colonnes, boolean mono) {

        int position = (12 - colonnes) / 2;
        int ordre = 1;
        for (Voyant v : voyants) {

            if (voyants.indexOf(v) > position && voyants.indexOf(v) < position + colonnes + 1) {

                v.setOrdre((this.numero - 1) * colonnes + ordre);
                v.processing(false);
                ordre++;

            } else {

                v.masquer(mono);
                v.setOrdre(0);
            }

        }

    }

    public void afficher() {

        for (Voyant v : voyants) {
            v.processing(false);

        }

    }
}
