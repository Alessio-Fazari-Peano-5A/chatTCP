/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author alessiofazari
 */
public class ClientTCP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Dichiarazione variabile di tipo Scanner per prendere gli input inseriti da tastiera dall'utente
        Scanner input = new Scanner(System.in);
        //Dichiarazione variabili per IP del Server, numero di porta e username dell'utente
        String IPServer;
        int NumeroPorta;
        String Username;

        //Salvo l'indirizzo IP del server, il numero di porta e l'username scelti dall'utente
        System.out.print("Inserisci l'indirizzo IP del Server: ");
        IPServer = input.next();
        System.out.print("Inserisci il numero di porta: ");
        NumeroPorta = input.nextInt();
        System.out.print("Inserisci Username: ");
        Username = input.next();

        try {
            //Utilizzo della classe InetAddress per la rappresentazione e la gestione dell'indirizzo IP
            InetAddress address = InetAddress.getByName(IPServer);

            //Creazione del socket per la comunicazione 
            Socket clientSocket = new Socket(address, NumeroPorta);

            //Avvio di un thread che "ascolta" la ricezione dei messaggi provenienti dal server
            try {
                //Passo il socket della comunicazione al thread
                ThreadAscolta thAscolta = new ThreadAscolta(clientSocket);
                Thread th = new Thread(thAscolta);
                //Avvio il Thread
                th.start();
            } catch (Exception e) {
                //Segnalazione di un errore in caso la connessione al server non vada a buon fine
                System.out.println("Connessione al server non riuscita!");
                e.printStackTrace();
            }

            //Gestione dei messaggi tramite flussi di caratteri
            PrintWriter Pw = new PrintWriter(clientSocket.getOutputStream(), true);

            //Classe BufferedReader per la creazione di un buffer in cui contenere il messaggio inserito dall'utente
            BufferedReader Br = new BufferedReader(new InputStreamReader(System.in));
            //Dichiarazione variabile per contenere il messaggio che il client vuole inviare
            String messaggio;

            //Prompt per comunicare all'utente della possibilità di scrivere il suo messaggio
            System.out.println("Puoi cominciare a scrivere i tuoi messaggi: ");

            //Lettura del testo del messaggio
            while ((messaggio = Br.readLine()) != null) {
                //Stream del messaggio in output preceduto dall'username del client
                Pw.println(Username + ": " + messaggio);
            }

            //Chiudo il socket
            clientSocket.close();

        } catch (IOException e) {
            //Segnalazione di eventuali errori
            System.out.println("Errore");
            e.printStackTrace();
        }
    }
}
