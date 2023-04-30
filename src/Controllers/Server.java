package Controllers;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import Database.ConnectionDAO;
import Database.HistoricoDAO;
import Database.PlayerDAO;
import Database.ProtocolAll;
import Models.Connection;
import Models.EventScheduler;
import Models.Message;
import Models.Player;

public class Server {
	
	private final int SERVER_PORT = 6666;

	private final HistoricoDAO historicoDao = HistoricoDAO.getInstance();
	
	public Server() {
		init();
	}
	
	public void init() {
		try(ServerSocket instance = new ServerSocket(SERVER_PORT)){
			while(true){
				Socket connectionSocket = instance.accept();
				Connection conn = ConnectionDAO.getInstance().findConnectionBySocket(connectionSocket);
				if(conn == null){
					Player player = PlayerDAO.getInstance().generatePlayer(connectionSocket);
					
					new Thread(new EventScheduler().addEvent(1000, new Runnable() {
						@Override
						public void run() {
							//@@ Enviar historico do chat ao jogador que acabou de entrar
							Connection conn = ConnectionDAO.getInstance().findConnectionBySocket(connectionSocket);
							conn.sendHistorico();
				    		String timeString = new SimpleDateFormat("HH:mm:ss").format(new Date());
				    		conn.sendChatMessage("System", player.getUsername() + " acabou de se conectar.", timeString);
				    		ProtocolAll.getInstance().sendUsersList();
						}
					}));
				}
			}

		}catch(IOException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

}
