package Analysis;

import entity.DAG;
import entity.Node;

public class Melanni2015 {
	
	
	
	
	
	
	
	
	
	
	public long WCmakespan(DAG t, int m) {
		

		
		for(Node A: t.DagList) {
			
			A.selfclean();
			
			
		}
		
		
		
		
		long makespan = 0;
		
		
		for(Node a: t.Cpath) {
			
			makespan +=  a.getWCET("max");
			
		}
		
		
		
		long I  = 0;
		for(Node b : t.DagList) {
			
			if(! t.Cpath.contains(b)) {
				
				
				I += b.getWCET("max");
			}
			
			
		}
		
		
		makespan += Math.ceil(I/(double)m);
		
		
		
		
		
		
		
		
		
		
		return makespan;
		
		
		
		
	
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
