

package GenerationTool;


import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import Util.AnalysisUtil;
import entity.DAG;
import entity.Node;




 

public class PriorityGenerator {
	
	
// WCET ������ȼ���
	
	
//	public void MyAssignment(DAG t){
//		
//		
//		t.DagList.sort((p1, p2) -> Long.compare(p1.getWCET(), p2.getWCET()));
//		
//		Collections.reverse(t.DagList);
//		
//		for(int i=0; i<t.DagList.size();i++) {
//			
//			t.DagList.get(i).setPriority(i);
//			
//		}
//		
//		
//		
//		
//	}
//	
//	
//	public ArrayList<Node> GetWeight(ArrayList<Node> list ){
//		
//		
//		
//		
//		for(int i=0;i<list.size();i++) {
//			
//			list.get(i).W = list.get(i).getWCET();
//			for(int j=0;j<list.size();j++) {
//				
//				if(list.get(i).Descendant.contains(list.get(j))) {
//					
//					list.get(i).W += list.get(j).getWCET();
//					
//					
//				}
//				
//			
//			}
//		
//			
//			
//			
//		}
//		
//		
//		
//		list.sort((p1, p2) -> Long.compare(p1.W, p2.W));
//		
//		Collections.reverse(list);
//		
//		
//		
//		return list;
//		
//		
//		
//	}
//	
	
	public ArrayList<Node> GetOrder(ArrayList<Node> list ){
		
		
//		System.out.print("����ǰ\n");
//		
//		
//		for(Node A:list) {
//			
//			System.out.print(A.getIndex()+",");
//		}
//		
		
		
		
//		list.sort((p1, p2) -> Long.compare(p1.getWeight(), p2.getWeight()));
//		
//		Collections.reverse(list);
		
//		list = (ArrayList<Node>) list.stream().sorted(Comparator.comparing(Node::getWeight).reversed().thenComparing(Comparator.comparing(Node::getWCET).reversed())).collect(Collectors.toList());


//		list.sort((p1, p2) -> Long.compare(p1.Descendant.size(), p2.Descendant.size()));

		
		list = (ArrayList<Node>) list.stream().sorted(Comparator.comparing(Node::getWeight).reversed().thenComparing(Comparator.comparing(Node::getDe).reversed())).collect(Collectors.toList());		
	
		
//		Collections.reverse(list);
		
		
		
		
		
		
//		
//		int size= list.size();
//		
//		ArrayList<Node> temp = new ArrayList<Node>();
//		
//		if(size>1) {
//		
//			while (true) {	
//				int count = 0;
//				for(int i=0 ;i<list.size();i++) {
//					for(int j=i+1;j<list.size();j++) {
//						if(list.get(i).Ancestor.contains(list.get(j))) {
//							temp.add(list.get(j));
//							}}
//					for(int p=0;p<temp.size();p++) {
//						list.remove(temp.get(p));}
//					
//					list.addAll(i, temp);		
//		
//					if(!temp.isEmpty()) {
//						temp.removeAll(temp);
//						break;
//					}else {
//						
//						count++;	
//						if(count == size-1) {
//							
//							break;
//						}}}
//				
//				if(count == size-1) {
//					break;
//				}	
//			}
//	
//	
//
//		}
//			
		
		
		
		return list;
		
		
		
	}
	
	
	
	
	
	public void MyAssignment(DAG t){
		
		
		List<Node> tempdaglist = new ArrayList<Node>();
		
		//���������dag�ڵ㣬2d
     	ArrayList<ArrayList<Node>> prioritylist = new ArrayList<ArrayList<Node>>();
		
     	// ռʱ��dag�б�����ɾ����
		tempdaglist.addAll(t.DagList);  
			
		
		for(int i =1; i< t.Cpath.size(); i++) {
			
			
			ArrayList<Node> plist = new ArrayList<Node>();	
				
				//�����ؼ�·��
				for(Node k : t.Cpath.get(i).Ancestor) {
					
					for(int j = 0; j< tempdaglist.size();j++) {
					
					// ��ȡ��
					if(k.getIndex() == tempdaglist.get(j).getIndex()) {
						
						plist.add(k);
						tempdaglist.remove(k);
						
					}}	}	
				
				
				plist = (ArrayList<Node>) plist.stream()
					    .sorted(Comparator
					        .comparing(Node::getWeight).reversed()
					        .thenComparing(node -> node.getWCET("max")).reversed())
					    .collect(Collectors.toCollection(ArrayList::new));
//				plist.sort((p1, p2) -> Long.compare(p1.getWCET(), p2.getWCET()));
//				
//				Collections.reverse(plist);
//				prioritylist.add(plist);	
				
				prioritylist.add(plist);		
				
				
//						prioritylist.add(GetWeight(plist));	
		
		}
		
		//���һ���ڵ�
		ArrayList<Node> plist2 = new ArrayList<Node>();
		
		plist2.add( t.DagList.get(t.DagList.size()-1));
		
		prioritylist.add(plist2);	
		

		
		int p = t.getPriority()*1000;
		
		for(int i =0; i< prioritylist.size(); i++) {
		
			for(int j = 0; j< prioritylist.get(i).size();j++) {
				
				
				prioritylist.get(i).get(j).setPriority(p);
				 
				p++;
		
			
			}
			}
		

		
		
		for(int k = 0; k< t.DagList.size();k++) {
			for(int i =0; i< prioritylist.size(); i++) {
			
			for(int j = 0; j< prioritylist.get(i).size();j++) {
				
				
				if (prioritylist.get(i).get(j).getIndex() == t.DagList.get(k).getIndex()) {
					
					t.DagList.get(k).setPriority(prioritylist.get(i).get(j).getPriority());
					
				}
				
				
				}}}
		
		
		
		

				
		}
	
	

	
	public void otherAssignment(DAG t){
		
		
		List<Node> tempdaglist = new ArrayList<Node>();
		
		//���������dag�ڵ㣬2d
     	ArrayList<ArrayList<Node>> prioritylist = new ArrayList<ArrayList<Node>>();
		
     	// ռʱ��dag�б�����ɾ����
		tempdaglist.addAll(t.DagList);  
			
		
		for(int i =1; i< t.Cpath.size(); i++) {
			
			
			ArrayList<Node> plist = new ArrayList<Node>();	
				
				//�����ؼ�·��
				for(Node k : t.Cpath.get(i).Ancestor) {
					
					for(int j = 0; j< tempdaglist.size();j++) {
					
					// ��ȡ��
					if(k.getIndex() == tempdaglist.get(j).getIndex()&&!t.Cpath.contains(k)) {
						
						plist.add(k);
						tempdaglist.remove(k);
						
					}}	}	
				
				
					
				plist = plist.stream()
					    .sorted(Comparator
					        .comparing(Node::getWeight).reversed()
					        .thenComparing((Node node) -> node.getWCET("max")).reversed())
					    .collect(Collectors.toCollection(ArrayList::new));
				
//				Collections.reverse(plist);
//				prioritylist.add(plist);	
				
				prioritylist.add(plist);		
				
				
//						prioritylist.add(GetWeight(plist));	
		
		}
		


		
		int p = 0;
		
		t.Cpath.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		
		for(Node A: t.Cpath) {
			
			
			A.setPriority(p);
			
			p++;
		}
		

		
		
		for(int i =0; i< prioritylist.size(); i++) {
		
			for(int j = 0; j< prioritylist.get(i).size();j++) {
				
				
				prioritylist.get(i).get(j).setPriority(p);
				 
				p++;
		
			
			}
			}
		

		
		
		for(int k = 0; k< t.DagList.size();k++) {
			for(int i =0; i< prioritylist.size(); i++) {
			
			for(int j = 0; j< prioritylist.get(i).size();j++) {
				
				
				if (prioritylist.get(i).get(j).getIndex() == t.DagList.get(k).getIndex()) {
					
					t.DagList.get(k).setPriority(prioritylist.get(i).get(j).getPriority());
					
				}
				
				
				}}}
		
		
		
		

				
		}
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
//	WCET
	public void BasicAssignment(DAG t){
		
	
		
		t.DagList.sort((p1, p2) -> Long.compare(p1.getWCET("max"), p2.getWCET("max")));
		
		Collections.reverse(t.DagList);
		
		
		
		for(int i=0; i<t.DagList.size();i++) {
			
			t.DagList.get(i).setPriority(i);
			
		}
		
			
		
		
	}
	
	public void SecondAssignment(DAG t){
		
	
		
		t.DagList.sort((p1, p2) -> Long.compare(p1.getWCET("max"), p2.getWCET("max")));
		
		Collections.reverse(t.DagList);
		
		for(int i=0; i<t.DagList.size();i++) {
			
			t.DagList.get(i).setPriority(i);
			
		}
		
		new AnalysisUtil().Adjust(t.DagList);
		
		
		for(int i=0; i<t.DagList.size();i++) {
			
			t.DagList.get(i).setPriority(i);
			
		}
		
		
			
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
//	INDEX
//	public void BasicAssignment(DAG t){
//		
//	
//		
//		
//		for(int i=0; i<t.DagList.size();i++) {
//			
//			t.DagList.get(i).setPriority(t.DagList.get(i).getIndex()-1);
//			
//		}
//		
//			
//		
//		
//	}
	
	//random
	public void RandomAssignment(DAG t){
		
		
		
		
		Collections.shuffle(t.DagList);
		
	
		
		
		int  p =  t.getPriority()*1000;
		
		for(int i=0; i<t.DagList.size();i++) {
			
			t.DagList.get(i).setPriority(p);
			
			p++;			
		}
		
			
		
		
	}
	
	public void Weightfirst(DAG t){
		
		
		
		t.DagList.sort((p1, p2) -> Long.compare(p1.getWeight(), p2.getWeight()));
		
		Collections.reverse(t.DagList);
		
		int  p =  t.getPriority()*1000;
		
		for(int i=0; i<t.DagList.size();i++) {
			
			t.DagList.get(i).setPriority(p);
			
			p++;			
		}
		
			
		
		
	}
	
	
	
	public void Shuai2020(DAG t){
		
		
		
		
		List<Node> tempdaglist = new ArrayList<Node>();
		
		//���������dag�ڵ㣬2d
	 	ArrayList<ArrayList<Node>> prioritylist = new ArrayList<ArrayList<Node>>();
		
	 	// ռʱ��dag�б�����ɾ����
		tempdaglist.addAll(t.DagList);  
			
		
		for(int i =1; i< t.Cpath.size(); i++) {
			
			
			ArrayList<Node> plist = new ArrayList<Node>();	
				
				//�����ؼ�·��
				for(Node k : t.Cpath.get(i).Ancestor) {
					
					for(int j = 0; j< tempdaglist.size();j++) {
					
					// ��ȡ��
					if(k.getIndex() == tempdaglist.get(j).getIndex()) {
						
						plist.add(k);
						tempdaglist.remove(k);
						
					}}	}	
				
				

				new AnalysisUtil().findPath(plist);
				



				
				plist= (ArrayList<Node>) plist.stream().sorted(Comparator.comparing(Node::getGrouppath).reversed().thenComparing(Comparator.comparing(Node::getIndex))).collect(Collectors.toList());
	
				
				Node critical = null;
				for(Node A:plist ) {
					
					if(t.Cpath.contains(A)) {
						
						critical=A;
					}
				}
				
				plist.remove(critical);
				
				
				
				prioritylist.add(plist);		
		
		}
		
//		//���һ���ڵ�
//		ArrayList<Node> plist2 = new ArrayList<Node>();
//		
//		plist2.add( t.DagList.get(t.DagList.size()-1));
//		
//		prioritylist.add(plist2);
		
		
		prioritylist.add(0,t.Cpath);
		
	
		
		int p = t.getPriority()*10;
		for(int i =0; i< prioritylist.size(); i++) {
		
			for(int j = 0; j< prioritylist.get(i).size();j++) {
				
				
				prioritylist.get(i).get(j).setPriority(p);
				

				p++;
		
			
			}
			}
		
	
		
		
		for(int k = 0; k< t.DagList.size();k++) {
			for(int i =0; i< prioritylist.size(); i++) {
			
			for(int j = 0; j< prioritylist.get(i).size();j++) {
				
				
				if (prioritylist.get(i).get(j).getIndex() == t.DagList.get(k).getIndex()) {
					
					t.DagList.get(k).setPriority(prioritylist.get(i).get(j).getPriority());
					
				}
				
				
				}}}
		
		

	
		
		
		
		
		
	
	}
	
	
//	public void Shuai2020(DAG t, int core){
//		
//	    ArrayList<String> StringDAG  = new AnalysisUtil().Stringmaker(t);
//	    
//	    t.DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
//	    
//	    try {
//
//	   
//			Process p = Runtime.getRuntime().exec("python3 DAG.py " + StringDAG.get(0) + " " + StringDAG.get(1) + " " + StringDAG.get(2) + " " + core + " " + 0);
//
//			
//			
//			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//
//
//			String s = null;
//			ArrayList<Integer> priorityList = new ArrayList<Integer>();
//
//			while ((s = stdInput.readLine()) != null) {
////				System.out.println(s);
//				s=  s.substring(1, s.length() - 1); //remove the bracket
//				
//
//				
//				ArrayList<String> StringList = new ArrayList<String>(Arrays.asList(s.split(",")));
//			
//				
//				for(String a : StringList) {
//					
//					String[] pair_array = a.split(":");
//					
//					
//					priorityList.add(Integer.parseInt(pair_array[0].trim())-1, 1000-Integer.parseInt(pair_array[1].trim()));
//					
//					
//				}
//				
//		
//			}
//			
//		    
//		    for(int i=0;i<t.DagList.size();i++) {
//		    	
//		    	
//		    	t.DagList.get(i).setPriority(priorityList.get(i));
//		    	
//		    }
//			
////			System.out.print(priorityList);
//
//			String error = null;
//			while ((error = stdError.readLine()) != null) {
//				System.out.println(error);
//			}
//			
//        } catch (IOException e) {
//            e.printStackTrace();
//
//        }
//	    
//	    
//	  
//
//	    
//
//	    
//
//	}
	
	

	
	public ArrayList<Node> He2019(ArrayList<Node> List, int p){
		
		ArrayList<Node> DagList = new ArrayList<Node>();
		
		
		DagList.addAll( List);
		ArrayList<Node> tempDAG2 = new ArrayList<Node>();
		
		
		while(!DagList.isEmpty()) {
			
			


			DagList.sort((p1, p2) -> Integer.compare(p1.predecessor.size(), p2.predecessor.size()));
			
		
			ArrayList<Node> tempS = new ArrayList<Node>();
			
			ArrayList<Node> tempSuccessor = new ArrayList<Node>();
			
			if(DagList.get(0).predecessor.isEmpty()) {
				
				
				DagList.get(0).setPriority(p);
				p++;
				tempDAG2.add(DagList.get(0));
				
				
					
				tempS.addAll(DagList.get(0).successor);
				
				DagList.remove(0);
				
				DagList = UpdateDag(DagList);
				
				for(int i =0;i<tempS.size();i++) {
					
				if(DagList.contains(tempS.get(i))) {
					
					
					tempSuccessor.add(tempS.get(i));
					
					
				
				}}
				
				}
			
			
			
			
			// ��һ���ڵ�ĺ����ڵ㣬�Լ�����ĺ����ڵ�
			while(!tempSuccessor.isEmpty()) {
				
				tempSuccessor.sort((p1, p2) -> Long.compare(p1.path, p2.path));
				Collections.reverse(tempSuccessor);
				
				if(tempSuccessor.size()>1) {
					if(tempSuccessor.get(0).path == tempSuccessor.get(1).path) {
						
						tempSuccessor.sort((p1, p2) -> Long.compare(p1.frontpath, p2.frontpath));
						Collections.reverse(tempSuccessor);
					}
				
				}
				
			
				
				//���ǰ�ýڵ㻹û������
				if(!tempSuccessor.get(0).predecessor.isEmpty()) {

								
					//graph
					ArrayList<Node> list = new ArrayList<Node>(); // priority list
					
					
					
					ArrayList<Node> templist = new ArrayList<Node>(); // ancestor list to assign priroity first
					
					//ȷ����ɾ�Ĳ����ӽ���
					for(Node i :tempSuccessor.get(0).Ancestor) {
					
						if(DagList.contains(i)) {
							templist.add(i);
							
						}
					
					
					}					
					
					
					templist= UpdateDag(templist);
					
	
					
					list = He2019(templist,p);
					
					p = list.get(0).getIndex();
					list.remove(0);
					
					for(int i =0; i < list.size(); i++ ) {
						
//						tempDAG.get(list.get(i)).setPriority(p);
						for(int j =0; j < DagList.size(); j++) {
							
							if(list.get(i).getIndex()==DagList.get(j).getIndex()) {
								
								DagList.get(j).setPriority(list.get(i).getPriority());
								
								
								tempDAG2.add(DagList.get(j));
								
								

								
							}
							
						}
						
					}
					
					DagList.removeAll( list );
					DagList = UpdateDag(DagList);
					
					
				}
				
				
//				tempDAG.sort((p1, p2) -> Long.compare(p1.path, p2.path));
				
				tempSuccessor.get(0).setPriority(p);
				p++;
				tempDAG2.add(tempSuccessor.get(0));
				
				DagList.remove(tempSuccessor.get(0));
				DagList = UpdateDag(DagList);
				
				tempSuccessor= tempSuccessor.get(0).successor;
				
				
			}
			
			
			
			
			
		}
		
		
		

		Node index = new Node(0,p);
		

		

		tempDAG2.add(0,index);
		
		return tempDAG2;
			
		
		
		
	}


	
	
	
	
	
	public ArrayList<Node> UpdateDag(ArrayList<Node> templist){
		
		
		
		
		for(int i =0; i < templist.size(); i++ ) {

			
			ArrayList<Node> tempA = new ArrayList<Node>();
			for(int k =0; k < templist.get(i).predecessor.size(); k++ ){
				
				if(  !templist.contains( templist.get(i).predecessor.get(k)) ) {
					
					tempA.add(templist.get(i).predecessor.get(k));
					
				}
				
			}
			
			templist.get(i).predecessor.removeAll(tempA);
			

			
			ArrayList<Node> tempB = new ArrayList<Node>();
			for(int k =0; k <templist.get(i).successor.size(); k++ ) {
				
				if(  !templist.contains( templist.get(i).successor.get(k)) ) {
					
					tempB.add( templist.get(i).successor.get(k));
					
				}}
			templist.get(i).successor.removeAll(tempB);
			
			
			
			
			
			 Set<Node> tempC = new HashSet<Node>();
			
			
			for(Node k : templist.get(i).Ancestor ) {
				

				
				if(  !templist.contains( k) ) {
					
				tempC.add(k);
					
				}}
			
			templist.get(i).Ancestor.removeAll(tempC);
			
			
			 Set<Node> tempD = new HashSet<Node>();
			 
			 tempD.addAll(templist.get(i).Descendant);
			
			for(Node k : tempD  ) {
				
			if(  !templist.contains( k) ) {
					
				tempD.add(k);

					
				}}
			
			templist.get(i).Descendant.removeAll(tempD);
		
		
		}	
		
		
		
		return templist;
	

	
	
	}
	
	
	
	
	
	
	public void He2021(DAG task){
		

		ArrayList<Node> DagList = new ArrayList<Node>();
		
		 DagList.addAll(task.DagList);
		
		
		DagList.sort((p1, p2) -> Long.compare(p1.path, p2.path));
		
		Collections.reverse(DagList);
		
		ArrayList<ArrayList<Node>> DagArray = new ArrayList<ArrayList<Node>>();
		
		
		while(!DagList.isEmpty()) {
			
			long temppath = DagList.get(0).path;
			
			ArrayList<Node> templist = new ArrayList<Node>();
			
			for(int i=0; i<DagList.size();i++) {
				
				if( DagList.get(i).path==temppath ) {
					templist.add(DagList.get(i));
					
				}
				
			}
			
			DagList.removeAll(templist);
			
			templist.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
			
			DagArray.add(templist);
		
		
		}
		
		int p = task.getPriority()*1000;
		
		for(int i=0; i<DagArray.size();i++) {
			for(int j=0; j<DagArray.get(i).size();j++) {
				
				DagArray.get(i).get(j).setPriority(p);
			
				DagList.add(DagArray.get(i).get(j));
				p++;
			
		}}

		
		
		DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		
		
		for(Node A: task.DagList) {
			
			for(Node B: DagList) {
				
				if(A.getIndex()==B.getIndex()) {
					
					B.setPriority(A.getPriority());
					
				}
				
				
				
			}
			
			
		}
		
		
//		 for(Node a: DagList ) {
//			 
//				
//			 System.out.println( a.getPriority()+"  ,"  );
//		 
//		 
//		 }
		
		
		
		
	}
	
	
	

	
	
	public void DMassignment( ArrayList<DAG> tasks) {
		
		
		
		tasks.sort((p1, p2) -> Long.compare(p1.deadline, p2.deadline));
		
		
		for(int i=0; i < tasks.size(); i++) {
			
			
			tasks.get(i).setPriority(i);
			
			
			for(Node A: tasks.get(i).DagList) {
				
				A.setTaskP(i);
				
				
			}
			
		}
		

	
	}
	
	
	
	public void RandomTasks( ArrayList<DAG> tasks) {
		
		
		
		
		Collections.shuffle(tasks);
		
		for(int i=0; i<tasks.size();i++) {
			
			tasks.get(i).setPriority(i);
			
			for(Node A: tasks.get(i).DagList) {
				
				A.setTaskP(i);
				
				
			}
			
			
		}
		
		
		tasks.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		
		

	}
	
	
	
	public void Weightassignment( ArrayList<DAG> tasks) {
		
		
		
		tasks.sort((p1, p2) -> Double.compare(p1.getAverage() ,p2.getAverage()));
		
		
		Collections.reverse(tasks);
		
		
		for(int i=0; i < tasks.size(); i++) {
			
			
			tasks.get(i).setPriority(i);
			
			
			for(Node A: tasks.get(i).DagList) {
				
				A.setTaskP(i);
				
				
			}
			
		}
		

		tasks.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		
		
		
		
	}
	
	
	public void heft(DAG t){
		
		
		
		t.DagList.sort((p1, p2) -> Long.compare(p2.backpath, p1.backpath));

		int p=0;
		
		for(Node thenode: t.DagList) {
			
			thenode.setPriority(p);
			
			p++;
			
			
		}
		
		
		
		
		
	
	}
	
	
	
		
		
		
		
		
	}



