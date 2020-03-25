/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienttcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author alessiofazari
 */
//Classe Runnable
public class ThreadAscolta implements Runnable {

    private Socket client;

    //Costruttore
    ThreadAscolta(Socket client) {
        this.client = client;
        //Messaggio in output per notificare l'ascolto del Thread
        System.out.println("In ascolto con: " + client);
    }

    public void run() {

        //Gestione messaggi provenienti dal server
        //Buffer per contenere il messaggio in arrivo
        BufferedReader Br = null;
        try {
            Br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            //Segnalazione di eventuali errori
            System.out.println("Errore");
            e.printStackTrace();
            //Il programma termina in caso di errori
            System.exit(-1);
        }

        //Dichiarazione variabile che conterrà il messaggio in arrivo
        String MessaggioDaServer;
        try {
            while ((MessaggioDaServer = Br.readLine()) != null) {
                //Il testo ricevuto dal server viene stampato sul terminale del client
                System.out.println(MessaggioDaServer);
                //Se il messaggio è "exit", la comunicazione termina
                if (MessaggioDaServer.contains("exit")) {
                    client.close();
                    System.exit(0);
                    break;
                }
            }
        } catch (IOException e) {
            try {
                //Segnalazione di eventuali errori
                System.out.println("Connessione terminata dal Server");
                e.printStackTrace();
                client.close();
                System.exit(-1);
            } catch (IOException ex) {
                //Segnalazione di eventuali errori
                System.out.println("Errore nella chiusura del Socket");
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }

}
