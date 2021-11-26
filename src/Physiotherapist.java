
public class Physiotherapist {
	private int id;
	private double serviceTime;
	private double estimateFinishTime;
	public Physiotherapist(int id , double seriviceTime) {
		this.id = id;
		this.serviceTime = seriviceTime;
	}
	public int getId() {
		return id;
	}
	public double getServiceTime() {
		return serviceTime;
	}
	public double getEstimateFinishTime() {
		return estimateFinishTime;
	}
	public void setEstimateFinishTime(double estimateFinishTime) {
		this.estimateFinishTime = estimateFinishTime;
	}
	
}
