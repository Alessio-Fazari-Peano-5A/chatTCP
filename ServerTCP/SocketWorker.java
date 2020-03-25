/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author alessiofazari
 */
class SocketWorker implements Runnable, InviaMessaggio, RiceviMessaggio {

    //Creo il gestore di Messaggi che permette ai vari workers di comunicare tra loro
    private static final GestoreMessaggi gestoreMessaggi = new GestoreMessaggi();
    private Socket client;
    private PrintWriter Pw;

    //Costruttore
    SocketWorker(Socket client) {
        this.client = client;
        gestoreMessaggi.aggiungiClient(this);
        System.out.println("Connesso con: " + client);
    }

    public void MessaggioRicevuto(String m) {
        //Il metodo richiama il metodo "InviaNuovoMessaggio" della classe "gestoreMessaggi"
        this.gestoreMessaggi.InviaNuovoMessaggio(m);
    }

    //Metodo invocato dal metodo "InviaNuovoMessaggio" ogni volta che viene ricevuto un messaggio.
    //Restituisce il messaggio ricevuto dal client
    public void InviaMessaggio(String messaggio) {
        //Invia lo stesso messaggio appena ricevuto 
        Pw.println(messaggio);
    }

    //Funzione lanciata subito dopo la creazione del nuovo Thread
    public void run() {

        BufferedReader Br = null;
        try {
            //Gestione flusso di caratteri
            Br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            Pw = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            //Segnalazione di eventuali errori
            System.out.println("Errore");
            e.printStackTrace();
            System.exit(-1);
        }

        String messaggio = "";
        //Variabile contenente il numero di porta del client che ha inviato il messaggio
        int clientPort = client.getPort();
        while (messaggio != null) {
            try {
                //Attendo la ricezione di un nuovo messaggio dal client. Il messaggio verrà salvato nella variabile "messaggio"
                messaggio = Br.readLine();
                //Richiamo il metodo "MessaggioRicevuto" passando il messaggio stesso come parametro
                MessaggioRicevuto(messaggio);
                //Scrivo il messaggio ricevuto sul terminale del server
                System.out.println("Client sulla porta " + clientPort + " con Username -> " + messaggio);
            } catch (IOException e) {
                //Segnalazione di eventuali errori
                System.out.println("Lettura da socket fallito");
                e.printStackTrace();
                System.exit(-1);
            }
        }
        try {
            client.close();
            System.out.println("Connessione con client: " + client + " terminata!");
        } catch (IOException e) {
            System.out.println("Errore connessione con client: " + client);
        }
    }

}
