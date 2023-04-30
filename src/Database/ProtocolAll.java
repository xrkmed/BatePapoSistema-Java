package Database;

import Models.Connection;

public class ProtocolAll {

    private final ConnectionDAO connection = ConnectionDAO.getInstance();
    private static ProtocolAll instance;
    private final HistoricoDAO historico = HistoricoDAO.getInstance();

    private ProtocolAll() {
    }

    public static ProtocolAll getInstance() {
        if (instance == null) {
            instance = new ProtocolAll();
        }
        return instance;
    }

    public void sendChatMessage(String authorName, String message){
        for(Connection conn : connection.getConnections()){
            conn.sendChatMessage(authorName, message);
        }
    }
    
    public void sendUsersList() {
        for(Connection conn : connection.getConnections()){
            conn.sendUsersList();
        }
    }
    
    
}
