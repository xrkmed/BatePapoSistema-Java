package Models;

import java.util.HashMap;
import java.util.Objects;
import java.util.Queue;

import Controllers.Packet;

public class Message {

    private static Integer mIdCounter = 0;
    private final int mId;
    private final int mPacketId;
    private final Queue<Packet> mPackets;

    public Message(int mPacketId, Queue<Packet> mPackets){
        Message.mIdCounter++;
        this.mId = Message.mIdCounter;
        this.mPacketId = mPacketId;
        this.mPackets = mPackets;
    }

    public int getPacketId() {
        return mPacketId;
    }

    public Packet getPacket(){
        return mPackets.poll();
    }

    public int getPacketCount(){
        return mPackets.size();
    }

	@Override
	public int hashCode() {
		return Objects.hash(mId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return mId == other.mId;
	}

    
}
