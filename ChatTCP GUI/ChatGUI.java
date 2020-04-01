/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatgui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Alessio
 */
public class ChatGUI extends JFrame implements ActionListener {
    //File JAVA unico con Interfaccia grafica

    //Classi necessarie per la comunicazione
    private Socket client = null;
    private static String IP_address;
    private static InetAddress address;
    private static int NumeroPorta;
    private String username;

    //Dichiarazione elementi JFrame
    //Pannelli
    JPanel ContenutoInvio = new JPanel();
    //Pannelli

    //Menù
    JMenuBar menu = new JMenuBar();
    JMenu GestisciChat = new JMenu("Opzioni Chat");
    JMenuItem InserisciIPServer = new JMenuItem("Inserisci Indirizzo IP Server");
    JMenuItem AggiungiUsername = new JMenuItem("Aggiungi Username");
    //Menù

    //Area Chat
    Font fontArea = new Font("Arial", Font.BOLD, 16); //Fonts per l'area Chat e Invio
    Font fontInvio = new Font("Arial", Font.PLAIN, 14);
    private static JTextArea areaChat = new JTextArea();
    JScrollPane scroll = new JScrollPane(areaChat); //Aggiungo uno scrollPane per avere la possibilità di scrollare la chat
    //Area Chat

    //Area Invio
    JTextField messaggioField = new JTextField("Scrivi il tuo messaggio qui");
    JButton invia = new JButton("Invia");
    //Area Invio

    public ChatGUI(Socket s) {

        //Configurazione JFrame
        this.client = s;
        this.setTitle("Chat di gruppo");
        this.setSize(600, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);//Dimensione Frame non modificabile
        this.setLayout(new GridLayout(2, 1));
        //Configurazione JFrame

        //Configurazione menù
        menu.add(GestisciChat);
        GestisciChat.add(InserisciIPServer);
        GestisciChat.add(AggiungiUsername);
        this.setJMenuBar(menu);
        //Configurazione menù

        //Configurazione Pannelli
        ContenutoInvio.setLayout(new GridLayout(1, 2));
        ContenutoInvio.setBorder(new EmptyBorder(60, 20, 60, 20)); //Padding per l'area Invio
        messaggioField.setFont(fontInvio);
        messaggioField.setBorder(new EmptyBorder(10, 10, 10, 10)); //Padding per l'area messaggio
        invia.setFont(fontInvio); //Impostazione Font
        ContenutoInvio.add(messaggioField);
        ContenutoInvio.add(invia);
        areaChat.setEditable(false); //La TextArea in cui è presente la chat non è modificabile
        areaChat.setBorder(new EmptyBorder(20, 20, 20, 20)); //Padding per l'area messaggio
        areaChat.setFont(fontArea); //Impostazione Font
        areaChat.append("Prima di iniziare, inserisci il tuo Username e l'indirizzo IP del Server.\n"
                + "Per farlo clicca sul menù ''Opzioni Chat'' qui in alto.\n"
                + "Il Server deve esserve avviato per poter usare la chat correttamente.\n"); //Messaggio per l'utente
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); //Rendo sempre visualizzabile la barra di scroll verticale
        this.add(scroll);
        this.add(ContenutoInvio);
        //Configurazione Pannelli

        //FocusListener per il campo messaggio (se seleziono l'area per scrivere un messaggio, il placheholder viene cancellato)
        messaggioField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                messaggioField.setText(""); //Svuoto il TextField
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });

        //ActionListener Bottone Invia
        invia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username == null || IP_address == null) { //Controllo che l'username e l'IP del server siano stati inseriti
                    JOptionPane.showMessageDialog(null, "Username o Indirizzo IP del server non inseriti correttamente!");
                } else {
                    PrintWriter out = null;
                    try {
                        out = new PrintWriter(client.getOutputStream(), true);
                        out.println("[" + username + "(Sulla porta " + client.getLocalPort() + ")]:" + messaggioField.getText());
                    } catch (IOException ex) {
                        Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Svuoto il campo messaggio dopo l'invio
                    messaggioField.setText("");
                }

            }
        });

        //ActionListener InserisciIPServer
        InserisciIPServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                IP_address = JOptionPane.showInputDialog("Inserisci l'indirizzo IP del Server: "); //OptionPane per l'input dell'utente
                if (IP_address != null) { //Controllo che l'IP sia stato inserito
                    setTitle("Chat di gruppo [" + "Username: " + username + " -  IP Server: " + IP_address + "]");
                    areaChat.append("Indirizzo IP " + "''" + IP_address + "''" + " inserito correttamente!"); //Messaggio per l'utente
                    areaChat.append("\n");
                }
            }
        });

        //ActionListener AggiungiUsername
        AggiungiUsername.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = JOptionPane.showInputDialog("Inserisci il tuo username: "); //OptionPane per l'input dell'utente
                if (username != null) { //Controllo che l'username sia stato inserito
                    setTitle("Chat di gruppo [" + "Username: " + username + " -  IP Server: " + IP_address + "]");
                    areaChat.append("Username " + "''" + username + "''" + " inserito correttamente!"); //Messaggio per l'utente
                    areaChat.append("\n");
                }
            }
        });
        threadAscolta();
    }

    public void threadAscolta() {
        Thread thAscolta = new Thread(new Ascoltatore(client));
        thAscolta.start();
    }
    
    //Creazione socket di comunicazione e avvio interfaccia grafica
    public static void main(String[] args) throws UnknownHostException, IOException {

        address = InetAddress.getByName(IP_address);
        Socket socket = new Socket(address, 1234);

        //Avvio Interfaccia
        Runnable r = new Runnable() {
            public void run() {
                new ChatGUI(socket).setVisible(true);
            }
        };
        EventQueue.invokeLater(r);
    }

    //Classe per ascoltare i messaggi provenienti dal server
    public class Ascoltatore implements Runnable {

        private Socket client;

        public Ascoltatore(Socket s) {
            this.client = s;
        }

        @Override
        public void run() {
            BufferedReader Br = null;
            try {
                String messaggio;
                Br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((messaggio = Br.readLine()) != null) {
                    areaChat.append(messaggio + "\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    Br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(-1);
            }
        }
    }

    //Classe per creare un flusso di caratteri e "stampare" il messaggio del client
    public class ScriviMessaggio implements Runnable {

        private Socket client;

        public ScriviMessaggio(Socket s) {
            this.client = s;
        }

        @Override
        public void run() {
            PrintWriter Pw = null;
            BufferedReader Br = null;
            String messaggio = "";
            try {
                Pw = new PrintWriter(client.getOutputStream(), true);
                Br = new BufferedReader(new InputStreamReader(System.in));
                while ((messaggio = Br.readLine()) != null) {
                    Pw.println(messaggio);
                }
            } catch (IOException ex) {
                Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    Br.close();
                    Pw.close();
                } catch (IOException ex) {
                    Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    //Non utilizzato
    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
