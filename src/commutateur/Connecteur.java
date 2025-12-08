/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.File;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michel
 */
public class Connecteur extends Observable {

    public static String portName = null;
    private String portNameAuto = null;
    private SerialPort[] ports = null;
    public SerialPort portComm;
    private int baudeRate = 9600;
    private int numDatabits = 8;
    private int parity = 0;
    private int stopBits = 1;
    private int newReadTimeout = 1000;
    private int newWriteTimeout = 0;
    private ProgController progController = new ProgController();
    private int sequenceInterrompue = 1;
    private boolean error = false;

    private OutputStream outputStream;

    private String inputLine;

    private boolean echo = false;

    private ProcessBuilder processBuilder = new ProcessBuilder();

    private ProcessManager processManager = new ProcessManager();

    public static String getPortName() {
        return portName;
    }

    public static void setPortName(String portName) {
        Connecteur.portName = portName;
    }

    public int getBaudeRate() {
        return baudeRate;
    }

    public void setBaudeRate(int baudeRate) {
        this.baudeRate = baudeRate;
    }

    public int getNumDatabits() {
        return numDatabits;
    }

    public void setNumDatabits(int numDatabits) {
        this.numDatabits = numDatabits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getSequenceInterrompue() {
        return sequenceInterrompue;
    }

    public void setSequenceInterrompue(int sequenceInterrompue) {
        this.sequenceInterrompue = sequenceInterrompue;
    }

    public boolean isEcho() {
        return echo;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
    }

    public String getPortNameAuto() {
        return portNameAuto;
    }

    public void setPortNameAuto(String portNameAuto) {
        this.portNameAuto = portNameAuto;
    }

    public int makeConnection(String portName, int baudeRate, int numDataBits, int parity, int stopBits, boolean auto) {

        try {

            if (portName == null) {

                System.out.println("makeConnection() - Port non sélectionné");
                return 0;
            }

            if (!auto) {

                for (SerialPort p : ports) {

                    System.out.println("Interface.makeConnection() - getSystemPortName: " + p.getSystemPortName() + " // " + portName);
                    if (p.getSystemPortName().equals(portName)) {

                        portComm = p;

                    }
                }

            } else {

                for (SerialPort p : ports) {

                    System.out.println("Interface.makeConnection() - getSystemPortName: " + p.getSystemPortName() + " // " + portName);
                    if (p.getSystemPortName().equals(portNameAuto)) {

                        portComm = p;

                    }
                }

            }

            portComm.setBaudRate(baudeRate);
            portComm.setNumDataBits(numDatabits);
            portComm.setParity(parity);
            portComm.setNumStopBits(stopBits);
            portComm.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, newReadTimeout, newWriteTimeout);
            portComm.openPort();

            if (portComm.isOpen()) {

                System.out.println("Connexion réussie! - classe Connecteur");
                envoyerData(Constants.RESET_HARDWARE);
                // return 99;

            } else {

                System.out.println("Connexion échouée! 1 - classe Connecteur");
                return -1;
            }

        } catch (Exception e) {

            System.out.println("Connexion échouée! 2 - classe Connecteur");
            return -2;
        }

        portComm.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }

                try {

                    byte[] readBuffer = new byte[100];

                    int numRead = portComm.readBytes(readBuffer,
                            readBuffer.length);
                    byte[] lecture = new byte[numRead];
                    for (int i = 0; i < numRead; i++) {

                        lecture[i] = readBuffer[i];
                    }
                    inputLine = new String(lecture, StandardCharsets.UTF_8);

                    notifierResultat();

                } catch (Exception e) {   // Traitement des exceptions

                    System.err.println(e.toString());
                }
            }
        });

        return 99;

    }

    public int disconnect() {

        if (portComm != null) {
            portComm.closePort();
        }
        return 0;

    }

    public List<String> getListPorts() {

        List<String> portNames = new ArrayList<>();
        ports = SerialPort.getCommPorts();
        for (SerialPort p : ports) {

            portNames.add(p.getSystemPortName());
        }

        return portNames;

    }

    public int envoyerData(String dataToSend) {

        outputStream = portComm.getOutputStream();

        try {

            //    System.out.println("Interface.envoyerData(), données: " + dataToSend);
            outputStream.write(dataToSend.getBytes());

            return 1;

        } catch (IOException e) {

            return -1;

        }

    }

    public String getInputLine() {
        return inputLine;
    }

    public void setInputLine(String inputLine) {
        this.inputLine = inputLine;
    }

    public void notifierResultat() {

        this.setChanged();
        this.notifyObservers(this.getInputLine());
        if (this.getInputLine().startsWith("->GR")) {
            System.out.println("reception echo - connecteur");

            String[] tab = this.getInputLine().trim().split(":");

            if (tab[2].equals("ON") || tab[2].equals("OFF")) {
                echo = true;
                System.out.println("echo = true - connecteur - notification");
            }
        }

    }

    void resetTestBoard() {

    }

    void flushBuffer() {

        portComm.flushIOBuffers();
    }

    public void programmationCompleted(String operation) {

        this.setChanged();
        this.notifyObservers(operation);

    }

    public void erase(boolean envVariable, String programmerLocation) {

    }

    void tempo(long duree) {

        try {

            Thread.sleep(duree);

        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int program(String hexLocation, boolean envVariable, String programmerPath, String programmer, String device, String binaryLocation, int nombreDeVoiesCarteEnTest, String programmerPathTempDir) throws IOException, InterruptedException {

        char count = 48;
        for (int j = 1; j < sequenceInterrompue; j++) {

            count++;
        }

        for (int i = sequenceInterrompue; i < nombreDeVoiesCarteEnTest + 1; i++) {

            System.out.println("error: " + error);

            count++;
            envoyerData(Character.toString(count));

            cleanDirectory2(".\\logs\\logs.txt");
            //tempo(Constants.TIMING);  
            programmationCompleted("->START:99:" + i);

            //processManager.processShellCommand("commander flash C:\\Users\\Michel\\Desktop\\Smartloxx-programmateur\\binaires1\\app_v1_0.bin --address 0x0 --device EFR32BG12P432F1024GL125 >.\\logs\\logs.txt");
            //processBuilder.command("cmd.exe", "/c", "commander flash C:\\Users\\Michel\\Desktop\\Smartloxx-programmateur\\binaires1\\app_v1_0.bin --address 0x0 --device EFR32BG12P432F1024GL125 >.\\logs\\logs.txt");
            processManager.processShellCommand("commander flash C:\\Users\\Michel\\Desktop\\Smartloxx-programmateur\\binaires1\\app_v1_0.bin --address 0x0 --device EFR32BG12P432F1024GL125 | Out-File -FilePath .\\logs\\logs.txt -Encoding utf8");
            tempo(200);

            System.out.println("Fin programmation");
            System.out.println("Début vérification");
            int control = progController.find(".\\logs\\logs.txt", Constants.ERREURS_LOG1, Constants.REQUIS_LOG1);
            System.out.println("code control: " + control);

            programmationCompleted("->PROG:" + i + ":" + control);
            if (control == -1) {

                envoyerData(Character.toString('t'));
                break;
            }

            if (control == -9) {

                envoyerData(Character.toString('t'));
                break;
            }
            
        }

        sequenceInterrompue = 1;
        return 1;

    }

    public void cleanDirectory(String programmerPathTempDir) throws IOException {

        boolean deleteIfExists1 = Files.deleteIfExists(Paths.get(programmerPathTempDir + "2013.ini"));
        boolean deleteIfExists2 = Files.deleteIfExists(Paths.get(programmerPathTempDir + "2013.lock"));

    }

    public void cleanDirectory2(String logFile) {

        //System.out.println("Suppression fichier de log");
        Path path = Paths.get(logFile);
        try {
            boolean deleteIfExists1 = Files.deleteIfExists(path);
        } catch (IOException ex) {

            //System.out.println("Problème suppression fichier de log");
        }

    }

    void getFileSize(String logFile) {

        double size0 = 0;
        double size1 = 0;
        Boolean end = false;
        int counter = 0;
        int lim = 1000;

        while (!end) {

            File file = new File(logFile);
            size1 = (double) file.length();
            System.out.println(size1 / 1024 + "  kb");
            size0 = size1;
            if (size0 == size1 && size1 != 0) {

                counter++;
                if (counter > lim) {
                    end = true;

                }
            }
        }

        System.out.println("Mesure terminée!");

    }

    public void singleProgramme(String hexLocation, boolean envVariable, String programmerPath, String programmer, String device, String binaryLocation, int nombreDeVoiesCarteEnTest, String programmerPathTempDir) throws IOException, InterruptedException {

        cleanDirectory(programmerPathTempDir);
        cleanDirectory2(".\\logs\\logs.txt");
        tempo(250);
        // programmationCompleted("->START:99:" + i);
        //ProcessBuilder processBuilder = new ProcessBuilder();
        //processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /W /OY2013 >.\\logs\\logs.txt");
        processBuilder.command("cmd.exe", "/c", "java -jar " + programmerPath + " /" + programmer + " /" + device + " /F" + binaryLocation + " /M /OY2013 >.\\logs\\logs.txt");
        Process process4 = processBuilder.start();
        process4.waitFor();

        int control = progController.find(".\\logs\\logs.txt", Constants.ERREURS_LOG1, Constants.REQUIS_LOG1);
        System.out.println("code control: " + control);

        //tempo(10000);
        System.out.println("end single programming");
    }

    public void askForKillingProcess() {

        try {
            processManager.killProcess();
        } catch (IOException ex) {
            Logger.getLogger(Connecteur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Connecteur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void askForDeletingFiles() throws IOException {

        processManager.deleteFiles();
    }

    public void askForResetProcess() throws IOException {

        processManager.getJavaProcesses();
        System.out.println("IdProcess at start: " + processManager.getProcessId());
        try {
            processManager.killProcess();
        } catch (IOException ex) {
            Logger.getLogger(Connecteur.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Connecteur.class.getName()).log(Level.SEVERE, null, ex);
        }
        processManager.deleteFiles();
    }

    public String getSerialPort() throws IOException, InterruptedException {

        processManager.getSerialPort();
        processManager.extractSerialPort();
        portName = processManager.getPort();
        return portName;
    }

    public boolean executePowershellCommand(String log, String commande, String reponse) throws IOException {

        String[] requis = {reponse};

        try {
            this.processManager.processShellCommand(commande);
            int result = this.progController.find(log, null, requis);
            this.processManager.deleteFilesByName(".\\logs\\env.txt");
            if (result == 1) {

                return true;
            } else {
                return false;
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Connecteur.class.getName()).log(Level.SEVERE, null, ex);
            this.processManager.deleteFilesByName(".\\logs\\env.txt");
            return false;
        }

    }

}
