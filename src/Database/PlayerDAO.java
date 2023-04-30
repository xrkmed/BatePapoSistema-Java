package Database;

import java.net.Socket;

import Models.Connection;
import Models.Player;
import Models.Protocol;

public class PlayerDAO {

    private int mPlayerCount = 0;

    //singleton class
    private static PlayerDAO instance;

    private PlayerDAO() {
    }

    public static PlayerDAO getInstance() {
        if (instance == null) {
            instance = new PlayerDAO();
        }
        return instance;
    }

    public int generatePlayerId() {
        mPlayerCount++;
        return mPlayerCount;
    }


    public Player generatePlayer(Socket connectionSocket) {
        try{
            Player player = new Player("Player");
            Protocol protocol = new Protocol(player);
            Connection conn = new Connection(connectionSocket, protocol);

            ConnectionDAO.getInstance().addConnection(conn, player);
            Thread t = new Thread(conn);
            t.start();
            
            return player;
        }catch(RuntimeException ex){
            return ConnectionDAO.getInstance().findPlayerBySocket(connectionSocket);
        }
    }

    
}
