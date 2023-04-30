package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import Controllers.HistoricoEntity;
import Controllers.Packet;
import Controllers.SignalEntity;
import Database.ConnectionDAO;
import Database.HistoricoDAO;

public class Connection implements Runnable {

	private int connectionId;
    private final Socket mSocket;
    private long lastPacketSend;
    private final Protocol protocol;
    private PrintWriter output;

    public Connection(Socket connectionSocket, Protocol protocol){
    	this.connectionId = ConnectionDAO.getInstance().generateConnectionId();
        this.mSocket = connectionSocket;
        this.lastPacketSend = System.currentTimeMillis();
        this.protocol = protocol;
        updateOutput();
    }

    public Socket getSocket(){
        return this.mSocket;
    }

    public long getLastPacketSend(){
        return this.lastPacketSend;
    }

    public Protocol getConnectionProtocol(){
        return this.protocol;
    }

    
    //packet id 1
    public void sendChatMessage(String authorName, String message){
        Queue<Packet> queue = new LinkedList<Packet>();
        Packet packetAuthor = new Packet("String", authorName);
        Packet packetMessage = new Packet("String", message);
        queue.add(packetAuthor);
        queue.add(packetMessage);
        this.output.println(SignalEntity.encodeSignal(new Message(1, queue)));
    }

    //packet id 1
    public void sendChatMessage(String authorName, String message, String timeString){
        Queue<Packet> queue = new LinkedList<Packet>();
        Packet packetAuthor = new Packet("String", authorName);
        Packet packetMessage = new Packet("String", message);
        Packet packetTime = new Packet("String", timeString);
        queue.add(packetAuthor);
        queue.add(packetMessage);
        queue.add(packetTime);
        this.output.println(SignalEntity.encodeSignal(new Message(1, queue)));
    }
    
    //packet id 2
    public void sendHistorico() {
        Queue<HistoricoEntity> list = HistoricoDAO.getInstance().getHistorico();
        Queue<Packet> queue = new LinkedList<Packet>();
        while(list.size() > 0) {
        	HistoricoEntity e = list.poll();
    		Timestamp timestamp = new Timestamp(e.getTimeStamp());
    		String timeString = new SimpleDateFormat("HH:mm:ss").format(new Date(timestamp.getTime()));
    		
        	Packet packetAuthor = new Packet("String", e.getActor());
        	Packet packetMessage = new Packet("String", e.getContent());
        	Packet packetTime = new Packet("String", timeString);
        	queue.add(packetAuthor);
        	queue.add(packetMessage);
        	queue.add(packetTime);
        }
        
        if(queue.size() < 1)
        	return;
        
        String message = SignalEntity.encodeSignal(new Message(2, queue));
        System.out.println(message);
        this.output.println(message);
    }
    
    //packet id 3
    public void sendUsersList(){
        Queue<Packet> queue = new LinkedList<Packet>();
        for(Connection conn : ConnectionDAO.getInstance().getConnections()) {
        	if(!conn.getSocket().isConnected())
        		return;
        	
        	Packet packetUser = new Packet("String", conn.getConnectionProtocol().getPlayer().getUsername());
            System.out.println("Sending user: " + conn.getConnectionProtocol().getPlayer().getUsername());
        	queue.add(packetUser);
        }
        this.output.println(SignalEntity.encodeSignal(new Message(3, queue)));
    }

    public void updateOutput(){
        try{
            this.output = new PrintWriter(getSocket().getOutputStream(), true);
        }catch(IOException ex){
            System.out.println("Error updating output: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        try (BufferedReader input = new BufferedReader(new InputStreamReader(getSocket().getInputStream()))) {
            String message;
            while ((message = input.readLine()) != null) {
                this.lastPacketSend = System.currentTimeMillis();

                Message packet = SignalEntity.decodeSignal(message);
				ConnectionDAO.getInstance().parsePacket(this, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error handling connection: " + e.getMessage());
        }finally{
            ConnectionDAO.getInstance().closeConnection(getSocket());
        }
    }

	@Override
	public int hashCode() {
		return Objects.hash(connectionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connection other = (Connection) obj;
		return connectionId == other.connectionId;
	}

    
    
}
