package Models;

import java.util.Objects;

public class Player {

    private String username;

    public Player(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }

    public void onThink(){
    }

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(username, other.username);
	}
    
    
    
}
