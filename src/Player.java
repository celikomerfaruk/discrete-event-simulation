 
public class Player {
	private int id;
	private int skillLevel;
	private double trainTime;
	private double massageTime;
	private double physioTime;
	private double arriveMassageTime;
	private double arrivePhysioTime;
	private double arriveTrainTime;
	private double waitinPQ = 0;
	private double waitinMQ= 0;
	private boolean canTrain= true;
	private boolean canMassage= true;
	private int takenMassage;
	Player(int id, int skillLevel){
		this.id = id;
		this.skillLevel = skillLevel;
		
	}

	public int getTakenMassage() {
		return takenMassage;
	}

	public double getWaitinPQ() {
		return waitinPQ;
	}

	public void incWaitinPQ(double waitinPQ) {
		this.waitinPQ += waitinPQ;
	}

	public double getWaitinMQ() {
		return waitinMQ;
	}

	public void incWaitinMQ(double waitinMQ) {
		this.waitinMQ += waitinMQ;
	}

	public void incrementTakenMassage() {
		this.takenMassage++;
	}

	public double getArriveTrainTime() {
		return arriveTrainTime;
	}

	public void setArriveTrainTime(double arriveTrainTime) {
		this.arriveTrainTime = arriveTrainTime;
	}

	public double getArriveMassageTime() {
		return arriveMassageTime;
	}

	public void setArriveMassageTime(double arriveMassageTime) {
		this.arriveMassageTime = arriveMassageTime;
	}

	public double getArrivePhysioTime() {
		return arrivePhysioTime;
	}

	public void setArrivePhysioTime(double arrivePhysioTime) {
		this.arrivePhysioTime = arrivePhysioTime;
	}

	public double getTrainTime() {
		return trainTime;
	}

	public void setTrainTime(double trainTime) {
		this.trainTime = trainTime;
	}

	public boolean isCanMassage() {
		return canMassage;
	}

	public void setCanMassage(boolean canMassage) {
		this.canMassage = canMassage;
	}

	public boolean getCanTrain() {
		return canTrain;
	}

	public void setCanTrain(Boolean canTrain) {
		this.canTrain = canTrain;
	}

	
	public double getMassageTime() {
		return massageTime;
	}

	public void setMassageTime(double arriveMassageTime) {
		this.massageTime = arriveMassageTime;
	}

	public double getPhysioTime() {
		return physioTime;
	}

	public void setPhysioTime(double arrivePhysioTime) {
		this.physioTime = arrivePhysioTime;
	}

	public int getId() {
		return id;
	}

	public int getSkillLevel() {
		return skillLevel;
	}
}
