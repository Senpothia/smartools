/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Michel
 */
public class Initializer {

    public Initialisation getInit() throws FileNotFoundException, IOException {

        Properties progProperpies = new Properties();
        FileReader reader = new FileReader(".\\params.properties");
        progProperpies.load(reader);

        String programmerDirectory = progProperpies.getProperty("programmerDirectory");
        String binaryLocations = progProperpies.getProperty("binaryLocations");
        String varEnv = progProperpies.getProperty("varEnv");
        String productNames = progProperpies.getProperty("productNames");
        String voies = progProperpies.getProperty("voies");
        String commutateur = progProperpies.getProperty("commutateur");
        String device1 = progProperpies.getProperty("device1");
        String device2 = progProperpies.getProperty("device2");
        String programmer = progProperpies.getProperty("programmer");
        String matrice = progProperpies.getProperty("matrice");
        String autoConnection = progProperpies.getProperty("autoConnexion");
        String item = progProperpies.getProperty("item");
        String cycles = progProperpies.getProperty("cycles");
        String timing = progProperpies.getProperty("timing");
        String single = progProperpies.getProperty("single");
        String adresses1 = progProperpies.getProperty("adresses1");
        String adresses2 = progProperpies.getProperty("adresses2");
       
        Initialisation init = new Initialisation(programmerDirectory, varEnv, binaryLocations, binaryLocations, item, item, productNames, voies, commutateur, device1, device2, adresses1, adresses2, programmer, matrice, autoConnection, item, cycles, timing, single);

        return init;
    }

    public void update(String key, String value) {

        try {

            Properties progProperpies = new Properties();
            FileInputStream configStream = new FileInputStream(".\\params.properties");
            progProperpies.load(configStream);
            configStream.close();

            //modifies existing or adds new property
            progProperpies.setProperty(key, value);

            FileOutputStream output = new FileOutputStream(".\\params.properties");
            progProperpies.store(output, "Smartloxx Programmer - Properties");
            output.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
