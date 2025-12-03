/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michel
 */
public class ProgController {

    public int createLogFolder(String path) {

        try {
            Path location = Paths.get(path);

            //java.nio.file.Files;
            Files.createDirectories(location);
            return 1;

        } catch (IOException ex) {
            Logger.getLogger(ProgController.class.getName()).log(Level.SEVERE, null, ex);
            return 0;

        }

    }

    public int find(String logfile, String[] erreurs, String[] requis) throws IOException {

        int j = 0;
        int codeControl = 0;
        while (j < 2) {

            System.out.println("Test erreur/preréquis dans le log");
            int analyse = isFileAvailable4(logfile);

            if (analyse == 0) {

                return -33;
            }

            if (analyse == 8) {

                return -88;
            }

            try {

                //System.out.println("le fichier est disponible");
                // Création d'un fileReader pour lire le fichier
                FileReader fileReader = new FileReader(logfile);

                // Création d'un bufferedReader qui utilise le fileReader
                BufferedReader reader = new BufferedReader(fileReader);
                String line = reader.readLine();

                while (line != null) {

                    // lecture de la prochaine ligne
                    line = reader.readLine();
                    //System.out.println(line);

                    if (erreurs != null && line != null) {

                        for (int i = 0; i < erreurs.length; i++) {

                            if (line.contains(erreurs[i])) {

                                codeControl = -1 * (i + 1);
                                break;
                            }
                        }

                    }

                    if (requis != null && line != null) {

                        for (int i = 0; i < requis.length; i++) {

                            if (line.contains(requis[i])) {

                                codeControl = 1;
                            }
                        }

                    }

                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                //System.out.println("Fichier en cours d'écriture");
            }

            if (codeControl == 0) {

                j++;
            } else {

                return codeControl;
            }

        }
        return codeControl;
    }

    private void isFileAvailable(String logFile) {

        double size = 0;
        while (size < 0.7) {

            File file = new File(logFile);
            size = (double) file.length() / 1024;
            System.out.println(getFileSizeKiloBytes(file));
        }
    }

    private int isFileAvailable4(String logFile) {

        double size0 = 0;
        double size1 = 0;
        Boolean end = false;
        int counter0 = 0;
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        int counter5 = 0;

        // Sans erreur de programmation
        // ICD4
        /*
        double d1 = 0.98335d;
        double d2 = 0.98340d;
         */
        // ICD5
        double d1 = 0.9677734d;
        //double d2 = 0.9677740d;
        double d2 = 1.0035;

        // Erreur après déconnexion 1
        double d3 = 0.618162d;
        double d4 = 0.618170d;

        // Erreur après déconnexion 2
        double d5 = 0.583007d;
        double d6 = 0.583008d;

        // Reprise connexion après rupture
        //1.0185546875 
        double d7 = 1.01850d;
        double d8 = 1.01870d;

        // Surcharge
        // ICD5
        // 0.0546875
        double d9 = 0.0546860d;
        double d10 = 0.064550d;

        // Absence carte
        double d11 = 0.538710d;
        double d12 = 0.5510000;

        // Programmation ok après détection d'une erreur
        double d13 = 0.90120d;
        double d14 = 0.92120;

        int lim = 200000;

        while (!end) {

            File file = new File(logFile);
            size1 = (double) file.length();
            System.out.println(size1 / 1024 + "  kb");
            //Constants.tempo(50);
            size0 = size1;

            if (size0 == size1 && size1 != 0) {
                //System.out.println("test gr1");
                counter0++;
                if (counter0 > lim) {
                    System.out.println("Sortie 1");
                    end = true;

                }
            }

            if (Double.compare(size0 / 1024, d1) == 1 && Double.compare(size0 / 1024, d2) == -1) {
                //System.out.println("test gr2");
                counter1++;
                if (counter1 > 1000) {
                    System.out.println("Sortie 2");
                    end = true;

                }
            }

            if (Double.compare(size0 / 1024, d3) == 1 && Double.compare(size0 / 1024, d4) == -1) {
                //System.out.println("test gr3");
                counter2++;
                if (counter2 > 10000) {
                    System.out.println("Sortie 3");
                    end = true;

                }
            }

            if (Double.compare(size0 / 1024, d5) == 1 && Double.compare(size0 / 1024, d6) == -1) {
                //System.out.println("test gr4");
                counter3++;
                if (counter3 > 10000) {
                    System.out.println("Sortie 4");
                    end = true;

                }
            }

            if (Double.compare(size0 / 1024, d7) == 1 && Double.compare(size0 / 1024, d8) == -1) {
                //System.out.println("test gr5");
                counter4++;
                if (counter4 > 10000) {
                    System.out.println("Sortie 5");
                    end = true;

                }
            }

            if (Double.compare(size0 / 1024, d9) == 1 && Double.compare(size0 / 1024, d10) == -1) {
                //System.out.println("test gr6");
                counter5++;
                if (counter5 > 200000) {  // 100000, 200000

                    System.out.println("Sortie 6");
                    end = true;
                    return 8;
                }

            }

            if (Double.compare(size0 / 1024, d11) == 1 && Double.compare(size0 / 1024, d12) == -1) {
                //System.out.println("test gr7");
                counter3++;
                if (counter3 > 10000) {
                    System.out.println("Sortie 41");
                    end = true;

                }
            }

            if (Double.compare(size0 / 1024, d13) == 1 && Double.compare(size0 / 1024, d14) == -1) {
                //System.out.println("test gr8");
                counter1++;
                if (counter1 > 1000) {
                    System.out.println("Sortie 21");
                    end = true;

                }
            }

        }

        return 1;
    }

    private static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  kb";
    }

    private Boolean isFileAvailable2(String logFile) {

        File processCheck = new File(logFile);
        Boolean canBeDeleted = processCheck.canWrite();
        System.out.println("isAvailable2: " + canBeDeleted);
        return canBeDeleted;

    }

    public boolean isFileInUse(File file) {
        try (FileInputStream fis = new FileInputStream(file);
                FileChannel channel = fis.getChannel()) {

            // Tente d'obtenir un verrou exclusif
            channel.tryLock();
            System.out.println("Le fichier est disponible.");
            return false; // Si on obtient le verrou, le fichier n'est pas utilisé

        } catch (OverlappingFileLockException | IOException e) {

            System.out.println("Le fichier est en cours d'utilisation.");
            return true; // Le fichier est en cours d'utilisation
        }
    }

}
