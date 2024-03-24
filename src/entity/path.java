package entity;


import java.util.HashSet;
import java.util.Set;

public class path {
	
	
	
	
	Node start;
	Node end;
	
	long pathR;
	
	
	public Set<Node> nodes= new HashSet<Node>();
	
	public Set<Node> PathI = new HashSet<Node>();
	
	public Set<Node> PathIlow = new HashSet<Node>();
	
	public Set<Node> PathIhi = new HashSet<Node>();
	
	
	public path( Node start, Node end, Set<Node> nodes) {
		
		
		this.nodes = nodes;
		this.start = start;
		
		this.end = end;
		
		
		
		for(Node node: nodes) {
			
			PathI.addAll(node.Interference);
			
			
		}
		
		
		
	}
	
	
	
	public Node getStart() {
		return start;
	}
	
	
	public Node getEnd() {
		return end;
	}
	
	
	public long getR() {
		
		
		return pathR;
		
	}
	
	

	
	
	public void setR(long R) {
		
		
		this.pathR = R;
		
	}
	
	
	
	public Node getConnector() {
		
		
		
		if(start.predecessor.isEmpty()&&end.successor.isEmpty()) {
			
			return null;
			
		}
		
		if(start.predecessor.isEmpty()&& !end.successor.isEmpty()) {
			
			return end;
			
		}
		
		if(!start.predecessor.isEmpty()&&end.successor.isEmpty()) {
			
			return start;
			
		}
		
		
		if(!start.predecessor.isEmpty()&&!end.successor.isEmpty()) {
			
			if(start.getPriority()<end.getPriority()) {
			
				return start;
			}
			
			if( start.getPriority()>end.getPriority()) {
				
				
				
				return end;
				
			}
			
			
			
			
			
		}
		
		
		
		
		return null;
		
		
		
		
		
		
	}
	
	
	
	public boolean CheckConnection( path p2) {
		
		if(this.getConnector()!=null&&p2.getConnector()!=null) {
			if(this.getConnector().equals(p2.getConnector())&&this.getConnector().equals(this.getEnd())&&p2.getConnector().equals(p2.getStart())) {
				
				return true;
				
				
			}else{
				
				return false;
			}
			
		
		}else {
		
		return false;}
		
		
	}
	
	
	public Node getNew() {
		
		if( getConnector().equals(start)) {
			return end;
			
		}
		
		if( getConnector().equals(end)) {
			return start;
			
		}
		
		return null;
		
		
		
		
	}
	
	
	public boolean Check(path a) {
		
		
		if(a.getStart().equals(start)&& a.getEnd().equals(end)) {
			
			return true;
			
		}else {
			
			
			return false;
		}
		
		
	}
	
	
	public long getInter() {
		
		long interference =0;
		for(Node inter: PathI) {
			
			interference +=  inter.getWCET("max");
			
			
		}
		
		return interference;
		
		
		
	}
	

	
//	public Set<Node> getPathI() {
//		
//
//	
//		for(Node node: nodes) {
//			
//			PathI.addAll(node.Interference);
//			
//			
//		}
//		
//		return PathI;
//	
//	
//	
//	}
	
	
	
	
}
