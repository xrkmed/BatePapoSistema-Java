package Database;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import Models.Connection;
import Models.Message;
import Models.Player;

public class ConnectionDAO {

    //Singleton class
    private static ConnectionDAO instance;

    private final HashMap<Connection, Player> connections = new HashMap<>();
    private int connectionsId = 0;
    
    
    private ConnectionDAO() {
    }

    public static ConnectionDAO getInstance() {
        if (instance == null) {
            instance = new ConnectionDAO();
        }
        return instance;
    }
    
    public int generateConnectionId() {
    	++connectionsId;
    	return connectionsId;
    }

    public Player findPlayerBySocket(Socket socket){
        for(Connection conn : connections.keySet()){
            if(conn.getSocket().equals(socket)){
                return connections.get(conn);
            }
        }

        return null;
    }

    public Connection findConnectionBySocket(Socket socket){
        for(Connection conn : connections.keySet()){
            if(conn.getSocket().equals(socket)){
                return conn;
            }
        }

        return null;
    }

    public void addConnection(Connection conn, Player player) throws RuntimeException{
        if(connections.get(conn) != null)
            throw new RuntimeException("Connection already exists");

        connections.put(conn, player);
        return;
    }

    public void closeConnection(Socket socket){
        try{
            socket.close();
            Connection conn = findConnectionBySocket(socket);
            System.out.println(conn.getConnectionProtocol().getPlayer().getUsername() + " acabou de se desconectar.");
            connections.remove(conn);
        }catch(IOException e){
            System.err.println("Error closing client socket: " + e.getMessage());
        }finally{
            ProtocolAll.getInstance().sendUsersList();
        }
    }

    public void parsePacket(Connection source, Message packet){
        source.getConnectionProtocol().parsePacket(packet);
        return;
    }

    public ArrayList<Connection> getConnections(){
        return new ArrayList<>(connections.keySet());
    }
}
