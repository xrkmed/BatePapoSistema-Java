package Controllers;

import Database.HistoricoDAO;

public class HistoricoEntity implements Comparable<HistoricoEntity>{

    private int id;
    private final String actor;
    private final String content;
    private final Long timestamp;

    public HistoricoEntity(String actor, String content, Long timestamp) {
        this.id = HistoricoDAO.getInstance().generateId();
        this.actor = actor;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getActor() {
        return actor;
    }

    public String getContent() {
        return content;
    }

    public Long getTimeStamp(){
        return timestamp;
    }

	@Override
	public int compareTo(HistoricoEntity o) {
		return this.timestamp.compareTo(o.getTimeStamp());
	}
    
}
