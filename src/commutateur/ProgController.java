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

}
