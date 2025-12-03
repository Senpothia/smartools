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
        String adresse1Device1 = progProperpies.getProperty("adresse1Device1");
        String adresse2Device1 = progProperpies.getProperty("adresse2Device1");
        String adresse3Device1 = progProperpies.getProperty("adresse3Device1");
        String adresse4Device1 = progProperpies.getProperty("adresse4Device1");
        String adresse5Device1 = progProperpies.getProperty("adresse5Device1");
        String adresse6Device1 = progProperpies.getProperty("adresse6Device1");
        String adresse7Device1 = progProperpies.getProperty("adresse7Device1");
        String adresse1Device2 = progProperpies.getProperty("adresse1Device1");
        String adresse2Device2 = progProperpies.getProperty("adresse2Device2");
        String adresse3Device2 = progProperpies.getProperty("adresse3Device2");
        String adresse4Device2 = progProperpies.getProperty("adresse4Device2");
        String adresse5Device2 = progProperpies.getProperty("adresse5Device2");
        String adresse6Device2 = progProperpies.getProperty("adresse6Device2");
        String adresse7Device2 = progProperpies.getProperty("adresse7Device2");

        Initialisation init = new Initialisation(programmerDirectory, varEnv, binaryLocations, binaryLocations, productNames, voies, commutateur, device1, device2, adresse1Device1, adresse2Device1, adresse3Device1, adresse4Device1, adresse5Device1, adresse6Device1, adresse7Device1, adresse1Device2, adresse2Device2, adresse3Device2, adresse4Device2, adresse5Device2, adresse6Device2, adresse7Device2, programmer, matrice, autoConnection, item, cycles, timing, single);

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
