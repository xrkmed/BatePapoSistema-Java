package Server;

import javax.swing.JOptionPane;

import Controllers.MessageEntity;
import Controllers.Packet;
import Controllers.Server;
import Controllers.SignalEntity;
import Models.Message;

public class initServer {
    public static void main(String[] args){
        System.out.println("Servidor iniciando");

        try{
            Server server = new Server();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return;
    }

}
