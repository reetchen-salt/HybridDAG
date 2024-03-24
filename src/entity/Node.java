package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import Util.AnalysisUtil;

public class Node {

	public ArrayList<Node> lowcon = new ArrayList<Node>();

	public ArrayList<Node> concurrent = new ArrayList<Node>();
	public ArrayList<Node> remove = new ArrayList<Node>();

	public ArrayList<Node> Interference = new ArrayList<Node>();
	public ArrayList<Node> lowInter = new ArrayList<Node>();
	public ArrayList<Node> hiInter = new ArrayList<Node>();

	public ArrayList<Node> group = new ArrayList<Node>();
	public ArrayList<Node> groupA = new ArrayList<Node>();
	public ArrayList<Node> groupB = new ArrayList<Node>();
	public ArrayList<Node> groupC = new ArrayList<Node>();
	


	private long lenA;

	public long reference;
	private long lenC;

	public long W;
	
	public boolean anomaly = false;

	private long WCET;
	
	private long newWCET;
	private long start;
	private int index;

	private long weight;

	private int priority;

	private int taskPriority;

	private long tempf = 0;
	
	
	private long earlyT = 0;
	private long lateT=0;
	
	private long randomT=0;
	

	public long[] simulation;

	public long f;

	public long path;
	public long frontpath;
	public long backpath;

	public long grouppath;
	public long groupfront;
	public long groupback;

	public ArrayList<Node> predecessor = new ArrayList<Node>();
	public ArrayList<Node> successor = new ArrayList<Node>();

	public ArrayList<Node> grouppre = new ArrayList<Node>();
	public ArrayList<Node> groupsuc = new ArrayList<Node>();

	public ArrayList<Node> pre_backup = new ArrayList<Node>();
	public ArrayList<Node> suc_backup = new ArrayList<Node>();

	public Set<Node> Ancestor = new HashSet<Node>();
	public Set<Node> Anc_backup = new HashSet<Node>();

	public Set<Node> Descendant = new HashSet<Node>();
	public Set<Node> Des_backup = new HashSet<Node>();

	public Node(long WCET, int index) {

		this.WCET = WCET;
		this.index = index;

	}

	public int getDe() {

		return Descendant.size();
	}

	public int getPriority() {

		return priority;
	}

	public void setPriority(int P) {

		this.priority = P;
	}

	public int getTaskP() {

		return taskPriority;
	}

	public void setTaskP(int P) {

		this.taskPriority = P;
	}

	public long getref() {

		return reference;
	}

	public void setref(long R) {

		this.reference = R;
	}

	public long getWeight() {

		weight = getWCET("max");
		for (Node i : Descendant) {

			weight += i.getWCET("max");

		}

		return weight;
	}

	public int getIndex() {

		return index;
	}

    public long getWCET(String input) {
        switch (input) {
            case "max":
                return WCET;
            case "min":
                return (long) (0.5 * WCET);
            case "random":
                return getNewWCET();
            default:
                throw new IllegalArgumentException("Unknown output type.");
        }
    }
	public void setNewWCET() {
		
		double scaleFactor = 0.5 * Math.random() + 0.5;
    	

		this.newWCET = (long)(scaleFactor * WCET);
	}
	
	public long getNewWCET() {
		
		return newWCET;
	}


	public void setWCET(long W) {

		this.WCET = W;
	}

	public long getLargestP() {

		if (predecessor.isEmpty()) {

			return 0;

		} else {

			predecessor.sort((p1, p2) -> Long.compare(p1.tempf, p2.tempf));
			Collections.reverse(predecessor);

			return predecessor.get(0).tempf;

		}

	}
	
	
//	public long getLP() {
//
//		if (predecessor.isEmpty()) {
//
//			return 0;
//
//		} else {
//
//			predecessor.sort((p1, p2) -> Long.compare(p1.getLateT(), p2.getLateT()));
//			Collections.reverse(predecessor);
//
//			return predecessor.get(0).getLateT();
//
//		}
//
//	}
	
	
	

	

	
	

	public Node getP() {

		if (predecessor.isEmpty()) {

			return null;

		} else {

			predecessor.sort((p1, p2) -> Long.compare(p1.tempf, p2.tempf));
			Collections.reverse(predecessor);

			return predecessor.get(0);

		}

	}

	public long getGrouppath() {

		return grouppath;
	}

	public boolean getState() {

		if (predecessor.isEmpty()) {

			return true;

		} else {

			predecessor.sort((p1, p2) -> Long.compare(p1.tempf, p2.tempf));

			if (predecessor.get(0).tempf != 0) {

				return true;

			} else {

				return false;
			}

		}

	}

	public long getTempf() {

		return tempf;

	}
	
	

	public void setTempf(long tempf) {

		this.tempf = tempf;

	}
	
	
	public long getNewf(String input) {

	      switch (input) {
          case "max":
               return lateT;
          case "min":
              return earlyT;
          case "random":
              return randomT;
          default:
              throw new IllegalArgumentException("Unknown output type.");
      }

	}
	
	
	public void setNewf(Long tempf, String input) {
	    switch (input) {
	        case "max":
	            lateT = tempf;
	            break; // Prevents fall-through
	        case "min":
	            earlyT = tempf;
	            break; // Prevents fall-through
	        case "random":
	            randomT = tempf;
	            break; // Prevents fall-through
	        default:
	            throw new IllegalArgumentException("Unknown output type.");
	    }
	}
	

	public long getlenA() {

		return lenA;

	}

	public void setlenA(long lenA) {

		this.lenA = lenA;

	}

	public long getlenC() {

		return lenC;

	}

	public void setlenC(long lenC) {

		this.lenC = lenC;

	}

	public long getStart(String input) {
		

	
		return getNewf(input)-getWCET(input);

		

	}
	
	

	
	
	
	
	
//	
//    // Getter for earlyT
//    public long getEarlyT() {
//        return earlyT;
//    }
//
//    // Setter for earlyT
//    public void setEarlyT(long earlyT) {
//        this.earlyT = earlyT;
//       
//    }
//
//    // Getter for lateT
//    public long getLateT() {
//        return lateT;
//    }
//
//    // Setter for lateT
//    public void setLateT(long lateT) {
//        this.lateT = lateT;
//    }

//	public long getInterference(long point) {
//		
//		
//		long interference=0;
//		
//		for(Node inter: Interference) {
//			
//			
//			interference +=  inter.tempf==0? inter.getWCET(): Math.min( inter.tempf-point, inter.getWCET());
//			
//			
//		}
//		
//		return interference;
//		
//		
//		
//	}

//	public long Intersum() {
//
//		long interference = 0;
//
//		for (Node inter : Interference) {
//
//			interference += inter.getWCET();
//
//		}
//
//		return interference;
//
//	}

	public void selfclean() {

		this.remove = new ArrayList<Node>();

		this.Interference = new ArrayList<Node>();

//		this.group =  new ArrayList<Node>();
//		this.groupA =  new ArrayList<Node>();
//		this.groupB =  new ArrayList<Node>();
//		this.groupC =  new ArrayList<Node>();
//		
//		this.lenA=0;
//
//		this.lenC=0;
		this.tempf = 0;

	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return Objects.equals(Ancestor, other.Ancestor) && WCET == other.WCET && index == other.index
				&& Objects.equals(predecessor, other.predecessor) && Objects.equals(successor, other.successor);
	}

	@Override
	public String toString() {
		System.out.print(index + "\n");
		return super.toString();
	}

}
