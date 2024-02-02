package entity;

import java.util.ArrayList;

public class Core {
	
	

	
	public ArrayList<Node> Nodes = new ArrayList<Node>();
	
	
	private long load =0;
	
	
	public int id ;
	
	public Core (int x ) {
		
	 this.id =x ;	
		
	}
	
	
	public int getID(){
		
		return id;
		
		
	}
	
	
	
	public void setLoad(long load) {
		
		
		this.load = load;
	}
	
	public long getLoad() {
		
		
		return load;
	}
	
	
	
	
	
	
	
	
//	public Node lastNode() {
//		
//		if( !Nodes.isEmpty()) {
//		return Nodes.get(Nodes.size()-1);
//		}
//		else {
//		return null;
//		}
//	}
	
	
	

	
	

}
