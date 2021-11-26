
public class Arrival {
	private String type;
	private int idOfPlayer;
	private double arrivalTime;
	private double duration;
	public Arrival(String type,int id,double arrivalTime,double duration) {
		this.type=type;
		this.idOfPlayer = id;
		this.duration = duration;
		this.arrivalTime = arrivalTime;
	}
	public String getType() {
		return type;
	}
	public int getIdOfPlayer() {
		return idOfPlayer;
	}
	public double getArrivalTime() {
		return arrivalTime;
	}
	public double getDuration() {
		return duration;
	}
}
