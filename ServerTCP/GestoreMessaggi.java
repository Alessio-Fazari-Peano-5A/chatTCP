/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servertcp;

import java.util.ArrayList;

/**
 *
 * @author alessiofazari
 */
class GestoreMessaggi {

    //Ultimo messaggio inviato dai clients
    private String messaggio;
    //Arraylist contenente tutti i worker creati per ciascun client
    private ArrayList<SocketWorker> workers = new ArrayList<>();

    //aggiungo un client alla lista
    void aggiungiClient(SocketWorker worker) {
        this.workers.add(worker);
    }

    //rimuovo un client dalla lista
    void rimuoviClient(SocketWorker worker) {
        this.workers.remove(worker);
    }

    //Metodo synchronized nel caso in cui vengono ricevuti simultaneamente messaggi da piu' clients
    synchronized void InviaNuovoMessaggio(String m) {
        this.messaggio = m;
        //Ogni worker invia il messaggio ricevuto
        //Per ogni worker dell'arraylist, ossia a tutti i client connessi
        for (SocketWorker worker : this.workers) {
            worker.InviaMessaggio(this.messaggio);
        }
    }

}

//Interfaccia implementata dalla classe SocketWorker, utile a tutti i workers per inviare e ricevere messaggi
interface InviaMessaggio {

    //Questo metodo conterra' il codice da eseguire da ogni worker per inviare il messaggio al proprio client
    public void InviaMessaggio(String m);
}

//Interfaccia implementata dalla classe SocketWorker che avvisa la ricezione di un messaggio per poterlo inviare a tutti i clients tramite i relativi workers
interface RiceviMessaggio {

    public void MessaggioRicevuto(String m);

}
