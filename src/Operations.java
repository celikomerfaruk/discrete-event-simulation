import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

public class Operations {
	private int availableCoach;
	private int availableMasseur;
	
	private Queue<Player> trainingQ;
	private PriorityQueue<Player> physiotherapyQ;
	private PriorityQueue<Player> massageQ;
	private ArrayList<Player> players ;
	private PriorityQueue<Arrival> arrivals ;
	private PriorityQueue<Physiotherapist> physiotherapists ;
	private HashMap<Integer,Physiotherapist> busyPhysiotherapists;
	private PriorityQueue<Player> waitinPhysiyo;
	private PriorityQueue<Player> waitinMassage;
	
	private int maxLenTQ = 0;
	private int maxLenMQ= 0;
	private int invalidAttempts =0;
	private int maxLenPQ =0;
	private int totTrainOccured = 0;
	private int cancelledAttemptes = 0;
	private int totMassageOccured = 0;
	private int totPhysioOccured = 0;
	private double totWaitingTQ = 0;
	private double totWaitingPQ = 0 ;
	private double totWaitingMQ = 0;
	private double totMassageTime = 0;
	private double totTrainTime = 0 ;
	private double totPhysioTime = 0;
	private double totTime = 0;
	public Operations() {
		players = new ArrayList<>();
		
		arrivals = new PriorityQueue<>(new ArrivalComparator());
		physiotherapists = new PriorityQueue<>(new PhysiotherapistComparator());
		physiotherapyQ= new PriorityQueue<Player>(new PhysioQComparator()) ;
		massageQ = new PriorityQueue<Player>(new MassageQComparator());
		trainingQ = new LinkedList<Player>();
		waitinPhysiyo = new PriorityQueue<>(new WaitinPhysioComparator());
		waitinMassage = new PriorityQueue<>(new WaitinMassageComparator());
	}
	
	//Function where discrete events are executed
	public void Operate(){
		busyPhysiotherapists = new HashMap<>(physiotherapists.size());
		
		while(!arrivals.isEmpty()) {
			Arrival arrival = arrivals.poll();
			
			if(arrival.getType().equals("t")){
				
				if(players.get(arrival.getIdOfPlayer()).getCanTrain() ==true){
					players.get(arrival.getIdOfPlayer()).setTrainTime(arrival.getDuration());
					setTrainArrive(arrival.getIdOfPlayer(), arrival.getArrivalTime());
					players.get(arrival.getIdOfPlayer()).setCanMassage(false);
					players.get(arrival.getIdOfPlayer()).setCanTrain(false);
					if(availableCoach == 0) {
						trainingQ.add(players.get(arrival.getIdOfPlayer()));
						if(maxLenTQ <trainingQ.size()) {
							maxLenTQ = trainingQ.size();
						}
					}
			
					if(availableCoach > 0 ) {
					
						
						totTrainOccured++;
						availableCoach--;
						totTrainTime+= arrival.getDuration();
						Arrival endOfTrain = new Arrival("endOfTrain", arrival.getIdOfPlayer(), arrival.getArrivalTime()+arrival.getDuration(),0 ); 
						arrivals.add(endOfTrain);
					}
			}else {
				cancelledAttemptes++;
			}
			}
			if (arrival.getType().equals("startTrain")) {
				if(availableCoach > 0 ) {
					
					totWaitingTQ+= (arrival.getArrivalTime()-players.get(arrival.getIdOfPlayer()).getArriveTrainTime());
					
					totTrainOccured++;
					availableCoach--;
					totTrainTime+= arrival.getDuration();
					Arrival endOfTrain = new Arrival("endOfTrain", arrival.getIdOfPlayer(), arrival.getArrivalTime()+arrival.getDuration(),0 ); 
					arrivals.add(endOfTrain);
				}
				
			}
			if (arrival.getType().equals("endOfTrain")) {
				availableCoach++;
				if (trainingQ.size() >0) {
					Player temPlayer = trainingQ.poll();
					Arrival train = new Arrival("startTrain", temPlayer.getId(), arrival.getArrivalTime(), temPlayer.getTrainTime());
					arrivals.add(train);
				}
				players.get(arrival.getIdOfPlayer()).setArrivePhysioTime(arrival.getArrivalTime());
				Arrival startPhysio = new Arrival("startPhysio", arrival.getIdOfPlayer(), arrival.getArrivalTime(), 0);
				arrivals.add(startPhysio);
				
				
			}
			if(arrival.getType().equals("m")){
				if(players.get(arrival.getIdOfPlayer()).getTakenMassage()<3) {
				
				if (players.get(arrival.getIdOfPlayer()).isCanMassage() == true) {
					players.get(arrival.getIdOfPlayer()).setCanTrain(false);
					players.get(arrival.getIdOfPlayer()).incrementTakenMassage();
					players.get(arrival.getIdOfPlayer()).setCanMassage(false);
					players.get(arrival.getIdOfPlayer()).setMassageTime(arrival.getDuration());
					setMassageArrive(arrival.getIdOfPlayer(), arrival.getArrivalTime());
					if (availableMasseur>0) {
						availableMasseur--;
						
						totMassageOccured++;
						totMassageTime+=arrival.getDuration();
						Arrival endOfMassage = new Arrival("endOfMassage", arrival.getIdOfPlayer(), arrival.getArrivalTime()+arrival.getDuration(), 0);
						arrivals.add(endOfMassage);
					}else {
						massageQ.add(players.get(arrival.getIdOfPlayer()));
						if(maxLenMQ<massageQ.size()) {
							maxLenMQ = massageQ.size();
						}
					}
				}else {
					cancelledAttemptes++;
				}
			}else {
				invalidAttempts++;
			}}
			if(arrival.getType().equals("startMassage")){
				 
					if (availableMasseur>0) {
						double tempWait = arrival.getArrivalTime()- players.get(arrival.getIdOfPlayer()).getArriveMassageTime();
						
						players.get(arrival.getIdOfPlayer()).incWaitinMQ(tempWait);
						
						totWaitingMQ += tempWait;
						availableMasseur--;
						
						totMassageOccured++;
						totMassageTime+=arrival.getDuration();
						Arrival endOfMassage = new Arrival("endOfMassage", arrival.getIdOfPlayer(), arrival.getArrivalTime()+arrival.getDuration(), 0);
						arrivals.add(endOfMassage);
					
					}
				
			}
			if (arrival.getType().equals("endOfMassage")) {
				availableMasseur++;
				players.get(arrival.getIdOfPlayer()).setCanTrain(true);
				players.get(arrival.getIdOfPlayer()).setCanMassage(true);
				if (massageQ.size()>0) {
					Player temPlayer = massageQ.poll();
					Arrival startMassage = new Arrival("startMassage", temPlayer.getId(), arrival.getArrivalTime(),temPlayer.getMassageTime() );
					arrivals.add(startMassage);
				}
			}
			if(arrival.getType().equals("startPhysio")){
				
				if(physiotherapists.size() > 0) {
					double tempWait = arrival.getArrivalTime() - players.get(arrival.getIdOfPlayer()).getArrivePhysioTime();
					
					players.get(arrival.getIdOfPlayer()).incWaitinPQ(tempWait);
					
					totWaitingPQ += tempWait;
					totPhysioOccured++;
					Physiotherapist tempP = physiotherapists.poll();
					busyPhysiotherapists.put(tempP.getId(), tempP);;
					totPhysioTime += tempP.getServiceTime();
					Arrival physioFinish = new Arrival("physioFinish",arrival.getIdOfPlayer() , arrival.getArrivalTime()+tempP.getServiceTime(), tempP.getId());
					arrivals.add(physioFinish);
				}else {
					physiotherapyQ.add(players.get(arrival.getIdOfPlayer()));
					if (maxLenPQ < physiotherapyQ.size()) {
						maxLenPQ = physiotherapyQ.size();
					}
				}
				
			}
			if (arrival.getType().equals("physioFinish") ) {
				players.get(arrival.getIdOfPlayer()).setCanTrain(true);
				players.get(arrival.getIdOfPlayer()).setCanMassage(true);
				Physiotherapist temPhysiotherapist = busyPhysiotherapists.get((int)arrival.getDuration());
				busyPhysiotherapists.remove((int)arrival.getDuration());
				physiotherapists.add(temPhysiotherapist);
				if (physiotherapyQ.size()>0) {
					Player temPlayer = physiotherapyQ.poll();
					Arrival startPhysio = new Arrival("startPhysio", temPlayer.getId(), arrival.getArrivalTime(), 0);
					arrivals.add(startPhysio);
				}
			}
		
	    // calculating the total time elapsed
		if(arrivals.size() == 0) {
			totTime = arrival.getArrivalTime();
		}
		}
		
		//Required data for output 11 and 12
		waitinPhysiyo.addAll(players);
		for (Iterator<Player> iterator = players.iterator(); iterator.hasNext();) {
			Player player =  iterator.next();
			if(player.getTakenMassage() == 3) {
				waitinMassage.add(player);
			}
		}
		
	}
	public void setTrainArrive(int id,double time) {
		players.get(id).setArriveTrainTime(time);
	}
	public void setMassageArrive(int id, double time) {
		players.get(id).setArriveMassageTime(time);
	}
	
	public void addPlayer(Player p) {
		players.add(p);
	}
	public void addArrival(Arrival a) {
		arrivals.add(a);
	}
	
	public void addPhysio(Physiotherapist p) {
		physiotherapists.add(p);
	}
	public void setAvailableCoach(int availableCoach) {
		this.availableCoach = availableCoach;
	}
	public void setAvailableMasseur(int availableMasseur) {
		this.availableMasseur = availableMasseur;
	}
	
	//generating output
	public String generateOutput() {
		String outputString = maxLenTQ + "\n" + maxLenPQ  +"\n"+ maxLenMQ + "\n";
		double avrWaitinTQ = totWaitingTQ/totTrainOccured ;
		double avrWaitinPQ = totWaitingPQ/totPhysioOccured ;
		double avrWaitinMQ = totWaitingMQ/totMassageOccured ;
		double avrTrainTime = totTrainTime/totTrainOccured ;
		double avrPhysioTime = totPhysioTime/totPhysioOccured;
		double avrMassageTime = totMassageTime/totMassageOccured ;
		double avrTurnaround = (totTrainTime+totPhysioTime+totWaitingPQ+totWaitingTQ)/totTrainOccured;
		
		
		outputString += String.format(Locale.US,"%.3f",avrWaitinTQ )+"\n"  + String.format(Locale.US,"%.3f",avrWaitinPQ ) +"\n"  + String.format(Locale.US,"%.3f",avrWaitinMQ ) + "\n" + String.format(Locale.US,"%.3f",avrTrainTime ) +  "\n" + String.format(Locale.US,"%.3f",avrPhysioTime ) +  "\n" + String.format(Locale.US,"%.3f",avrMassageTime )+ "\n"+String.format(Locale.US,"%.3f",avrTurnaround ) + "\n";
		
		outputString += waitinPhysiyo.peek().getId() + " " + String.format(Locale.US,"%.3f",waitinPhysiyo.peek().getWaitinPQ() );
		String str;
		if(waitinMassage.size() == 0) {
			str = "-1 -1";
		}else {
			str = waitinMassage.peek().getId() + " " + String.format(Locale.US,"%.3f",waitinMassage.peek().getWaitinMQ() );
		}
		outputString+= "\n" +str;
		outputString +="\n"+ invalidAttempts +"\n" +cancelledAttemptes + "\n" + String.format(Locale.US,"%.3f",totTime );
		
		return outputString;
	}
}
class PhysiotherapistComparator implements Comparator<Physiotherapist> {
	public int compare(Physiotherapist p1 , Physiotherapist p2) {
		return p1.getId()-p2.getId();
	}
}
class PhysioQComparator implements Comparator<Player>{

	@Override
	public int compare(Player p1, Player p2) {
		if (Math.abs(p1.getTrainTime()- p2.getTrainTime())< 0.0000000001) {
			if (Math.abs(p1.getArrivePhysioTime()-p2.getArrivePhysioTime())< 0.0000000001) {
				return p1.getId() - p2.getId();
			}else {
				return Double.compare(p1.getArrivePhysioTime(), p2.getArrivePhysioTime());
			}
				
			}
		else {
			return Double.compare(p2.getTrainTime(), p1.getTrainTime()); 
			
		}
		
	}
	
}

class MassageQComparator implements Comparator<Player>{

	@Override
	public int compare(Player o1, Player o2) {
		if (Math.abs(o1.getSkillLevel()-o2.getSkillLevel())< 0.0000000001) {
			if (Math.abs(o1.getArriveMassageTime()- o2.getArriveMassageTime())< 0.0000000001) {
				return o1.getId() - o2.getId();
			}else {
				return Double.compare(o1.getArriveMassageTime(), o2.getArriveMassageTime());
			}
		}else {
			return Double.compare(o2.getSkillLevel(), o1.getSkillLevel());
		}
		
	}
	
}
class WaitinMassageComparator implements Comparator<Player>{
	public int compare(Player o1, Player o2) {
		if (Math.abs(o1.getWaitinMQ()- o2.getWaitinMQ())< 0.0000000001) {
			return o1.getId() - o2.getId();
		}else {
			return Double.compare(o1.getWaitinMQ(), o2.getWaitinMQ());
		}
	}
}
class WaitinPhysioComparator implements Comparator<Player>{

	@Override
	public int compare(Player o1, Player o2) {
		if (Math.abs(o1.getWaitinPQ()- o2.getWaitinPQ())< 0.0000000001) {
			return o1.getId() - o2.getId();
		}else {
			return Double.compare(o2.getWaitinPQ(), o1.getWaitinPQ());
		}
	}
	
}

class ArrivalComparator implements Comparator<Arrival>{

	@Override
	public int compare(Arrival o1, Arrival o2) {
		if ( Math.abs(o1.getArrivalTime() - o2.getArrivalTime())< 0.0000000001) {
			return o1.getIdOfPlayer() - o2.getIdOfPlayer();
		}else {
		return Double.compare(o1.getArrivalTime(), o2.getArrivalTime()) ;
		}
	}
	
}