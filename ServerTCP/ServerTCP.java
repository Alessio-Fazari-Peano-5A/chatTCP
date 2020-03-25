/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author alessiofazari
 */
public class ServerTCP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Dichiarazione variabile di tipo Scanner per prendere gli input da tastiera dell'utente
        Scanner input = new Scanner(System.in);
        //Dichiarazione variabile per salvare il numero di porta
        int NumeroPorta;

        System.out.print("Inserisci il numero di porta su cui si vuole rimanere in ascolto: ");
        NumeroPorta = input.nextInt();

        try {
            //Il server si mette in ascolto sulla porta inserita dall'utente per gestire tutti i client che richiedono la connessione
            //Creazione del socket
            ServerSocket server = new ServerSocket(NumeroPorta);
            System.out.println("Server chatTCP in esecuzione e in ascolto sulla porta " + NumeroPorta + "...");

            while (true) {
                //Creazione dell'oggetto SocketWorker che gestisce un client. Ogni client possiede un suo thread personale
                SocketWorker WorkerNuovoClient;
                try {
                    //Aspetto la richiesta di connessione da parte di un nuovo client e accetto la connessione
                    Socket newSocket = server.accept();
                    //Creo un SocketWorker a cui "affido" il nuovo client
                    WorkerNuovoClient = new SocketWorker(newSocket);

                    //Genero un thread per il worker appena creato
                    Thread thWorker = new Thread(WorkerNuovoClient);
                    //Avvio il thread
                    thWorker.start();
                } catch (IOException e) {
                    //Segnalazione di un errore in caso la connessione non vada a buon fine
                    System.out.println("ATTENZIONE: Connessione al client non riuscita");
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            //Segnalazione di un errore in caso la porta sia occupata
            System.out.println("Errore! Porta: " + NumeroPorta + " non disponibile");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
