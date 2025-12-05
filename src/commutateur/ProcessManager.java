/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author Michel
 */
public class ProcessManager {

    private ProcessBuilder processBuilder = new ProcessBuilder();
    private boolean listed = false;
    private File outputFile0 = new File(".\\processes0.process");
    private File ports = new File(".\\ports.process");
    private String processId = "0";
    private String port = "none";
    private ArrayList<String> listPorts = new ArrayList<>();

    public boolean isListed() {
        return listed;
    }

    public void setListed(boolean listed) {
        this.listed = listed;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void getJavaProcesses() throws IOException {

        deleteFiles();
        Constants.tempo(200);
        String[] command = {"powershell.exe", "-Command", "Get-Process -ProcessName java | Format-List *"};

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(outputFile0);
        // Rediriger la sortie vers le fichier

        try {
            // Démarrer le processus
            Process process = processBuilder.start();

            // Attendre la fin du processus
            process.waitFor();
            System.out.println("La sortie a été redirigée vers " + outputFile0.getAbsolutePath());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        String[] command2 = {"powershell.exe", "exit"};
        ProcessBuilder processBuilder2 = new ProcessBuilder(command2);

        try {
            // Démarrer le processus
            Process process = processBuilder2.start();

            // Attendre la fin du processus
            process.waitFor();
            System.out.println("La sortie a été redirigée vers " + outputFile0.getAbsolutePath());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        extractProcessId();
        //Constants.tempo(100000);

    }

    public void deleteFiles() throws IOException {

        Files.deleteIfExists(Paths.get(".\\processes0.process"));
        resetFlags();
    }

    public void extractProcessId() {

        boolean lineIdFound = false;
        String iDline = "";
        try {

            //System.out.println("le fichier est disponible");
            // Création d'un fileReader pour lire le fichier
            FileReader fileReader = new FileReader(".\\processes0.process");

            // Création d'un bufferedReader qui utilise le fileReader
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();

            while (line != null && !lineIdFound) {

                // lecture de la prochaine ligne
                //System.out.println(line);
                if (line.contains("Id") && !line.contains("SessionId")) {

                    System.out.println(line);
                    iDline = line;
                }

                if (line.contains("Zulu Platform x64 Architecture")) {

                    System.out.println(line);
                    lineIdFound = true;
                }

                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("Fichier en cours d'écriture");
        }

        if (lineIdFound) {
            listed = true;
            System.out.println("line Id: " + iDline);
            int index = iDline.indexOf(':');
            processId = iDline.substring(index + 2);
            System.out.println("Id: " + processId);
        } else {

            processId = "none";
        }
    }

    public void killProcess() throws IOException, InterruptedException {

        if (!processId.equals("none")) {

            String[] command = {"powershell.exe", "-Command", "Stop-Process -Id " + processId};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            process.waitFor();
            String[] command2 = {"powershell.exe", "exit"};
            processBuilder = new ProcessBuilder(command2);
            // Démarrer le processus
            process = processBuilder.start();

            // Attendre la fin du processus
            process.waitFor();
            Files.deleteIfExists(Paths.get(".\\processes0.process"));
            resetFlags();

        }

    }

    public void resetFlags() {

        listed = false;
        processId = "0";
    }

    public void getSerialPort() throws IOException, InterruptedException {

        Constants.tempo(200);
        //String[] command = {"powershell.exe", "-Command", "Get-PnPDevice | Where-Object{$_.PNPClass -in \"Ports\" } | Where-Object{$_.Present -in \"True\"} | Select-Object Name,Description,Manufacturer,PNPClass,Service,Present,Status,DeviceID | Sort-Object Name"};
        String[] command = {"powershell.exe", "-Command", "Get-PnPDevice"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectOutput(ports);
        // Rediriger la sortie vers le fichier

        try {
            // Démarrer le processus
            Process process = processBuilder.start();

            // Attendre la fin du processus
            process.waitFor();
            System.out.println("La liste des ports a été redirigée vers " + ports.getAbsolutePath());

        } catch (IOException | InterruptedException e) {

            System.out.println("Erreur1");
            e.printStackTrace();

        }

        String[] command2 = {"powershell.exe", "exit"};
        ProcessBuilder processBuilder2 = new ProcessBuilder(command2);

        // Démarrer le processus
        Process process2 = processBuilder2.start();

        // Attendre la fin du processus
        process2.waitFor();
        System.out.println("La liste des ports a été redirigée vers " + ports.getAbsolutePath());

        // extractSerialPort();
    }

    public void extractSerialPort() {

        String serialPortName = "";
        try {

            System.out.println("le fichier est disponible");
            // Création d'un fileReader pour lire le fichier
            FileReader fileReader = new FileReader(".\\ports.process");
            //FileReader fileReader = new FileReader(".\\port22.process");

            // Création d'un bufferedReader qui utilise le fileReader
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();

            boolean portFound = false;

            while (line != null && !portFound) {

                // lecture de la prochaine ligne
                //System.out.println(line);
                if (line.contains("FTDI")) {

                    //System.out.println(line);
                    serialPortName = line;
                    portFound = true;
                }

                line = reader.readLine();

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

        // System.out.println("Resultat \n" + serialPortName);
        int index1 = serialPortName.indexOf('(');
        int index2 = serialPortName.indexOf(')');
        serialPortName = serialPortName.substring(index1 + 1, index2);
        System.out.println("Port: " + serialPortName);
        port = serialPortName;

    }
    
      public void processShellCommand(String commande) throws IOException, InterruptedException {

        if (!processId.equals("none")) {

            String[] command = {"powershell.exe", "-Command", commande};
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();
            process.waitFor();
            String[] command2 = {"powershell.exe", "exit"};
            processBuilder = new ProcessBuilder(command2);
            // Démarrer le processus
            process = processBuilder.start();

            // Attendre la fin du processus
            process.waitFor();
        }

    }

}
