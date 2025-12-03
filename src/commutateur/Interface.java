/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commutateur;

import java.awt.Color;
import java.awt.Font;
import java.awt.color.ColorSpace;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.UIManager;

/**
 *
 * @author Michel
 */
public class Interface extends javax.swing.JFrame implements Observer {

    private File programmerLocation = null;      // emplacement du repertoire programmateur
    private File nouveauBinaire = null;          // emplacement du repertoire du nouveau binaire à ajouter
    private String filePath = null;              // emplacement du binaire pour programmation
    private String programmerPathParamsProperties = null;        // emplacement du programmateur(repertoire plateforme Microchip)
    private String programmerPathTempFileDirectory = null;       // emplacement des fichiers temporaire du programmateur
    private String localisationNouveauBinaire = null;
    private String nomNouveauBinaire = null;
    private String devicesParamsProperties = null;               // devices lus dans params.properties
    private String nouveauDevice = null;         // device créé et ajouté à la liste
    private String deviceEnTest = null;          // microcontroleur à programmer sur le produits sélectionné
    private String hexLocationsParamsProperties = null;          // liste lues dans params.properties
    private String binaireLocation = null;
    private String nombreVoiesCommutateurParamsProperties = null;  // nombre de voies du commutateur lues dans params.properties
    private String programmerParamsProperties = null;
    private String workDirectoryPath = null;

    private int limCommutateur = 0;                 // nombre de voies du commutateur converties en int depuis la variable nombreVoiesCommutateurString
    private int intNombreDeVoiesNouvelleCarte = 0;
    private int intNombreDeVoiesCarteEnTest = 0;

    private int lignes = 0;
    private int colonnes = 0;
    private int monoLocation = 0;

    private String nombreDeVoiesEnregistresParamsProperties = null;  // lues dans params.properties
    private String nombreDeVoiesCarteEnTest = null;
    private String nombreDeVoiesNouvelleCarte = null;

    private String matricesProperties = null;        // Liste de toutes les matrices connues et enregistrées dans params.properties
    private String matriceAprogrammer = null;        // la matrice du panneau à programmer
    private String matriceNouveauPanneau = null;     // la matrice du panneau en cours d'enregistrement
    private String monoPositionString = null;        // emplacement pour programmation mono

    private boolean envVariable = false;
    private String produitAprogrammer = null;       // produit sélectionné pour programmation via l'interface
    private String listeProduitsConnusParamsProperties = null;
    private boolean confirmationParams = false;

    private ArrayList<String> listesProduits = new ArrayList<String>();
    private ArrayList<String> listesVoies = new ArrayList<String>();
    private ArrayList<String> listesMatrices = new ArrayList<String>();

    private ArrayList<String> ListeBinairesEnregistres = new ArrayList<String>();
    private ArrayList<String> listeDevicesEnregistres = new ArrayList<String>();
    private int selectedProduct = 0;

    Connecteur connecteur = getConnecteur();            // gére la connexion RS232

    private int baudeRate = 9600;
    private int numDatabits = 8;
    private int parity = 0;
    private int stopBits = 1;
    private int newReadTimeout = 1000;
    private int newWriteTimeout = 0;

    private boolean connexionRS232Active = false;               // état de la connexion RS-232

    private boolean testActif = false;
    private boolean programmationActive = false;
    private boolean auto = true;
    private boolean AttenteReponseOperateur = false;
    private boolean mono = false;

    public static Initializer initializer = new Initializer();  // Charge les propriétés du fichier properties contenant les paramètres de programmation
    public static Initialisation initialisation;                // Centralise les données rapportées par l'initializer

    private List<String> listePortString = new ArrayList<>();
    private List<JRadioButtonMenuItem> listePorts = new ArrayList<JRadioButtonMenuItem>();

    private ProgController progController = new ProgController();

    private ArrayList<JLabel> matriceLigne01 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne02 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne03 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne04 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne05 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne06 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne07 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne08 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne09 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne10 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne11 = new ArrayList<>();
    private ArrayList<JLabel> matriceLigne12 = new ArrayList<>();

    private Ligne ligne01 = new Ligne(matriceLigne01, true, 1);
    private Ligne ligne02 = new Ligne(matriceLigne02, true, 2);
    private Ligne ligne03 = new Ligne(matriceLigne03, true, 3);
    private Ligne ligne04 = new Ligne(matriceLigne04, true, 4);
    private Ligne ligne05 = new Ligne(matriceLigne05, true, 5);
    private Ligne ligne06 = new Ligne(matriceLigne06, true, 6);
    private Ligne ligne07 = new Ligne(matriceLigne07, true, 7);
    private Ligne ligne08 = new Ligne(matriceLigne08, true, 8);
    private Ligne ligne09 = new Ligne(matriceLigne09, true, 9);
    private Ligne ligne10 = new Ligne(matriceLigne10, true, 10);
    private Ligne ligne11 = new Ligne(matriceLigne11, true, 11);
    private Ligne ligne12 = new Ligne(matriceLigne12, true, 12);

    private ArrayList<Ligne> listeLignes = new ArrayList<Ligne>();

    private ImageIcon icon = null;

    private ProcessBuilder processBuilder = new ProcessBuilder();
    private LocalDateTime dateOfStart;
    private LocalDateTime dateOfEnd;

    private int CYCLES = 0;
    private int STOPS = 0;
    private int TOTAL = 0;

    private boolean echo = false;
    private boolean panne = false;

    private int compteurReset = 0;
    private String serialPortModeAuto = null;
    private boolean autoConnexion = false;

    public Interface() throws IOException, InterruptedException {

        initComponents();
        statutRs232.setBackground(Color.RED);
        statutRs232.setForeground(Color.RED);
        statutRs232.setOpaque(true);

        statutPGRM.setBackground(Color.RED);
        statutPGRM.setForeground(Color.RED);
        statutPGRM.setOpaque(true);

        this.getContentPane().setBackground(new Color(50, 131, 168));

        console.setBackground(new Color(247, 242, 208));
        console.setOpaque(true);
        console.setForeground(Color.red);
        console.setFont(new Font("Serif", Font.BOLD, 20));

        nomProduit.setBackground(new Color(247, 242, 208));
        nomProduit.setOpaque(true);
        nomProduit.setForeground(Color.red);
        nomProduit.setFont(new Font("Serif", Font.BOLD, 20));

        emplacementBinaire.setBackground(new Color(247, 242, 208));
        emplacementBinaire.setOpaque(true);
        emplacementBinaire.setForeground(Color.red);
        emplacementBinaire.setFont(new Font("Serif", Font.BOLD, 20));

        nombreVoies.setBackground(new Color(247, 242, 208));
        nombreVoies.setOpaque(true);
        nombreVoies.setForeground(Color.red);
        nombreVoies.setFont(new Font("Serif", Font.BOLD, 20));

        hexLocalisation.setBackground(new Color(247, 242, 208));
        hexLocalisation.setOpaque(true);
        hexLocalisation.setForeground(Color.red);
        hexLocalisation.setFont(new Font("Serif", Font.BOLD, 20));

        messageCreation.setBackground(new Color(252, 242, 3));
        messageCreation.setOpaque(true);
        messageCreation.setForeground(Color.red);
        messageCreation.setFont(new Font("Serif", Font.BOLD, 20));

        nomNouvelleCarte.setBackground(new Color(252, 242, 3));
        nomNouvelleCarte.setOpaque(true);
        nomNouvelleCarte.setForeground(Color.red);
        nomNouvelleCarte.setFont(new Font("Serif", Font.BOLD, 20));

        messageBinaireSelectionne.setBackground(new Color(252, 242, 3));
        messageBinaireSelectionne.setOpaque(true);
        messageBinaireSelectionne.setForeground(Color.red);
        messageBinaireSelectionne.setFont(new Font("Serif", Font.BOLD, 20));

        nombreVoiesNouvelleCarte.setBackground(new Color(252, 242, 3));
        nombreVoiesNouvelleCarte.setOpaque(true);
        nombreVoiesNouvelleCarte.setForeground(Color.red);
        nombreVoiesNouvelleCarte.setFont(new Font("Serif", Font.BOLD, 20));

        nouvelleMatrice.setBackground(new Color(252, 242, 3));
        nouvelleMatrice.setOpaque(true);
        nouvelleMatrice.setForeground(Color.red);
        nouvelleMatrice.setFont(new Font("Serif", Font.BOLD, 20));

        nouveauMicroController.setBackground(new Color(252, 242, 3));
        nouveauMicroController.setOpaque(true);
        nouveauMicroController.setForeground(Color.red);
        nouveauMicroController.setFont(new Font("Serif", Font.BOLD, 20));

        paramsWin.getContentPane().setBackground(new Color(131, 50, 168));
        paramsWin.setSize(1300, 600);

        messageCreation.setBackground(new Color(247, 242, 208));

        progLocLabel.setBackground(new Color(247, 242, 208));
        hexLocalisation.setOpaque(true);

        messageCreation.setOpaque(true);
        progLocLabel.setOpaque(true);
        menuItemMono.setSelected(false);
        inhibBtn();

        aide.getContentPane().setBackground(new Color(247, 242, 208));
        initialisationParams();
        rechercherPortsComms();
        if (autoConnexion) {

            //serialPortModeAuto = connecteur.getSerialPort();
            //System.out.println("serialPortModeAuto récupéré: " + serialPortModeAuto);
            int nbreDePorts = listePortString.size();
            System.out.println("nbre de ports:" + nbreDePorts);
            int i = 0;

            while (!connexionRS232Active && i < nbreDePorts) {

                serialPortModeAuto = listePortString.get(i);
                connecteur.setPortNameAuto(serialPortModeAuto);
                makeSerialAutoConnexion();
                i++;
            }

            if (!connexionRS232Active) {

                montrerError("Aucune connexion série détectée!\nVérifier que le banc est raccordé au PC\n Si le porblème persiste relancer l'application.", "Défaut de connexion");
                if (!echo) {

                    System.exit(0);
                }
            }

            echo = false;

        }

        //initialisationParams();
        // Création repertoire de logs
        int dirCreation = progController.createLogFolder(Constants.LOG_DIRECTORY);
        if (dirCreation != 1) {

            montrerError("Echec à la création du repertoire de logs", "Erreur d'initialisation");
            System.exit(0);

        }

        // Création repertoire images
        dirCreation = progController.createLogFolder(Constants.IMAGES_DIRECTORY);
        if (dirCreation != 1) {

            montrerError("Echec à la création du repertoire images", "Erreur d'initialisation");
            System.exit(0);

        }

        progBarre.setStringPainted(true);
        progBarre.setForeground(Color.blue);
        progBarre.setOpaque(true);
        progBarre.setVisible(true);

        matriceLigne01.add(pcbL01C01);
        matriceLigne01.add(pcbL01C02);
        matriceLigne01.add(pcbL01C03);
        matriceLigne01.add(pcbL01C04);
        matriceLigne01.add(pcbL01C05);
        matriceLigne01.add(pcbL01C06);
        matriceLigne01.add(pcbL01C07);
        matriceLigne01.add(pcbL01C08);
        matriceLigne01.add(pcbL01C09);
        matriceLigne01.add(pcbL01C10);
        matriceLigne01.add(pcbL01C11);
        matriceLigne01.add(pcbL01C12);

        matriceLigne02.add(pcbL02C01);
        matriceLigne02.add(pcbL02C02);
        matriceLigne02.add(pcbL02C03);
        matriceLigne02.add(pcbL02C04);
        matriceLigne02.add(pcbL02C05);
        matriceLigne02.add(pcbL02C06);
        matriceLigne02.add(pcbL02C07);
        matriceLigne02.add(pcbL02C08);
        matriceLigne02.add(pcbL02C09);
        matriceLigne02.add(pcbL02C10);
        matriceLigne02.add(pcbL02C11);
        matriceLigne02.add(pcbL02C12);

        matriceLigne03.add(pcbL03C01);
        matriceLigne03.add(pcbL03C02);
        matriceLigne03.add(pcbL03C03);
        matriceLigne03.add(pcbL03C04);
        matriceLigne03.add(pcbL03C05);
        matriceLigne03.add(pcbL03C06);
        matriceLigne03.add(pcbL03C07);
        matriceLigne03.add(pcbL03C08);
        matriceLigne03.add(pcbL03C09);
        matriceLigne03.add(pcbL03C10);
        matriceLigne03.add(pcbL03C11);
        matriceLigne03.add(pcbL03C12);

        matriceLigne04.add(pcbL04C01);
        matriceLigne04.add(pcbL04C02);
        matriceLigne04.add(pcbL04C03);
        matriceLigne04.add(pcbL04C04);
        matriceLigne04.add(pcbL04C05);
        matriceLigne04.add(pcbL04C06);
        matriceLigne04.add(pcbL04C07);
        matriceLigne04.add(pcbL04C08);
        matriceLigne04.add(pcbL04C09);
        matriceLigne04.add(pcbL04C10);
        matriceLigne04.add(pcbL04C11);
        matriceLigne04.add(pcbL04C12);

        matriceLigne05.add(pcbL05C01);
        matriceLigne05.add(pcbL05C02);
        matriceLigne05.add(pcbL05C03);
        matriceLigne05.add(pcbL05C04);
        matriceLigne05.add(pcbL05C05);
        matriceLigne05.add(pcbL05C06);
        matriceLigne05.add(pcbL05C07);
        matriceLigne05.add(pcbL05C08);
        matriceLigne05.add(pcbL05C09);
        matriceLigne05.add(pcbL05C10);
        matriceLigne05.add(pcbL05C11);
        matriceLigne05.add(pcbL05C12);

        matriceLigne06.add(pcbL06C01);
        matriceLigne06.add(pcbL06C02);
        matriceLigne06.add(pcbL06C03);
        matriceLigne06.add(pcbL06C04);
        matriceLigne06.add(pcbL06C05);
        matriceLigne06.add(pcbL06C06);
        matriceLigne06.add(pcbL06C07);
        matriceLigne06.add(pcbL06C08);
        matriceLigne06.add(pcbL06C09);
        matriceLigne06.add(pcbL06C10);
        matriceLigne06.add(pcbL06C11);
        matriceLigne06.add(pcbL06C12);

        matriceLigne07.add(pcbL07C01);
        matriceLigne07.add(pcbL07C02);
        matriceLigne07.add(pcbL07C03);
        matriceLigne07.add(pcbL07C04);
        matriceLigne07.add(pcbL07C05);
        matriceLigne07.add(pcbL07C06);
        matriceLigne07.add(pcbL07C07);
        matriceLigne07.add(pcbL07C08);
        matriceLigne07.add(pcbL07C09);
        matriceLigne07.add(pcbL07C10);
        matriceLigne07.add(pcbL07C11);
        matriceLigne07.add(pcbL07C12);

        matriceLigne08.add(pcbL08C01);
        matriceLigne08.add(pcbL08C02);
        matriceLigne08.add(pcbL08C03);
        matriceLigne08.add(pcbL08C04);
        matriceLigne08.add(pcbL08C05);
        matriceLigne08.add(pcbL08C06);
        matriceLigne08.add(pcbL08C07);
        matriceLigne08.add(pcbL08C08);
        matriceLigne08.add(pcbL08C09);
        matriceLigne08.add(pcbL08C10);
        matriceLigne08.add(pcbL08C11);
        matriceLigne08.add(pcbL08C12);

        matriceLigne09.add(pcbL09C01);
        matriceLigne09.add(pcbL09C02);
        matriceLigne09.add(pcbL09C03);
        matriceLigne09.add(pcbL09C04);
        matriceLigne09.add(pcbL09C05);
        matriceLigne09.add(pcbL09C06);
        matriceLigne09.add(pcbL09C07);
        matriceLigne09.add(pcbL09C08);
        matriceLigne09.add(pcbL09C09);
        matriceLigne09.add(pcbL09C10);
        matriceLigne09.add(pcbL09C11);
        matriceLigne09.add(pcbL09C12);

        matriceLigne10.add(pcbL10C01);
        matriceLigne10.add(pcbL10C02);
        matriceLigne10.add(pcbL10C03);
        matriceLigne10.add(pcbL10C04);
        matriceLigne10.add(pcbL10C05);
        matriceLigne10.add(pcbL10C06);
        matriceLigne10.add(pcbL10C07);
        matriceLigne10.add(pcbL10C08);
        matriceLigne10.add(pcbL10C09);
        matriceLigne10.add(pcbL10C10);
        matriceLigne10.add(pcbL10C11);
        matriceLigne10.add(pcbL10C12);

        matriceLigne11.add(pcbL11C01);
        matriceLigne11.add(pcbL11C02);
        matriceLigne11.add(pcbL11C03);
        matriceLigne11.add(pcbL11C04);
        matriceLigne11.add(pcbL11C05);
        matriceLigne11.add(pcbL11C06);
        matriceLigne11.add(pcbL11C07);
        matriceLigne11.add(pcbL11C08);
        matriceLigne11.add(pcbL11C09);
        matriceLigne11.add(pcbL11C10);
        matriceLigne11.add(pcbL11C11);
        matriceLigne11.add(pcbL11C12);

        matriceLigne12.add(pcbL12C01);
        matriceLigne12.add(pcbL12C02);
        matriceLigne12.add(pcbL12C03);
        matriceLigne12.add(pcbL12C04);
        matriceLigne12.add(pcbL12C05);
        matriceLigne12.add(pcbL12C06);
        matriceLigne12.add(pcbL12C07);
        matriceLigne12.add(pcbL12C08);
        matriceLigne12.add(pcbL12C09);
        matriceLigne12.add(pcbL12C10);
        matriceLigne12.add(pcbL12C11);
        matriceLigne12.add(pcbL12C12);

        Ligne ligne01 = new Ligne(matriceLigne01, true, 1);
        Ligne ligne02 = new Ligne(matriceLigne02, true, 2);
        Ligne ligne03 = new Ligne(matriceLigne03, true, 3);
        Ligne ligne04 = new Ligne(matriceLigne04, true, 4);
        Ligne ligne05 = new Ligne(matriceLigne05, true, 5);
        Ligne ligne06 = new Ligne(matriceLigne06, true, 6);
        Ligne ligne07 = new Ligne(matriceLigne07, true, 7);
        Ligne ligne08 = new Ligne(matriceLigne08, true, 8);
        Ligne ligne09 = new Ligne(matriceLigne09, true, 9);
        Ligne ligne10 = new Ligne(matriceLigne10, true, 10);
        Ligne ligne11 = new Ligne(matriceLigne11, true, 11);
        Ligne ligne12 = new Ligne(matriceLigne12, true, 12);

        listeLignes.add(ligne01);
        listeLignes.add(ligne02);
        listeLignes.add(ligne03);
        listeLignes.add(ligne04);
        listeLignes.add(ligne05);
        listeLignes.add(ligne06);
        listeLignes.add(ligne07);
        listeLignes.add(ligne08);
        listeLignes.add(ligne09);
        listeLignes.add(ligne10);
        listeLignes.add(ligne11);
        listeLignes.add(ligne12);

        this.setSize(1141, 620 + lignes * 38);

        ligne01.supprimer();
        ligne02.supprimer();
        ligne03.supprimer();
        ligne04.supprimer();
        ligne05.supprimer();
        ligne06.supprimer();
        ligne07.supprimer();

        ligne08.supprimer();
        ligne09.supprimer();
        ligne10.supprimer();
        ligne11.supprimer();
        ligne12.supprimer();

        raffraichirInterface();
        connecteur.cleanDirectory(programmerPathTempFileDirectory);

        if (!initialisation.getItem().equals("none")) {

            System.out.println("keypadprogrammer.Interface.<init>() - test valeur item");
            selectedProduct = Integer.parseInt(initialisation.getItem());
            initInterface();

            activerBtnEffacer(true);
            activerBtnProgrammer(false);
            testParamsProg();
            raffraichirImagePanneau();
            connecteur.askForResetProcess();
            connecteur.envoyerData(Character.toString('t'));
            echo = false;
            Constants.tempo(2000);
            confirmationParams = true;

            menuParametres.setEnabled(false);
            menuConnexion.setEnabled(false);
            console.setText("Cycle de programmation en cours...");
            progBarre.setString("Programmation en cours...");
            programmationActive = true;
            progBarre.setVisible(true);

            // activerProgrammation();
            btnProgActionPerformed(null);

        } else {

            testParamsProg();
            raffraichirImagePanneau();
            connecteur.askForResetProcess();
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        programmerLoc = new javax.swing.JFileChooser();
        btnSelectLocationProg = new javax.swing.JButton();
        btnSelectBinaryLoc = new javax.swing.JButton();
        binaryLoc = new javax.swing.JFileChooser();
        groupPorts = new javax.swing.ButtonGroup();
        groupBits = new javax.swing.ButtonGroup();
        groupBaud = new javax.swing.ButtonGroup();
        groupParity = new javax.swing.ButtonGroup();
        groupStop = new javax.swing.ButtonGroup();
        paramsWin = new javax.swing.JFrame();
        titreParamsWin = new javax.swing.JLabel();
        progLocLabel = new javax.swing.JLabel();
        btnFermerParams = new javax.swing.JButton();
        titreLabProg = new javax.swing.JLabel();
        EnvVarBox = new javax.swing.JCheckBox();
        labelAjoutCarte = new javax.swing.JLabel();
        comboListeProduits = new javax.swing.JComboBox<>();
        listeProduits = new javax.swing.JLabel();
        btnAjouter = new javax.swing.JButton();
        hexLocalisation = new javax.swing.JLabel();
        nomNouvelleCarte = new javax.swing.JTextField();
        LabfichierBinaire = new javax.swing.JLabel();
        btnSelectionBinaireAjouter = new javax.swing.JButton();
        btnEnregistrer = new javax.swing.JButton();
        messageBinaireSelectionne = new javax.swing.JTextField();
        labelBinaireSelectionne = new javax.swing.JLabel();
        LabNombreVoies = new javax.swing.JLabel();
        nombreVoies = new javax.swing.JLabel();
        LabelNbreDeVoiesNouvelleCarte = new javax.swing.JLabel();
        nombreVoiesNouvelleCarte = new javax.swing.JTextField();
        messageCreation = new javax.swing.JLabel();
        LabelmicroController = new javax.swing.JLabel();
        nouveauMicroController = new javax.swing.JTextField();
        LabelNouvelleMatrice = new javax.swing.JLabel();
        nouvelleMatrice = new javax.swing.JTextField();
        aide = new javax.swing.JFrame();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextArea1 = new javax.swing.JTextArea();
        jTextArea2 = new javax.swing.JTextArea();
        jTextArea3 = new javax.swing.JTextArea();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        AfficheurPanneau = new javax.swing.JFrame();
        imagePanneau = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AlerteLancement = new javax.swing.JFrame();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        titre = new javax.swing.JLabel();
        btnProg = new javax.swing.JButton();
        btnEffacer = new javax.swing.JButton();
        statutRs232 = new javax.swing.JLabel();
        console = new javax.swing.JLabel();
        StatutRS232Lab = new javax.swing.JLabel();
        statutPRGLabel = new javax.swing.JLabel();
        statutPGRM = new javax.swing.JLabel();
        progBarre = new javax.swing.JProgressBar();
        btnACQ = new javax.swing.JButton();
        nomProduit = new javax.swing.JLabel();
        emplacementBinaire = new javax.swing.JLabel();
        pcbL01C01 = new javax.swing.JLabel();
        pcbL01C02 = new javax.swing.JLabel();
        pcbL01C03 = new javax.swing.JLabel();
        pcbL01C04 = new javax.swing.JLabel();
        pcbL01C05 = new javax.swing.JLabel();
        pcbL01C06 = new javax.swing.JLabel();
        pcbL01C07 = new javax.swing.JLabel();
        pcbL01C08 = new javax.swing.JLabel();
        pcbL01C09 = new javax.swing.JLabel();
        pcbL01C10 = new javax.swing.JLabel();
        pcbL01C11 = new javax.swing.JLabel();
        pcbL01C12 = new javax.swing.JLabel();
        pcbL02C10 = new javax.swing.JLabel();
        pcbL02C11 = new javax.swing.JLabel();
        pcbL02C12 = new javax.swing.JLabel();
        pcbL02C01 = new javax.swing.JLabel();
        pcbL02C02 = new javax.swing.JLabel();
        pcbL02C03 = new javax.swing.JLabel();
        pcbL02C04 = new javax.swing.JLabel();
        pcbL02C05 = new javax.swing.JLabel();
        pcbL02C06 = new javax.swing.JLabel();
        pcbL02C07 = new javax.swing.JLabel();
        pcbL02C08 = new javax.swing.JLabel();
        pcbL02C09 = new javax.swing.JLabel();
        pcbL03C10 = new javax.swing.JLabel();
        pcbL03C11 = new javax.swing.JLabel();
        pcbL03C12 = new javax.swing.JLabel();
        pcbL03C01 = new javax.swing.JLabel();
        pcbL03C02 = new javax.swing.JLabel();
        pcbL03C03 = new javax.swing.JLabel();
        pcbL03C04 = new javax.swing.JLabel();
        pcbL03C05 = new javax.swing.JLabel();
        pcbL03C06 = new javax.swing.JLabel();
        pcbL03C07 = new javax.swing.JLabel();
        pcbL03C08 = new javax.swing.JLabel();
        pcbL03C09 = new javax.swing.JLabel();
        pcbL04C10 = new javax.swing.JLabel();
        pcbL04C11 = new javax.swing.JLabel();
        pcbL04C12 = new javax.swing.JLabel();
        pcbL04C01 = new javax.swing.JLabel();
        pcbL04C02 = new javax.swing.JLabel();
        pcbL04C03 = new javax.swing.JLabel();
        pcbL04C04 = new javax.swing.JLabel();
        pcbL04C05 = new javax.swing.JLabel();
        pcbL04C06 = new javax.swing.JLabel();
        pcbL04C07 = new javax.swing.JLabel();
        pcbL04C08 = new javax.swing.JLabel();
        pcbL04C09 = new javax.swing.JLabel();
        pcbL05C10 = new javax.swing.JLabel();
        pcbL05C11 = new javax.swing.JLabel();
        pcbL05C12 = new javax.swing.JLabel();
        pcbL06C10 = new javax.swing.JLabel();
        pcbL06C11 = new javax.swing.JLabel();
        pcbL06C12 = new javax.swing.JLabel();
        pcbL06C01 = new javax.swing.JLabel();
        pcbL06C02 = new javax.swing.JLabel();
        pcbL06C03 = new javax.swing.JLabel();
        pcbL06C04 = new javax.swing.JLabel();
        pcbL05C01 = new javax.swing.JLabel();
        pcbL05C02 = new javax.swing.JLabel();
        pcbL05C03 = new javax.swing.JLabel();
        pcbL05C04 = new javax.swing.JLabel();
        pcbL05C05 = new javax.swing.JLabel();
        pcbL05C06 = new javax.swing.JLabel();
        pcbL05C07 = new javax.swing.JLabel();
        pcbL06C05 = new javax.swing.JLabel();
        pcbL06C06 = new javax.swing.JLabel();
        pcbL06C07 = new javax.swing.JLabel();
        pcbL06C08 = new javax.swing.JLabel();
        pcbL06C09 = new javax.swing.JLabel();
        pcbL07C10 = new javax.swing.JLabel();
        pcbL07C11 = new javax.swing.JLabel();
        pcbL07C12 = new javax.swing.JLabel();
        pcbL07C01 = new javax.swing.JLabel();
        pcbL07C02 = new javax.swing.JLabel();
        pcbL07C03 = new javax.swing.JLabel();
        pcbL07C04 = new javax.swing.JLabel();
        pcbL07C05 = new javax.swing.JLabel();
        pcbL07C06 = new javax.swing.JLabel();
        pcbL07C07 = new javax.swing.JLabel();
        pcbL07C08 = new javax.swing.JLabel();
        pcbL07C09 = new javax.swing.JLabel();
        pcbL08C10 = new javax.swing.JLabel();
        pcbL08C11 = new javax.swing.JLabel();
        pcbL08C12 = new javax.swing.JLabel();
        pcbL08C01 = new javax.swing.JLabel();
        pcbL08C02 = new javax.swing.JLabel();
        pcbL08C03 = new javax.swing.JLabel();
        pcbL08C04 = new javax.swing.JLabel();
        pcbL05C08 = new javax.swing.JLabel();
        pcbL08C05 = new javax.swing.JLabel();
        pcbL08C06 = new javax.swing.JLabel();
        pcbL08C07 = new javax.swing.JLabel();
        pcbL08C08 = new javax.swing.JLabel();
        pcbL08C09 = new javax.swing.JLabel();
        pcbL05C09 = new javax.swing.JLabel();
        pcbL09C10 = new javax.swing.JLabel();
        pcbL09C11 = new javax.swing.JLabel();
        pcbL09C12 = new javax.swing.JLabel();
        pcbL10C10 = new javax.swing.JLabel();
        pcbL10C11 = new javax.swing.JLabel();
        pcbL10C12 = new javax.swing.JLabel();
        pcbL10C01 = new javax.swing.JLabel();
        pcbL10C02 = new javax.swing.JLabel();
        pcbL10C03 = new javax.swing.JLabel();
        pcbL10C04 = new javax.swing.JLabel();
        pcbL09C01 = new javax.swing.JLabel();
        pcbL09C02 = new javax.swing.JLabel();
        pcbL09C03 = new javax.swing.JLabel();
        pcbL09C04 = new javax.swing.JLabel();
        pcbL09C05 = new javax.swing.JLabel();
        pcbL09C06 = new javax.swing.JLabel();
        pcbL09C07 = new javax.swing.JLabel();
        pcbL10C05 = new javax.swing.JLabel();
        pcbL10C06 = new javax.swing.JLabel();
        pcbL10C07 = new javax.swing.JLabel();
        pcbL10C08 = new javax.swing.JLabel();
        pcbL10C09 = new javax.swing.JLabel();
        pcbL11C10 = new javax.swing.JLabel();
        pcbL11C11 = new javax.swing.JLabel();
        pcbL11C12 = new javax.swing.JLabel();
        pcbL11C01 = new javax.swing.JLabel();
        pcbL11C02 = new javax.swing.JLabel();
        pcbL11C03 = new javax.swing.JLabel();
        pcbL11C04 = new javax.swing.JLabel();
        pcbL11C05 = new javax.swing.JLabel();
        pcbL11C06 = new javax.swing.JLabel();
        pcbL11C07 = new javax.swing.JLabel();
        pcbL11C08 = new javax.swing.JLabel();
        pcbL11C09 = new javax.swing.JLabel();
        pcbL12C10 = new javax.swing.JLabel();
        pcbL12C11 = new javax.swing.JLabel();
        pcbL12C12 = new javax.swing.JLabel();
        pcbL12C01 = new javax.swing.JLabel();
        pcbL12C02 = new javax.swing.JLabel();
        pcbL12C03 = new javax.swing.JLabel();
        pcbL12C04 = new javax.swing.JLabel();
        pcbL09C08 = new javax.swing.JLabel();
        pcbL12C05 = new javax.swing.JLabel();
        pcbL12C06 = new javax.swing.JLabel();
        pcbL12C07 = new javax.swing.JLabel();
        pcbL12C08 = new javax.swing.JLabel();
        pcbL12C09 = new javax.swing.JLabel();
        pcbL09C09 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuParametres = new javax.swing.JMenu();
        menuVoir = new javax.swing.JMenuItem();
        paramsProg = new javax.swing.JMenuItem();
        menuCreer = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        btnFermer = new javax.swing.JMenuItem();
        menuConnexion = new javax.swing.JMenu();
        menuPort = new javax.swing.JMenu();
        menuBaud = new javax.swing.JMenu();
        baud9600 = new javax.swing.JRadioButtonMenuItem();
        baud19200 = new javax.swing.JRadioButtonMenuItem();
        baud38400 = new javax.swing.JRadioButtonMenuItem();
        baud115200 = new javax.swing.JRadioButtonMenuItem();
        menuBits = new javax.swing.JMenu();
        bits6 = new javax.swing.JRadioButtonMenuItem();
        bits7 = new javax.swing.JRadioButtonMenuItem();
        bits8 = new javax.swing.JRadioButtonMenuItem();
        bits9 = new javax.swing.JRadioButtonMenuItem();
        menuStop = new javax.swing.JMenu();
        stop1 = new javax.swing.JRadioButtonMenuItem();
        stop2 = new javax.swing.JRadioButtonMenuItem();
        menuParity = new javax.swing.JMenu();
        parityNone = new javax.swing.JRadioButtonMenuItem();
        parityOdd = new javax.swing.JRadioButtonMenuItem();
        parityEven = new javax.swing.JRadioButtonMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        btnConnexion = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        btnDeconnexion = new javax.swing.JMenuItem();
        menuAide = new javax.swing.JMenu();
        menuItemMono = new javax.swing.JCheckBoxMenuItem();
        voirAide = new javax.swing.JMenuItem();

        programmerLoc.setFileFilter(null);
        programmerLoc.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);

        btnSelectLocationProg.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSelectLocationProg.setText("Programmer location");
        btnSelectLocationProg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectLocationProgActionPerformed(evt);
            }
        });

        btnSelectBinaryLoc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnSelectBinaryLoc.setText("Binary location");
        btnSelectBinaryLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectBinaryLocActionPerformed(evt);
            }
        });

        paramsWin.setTitle("Programmateur keypad - Paramètres système");

        titreParamsWin.setBackground(new java.awt.Color(153, 153, 255));
        titreParamsWin.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titreParamsWin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titreParamsWin.setText("PARAMETRES SYSTEMES");
        titreParamsWin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        progLocLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        progLocLabel.setForeground(new java.awt.Color(255, 0, 0));
        progLocLabel.setText("jLabel2");
        progLocLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnFermerParams.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnFermerParams.setForeground(new java.awt.Color(255, 0, 51));
        btnFermerParams.setText("Fermer");
        btnFermerParams.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFermerParamsActionPerformed(evt);
            }
        });

        titreLabProg.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        titreLabProg.setText("Répertoire programmateur");

        EnvVarBox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        EnvVarBox.setText("Variable d'environnement");
        EnvVarBox.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                EnvVarBoxStateChanged(evt);
            }
        });
        EnvVarBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnvVarBoxActionPerformed(evt);
            }
        });

        labelAjoutCarte.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelAjoutCarte.setText("Indiquez le nom de la nouvelle carte");

        comboListeProduits.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        comboListeProduits.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboListeProduitsItemStateChanged(evt);
            }
        });
        comboListeProduits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboListeProduitsActionPerformed(evt);
            }
        });

        listeProduits.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        listeProduits.setText("Sélection carte");

        btnAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnAjouter.setForeground(new java.awt.Color(255, 0, 0));
        btnAjouter.setText("Ajouter");
        btnAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAjouterActionPerformed(evt);
            }
        });

        hexLocalisation.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        hexLocalisation.setForeground(new java.awt.Color(255, 0, 51));
        hexLocalisation.setText("Jlabel");
        hexLocalisation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        nomNouvelleCarte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nomNouvelleCarte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomNouvelleCarteActionPerformed(evt);
            }
        });

        LabfichierBinaire.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabfichierBinaire.setText("Fichier binaire");

        btnSelectionBinaireAjouter.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnSelectionBinaireAjouter.setForeground(new java.awt.Color(255, 0, 0));
        btnSelectionBinaireAjouter.setText("Sélectionner le binaire");
        btnSelectionBinaireAjouter.setActionCommand("");
        btnSelectionBinaireAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectionBinaireAjouterActionPerformed(evt);
            }
        });

        btnEnregistrer.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnEnregistrer.setForeground(new java.awt.Color(255, 0, 0));
        btnEnregistrer.setText("Enregistrer");
        btnEnregistrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnregistrerActionPerformed(evt);
            }
        });

        messageBinaireSelectionne.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        messageBinaireSelectionne.setForeground(new java.awt.Color(255, 51, 0));
        messageBinaireSelectionne.setText("A définir");
        messageBinaireSelectionne.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messageBinaireSelectionneActionPerformed(evt);
            }
        });

        labelBinaireSelectionne.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelBinaireSelectionne.setText("Binaire sélectionné");

        LabNombreVoies.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabNombreVoies.setText("Nombre de voies");

        nombreVoies.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nombreVoies.setForeground(new java.awt.Color(255, 0, 51));
        nombreVoies.setText("Jlabel");
        nombreVoies.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        LabelNbreDeVoiesNouvelleCarte.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabelNbreDeVoiesNouvelleCarte.setText("Nombre de voies");

        nombreVoiesNouvelleCarte.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nombreVoiesNouvelleCarte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreVoiesNouvelleCarteActionPerformed(evt);
            }
        });

        messageCreation.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        messageCreation.setForeground(new java.awt.Color(255, 0, 51));
        messageCreation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        messageCreation.setText("Veuillez compléter le formulaire ci-dessous");
        messageCreation.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        LabelmicroController.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabelmicroController.setText("Microcontrôleur");

        nouveauMicroController.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nouveauMicroController.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauMicroControllerActionPerformed(evt);
            }
        });

        LabelNouvelleMatrice.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        LabelNouvelleMatrice.setText("Matrice");

        nouvelleMatrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nouvelleMatrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouvelleMatriceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout paramsWinLayout = new javax.swing.GroupLayout(paramsWin.getContentPane());
        paramsWin.getContentPane().setLayout(paramsWinLayout);
        paramsWinLayout.setHorizontalGroup(
            paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelBinaireSelectionne, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(nouvelleMatrice, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(messageCreation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nombreVoies, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(progLocLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paramsWinLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(EnvVarBox)
                                            .addComponent(titreLabProg, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 856, Short.MAX_VALUE)
                                        .addComponent(btnFermerParams))
                                    .addComponent(btnAjouter, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LabfichierBinaire, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hexLocalisation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(paramsWinLayout.createSequentialGroup()
                                    .addGap(482, 482, 482)
                                    .addComponent(titreParamsWin, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(listeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabNombreVoies, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(LabelNbreDeVoiesNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nombreVoiesNouvelleCarte, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1247, Short.MAX_VALUE)
                                    .addComponent(nouveauMicroController))
                                .addComponent(labelAjoutCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(comboListeProduits, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelmicroController, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nomNouvelleCarte, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(LabelNouvelleMatrice, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(465, 465, 465)
                        .addComponent(btnSelectionBinaireAjouter, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(messageBinaireSelectionne, javax.swing.GroupLayout.PREFERRED_SIZE, 1247, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addGap(468, 468, 468)
                        .addComponent(btnEnregistrer, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(329, Short.MAX_VALUE))
        );
        paramsWinLayout.setVerticalGroup(
            paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paramsWinLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(titreParamsWin)
                .addGap(18, 18, 18)
                .addGroup(paramsWinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFermerParams)
                    .addGroup(paramsWinLayout.createSequentialGroup()
                        .addComponent(titreLabProg)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EnvVarBox)))
                .addGap(18, 18, 18)
                .addComponent(progLocLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(listeProduits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(comboListeProduits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabfichierBinaire)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hexLocalisation, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabNombreVoies)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreVoies, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAjouter)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageCreation)
                .addGap(18, 18, 18)
                .addComponent(labelAjoutCarte)
                .addGap(18, 18, 18)
                .addComponent(nomNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelNbreDeVoiesNouvelleCarte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreVoiesNouvelleCarte, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelmicroController)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nouveauMicroController, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelNouvelleMatrice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nouvelleMatrice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSelectionBinaireAjouter)
                .addGap(3, 3, 3)
                .addComponent(labelBinaireSelectionne)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(messageBinaireSelectionne, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btnEnregistrer)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        aide.setTitle("Programmateur keypad - Aide");
        aide.setMinimumSize(new java.awt.Dimension(1400, 1000));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setMinimumSize(new java.awt.Dimension(1200, 1000));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(1200, 1000));

        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 1000));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 1000));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("2-A quoi sert cette application?");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("3- Comment fonctionne-t-elle?");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("4- Outils matériels");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setText("5-Principe");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Cette application sert à programmer des panneaux constituées de cartes équipées de microcontrôleurs Microchip.");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText("L'application peut être utilisée pour programmer différentes cartes équipées de microntrôleurs Microchip.Avant chaque séquence de \nprogrammation, la carte doit être sélectionnée parmi les cartes enregistrées dans le système. Cette sélection se fait via le menu \nParamètres->Voir. Une fênetre de paramétrage s'ouvre alors. Cette fenêtre contient une liste déroulante où apparaissent toutes les cartes\nenregsitrées. Il suffit se sélectionnée celle à programmer.\nUne fois la carte sélectionnée, il faut refermer la fenêtre de paramétrage. Les deux champs juste sous le titre de la fenêtre principale \nse complétent automatiquement avec les paramètres de la carte sélectionnées. Une matrice correspondant à la forme du panneau à programmer\napparait juste sous les deux champs précédemment cités. Cette matrice correspond aux indicateurs d'état de la phase de programmation.\nChaque indicateur correspond à une carte du panneau. Sa couleur détent de l'état du cycle de programmation; gris, la programmation n'a pas\ndémarré; jaune, la programmation est en cours; vert, la programmation a réussi; rouge, la programmation a échoué.\n\n");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jTextArea3.setText("Pour pouvoir porgrammer un panneau, il est nécessaire de l'enregistrer initialement. Cette enregistrement se fait via la fenetre de \nparamétrage accessible via le menu Paramètres->Voir puis en cliquant sur ajouter ou Paramétres->Créer.\nLa fenêtre de paramétrage s'agrandit et laisse apparaitre une formumaire à compléter avec les informations correspondants à la nouvelle \ncarte.\nLa matrice est définie de la façon suivant: Nombre de lignesxNombre de colonnes. \nLe nombre de carte ne doit pas être supérieur à la capacité du commutateur. ");

        jTextArea4.setColumns(20);
        jTextArea4.setRows(5);
        jTextArea4.setText("Pour s'executer, elle nécessite une machine Java version 1.8.La machine Java doit être inscrite dans les variables d'environnement.\nDe plus l'application MPLAB IPE doit être installée à la racine du disque dur ou idéalement dans la répertoire C:\\user\\[username] de \nla machine utilisée. MPLAB IPE s'installe avec l'environnement de développement MPLAX X IDE de Microchip.Elle est téléchargeable à partir\ndu site web de MIcrochip.");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("1- Version: V1.0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 1112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 1114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextArea4, javax.swing.GroupLayout.PREFERRED_SIZE, 1114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 1114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel6))))
                .addContainerGap(65, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(23, 23, 23)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jTextArea1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jTextArea4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jTextArea2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jTextArea3, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(299, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Présentation de l'application");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 51, 0));
        jButton1.setText("FERMER");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout aideLayout = new javax.swing.GroupLayout(aide.getContentPane());
        aide.getContentPane().setLayout(aideLayout);
        aideLayout.setHorizontalGroup(
            aideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aideLayout.createSequentialGroup()
                .addGap(289, 289, 289)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 683, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(428, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aideLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(aideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aideLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aideLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(74, 74, 74))))
        );
        aideLayout.setVerticalGroup(
            aideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aideLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(25, 25, 25))
        );

        AfficheurPanneau.setTitle("Panneau");
        AfficheurPanneau.setMinimumSize(new java.awt.Dimension(700, 700));

        imagePanneau.setToolTipText("");

        javax.swing.GroupLayout AfficheurPanneauLayout = new javax.swing.GroupLayout(AfficheurPanneau.getContentPane());
        AfficheurPanneau.getContentPane().setLayout(AfficheurPanneauLayout);
        AfficheurPanneauLayout.setHorizontalGroup(
            AfficheurPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AfficheurPanneauLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(imagePanneau, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );
        AfficheurPanneauLayout.setVerticalGroup(
            AfficheurPanneauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AfficheurPanneauLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(imagePanneau, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        AlerteLancement.setBackground(new java.awt.Color(0, 153, 153));

        jTextField1.setBackground(new java.awt.Color(0, 153, 153));
        jTextField1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("PROGRAMMATEUR MICROCHIP");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setBackground(new java.awt.Color(0, 153, 153));
        jTextField2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Démarrage en cours! ");

        javax.swing.GroupLayout AlerteLancementLayout = new javax.swing.GroupLayout(AlerteLancement.getContentPane());
        AlerteLancement.getContentPane().setLayout(AlerteLancementLayout);
        AlerteLancementLayout.setHorizontalGroup(
            AlerteLancementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlerteLancementLayout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addGroup(AlerteLancementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE)
                    .addComponent(jTextField2))
                .addContainerGap(150, Short.MAX_VALUE))
        );
        AlerteLancementLayout.setVerticalGroup(
            AlerteLancementLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AlerteLancementLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(164, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Programmateur Microchip");

        titre.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        titre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titre.setText("PROGRAMMATEUR SMARTLOXX");
        titre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnProg.setBackground(new java.awt.Color(255, 255, 255));
        btnProg.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnProg.setForeground(new java.awt.Color(51, 153, 0));
        btnProg.setText("PROGRAMMER");
        btnProg.setBorderPainted(false);
        btnProg.setContentAreaFilled(false);
        btnProg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProgActionPerformed(evt);
            }
        });

        btnEffacer.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEffacer.setForeground(new java.awt.Color(102, 51, 255));
        btnEffacer.setText("PANNEAU");
        btnEffacer.setBorderPainted(false);
        btnEffacer.setContentAreaFilled(false);
        btnEffacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEffacerActionPerformed(evt);
            }
        });

        statutRs232.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statutRs232.setText("rs-232");

        console.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        console.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        console.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        console.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        StatutRS232Lab.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        StatutRS232Lab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        StatutRS232Lab.setText("RS-232");

        statutPRGLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statutPRGLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statutPRGLabel.setText("PGRM");

        statutPGRM.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statutPGRM.setText("prgm");

        progBarre.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        progBarre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        progBarre.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        progBarre.setOpaque(true);
        progBarre.setStringPainted(true);

        btnACQ.setBackground(new java.awt.Color(255, 255, 255));
        btnACQ.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnACQ.setForeground(new java.awt.Color(51, 0, 255));
        btnACQ.setText("ACQ");
        btnACQ.setBorderPainted(false);
        btnACQ.setContentAreaFilled(false);
        btnACQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnACQActionPerformed(evt);
            }
        });

        nomProduit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nomProduit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nomProduit.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nomProduit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        emplacementBinaire.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        emplacementBinaire.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        emplacementBinaire.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        emplacementBinaire.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        pcbL01C01.setText("jLabel6");

        pcbL01C02.setText("jLabel6");

        pcbL01C03.setText("jLabel6");

        pcbL01C04.setText("jLabel6");

        pcbL01C05.setText("jLabel6");

        pcbL01C06.setText("jLabel6");

        pcbL01C07.setText("jLabel6");

        pcbL01C08.setText("jLabel6");

        pcbL01C09.setText("jLabel6");

        pcbL01C10.setText("jLabel6");

        pcbL01C11.setText("jLabel6");

        pcbL01C12.setText("jLabel6");

        pcbL02C10.setText("jLabel6");

        pcbL02C11.setText("jLabel6");

        pcbL02C12.setText("jLabel6");

        pcbL02C01.setText("jLabel6");

        pcbL02C02.setText("jLabel6");

        pcbL02C03.setText("jLabel6");

        pcbL02C04.setText("jLabel6");

        pcbL02C05.setText("jLabel6");

        pcbL02C06.setText("jLabel6");

        pcbL02C07.setText("jLabel6");

        pcbL02C08.setText("jLabel6");

        pcbL02C09.setText("jLabel6");

        pcbL03C10.setText("jLabel6");

        pcbL03C11.setText("jLabel6");

        pcbL03C12.setText("jLabel6");

        pcbL03C01.setText("jLabel6");

        pcbL03C02.setText("jLabel6");

        pcbL03C03.setText("jLabel6");

        pcbL03C04.setText("jLabel6");

        pcbL03C05.setText("jLabel6");

        pcbL03C06.setText("jLabel6");

        pcbL03C07.setText("jLabel6");

        pcbL03C08.setText("jLabel6");

        pcbL03C09.setText("jLabel6");

        pcbL04C10.setText("jLabel6");

        pcbL04C11.setText("jLabel6");

        pcbL04C12.setText("jLabel6");

        pcbL04C01.setText("jLabel6");

        pcbL04C02.setText("jLabel6");

        pcbL04C03.setText("jLabel6");

        pcbL04C04.setText("jLabel6");

        pcbL04C05.setText("jLabel6");

        pcbL04C06.setText("jLabel6");

        pcbL04C07.setText("jLabel6");

        pcbL04C08.setText("jLabel6");

        pcbL04C09.setText("jLabel6");

        pcbL05C10.setText("jLabel6");

        pcbL05C11.setText("jLabel6");

        pcbL05C12.setText("jLabel6");

        pcbL06C10.setText("jLabel6");

        pcbL06C11.setText("jLabel6");

        pcbL06C12.setText("jLabel6");

        pcbL06C01.setText("jLabel6");

        pcbL06C02.setText("jLabel6");

        pcbL06C03.setText("jLabel6");

        pcbL06C04.setText("jLabel6");

        pcbL05C01.setText("jLabel6");

        pcbL05C02.setText("jLabel6");

        pcbL05C03.setText("jLabel6");

        pcbL05C04.setText("jLabel6");

        pcbL05C05.setText("jLabel6");

        pcbL05C06.setText("jLabel6");

        pcbL05C07.setText("jLabel6");

        pcbL06C05.setText("jLabel6");

        pcbL06C06.setText("jLabel6");

        pcbL06C07.setText("jLabel6");

        pcbL06C08.setText("jLabel6");

        pcbL06C09.setText("jLabel6");

        pcbL07C10.setText("jLabel6");

        pcbL07C11.setText("jLabel6");

        pcbL07C12.setText("jLabel6");

        pcbL07C01.setText("jLabel6");

        pcbL07C02.setText("jLabel6");

        pcbL07C03.setText("jLabel6");

        pcbL07C04.setText("jLabel6");

        pcbL07C05.setText("jLabel6");

        pcbL07C06.setText("jLabel6");

        pcbL07C07.setText("jLabel6");

        pcbL07C08.setText("jLabel6");

        pcbL07C09.setText("jLabel6");

        pcbL08C10.setText("jLabel6");

        pcbL08C11.setText("jLabel6");

        pcbL08C12.setText("jLabel6");

        pcbL08C01.setText("jLabel6");

        pcbL08C02.setText("jLabel6");

        pcbL08C03.setText("jLabel6");

        pcbL08C04.setText("jLabel6");

        pcbL05C08.setText("jLabel6");

        pcbL08C05.setText("jLabel6");

        pcbL08C06.setText("jLabel6");

        pcbL08C07.setText("jLabel6");

        pcbL08C08.setText("jLabel6");

        pcbL08C09.setText("jLabel6");

        pcbL05C09.setText("jLabel6");

        pcbL09C10.setText("jLabel6");

        pcbL09C11.setText("jLabel6");

        pcbL09C12.setText("jLabel6");

        pcbL10C10.setText("jLabel6");

        pcbL10C11.setText("jLabel6");

        pcbL10C12.setText("jLabel6");

        pcbL10C01.setText("jLabel6");

        pcbL10C02.setText("jLabel6");

        pcbL10C03.setText("jLabel6");

        pcbL10C04.setText("jLabel6");

        pcbL09C01.setText("jLabel6");

        pcbL09C02.setText("jLabel6");

        pcbL09C03.setText("jLabel6");

        pcbL09C04.setText("jLabel6");

        pcbL09C05.setText("jLabel6");

        pcbL09C06.setText("jLabel6");

        pcbL09C07.setText("jLabel6");

        pcbL10C05.setText("jLabel6");

        pcbL10C06.setText("jLabel6");

        pcbL10C07.setText("jLabel6");

        pcbL10C08.setText("jLabel6");

        pcbL10C09.setText("jLabel6");

        pcbL11C10.setText("jLabel6");

        pcbL11C11.setText("jLabel6");

        pcbL11C12.setText("jLabel6");

        pcbL11C01.setText("jLabel6");

        pcbL11C02.setText("jLabel6");

        pcbL11C03.setText("jLabel6");

        pcbL11C04.setText("jLabel6");

        pcbL11C05.setText("jLabel6");

        pcbL11C06.setText("jLabel6");

        pcbL11C07.setText("jLabel6");

        pcbL11C08.setText("jLabel6");

        pcbL11C09.setText("jLabel6");

        pcbL12C10.setText("jLabel6");

        pcbL12C11.setText("jLabel6");

        pcbL12C12.setText("jLabel6");

        pcbL12C01.setText("jLabel6");

        pcbL12C02.setText("jLabel6");

        pcbL12C03.setText("jLabel6");

        pcbL12C04.setText("jLabel6");

        pcbL09C08.setText("jLabel6");

        pcbL12C05.setText("jLabel6");

        pcbL12C06.setText("jLabel6");

        pcbL12C07.setText("jLabel6");

        pcbL12C08.setText("jLabel6");

        pcbL12C09.setText("jLabel6");

        pcbL09C09.setText("jLabel6");

        menuParametres.setText("Paramètres");
        menuParametres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuParametresActionPerformed(evt);
            }
        });

        menuVoir.setText("Voir");
        menuVoir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVoirActionPerformed(evt);
            }
        });
        menuParametres.add(menuVoir);

        paramsProg.setText("Programmateur");
        paramsProg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paramsProgActionPerformed(evt);
            }
        });
        menuParametres.add(paramsProg);

        menuCreer.setText("Créer");
        menuCreer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCreerActionPerformed(evt);
            }
        });
        menuParametres.add(menuCreer);
        menuParametres.add(jSeparator1);

        btnFermer.setText("Fermer");
        btnFermer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFermerActionPerformed(evt);
            }
        });
        menuParametres.add(btnFermer);

        jMenuBar1.add(menuParametres);

        menuConnexion.setText("Communication");
        menuConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuConnexionActionPerformed(evt);
            }
        });

        menuPort.setText("Ports");
        menuPort.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuPortStateChanged(evt);
            }
        });
        menuPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPortActionPerformed(evt);
            }
        });
        menuConnexion.add(menuPort);

        menuBaud.setText("Baud");

        groupBaud.add(baud9600);
        baud9600.setSelected(true);
        baud9600.setText("9600");
        menuBaud.add(baud9600);

        groupBaud.add(baud19200);
        baud19200.setText("19200");
        menuBaud.add(baud19200);

        groupBaud.add(baud38400);
        baud38400.setText("38400");
        menuBaud.add(baud38400);

        groupBaud.add(baud115200);
        baud115200.setText("115200");
        menuBaud.add(baud115200);

        menuConnexion.add(menuBaud);

        menuBits.setText("Bits");

        groupBits.add(bits6);
        bits6.setSelected(true);
        bits6.setText("6");
        menuBits.add(bits6);

        groupBits.add(bits7);
        bits7.setText("7");
        menuBits.add(bits7);

        groupBits.add(bits8);
        bits8.setText("8");
        menuBits.add(bits8);

        groupBits.add(bits9);
        bits9.setText("9");
        menuBits.add(bits9);

        menuConnexion.add(menuBits);

        menuStop.setText("Stop");

        groupStop.add(stop1);
        stop1.setSelected(true);
        stop1.setText("1");
        menuStop.add(stop1);

        groupStop.add(stop2);
        stop2.setText("2");
        menuStop.add(stop2);

        menuConnexion.add(menuStop);

        menuParity.setText("Parity");

        groupParity.add(parityNone);
        parityNone.setSelected(true);
        parityNone.setText("None");
        menuParity.add(parityNone);

        groupParity.add(parityOdd);
        parityOdd.setText("Odd");
        menuParity.add(parityOdd);

        groupParity.add(parityEven);
        parityEven.setText("Even");
        menuParity.add(parityEven);

        menuConnexion.add(menuParity);
        menuConnexion.add(jSeparator2);

        btnConnexion.setText("Connexion");
        btnConnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnexionActionPerformed(evt);
            }
        });
        menuConnexion.add(btnConnexion);
        menuConnexion.add(jSeparator3);

        btnDeconnexion.setText("Déconnexion");
        btnDeconnexion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeconnexionActionPerformed(evt);
            }
        });
        menuConnexion.add(btnDeconnexion);

        jMenuBar1.add(menuConnexion);

        menuAide.setText("Autres");
        menuAide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAideActionPerformed(evt);
            }
        });

        menuItemMono.setSelected(true);
        menuItemMono.setText("Mono");
        menuItemMono.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuItemMonoStateChanged(evt);
            }
        });
        menuItemMono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemMonoActionPerformed(evt);
            }
        });
        menuAide.add(menuItemMono);

        voirAide.setText("Aide");
        voirAide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voirAideActionPerformed(evt);
            }
        });
        menuAide.add(voirAide);

        jMenuBar1.add(menuAide);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 32, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(console, javax.swing.GroupLayout.PREFERRED_SIZE, 893, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(108, 108, 108)
                            .addComponent(progBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 893, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(231, 231, 231)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(pcbL01C01)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C02)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL01C03)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C04)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C05)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C06)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL01C07)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C08)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C09)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(pcbL01C11)
                                    .addGap(18, 18, 18)
                                    .addComponent(pcbL01C12))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL03C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL03C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL03C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL03C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL03C12))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL02C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL02C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL02C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL02C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL02C12)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(pcbL05C01)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C02)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL05C03)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C04)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C05)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C06)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL05C07)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C08)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C09)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL05C11)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL05C12))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(pcbL06C01)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C02)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL06C03)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C04)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C05)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C06)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL06C07)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C08)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C09)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(pcbL06C11)
                                            .addGap(18, 18, 18)
                                            .addComponent(pcbL06C12))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(pcbL12C01)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C02)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL12C03)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C04)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C05)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C06)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL12C07)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C08)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C09)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C10)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL12C11)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL12C12))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(pcbL09C01)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C02)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL09C03)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C04)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C05)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C06)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL09C07)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C08)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C09)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C10)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL09C11)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL09C12))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(pcbL11C01)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C02)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pcbL11C03)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C04)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C05)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C06)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pcbL11C07)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C08)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C09)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C10)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pcbL11C11)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL11C12))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(pcbL10C01)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C02)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C03)
                                                        .addGap(10, 10, 10)
                                                        .addComponent(pcbL10C04)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C05)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C06)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pcbL10C07)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C08)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C09)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C10)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(pcbL10C11)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(pcbL10C12))))
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(pcbL08C01)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C02)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL08C03)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C04)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C05)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C06)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL08C07)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C08)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C09)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C10)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL08C11)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL08C12))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(pcbL07C01)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C02)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL07C03)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C04)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C05)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C06)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL07C07)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C08)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C09)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C10)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(pcbL07C11)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(pcbL07C12)))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbL04C01)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C02)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL04C03)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C04)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C05)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C06)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL04C07)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C08)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C09)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(pcbL04C11)
                                        .addGap(18, 18, 18)
                                        .addComponent(pcbL04C12)))))))
                .addGap(162, 162, 162))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(257, 257, 257)
                .addComponent(btnEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(btnProg, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(btnACQ, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nomProduit, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
                            .addComponent(emplacementBinaire, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(StatutRS232Lab, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(titre, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(253, 253, 253)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(statutRs232, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statutPRGLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statutPGRM, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(69, 69, 69))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(StatutRS232Lab)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(statutRs232, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(statutPRGLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statutPGRM, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(nomProduit, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(emplacementBinaire, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL01C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL01C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL02C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL02C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL03C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL03C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL04C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL04C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL05C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL05C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL06C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL06C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL07C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL07C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL08C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL08C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL09C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL09C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL10C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL10C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL11C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL11C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbL12C01, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C02, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C03, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C04, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C05, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C06, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C07, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C08, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C09, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C11, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pcbL12C12, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(progBarre, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(console, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEffacer, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnProg, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnACQ, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titre, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProgActionPerformed

        dateOfStart = LocalDateTime.now();

        try {
            getActivityPeriod();
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

        connecteur.envoyerData(Character.toString('t'));
        echo = false;

        if (!confirmationParams) {

            boolean confirmation = confirmeParams();
            if (!confirmation) {

                return;

            } else {

                confirmationParams = true;
            }
        }

        menuParametres.setEnabled(false);
        menuConnexion.setEnabled(false);
        console.setText("Cycle de programmation en cours...");
        progBarre.setString("Programmation en cours...");
        programmationActive = true;
        progBarre.setVisible(true);
        activerBtnProgrammer(false);
        activerBtnEffacer(true);
        activerProgrammation();


    }//GEN-LAST:event_btnProgActionPerformed

    private void btnEffacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEffacerActionPerformed

        AfficheurPanneau.setVisible(true);
        // TEST PROGRAMMATION UNIQUE
        /*
            Thread t = new Thread() {
            public void run() {
            
            try {
            connecteur.singleProgramme(hexLocationsParamsProperties, envVariable, programmerPathParamsProperties, programmerParamsProperties, deviceEnTest, binaireLocation, intNombreDeVoiesCarteEnTest, programmerPathTempFileDirectory);
            //System.out.println("Retour programmation. Code reçu: " + comm);
            
            } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            };
            
            t.start();
         */

    }//GEN-LAST:event_btnEffacerActionPerformed

    private void btnSelectLocationProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectLocationProgActionPerformed


    }//GEN-LAST:event_btnSelectLocationProgActionPerformed

    private void btnSelectBinaryLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectBinaryLocActionPerformed


    }//GEN-LAST:event_btnSelectBinaryLocActionPerformed

    private void btnFermerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerActionPerformed

        initializer.update("item", "none");
        System.exit(0);
    }//GEN-LAST:event_btnFermerActionPerformed

    private void paramsProgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paramsProgActionPerformed

        programmerLoc.showOpenDialog(this);
        programmerLocation = programmerLoc.getSelectedFile();
        if (programmerLoc.getSelectedFile() != null) {

            console.setText("Repertoire programmateur: " + programmerLocation.getPath());
            //System.out.println("Repertoire programmateur: " + programmerLocation.getPath());
            programmerPathParamsProperties = programmerLocation.getPath();
            initializer.update("programmerDirectory", programmerPathParamsProperties);

        }

        testParamsProg();
    }//GEN-LAST:event_paramsProgActionPerformed

    private void menuVoirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVoirActionPerformed

        paramsWin.setVisible(true);
        activerFonctionAjouter(false);
        EnvVarBox.setSelected(envVariable);

        if (comboListeProduits.getSelectedIndex() == 0) {

            hexLocalisation.setText("Aucun produit sélectionné.");

        }

        if (programmerPathParamsProperties != null || !programmerPathParamsProperties.equals("na")) {

            progLocLabel.setText("Repertoire programmateur: " + programmerPathParamsProperties);

        } else {

            progLocLabel.setText("Repertoire programmateur non défini!");
        }

    }//GEN-LAST:event_menuVoirActionPerformed

    private void menuAideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAideActionPerformed


    }//GEN-LAST:event_menuAideActionPerformed

    private void voirAideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voirAideActionPerformed

        aide.setVisible(true);
    }//GEN-LAST:event_voirAideActionPerformed

    private void menuParametresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuParametresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuParametresActionPerformed

    private void btnConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnexionActionPerformed

        int i = connecteur.makeConnection(Connecteur.portName, baudeRate, numDatabits, parity, stopBits, false);
        connecteur.envoyerData(Character.toString('t'));
        if (!testEcho()) {

            montrerError("La banc ne répond pas! Vérifiez les connexions et que le banc est sous-tension", "Erreur banc");
            echo = false;
            return;

        }

        if (i == 99) {

            console.setForeground(Color.BLUE);
            console.setText("Connexion réussie");
            setStatusRS232(true);
            btnConnexion.setEnabled(false);
            btnDeconnexion.setEnabled(true);
            connexionRS232Active = true;
            //activerBtnAttenteLancement();
            //activerBtnTester(true);
            //activerBtnProgrammer(true);
            testParamsProg();

        } else {

            console.setForeground(Color.red);
            console.setText("Tentative de connexion échouée");
            setStatusRS232(false);

        }

        setEnabledMenusConfiguration();


    }//GEN-LAST:event_btnConnexionActionPerformed

    private void btnDeconnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeconnexionActionPerformed

        int i = connecteur.disconnect();
        if (i == 0) {
            console.setForeground(Color.BLUE);
            console.setText("Déconnexion réussie");
            setStatusRS232(false);
            btnConnexion.setEnabled(true);
            btnDeconnexion.setEnabled(false);
            connexionRS232Active = false;
            //inhibBtn();
            activerBtnProgrammer(false);

        }
    }//GEN-LAST:event_btnDeconnexionActionPerformed

    private void menuConnexionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuConnexionActionPerformed

        rechercherPortsComms();

    }//GEN-LAST:event_menuConnexionActionPerformed

    private void menuPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuPortActionPerformed

    private void btnACQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnACQActionPerformed

        echo = false;
        checkEcho();
        if (panne) {

            console.setForeground(Color.red);
            console.setText("PROBLEME PERSISTANT!");
            activerBtnACQ(false);
            activerBtnProgrammer(false);
            activerBtnEffacer(false);
            return;
        }
        console.setText("RESULTAT ACQUITTE - PRET POUR UN NOUVEAU CYCLE DE PROGRAMMATION!");
        activerBtnACQ(false);
        activerBtnProgrammer(true);
        activerBtnEffacer(true);
        raffraichirInterface();

        progBarre.setValue(0);
        progBarre.setString("En attente lancement de programmation");
        progBarre.setStringPainted(true);
        if (CYCLES == Constants.CYCLES_LIM) {

            System.err.println("RELANCE APRES INTERRUPTION PROCESSUS!");
            connecteur.askForKillingProcess();
            CYCLES = 0;
            STOPS++;

        }

        TOTAL++;

        System.out.println("Nombre de cycles total: " + TOTAL);
        System.out.println("Nombre d'interruptions: " + STOPS);
        System.out.println("Nombre de cycles en cours: " + CYCLES);
        System.out.println("Nombre de programmations: " + TOTAL * intNombreDeVoiesCarteEnTest);


    }//GEN-LAST:event_btnACQActionPerformed

    private void nombreVoiesNouvelleCarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreVoiesNouvelleCarteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreVoiesNouvelleCarteActionPerformed

    private void messageBinaireSelectionneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_messageBinaireSelectionneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messageBinaireSelectionneActionPerformed

    private void btnEnregistrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnregistrerActionPerformed

        if (nomNouvelleCarte.getText().equals("")) {

            messageCreation.setText("Le nom de la nouvelle carte doit être défini!");
            montrerError("Le nom de la nouvelle carte doit être défini", "Formulaire imcomplet");

            return;
        } else {

            nomNouveauBinaire = nomNouvelleCarte.getText();
        }

        if (localisationNouveauBinaire == null) {

            messageCreation.setText("Indiquer l'accès au bianire");
            montrerError("L'accès au binaire doit être défini", "Formulaire imcomplet");
            return;
        }

        if (nombreVoiesNouvelleCarte.getText().equals("")) {

            montrerError("Le nombre de voies doit être défini!", "Formulaire imcomplet");
            return;
        } else {

            try {

                nombreDeVoiesNouvelleCarte = nombreVoiesNouvelleCarte.getText();
                intNombreDeVoiesNouvelleCarte = Integer.parseInt(nombreDeVoiesNouvelleCarte);

            } catch (Exception e) {

                //System.out.println("Exception nombre de voie: " + nombreDeVoiesNouvelleCarte);
                montrerError("Le nombre de voies doit être compris entre 1 et " + limCommutateur, "Formulaire imcomplet");
                return;

            }

        }

        if (nouveauMicroController.getText().equals("")) {

            messageCreation.setText("Le nom du mirocontrôleur doit être défini!");
            montrerError("Le nom du mirocontrôleur doit être défini!", "Formulaire imcomplet");

            return;
        } else {

            nouveauDevice = nouveauMicroController.getText();
        }

        if (nouvelleMatrice.getText().equals("")) {

            messageCreation.setText("La matrice doit être définie!");
            montrerError("La nouvelle matrice doit être définie", "Formulaire imcomplet");
            return;
        } else {

            verifierNouvelleMatrice(nouvelleMatrice.getText(), intNombreDeVoiesNouvelleCarte);

        }

        matriceNouveauPanneau = nouvelleMatrice.getText();
        enregistrerNouvelleCarte(localisationNouveauBinaire, nomNouveauBinaire, nombreDeVoiesNouvelleCarte, nouveauDevice, matriceNouveauPanneau);

        montrerError(
                "La nouvelle carte à été enregistrée", "Enregistrement effectué");

        paramsWin.setVisible(
                false);

    }//GEN-LAST:event_btnEnregistrerActionPerformed

    private void btnSelectionBinaireAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectionBinaireAjouterActionPerformed

        programmerLoc.showOpenDialog(this);
        nouveauBinaire = programmerLoc.getSelectedFile();
        if (programmerLoc.getSelectedFile() != null) {

            console.setText("Repertoire binaire: " + nouveauBinaire.getPath());
            localisationNouveauBinaire = nouveauBinaire.getPath();
            //System.out.println("Binaire sélectionné: " + localisationNouveauBinaire);
            messageBinaireSelectionne.setText("Binaire sélectionné: " + localisationNouveauBinaire);

        } else {

            //System.out.println("Aucun binaire sélectionné!");
        }

        //testParamsProg();
    }//GEN-LAST:event_btnSelectionBinaireAjouterActionPerformed

    private void nomNouvelleCarteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomNouvelleCarteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomNouvelleCarteActionPerformed

    private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAjouterActionPerformed

        activerFonctionAjouter(true);
    }//GEN-LAST:event_btnAjouterActionPerformed

    private void comboListeProduitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboListeProduitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboListeProduitsActionPerformed

    private void comboListeProduitsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboListeProduitsItemStateChanged
        //System.out.println("item selected: " + comboListeProduits.getSelectedIndex());

        if (comboListeProduits.getSelectedIndex() != 0) {

            hexLocalisation.setText(ListeBinairesEnregistres.get(comboListeProduits.getSelectedIndex() - 1));
            nombreVoies.setText(listesVoies.get(comboListeProduits.getSelectedIndex() - 1));
            nombreDeVoiesCarteEnTest = listesVoies.get(comboListeProduits.getSelectedIndex() - 1);
            deviceEnTest = listeDevicesEnregistres.get(comboListeProduits.getSelectedIndex() - 1);
            matriceAprogrammer = listesMatrices.get(comboListeProduits.getSelectedIndex() - 1);
            extraireLignesColonnes(matriceAprogrammer);

        } else {

            hexLocalisation.setText("Aucun produit sélectionné!");
            nombreVoies.setText("");
            nombreDeVoiesCarteEnTest = null;
            deviceEnTest = null;
            produitAprogrammer = null;
            matriceAprogrammer = null;

            testParamsProg();
        }

    }//GEN-LAST:event_comboListeProduitsItemStateChanged

    private void EnvVarBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnvVarBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EnvVarBoxActionPerformed

    private void EnvVarBoxStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_EnvVarBoxStateChanged

        if (EnvVarBox.isSelected()) {

            progLocLabel.setText("Variable d'environnement définie!");

        } else {

            progLocLabel.setText("Vérifier variables d'environnement! JAVA doit être dans le path");

        }
    }//GEN-LAST:event_EnvVarBoxStateChanged

    private void btnFermerParamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFermerParamsActionPerformed

        paramsWin.setSize(1300, 600);
        paramsWin.setVisible(false);

        /*
        if (EnvVarBox.isSelected()) {

            envVariable = true;

        } else {

            envVariable = false;
        }

        selectedProduct = comboListeProduits.getSelectedIndex();

        if (selectedProduct != 0) {

            produitAprogrammer = listesProduits.get(selectedProduct);
            nomProduit.setText(produitAprogrammer + " - Microcontôleur: " + deviceEnTest + " - Voies: " + nombreDeVoiesCarteEnTest + " - Programmateur: " + programmerParamsProperties + " - Matrice: " + matriceAprogrammer);
            binaireLocation = ListeBinairesEnregistres.get(selectedProduct - 1);
            //System.out.println("localistaion binaire: " + binaireLocation);
            emplacementBinaire.setText(binaireLocation);
            intNombreDeVoiesCarteEnTest = Integer.parseInt(nombreDeVoiesCarteEnTest);
            //System.out.println("nombre de voies carte en test (int): " + intNombreDeVoiesCarteEnTest);
            raffraichirInterface();
            initializer.update("item", String.valueOf(selectedProduct));

        } else {

            nomProduit.setText("Aucun produit sélectionné!");
            emplacementBinaire.setText("");
            nombreDeVoiesCarteEnTest = null;
            deviceEnTest = null;
            produitAprogrammer = null;

        }

        testParamsProg();
        raffraichirImagePanneau();
         */
        initInterface();

    }//GEN-LAST:event_btnFermerParamsActionPerformed

    private void nouveauMicroControllerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauMicroControllerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nouveauMicroControllerActionPerformed

    private void nouvelleMatriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouvelleMatriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nouvelleMatriceActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        aide.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void menuPortStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuPortStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_menuPortStateChanged

    private void menuItemMonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemMonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuItemMonoActionPerformed

    private void menuItemMonoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuItemMonoStateChanged

        mono = menuItemMono.isSelected();
        if (mono) {
            this.getContentPane().setBackground(new Color(140, 3, 252));
            console.setText("Programmation unitaire activée sur l'emplacement " + monoLocation);
        } else {
            this.getContentPane().setBackground(new Color(50, 131, 168));
        }
        raffraichirInterface();

    }//GEN-LAST:event_menuItemMonoStateChanged

    private void menuCreerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCreerActionPerformed

        paramsWin.setVisible(true);
        EnvVarBox.setSelected(envVariable);
        activerFonctionAjouter(true);
        if (filePath != null) {
            progLocLabel.setText("Repertoire programmateur: " + filePath);
        } else {

            progLocLabel.setText("Repertoire programmateur non défini!");
        }
        messageCreation.setText("Compléter le formulaire ci-dessous pour ajouter un produit à programmer!");
    }//GEN-LAST:event_menuCreerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Interface.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Interface.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Interface.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Interface.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Interface().setVisible(true);

                } catch (IOException ex) {
                    Logger.getLogger(Interface.class
                            .getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame AfficheurPanneau;
    private javax.swing.JFrame AlerteLancement;
    private javax.swing.JCheckBox EnvVarBox;
    private javax.swing.JLabel LabNombreVoies;
    private javax.swing.JLabel LabelNbreDeVoiesNouvelleCarte;
    private javax.swing.JLabel LabelNouvelleMatrice;
    private javax.swing.JLabel LabelmicroController;
    private javax.swing.JLabel LabfichierBinaire;
    private javax.swing.JLabel StatutRS232Lab;
    private javax.swing.JFrame aide;
    private javax.swing.JRadioButtonMenuItem baud115200;
    private javax.swing.JRadioButtonMenuItem baud19200;
    private javax.swing.JRadioButtonMenuItem baud38400;
    private javax.swing.JRadioButtonMenuItem baud9600;
    private javax.swing.JFileChooser binaryLoc;
    private javax.swing.JRadioButtonMenuItem bits6;
    private javax.swing.JRadioButtonMenuItem bits7;
    private javax.swing.JRadioButtonMenuItem bits8;
    private javax.swing.JRadioButtonMenuItem bits9;
    private javax.swing.JButton btnACQ;
    private javax.swing.JButton btnAjouter;
    private javax.swing.JMenuItem btnConnexion;
    private javax.swing.JMenuItem btnDeconnexion;
    private javax.swing.JButton btnEffacer;
    private javax.swing.JButton btnEnregistrer;
    private javax.swing.JMenuItem btnFermer;
    private javax.swing.JButton btnFermerParams;
    private javax.swing.JButton btnProg;
    private javax.swing.JButton btnSelectBinaryLoc;
    private javax.swing.JButton btnSelectLocationProg;
    private javax.swing.JButton btnSelectionBinaireAjouter;
    private javax.swing.JComboBox<String> comboListeProduits;
    private javax.swing.JLabel console;
    private javax.swing.JLabel emplacementBinaire;
    private javax.swing.ButtonGroup groupBaud;
    private javax.swing.ButtonGroup groupBits;
    private javax.swing.ButtonGroup groupParity;
    private javax.swing.ButtonGroup groupPorts;
    private javax.swing.ButtonGroup groupStop;
    private javax.swing.JLabel hexLocalisation;
    private javax.swing.JLabel imagePanneau;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel labelAjoutCarte;
    private javax.swing.JLabel labelBinaireSelectionne;
    private javax.swing.JLabel listeProduits;
    private javax.swing.JMenu menuAide;
    private javax.swing.JMenu menuBaud;
    private javax.swing.JMenu menuBits;
    private javax.swing.JMenu menuConnexion;
    private javax.swing.JMenuItem menuCreer;
    private javax.swing.JCheckBoxMenuItem menuItemMono;
    private javax.swing.JMenu menuParametres;
    private javax.swing.JMenu menuParity;
    private javax.swing.JMenu menuPort;
    private javax.swing.JMenu menuStop;
    private javax.swing.JMenuItem menuVoir;
    private javax.swing.JTextField messageBinaireSelectionne;
    private javax.swing.JLabel messageCreation;
    private javax.swing.JTextField nomNouvelleCarte;
    private javax.swing.JLabel nomProduit;
    private javax.swing.JLabel nombreVoies;
    private javax.swing.JTextField nombreVoiesNouvelleCarte;
    private javax.swing.JTextField nouveauMicroController;
    private javax.swing.JTextField nouvelleMatrice;
    private javax.swing.JMenuItem paramsProg;
    private javax.swing.JFrame paramsWin;
    private javax.swing.JRadioButtonMenuItem parityEven;
    private javax.swing.JRadioButtonMenuItem parityNone;
    private javax.swing.JRadioButtonMenuItem parityOdd;
    private javax.swing.JLabel pcbL01C01;
    private javax.swing.JLabel pcbL01C02;
    private javax.swing.JLabel pcbL01C03;
    private javax.swing.JLabel pcbL01C04;
    private javax.swing.JLabel pcbL01C05;
    private javax.swing.JLabel pcbL01C06;
    private javax.swing.JLabel pcbL01C07;
    private javax.swing.JLabel pcbL01C08;
    private javax.swing.JLabel pcbL01C09;
    private javax.swing.JLabel pcbL01C10;
    private javax.swing.JLabel pcbL01C11;
    private javax.swing.JLabel pcbL01C12;
    private javax.swing.JLabel pcbL02C01;
    private javax.swing.JLabel pcbL02C02;
    private javax.swing.JLabel pcbL02C03;
    private javax.swing.JLabel pcbL02C04;
    private javax.swing.JLabel pcbL02C05;
    private javax.swing.JLabel pcbL02C06;
    private javax.swing.JLabel pcbL02C07;
    private javax.swing.JLabel pcbL02C08;
    private javax.swing.JLabel pcbL02C09;
    private javax.swing.JLabel pcbL02C10;
    private javax.swing.JLabel pcbL02C11;
    private javax.swing.JLabel pcbL02C12;
    private javax.swing.JLabel pcbL03C01;
    private javax.swing.JLabel pcbL03C02;
    private javax.swing.JLabel pcbL03C03;
    private javax.swing.JLabel pcbL03C04;
    private javax.swing.JLabel pcbL03C05;
    private javax.swing.JLabel pcbL03C06;
    private javax.swing.JLabel pcbL03C07;
    private javax.swing.JLabel pcbL03C08;
    private javax.swing.JLabel pcbL03C09;
    private javax.swing.JLabel pcbL03C10;
    private javax.swing.JLabel pcbL03C11;
    private javax.swing.JLabel pcbL03C12;
    private javax.swing.JLabel pcbL04C01;
    private javax.swing.JLabel pcbL04C02;
    private javax.swing.JLabel pcbL04C03;
    private javax.swing.JLabel pcbL04C04;
    private javax.swing.JLabel pcbL04C05;
    private javax.swing.JLabel pcbL04C06;
    private javax.swing.JLabel pcbL04C07;
    private javax.swing.JLabel pcbL04C08;
    private javax.swing.JLabel pcbL04C09;
    private javax.swing.JLabel pcbL04C10;
    private javax.swing.JLabel pcbL04C11;
    private javax.swing.JLabel pcbL04C12;
    private javax.swing.JLabel pcbL05C01;
    private javax.swing.JLabel pcbL05C02;
    private javax.swing.JLabel pcbL05C03;
    private javax.swing.JLabel pcbL05C04;
    private javax.swing.JLabel pcbL05C05;
    private javax.swing.JLabel pcbL05C06;
    private javax.swing.JLabel pcbL05C07;
    private javax.swing.JLabel pcbL05C08;
    private javax.swing.JLabel pcbL05C09;
    private javax.swing.JLabel pcbL05C10;
    private javax.swing.JLabel pcbL05C11;
    private javax.swing.JLabel pcbL05C12;
    private javax.swing.JLabel pcbL06C01;
    private javax.swing.JLabel pcbL06C02;
    private javax.swing.JLabel pcbL06C03;
    private javax.swing.JLabel pcbL06C04;
    private javax.swing.JLabel pcbL06C05;
    private javax.swing.JLabel pcbL06C06;
    private javax.swing.JLabel pcbL06C07;
    private javax.swing.JLabel pcbL06C08;
    private javax.swing.JLabel pcbL06C09;
    private javax.swing.JLabel pcbL06C10;
    private javax.swing.JLabel pcbL06C11;
    private javax.swing.JLabel pcbL06C12;
    private javax.swing.JLabel pcbL07C01;
    private javax.swing.JLabel pcbL07C02;
    private javax.swing.JLabel pcbL07C03;
    private javax.swing.JLabel pcbL07C04;
    private javax.swing.JLabel pcbL07C05;
    private javax.swing.JLabel pcbL07C06;
    private javax.swing.JLabel pcbL07C07;
    private javax.swing.JLabel pcbL07C08;
    private javax.swing.JLabel pcbL07C09;
    private javax.swing.JLabel pcbL07C10;
    private javax.swing.JLabel pcbL07C11;
    private javax.swing.JLabel pcbL07C12;
    private javax.swing.JLabel pcbL08C01;
    private javax.swing.JLabel pcbL08C02;
    private javax.swing.JLabel pcbL08C03;
    private javax.swing.JLabel pcbL08C04;
    private javax.swing.JLabel pcbL08C05;
    private javax.swing.JLabel pcbL08C06;
    private javax.swing.JLabel pcbL08C07;
    private javax.swing.JLabel pcbL08C08;
    private javax.swing.JLabel pcbL08C09;
    private javax.swing.JLabel pcbL08C10;
    private javax.swing.JLabel pcbL08C11;
    private javax.swing.JLabel pcbL08C12;
    private javax.swing.JLabel pcbL09C01;
    private javax.swing.JLabel pcbL09C02;
    private javax.swing.JLabel pcbL09C03;
    private javax.swing.JLabel pcbL09C04;
    private javax.swing.JLabel pcbL09C05;
    private javax.swing.JLabel pcbL09C06;
    private javax.swing.JLabel pcbL09C07;
    private javax.swing.JLabel pcbL09C08;
    private javax.swing.JLabel pcbL09C09;
    private javax.swing.JLabel pcbL09C10;
    private javax.swing.JLabel pcbL09C11;
    private javax.swing.JLabel pcbL09C12;
    private javax.swing.JLabel pcbL10C01;
    private javax.swing.JLabel pcbL10C02;
    private javax.swing.JLabel pcbL10C03;
    private javax.swing.JLabel pcbL10C04;
    private javax.swing.JLabel pcbL10C05;
    private javax.swing.JLabel pcbL10C06;
    private javax.swing.JLabel pcbL10C07;
    private javax.swing.JLabel pcbL10C08;
    private javax.swing.JLabel pcbL10C09;
    private javax.swing.JLabel pcbL10C10;
    private javax.swing.JLabel pcbL10C11;
    private javax.swing.JLabel pcbL10C12;
    private javax.swing.JLabel pcbL11C01;
    private javax.swing.JLabel pcbL11C02;
    private javax.swing.JLabel pcbL11C03;
    private javax.swing.JLabel pcbL11C04;
    private javax.swing.JLabel pcbL11C05;
    private javax.swing.JLabel pcbL11C06;
    private javax.swing.JLabel pcbL11C07;
    private javax.swing.JLabel pcbL11C08;
    private javax.swing.JLabel pcbL11C09;
    private javax.swing.JLabel pcbL11C10;
    private javax.swing.JLabel pcbL11C11;
    private javax.swing.JLabel pcbL11C12;
    private javax.swing.JLabel pcbL12C01;
    private javax.swing.JLabel pcbL12C02;
    private javax.swing.JLabel pcbL12C03;
    private javax.swing.JLabel pcbL12C04;
    private javax.swing.JLabel pcbL12C05;
    private javax.swing.JLabel pcbL12C06;
    private javax.swing.JLabel pcbL12C07;
    private javax.swing.JLabel pcbL12C08;
    private javax.swing.JLabel pcbL12C09;
    private javax.swing.JLabel pcbL12C10;
    private javax.swing.JLabel pcbL12C11;
    private javax.swing.JLabel pcbL12C12;
    private javax.swing.JProgressBar progBarre;
    private javax.swing.JLabel progLocLabel;
    private javax.swing.JFileChooser programmerLoc;
    private javax.swing.JLabel statutPGRM;
    private javax.swing.JLabel statutPRGLabel;
    private javax.swing.JLabel statutRs232;
    private javax.swing.JRadioButtonMenuItem stop1;
    private javax.swing.JRadioButtonMenuItem stop2;
    private javax.swing.JLabel titre;
    private javax.swing.JLabel titreLabProg;
    private javax.swing.JLabel titreParamsWin;
    private javax.swing.JMenuItem voirAide;
    // End of variables declaration//GEN-END:variables

    private void testParamsProg() {

        //System.out.println("produit à programmer: " + produitAprogrammer);
        if (produitAprogrammer == null) {

            console.setText("Sélectionnez un produit avant de commencer!");

            activerLedPRGM(false);
            return;

        }

        if (!envVariable) {

            console.setText("Vérifier que JAVA est ajouté aux variables d'environnement");

            activerLedPRGM(false);
            return;

        }

        if (programmerPathParamsProperties == null || programmerPathParamsProperties.equals("na")) {

            console.setText("Le programmateur n'est pas localisé");

            activerLedPRGM(false);
            return;

        }

        console.setText("Paramètres de programmation définis. Vous pouvez commencer!");
        activerLedPRGM(true);

        if (envVariable) {

            initializer.update("varEnv", "true");

        } else {

            initializer.update("varEnv", "false");
        }

        if (connexionRS232Active) {

            statutRs232.setBackground(Color.GREEN);
            activerBtnProgrammer(true);
            activerBtnEffacer(true);

        } else {

            statutRs232.setBackground(Color.RED);
            activerBtnProgrammer(false); // forcer pour besoin de test
            activerBtnEffacer(false); // forcer pour besoin de test

        }

    }

    public void montrerError(String message, String titre) {

        JOptionPane.showMessageDialog(this, message, titre, JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmeParams() {

        int response = JOptionPane.showConfirmDialog(this, "Confirmez-vous ces paramètres?", "Paramètres de programmation définis", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.NO_OPTION) {
            //System.out.println("No button clicked");
            return false;
        }
        if (response == JOptionPane.YES_OPTION) {

            return true;

        }
        return false;

    }

    private Connecteur getConnecteur() {

        if (this.connecteur == null) {
            this.connecteur = new Connecteur();
            this.connecteur.addObserver(this);
        }
        return this.connecteur;

    }

    @Override
    public void update(Observable o, Object arg) {

        if (arg instanceof Integer) {

            progBarre.setValue((100 / intNombreDeVoiesCarteEnTest) * (Integer) arg);
            console.setText("Numéro de carte: " + (Integer) arg);

        }

        if (arg instanceof String) {

            if (((String) arg).startsWith("->PROG")) {
                console.setText((String) arg);
                String[] tab = ((String) arg).trim().split(":");
                raffraichirIndicateur(tab);
                progBarre.setValue((100 / intNombreDeVoiesCarteEnTest) * Integer.parseInt(tab[1]));
                if ((100 / intNombreDeVoiesCarteEnTest) * Integer.parseInt(tab[1]) == 100) {

                } else {

                    progBarre.setString("Programmation en cours...");
                }

                if (Integer.parseInt(tab[1]) == intNombreDeVoiesCarteEnTest) {
                    progBarre.setValue(100);
                    progBarre.setString("Programmation terminée!");
                    dateOfEnd = LocalDateTime.now();
                    long delay = dateOfStart.until(dateOfEnd, ChronoUnit.SECONDS);
                    long delay2 = dateOfStart.until(dateOfEnd, ChronoUnit.MINUTES);
                    System.out.println("Durée du cycle de programmation: " + delay + "s");

                    CYCLES++;

                }

                if (Integer.parseInt(tab[2]) == -33) {

                    compteurReset++;
                    System.out.println("demande relance après interruption processus");
                    progBarre.setString("RESET en cours...");
                    if (compteurReset < 3) {

                        activerProgrammation();

                    } else {

                        montrerError("Problème d'alimentation!\nVérifiez que le banc est sous tension et relancez l'application", "Erreur banc");
                        console.setText("PROBLEME D'ALIMENTATION");
                        inhibBtn();
                        menuParametres.setEnabled(true);
                        menuConnexion.setEnabled(true);

                    }

                    TOTAL = CYCLES;
                    CYCLES = 0;
                }

            }

            if (((String) arg).startsWith("->START")) {
                console.setText((String) arg);
                String[] tab = ((String) arg).trim().split(":");
                raffraichirIndicateur(tab);

            }

            if (((String) arg).startsWith("->GR")) {
                System.out.println("reception echo");
                console.setText((String) arg);
                String[] tab = ((String) arg).trim().split(":");

                if (tab[2].equals("ON") || tab[2].equals("OFF")) {
                    echo = true;
                    System.out.println("echo = true - update");
                }
            }

        }

    }

    private void setEnabledMenusConfiguration() {

    }

    private void setStatusRS232(boolean statut) {

        if (statut) {

            statutRs232.setForeground(Color.GREEN);
            statutRs232.setBackground(Color.GREEN);
        } else {
            statutRs232.setForeground(Color.RED);
            statutRs232.setBackground(Color.RED);
        }

    }

    private void processRapport(String inputLine) {

        if (inputLine.trim().startsWith("-> INIT")) {

            //System.out.println("reset system");
            messageConsole(inputLine.trim());
            resetSystem();

        }

        // traitement des résultats aux étapes de test
        if (inputLine.trim().startsWith("-> COMMUTATION")) {

            String[] tab = inputLine.trim().split(":");
            int etape = Integer.parseInt(tab[1]);

        }

    }

    private void activerBtnReponseOp(boolean active) {

        if (active) {

            btnProg.setText("OK");
            btnProg.setBackground(new Color(163, 194, 240));
            btnProg.setEnabled(true);

            btnEffacer.setText("NON OK");
            btnEffacer.setBackground(new Color(163, 194, 240));
            btnEffacer.setEnabled(true);

        } else {

            btnProg.setText("OK");
            btnProg.setBackground(new Color(163, 194, 240));
            btnProg.setEnabled(false);

            btnEffacer.setText("NON OK");
            btnEffacer.setBackground(new Color(163, 194, 240));
            btnEffacer.setEnabled(false);

        }

    }

    private void activerBtnAcquittement(boolean active) {

        if (active) {

            btnProg.setEnabled(false);
            btnProg.setBackground(Color.GRAY);
            btnEffacer.setEnabled(false);
            btnEffacer.setBackground(Color.GRAY);

        } else {

        }

    }

    private void messageConsole(String message) {

        console.setText(message);

    }

    private void activerBtnProgrammation(boolean active) {

        if (active) {

            btnProg.setText("PROGRAMMER");
            btnProg.setEnabled(true);
            btnProg.setBackground(new Color(163, 194, 240));

            btnEffacer.setText("EFFACER");
            btnEffacer.setEnabled(true);
            btnEffacer.setBackground(new Color(163, 194, 240));
            testActif = false;
            auto = true;

        } else {

            btnProg.setText("PROGRAMMER");
            btnProg.setEnabled(false);
            btnProg.setBackground(Color.GRAY);

            btnEffacer.setText("EFFACER");
            btnEffacer.setEnabled(false);
            btnEffacer.setBackground(Color.GRAY);
            testActif = false;
            auto = false;

        }
    }

    private void waitForErasing(boolean active) {

        if (active) {

            console.setText("Effacement demandé");

            //System.out.println("Affichage yellow");
        } else {

            console.setText("Effacement terminé");

            //System.out.println("Affichage green");
        }

    }

    private void waitForProgramming(boolean active) {

        if (active) {

            console.setText("Programation en cours");

        } else {

            console.setText("Programation terminé");

        }

    }

    private void alerteRS232() {

        int i = connecteur.disconnect();
        if (i == 0) {

            console.setForeground(Color.BLUE);
            console.setText("La connexion a été perdue! Vous devez la rétablir.");
            setStatusRS232(false);
            btnConnexion.setEnabled(true);
            btnDeconnexion.setEnabled(false);
            connexionRS232Active = false;

            setEnabledMenusConfiguration();
            testActif = false;
            auto = false;
            // activerBtnProgrammation(false);
            inhibBtn();

        }

        montrerError("La connexion RS-232 a été perdue!\n Vérifer la connexion RS-232 et appuyer sur OK\n L'application va redémarrer", "Défaut de connexion");
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("java -jar keypadProgrammerModule2.jar");
            System.exit(0);

        } catch (IOException ex) {
            Logger.getLogger(Interface.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rechercherPortsComms() {

        menuPort.removeAll();

        listePorts.clear();
        listePortString.clear();
        if (!autoConnexion) {

            listePortString = connecteur.getListPorts();
        } else {

            listePortString = connecteur.getListPorts();
            //listePortString.add(serialPortModeAuto);

        }

        for (String p : listePortString) {

            JRadioButtonMenuItem m = new JRadioButtonMenuItem(p);
            groupPorts.add(m);
            m.addActionListener(new PortSupplier());
            menuPort.add(m);
        }

    }

    // Fonction d'activation des boutons selon phase en cours
    void activerBtnAttenteLancement() {

        activerBtnLancer(true);
        activerBtnACQ(false);
        activerBtnEffacer(false);
        activerBtnOK(false);
        activerBtnNOK(false);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    void activerBtnAttenteACQ() {

        activerBtnLancer(false);
        activerBtnACQ(true);
        activerBtnEffacer(false);
        activerBtnOK(false);
        activerBtnNOK(false);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    void activerBtnTestEnCours() {

        activerBtnLancer(false);
        activerBtnACQ(false);
        activerBtnEffacer(false);
        activerBtnOK(true);
        activerBtnNOK(true);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    void inhibBtn() {

        activerBtnLancer(false);
        activerBtnACQ(false);
        activerBtnEffacer(false);
        activerBtnOK(false);
        activerBtnNOK(false);
        activerBtnTester(false);
        activerBtnProgrammer(false);

    }

    // Fonctions d'activation des boutons
    void activerBtnLancer(boolean active) {

    }

    void activerBtnOK(boolean active) {

    }

    void activerBtnNOK(boolean active) {

    }

    void activerBtnTester(boolean active) {

    }

    void activerBtnACQ(boolean active) {

        echo = false;
        btnACQ.setOpaque(true);
        if (active) {

            btnACQ.setEnabled(true);
            btnACQ.setBackground(new Color(163, 194, 240));

        } else {

            btnACQ.setEnabled(false);
            btnACQ.setBackground(Color.GRAY);

        }
    }

    void activerBtnProgrammer(boolean active) {

        btnProg.setOpaque(true);
        if (active) {

            btnProg.setEnabled(true);
            btnProg.setBackground(new Color(163, 194, 240));

        } else {

            btnProg.setEnabled(false);
            btnProg.setBackground(Color.GRAY);

        }
    }

    void activerBtnEffacer(boolean active) {

        btnEffacer.setOpaque(true);
        if (active) {

            btnEffacer.setEnabled(true);
            btnEffacer.setBackground(new Color(163, 194, 240));

        } else {

            btnEffacer.setEnabled(false);
            btnEffacer.setBackground(Color.GRAY);

        }
    }

    private void clignottementVoyant() {

        while (AttenteReponseOperateur) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Interface.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(Interface.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void resetSystem() {

        //System.out.println("reset system");
        testActif = false;
        console.setText("Système réinitialisé");
        testParamsProg();

        progBarre.setValue(0);
        progBarre.setString("En attente lancement de programmation");
        progBarre.setStringPainted(true);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Interface.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        int comm = connecteur.envoyerData(Constants.START);
        if (comm == -1) {

            alerteRS232();
            return;

        }
        testActif = true;
        auto = true;

        // activerBtnReponseOp(testActif);
        activerBtnTestEnCours();
    }

    private void activerFonctionAjouter(boolean active) {

        if (active) {
            paramsWin.setSize(1300, 2600);
        } else {
            paramsWin.setSize(1300, 600);
        }

        btnAjouter.setVisible(!active);
        messageCreation.setVisible(active);
        labelAjoutCarte.setVisible(active);
        nomNouvelleCarte.setVisible(active);
        LabelNbreDeVoiesNouvelleCarte.setVisible(active);
        nombreVoiesNouvelleCarte.setVisible(active);
        btnSelectionBinaireAjouter.setVisible(active);
        btnEnregistrer.setVisible(active);
        messageBinaireSelectionne.setVisible(active);
        labelBinaireSelectionne.setVisible(active);
        LabelmicroController.setVisible(active);
        nouveauMicroController.setVisible(active);
        LabelNouvelleMatrice.setVisible(active);
        nouvelleMatrice.setVisible(active);
        if (active) {
            messageCreation.setText("Compléter le formulaire ci-dessous");
        }
    }

    private ArrayList<String> extraireProduits(String listeProduits) {

        String[] liste = listeProduits.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        arrList.add("---");
        for (int i = 0; i < liste.length; i++) {

            // System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private ArrayList<String> extraireLocalisationBinaires(String hexLocations) {

        String[] liste = hexLocations.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            //System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private ArrayList<String> extraireVoies(String listeVoies) {

        String[] liste = listeVoies.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            //System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private ArrayList<String> extraireMatrices(String matricesProperties) {

        String[] liste = matricesProperties.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            //System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;

    }

    private void extraireLignesColonnes(String matriceAprogrammer) {

        //System.out.println("Matrice à programmer: " + matriceAprogrammer);
        String[] tab = matriceAprogrammer.split("x");
        lignes = Integer.parseInt(tab[0]);
        colonnes = Integer.parseInt(tab[1]);
        //System.out.println("Lignes: " + lignes);
        //System.out.println("colonnes: " + colonnes);
    }

    private Boolean verifierNouvelleMatrice(String nouvelleMatrice, int voies) {

        //System.out.println("Matrice à programmer: " + nouvelleMatrice);
        try {
            String[] tab = nouvelleMatrice.split("x");
            lignes = Integer.parseInt(tab[0]);
            colonnes = Integer.parseInt(tab[1]);
            //System.out.println("Lignes: " + lignes);
            //System.out.println("colonnes: " + colonnes);
        } catch (Exception e) {

            montrerError("Format matrice non conforme!", "Erreur saisie");
            return false;
        }
        if (lignes * colonnes != voies) {

            montrerError("Matrice non conforme au nombre de voies!", "Erreur saisie");
            return false;

        } else {
            return true;
        }

    }

    private void enregistrerNouvelleCarte(String localisationNouveauBinaire, String nomNouveauBinaire, String nombreDeVoiesNouvelleCarte, String nouveauDevice1, String matriceNouveauPanneau) {
        
        
        comboListeProduits.addItem(nomNouveauBinaire);
        ListeBinairesEnregistres.add(localisationNouveauBinaire);
        listesProduits.add(nomNouveauBinaire);
        listesVoies.add(nombreDeVoiesNouvelleCarte);
        listeDevicesEnregistres.add(nouveauDevice1);
        listesMatrices.add(matriceNouveauPanneau);

        nomNouvelleCarte.setText("");
        messageBinaireSelectionne.setText("");
        nouveauMicroController.setText("");
        nombreVoiesNouvelleCarte.setText("");
        nouvelleMatrice.setText("");

        initialisation.setBinaryLocations1(initialisation.getBinaryLocations1()+ ";" + localisationNouveauBinaire);
        initialisation.setProductNames(initialisation.getProductNames() + ";" + nomNouveauBinaire);
        initialisation.setNombreVoies(initialisation.getNombreVoies() + ";" + nombreDeVoiesNouvelleCarte);
        initialisation.setDevice1(initialisation.getDevice1()+ ";" + nouveauDevice1);
        initialisation.setMatrice(initialisation.getMatrice() + ";" + matriceNouveauPanneau);

        initializer.update("binaryLocations1", initialisation.getBinaryLocations1());
        initializer.update("productNames", initialisation.getProductNames());
        initializer.update("voies", initialisation.getNombreVoies());
        initializer.update("device1", initialisation.getDevice1());
        initializer.update("matrice", initialisation.getMatrice());
        
    }

    private ArrayList<String> extraireDevices(String devices) {

        String[] liste = devices.split(";");
        ArrayList<String> arrList = new ArrayList<String>();
        for (int i = 0; i < liste.length; i++) {

            //System.out.println(liste[i]);
            arrList.add(liste[i]);
        }
        return arrList;
    }

    private void activerLedPRGM(boolean active) {

        if (!active) {

            statutPGRM.setBackground(Color.RED);
            statutPGRM.setForeground(Color.RED);
        } else {

            statutPGRM.setBackground(Color.GREEN);
            statutPGRM.setForeground(Color.GREEN);
        }
    }

    private void activerLedRS232(boolean active) {

        if (!active) {

            statutPGRM.setBackground(Color.RED);
            statutPGRM.setForeground(Color.RED);
        } else {

            statutPGRM.setBackground(Color.GREEN);
            statutPGRM.setForeground(Color.GREEN);
        }
    }

    void initialisationParams() throws IOException {

        initialisation = initializer.getInit();

        System.out.println("commutateur.Interface.initialisationParams(): Valeur cyles dans params: " + initialisation.getCycles());
        Constants.CYCLES_LIM = Integer.parseInt(initialisation.getCycles());
        System.out.println("commutateur.Interface.initialisationParams(): Valeur Cycles dans classe Constants: " + Constants.CYCLES_LIM);

        System.out.println("commutateur.Interface.initialisationParams(): Valeur cyles dans params: " + initialisation.getTiming());
        Constants.TIMING = Long.parseLong(initialisation.getTiming());
        System.out.println("commutateur.Interface.initialisationParams(): Valeur Cycles dans classe Constants: " + Constants.TIMING);

        // Détermination du mode auto connexion 
        if (initialisation.getAutoConnection().equals("yes")) {

            System.out.println("auto connexion = " + initialisation.getAutoConnection());
            autoConnexion = true;

        } else {

            System.out.println("auto connexion = " + initialisation.getAutoConnection());
            autoConnexion = false;
        }

        //Recherche nombre de voie du commutateur
        if (initialisation.getCommutateur().equals("na")) {

            //System.out.println("Commutateur = " + initialisation.getCommutateur());
        } else {

            //System.out.println("Commutateur = " + initialisation.getCommutateur());
            nombreVoiesCommutateurParamsProperties = initialisation.getCommutateur();
            limCommutateur = Integer.parseInt(nombreVoiesCommutateurParamsProperties);
        }

        // Recherche du fichier binaire
        if (initialisation.getBinaryLocations1().equals("na")) {

            //System.out.println("BinaryLocation = " + initialisation.getBinaryLocations());
        } else {

            //System.out.println("BinaryLocation = " + initialisation.getBinaryLocations());
            hexLocationsParamsProperties = initialisation.getBinaryLocations1();
            ListeBinairesEnregistres = extraireLocalisationBinaires(hexLocationsParamsProperties);
        }

        // Recherche nom du produit
        if (initialisation.getProductNames().equals("na")) {

            //System.out.println("liste noms de produits = " + initialisation.getProductNames());
            nomProduit.setText("Aucun produit crée");

        } else {

            //System.out.println("liste noms de produits  = " + initialisation.getProductNames());
            listeProduitsConnusParamsProperties = initialisation.getProductNames();
            //produits = extraireProduits(listeProduitsConnus);
            listesProduits = extraireProduits(listeProduitsConnusParamsProperties);
            //comboListeProduits.addItem("---");
            for (int i = 0; i < listesProduits.size(); i++) {

                //comboListeProduits.addItem(produits[i]);
                comboListeProduits.addItem(listesProduits.get(i));

            }

        }

        // Recherche nombre de voies à programmer (nombre de carte par panneau)
        if (initialisation.getNombreVoies().equals("na")) {

            //System.out.println("liste nombre de voies = " + initialisation.getNombreVoies());
            nombreVoies.setText("Aucune voie définie");

        } else {

            //System.out.println("liste du nombre de voies  = " + initialisation.getNombreVoies());
            nombreDeVoiesEnregistresParamsProperties = initialisation.getNombreVoies();
            listesVoies = extraireVoies(nombreDeVoiesEnregistresParamsProperties);

        }

        // Recherche nom du microcontrôleur à programmer
        if (initialisation.getDevice1().equals("na")) {

            //System.out.println("liste des devices lues = " + initialisation.getDevice());
            nombreVoies.setText("Aucun device enregistré");

        } else {

            //System.out.println("liste des devices lus  = " + initialisation.getDevice());
            devicesParamsProperties = initialisation.getDevice1();
            listeDevicesEnregistres = extraireDevices(devicesParamsProperties);

        }

        // Recherche type de programmateur (code programmateur) 
        if (initialisation.getProgrammer().equals("na")) {

            //System.out.println("liste des devices lues = " + initialisation.getProgrammer());
            nombreVoies.setText("Aucun programmateur enregistré");

        } else {

            //System.out.println("Programmateur enregistré  = " + initialisation.getProgrammer());
            programmerParamsProperties = initialisation.getProgrammer();

        }

        // Recherche repertoire d'installation de la plateforme Microchip
        if (initialisation.getProgrammerDirectory().equals("na")) {

            //System.out.println("Directory programmer = " + initialisation.getProgrammerDirectory());
            nombreVoies.setText("Aucune localisation programmateur enregistré");

        } else {

            //System.out.println("Directory programmer = " + initialisation.getProgrammerDirectory());
            programmerPathParamsProperties = initialisation.getProgrammerDirectory();

        }

        // Recherche emplacement du repertoire des fichiers temporaires du programmateur
        String[] tab = programmerPathParamsProperties.split("\\\\");

        for (int i = 0; i < tab.length; i++) {

            //System.out.println(tab[i]);
        }
        programmerPathTempFileDirectory = "C:\\Users\\" + tab[2] + "\\.mchp_ipe\\";
        //System.out.println("programmerPathTempFileDirectory: " + programmerPathTempFileDirectory);

        workDirectoryPath = "C:\\Users\\" + tab[2] + "\\picProgrammer";

        System.out.println("complete path: " + "java -jar " + workDirectoryPath + "\\commutateur.jar");

        System.out.println("workDirectoryPath:" + workDirectoryPath);
        // Recherche variable d'environnement pour la commande Java
        if (initialisation.getVarEnv().equals("na")) {

            //System.out.println("varEnv = " + initialisation.getVarEnv());
        } else {

            //System.out.println("varEnv = " + initialisation.getVarEnv());
            if (initialisation.getVarEnv().equals("true")) {

                envVariable = true;
            }

            if (initialisation.getVarEnv().equals("false")) {

                envVariable = false;
            }

        }

        // Recherche des matrices
        if (initialisation.getMatrice().equals("na")) {

            //System.out.println("liste des matrices = " + initialisation.getMatrice());
            nombreVoies.setText("Aucune matrice définie");

        } else {

            //System.out.println("liste des matrices  = " + initialisation.getMatrice());
            matricesProperties = initialisation.getMatrice();
            listesMatrices = extraireMatrices(matricesProperties);

        }

        // Recherche position programmation mono
        if (initialisation.getSingle().equals("na")) {

            //System.out.println("liste des matrices = " + initialisation.getMatrice());
            nombreVoies.setText("Aucune position mono définie");

        } else {

            monoPositionString = initialisation.getSingle();
            monoLocation = Integer.parseInt(monoPositionString);

        }

    }

    private void raffraichirInterface() {

        if (produitAprogrammer != null) {
            this.setSize(1141, 620 + lignes * 38);
            int i = 0;
            while (i < lignes) {

                listeLignes.get(i).initialiser(colonnes, mono);
                listeLignes.get(i).setVisible(true);
                i++;
            }

            for (int j = i; j < 12; j++) {

                listeLignes.get(j).supprimer();
                listeLignes.get(j).setVisible(false);
            }
        }
    }

    private void raffraichirIndicateur(String[] tab) {

        int ligne = 1;

        while (ligne < 13) {

            if (listeLignes.get(ligne - 1).isVisible()) {

                //System.out.println("Ligne visible");
                for (int i = 0; i < 12; i++) {

                    //System.out.println("ordre: " + listeLignes.get(ligne - 1).getVoyants().get(i).getOrdre() + " - colonne:" + i);
                    if (Integer.parseInt(tab[1]) == 99) {

                        if (listeLignes.get(ligne - 1).getVoyants().get(i).getOrdre() == Integer.parseInt(tab[2])) {

                            listeLignes.get(ligne - 1).getVoyants().get(i).processing(true);
                        }

                    }

                    if (listeLignes.get(ligne - 1).getVoyants().get(i).getOrdre() == Integer.parseInt(tab[1])) {

                        if (Integer.parseInt(tab[2]) == 1) {

                            listeLignes.get(ligne - 1).getVoyants().get(i).ok(true);

                        } else {

                            listeLignes.get(ligne - 1).getVoyants().get(i).ok(false);

                            if (Integer.parseInt(tab[2]) == -54) {

                                listeLignes.get(ligne - 1).getVoyants().get(i).processing(true);
                                console.setText("Alerte défaut. Nouvelle tentative!");
                            }

                            if (Integer.parseInt(tab[2]) == -55) {

                                //System.out.println("problème liaison usb");
                                console.setText("Problème de connexion USB sur le programmateur!");
                                montrerError("Vérifier la liaison USB. Si le problème persiste relancer le PC", "Erreur programmateur");

                            }

                            if (Integer.parseInt(tab[2]) == -66) {

                                //System.out.println("Problème alimentation programmateur");
                                //console.setText("Le programmateur n'est pas alimenter!");
                                montrerError("Vérifier l'alimentation du programmateur!\nVérifier la connexion USB", "Défaut programmateur");

                            }

                            if (Integer.parseInt(tab[2]) == -77) {

                                //System.out.println("problème liaison usb");
                                console.setText("Problème d'alimentation");
                                montrerError("Vérifier que le banc est sous tension. Si le problème persiste relancer le PC", "Erreur programmateur");

                            }
                        }

                    }

                }

            } else {

                //System.out.println("ligne non visible - saut de ligne");
            }

            ligne++;

        }
    }

    private void raffraichirImagePanneau() {

        String pathImage = ".\\images\\" + produitAprogrammer + ".jpg";
        //System.out.println("localisation image: " + pathImage);
        icon = new ImageIcon(pathImage);
        imagePanneau.setIcon(icon);
    }

    private void lancerProgrammation() {

        Thread t = new Thread() {
            public void run() {
                int comm = 0;
                try {
                    if (!mono) {
                        connecteur.setSequenceInterrompue(1);
                        comm = connecteur.program(hexLocationsParamsProperties, envVariable, programmerPathParamsProperties, programmerParamsProperties, deviceEnTest, binaireLocation, intNombreDeVoiesCarteEnTest, programmerPathTempFileDirectory);
                    } else {
                        connecteur.setSequenceInterrompue(monoLocation);
                        comm = connecteur.program(hexLocationsParamsProperties, envVariable, programmerPathParamsProperties, programmerParamsProperties, deviceEnTest, binaireLocation, monoLocation, programmerPathTempFileDirectory);
                    }

                    //System.out.println("Retour programmation. Code reçu: " + comm);
                    switch (comm) {

                        case 1:

                            connecteur.envoyerData(Character.toString('t'));
                            console.setText("Cycle de programmation terminée");
                            Constants.tempo(2000);
                            activerBtnACQ(true);
                            activerBtnProgrammer(false);
                            menuParametres.setEnabled(true);
                            menuConnexion.setEnabled(true);
                            compteurReset = 0;
                            activerBtnACQ(true);
                            activerBtnProgrammer(false);

                            break;

                        case -33:

                            console.setText("Reset programmateur");

                            break;

                        case -77:

                            console.setText("Problème d'alimentation");
                            compteurReset = 0;

                            break;

                        case -88:

                            console.setText("Veuillez défaire la connexion USB et relancer l'application");
                            activerBtnACQ(false);
                            activerBtnProgrammer(false);
                            activerBtnEffacer(false);
                            connecteur.envoyerData("t");
                            Constants.tempo(2000);
                            montrerError("Déconnectez la liaison USB momentanément, remettez-la et relancer l'application!", "Blocage programmateur");

                            processBuilder.command("cmd.exe", "/c", "java -jar " + workDirectoryPath + "\\commutateur.jar");
                            Process process = processBuilder.start();

                            System.exit(0);

                    }

                } catch (IOException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        t.start();

    }

    private void activerProgrammation() {

        lancerProgrammation();

    }

    private boolean testEcho() {

        Constants.tempo(2000);
        if (echo) {

            echo = false;
            return true;

        } else {

            echo = false;
            return false;
        }

    }

    private void checkEcho() {

        boolean ok = false;
        int i = 0;

        while (!ok) {

            connecteur.envoyerData(Character.toString('t'));
            if (!testEcho()) {

                if (i != 2) {

                    montrerError("La banc ne répond pas! Vérifiez les connexions et que le banc est sous-tension", "Erreur banc");
                } else {

                    montrerError("La banc ne répond pas! Erreur persistante.", "Erreur banc");
                    panne = true;
                    return;

                }
                echo = false;
                i++;
                // return;

            } else {

                ok = true;
            }
        }

        panne = false;
        echo = false;

    }

    private void getActivityPeriod() throws IOException {

        if (dateOfEnd != null) {
            long delay = dateOfEnd.until(dateOfStart, ChronoUnit.MINUTES);
            System.out.println("Interval temps entre deux programmations: " + delay + "min");

            if (Long.compare(Constants.PERIOD_MAX_LENGTH, delay) < 0) {
                System.out.println("Période hors délai - demande de reset processus");
                connecteur.askForResetProcess();
                Constants.tempo(1000);
            }

        }
    }

    private void loadPortsCommsAuto(String portName) {

        menuPort.removeAll();

        listePorts.clear();
        listePortString.clear();
        listePortString.add(portName);

        for (String p : listePortString) {

            JRadioButtonMenuItem m = new JRadioButtonMenuItem(p);
            groupPorts.add(m);
            m.addActionListener(new PortSupplier());
            menuPort.add(m);
        }

    }

    void makeSerialAutoConnexion() {

        int i = connecteur.makeConnection(serialPortModeAuto, baudeRate, numDatabits, parity, stopBits, true);
        connecteur.envoyerData(Character.toString('t'));

        if (!testEcho()) {

            //montrerError("La banc ne répond pas! Vérifiez les connexions et que le banc est sous-tension", "Erreur banc");
            echo = false;
            return;

        } else {

            echo = true;

        }

        if (i == 99) {

            console.setForeground(Color.BLUE);
            console.setText("Connexion réussie");
            setStatusRS232(true);
            btnConnexion.setEnabled(false);
            btnDeconnexion.setEnabled(true);
            connexionRS232Active = true;
            testParamsProg();

        } else {

            console.setForeground(Color.red);
            console.setText("Tentative de connexion échouée");
            setStatusRS232(false);

        }

        setEnabledMenusConfiguration();

    }

    private void initInterface() {

        if (EnvVarBox.isSelected()) {

            envVariable = true;

        } else {

            envVariable = false;
        }

        if (initialisation.getItem().equals("none")) {

            selectedProduct = comboListeProduits.getSelectedIndex();
            System.out.println("keypadprogrammer.Interface.initInterface() -  valeur none");
        } else {

            System.out.println("keypadprogrammer.Interface.initInterface() -  valeur differente de none");
            selectedProduct = Integer.parseInt(initialisation.getItem());;

        }

        System.out.println("keypadprogrammer.Interface.initInterface() - valeur selectedProduct:" + selectedProduct);

        if (selectedProduct != 0) {

            System.out.println("test selectedProduct != 0");
            produitAprogrammer = listesProduits.get(selectedProduct);
            binaireLocation = ListeBinairesEnregistres.get(selectedProduct - 1);
            hexLocalisation.setText(ListeBinairesEnregistres.get(selectedProduct - 1));
            nombreVoies.setText(listesVoies.get(selectedProduct - 1));
            nombreDeVoiesCarteEnTest = listesVoies.get(selectedProduct - 1);
            intNombreDeVoiesCarteEnTest = Integer.parseInt(nombreDeVoiesCarteEnTest);
            System.out.println("nombre de voies carte en test (int): " + intNombreDeVoiesCarteEnTest);
            deviceEnTest = listeDevicesEnregistres.get(selectedProduct - 1);
            matriceAprogrammer = listesMatrices.get(selectedProduct - 1);
            extraireLignesColonnes(matriceAprogrammer);
            nomProduit.setText(produitAprogrammer + " - Microcontrôleur: " + deviceEnTest + " - Voies: " + nombreDeVoiesCarteEnTest + " - Programmateur: " + programmerParamsProperties + " - Matrice: " + matriceAprogrammer);
            emplacementBinaire.setText(binaireLocation);
            envVariable = true;
            raffraichirInterface();
            initializer.update("item", String.valueOf(selectedProduct));

        } else {

            nomProduit.setText("Aucun produit sélectionné!");
            emplacementBinaire.setText("");
            nombreDeVoiesCarteEnTest = null;
            deviceEnTest = null;
            produitAprogrammer = null;

        }

        testParamsProg();
        raffraichirImagePanneau();

    }

}
