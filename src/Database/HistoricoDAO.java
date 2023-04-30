package Database;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import Controllers.HistoricoEntity;
import Models.Connection;

public class HistoricoDAO {

    private static HistoricoDAO instance;
    private final Queue<HistoricoEntity> historicoList = new LinkedList<>();
    private static int id = 0;

    private HistoricoDAO() {
    }

    public static HistoricoDAO getInstance() {
        if (instance == null) {
            instance = new HistoricoDAO();
        }
        return instance;
    }
    
    public Queue<HistoricoEntity> getHistorico(){
    	return new LinkedList<>(historicoList);
    }

    public void addMessage(String author, String message) {
    	Long timestamp = System.currentTimeMillis();    	
        HistoricoEntity historico = new HistoricoEntity(author, message, timestamp);
        historicoList.add(historico);
    }
    
    public int generateId(){
        HistoricoDAO.id++;
        return HistoricoDAO.id;
    }


    
}
