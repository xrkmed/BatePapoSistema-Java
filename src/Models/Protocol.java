package Models;

import java.util.Objects;

import Database.HistoricoDAO;
import Database.ProtocolAll;

public class Protocol {

    private final Player player;

    public Protocol(Player player){
        this.player = player;
    }
    

    public Player getPlayer(){
        return player;
    }

    ProtocolAll protocolAll = ProtocolAll.getInstance();

    public void parsePacket(Message message){
        int packetId = message.getPacketId();

        switch(packetId){
	        case 0: {
	            parseUsername(message);
	            break;
	        }
            case 1: {
                parseMessage(message);
                break;
            }
            default: {
                System.out.println("> Unknow packet id: " + packetId + " from " + player.getUsername());
                break;
            }
        }
    }

    private void parseUsername(Message message){
        System.out.println(message.getPacketCount());
        String username = message.getPacket().getString(); //sender
        
        player.setUsername(username);
    }
    
    private void parseMessage(Message message){
        System.out.println(message.getPacketCount());
        String actor = message.getPacket().getString(); //sender
        String content = message.getPacket().getString(); //message
        System.out.println(actor + " disse: " + content);
        protocolAll.sendChatMessage(actor, content);
        
        if(actor != "System")
        	HistoricoDAO.getInstance().addMessage(actor, content);
    }


	@Override
	public int hashCode() {
		return Objects.hash(player);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Protocol other = (Protocol) obj;
		return Objects.equals(player, other.player);
	}
    
    


}
