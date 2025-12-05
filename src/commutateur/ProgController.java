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
import java.nio.charset.StandardCharsets;
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

    /*
    public int find(String logfile, String[] erreurs, String[] requis) throws IOException {

        int j = 0;
        int codeControl = 0;
        while (j < 2) {

            System.out.println("Test erreur/preréquis dans le log");
            int analyse = isFileAvailable4(logfile);

          
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
                    System.out.println(line);

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

                            if (line.contains("successfully")) {

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
     */
    public int find(String logfile, String[] erreurs, String[] requis) throws IOException {

        int j = 0;
        int codeControl = 0;

        while (j < 2) {
            System.out.println("Test erreur/preréquis dans le log");
            int analyse = isFileAvailable4(logfile);

            if (analyse == -9) {

                return -9;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(logfile))) {

                String line;

                while ((line = reader.readLine()) != null) {

                    System.out.println(line);

                    // Test erreurs
                    if (erreurs != null) {
                        for (int i = 0; i < erreurs.length; i++) {
                            if (line.contains(erreurs[i])) {
                                codeControl = -(i + 1);
                                break;
                            }
                        }
                    }

                    // Test requis
                    if (codeControl == 0 && requis != null) {
                        for (int i = 0; i < requis.length; i++) {
                            if (line.contains(requis[i])) {
                                codeControl = 1;
                            }
                        }
                    }

                    if (codeControl != 0) {
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
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

        int lim = 1000;
        int lim2 = 10000;

        while (!end) {

            File file = new File(logFile);
            size1 = (double) file.length();
            System.out.println(size1 / 1024 + "  kb");
            //Constants.tempo(50);
            size0 = size1;

            if (size0 == size1 && size1 == 0) {
                //System.out.println("test gr1");
                counter0++;
                if (counter0 > lim2) {
                    System.out.println("Sortie -9");
                    return -9;

                }
            }

            if (size0 == size1 && size1 != 0) {
                //System.out.println("test gr1");
                counter0++;
                if (counter0 > lim) {
                    System.out.println("Sortie 1");
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
