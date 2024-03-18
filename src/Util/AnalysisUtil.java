package Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import entity.Core;
import entity.DAG;
import entity.Node;

public class AnalysisUtil {

	public ArrayList<Node> Adjust(ArrayList<Node> tempdag) {

		tempdag.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

		int size = tempdag.size();

		ArrayList<Node> temp = new ArrayList<Node>();

		if (size > 1) {

			while (true) {
				int count = 0;
				for (int i = 0; i < tempdag.size(); i++) {
					for (int j = i + 1; j < tempdag.size(); j++) {
						if (tempdag.get(i).Ancestor.contains(tempdag.get(j))) {
							temp.add(tempdag.get(j));
						}
					}
					for (int p = 0; p < temp.size(); p++) {
						tempdag.remove(temp.get(p));
					}
					tempdag.addAll(i, temp);

					if (!temp.isEmpty()) {
						temp.removeAll(temp);
						break;
					} else {

						count++;
						if (count == size - 1) {

							break;
						}
					}
				}

				if (count == size - 1) {
					break;
				}
			}

		}

		return tempdag;

	}

	public void CopyP(ArrayList<Node> Daglist1, ArrayList<Node> Daglist2) {

		for (Node A : Daglist1) {

			for (Node B : Daglist2) {

				if (A.getIndex() == B.getIndex()) {

					B.setPriority(A.getPriority());

				}

			}

		}

	}

	public void printTempf(ArrayList<Node> Daglist) {

		Daglist.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		for (Node a : Daglist) {

			System.out.print("\n tempf of V" + a.getIndex() + " : " + a.getTempf());

		}

	}

	public void recover(ArrayList<Node> Daglist) {

		for (int i = 0; i < Daglist.size(); i++) {

			Daglist.get(i).successor = Daglist.get(i).suc_backup;
			Daglist.get(i).predecessor = Daglist.get(i).pre_backup;
			Daglist.get(i).Ancestor = Daglist.get(i).Anc_backup;

			Daglist.get(i).Descendant = Daglist.get(i).Des_backup;

//		     System.out.print("\n predecessor size \n");
//				
//		     for(int j = 0; j<  Daglist.size();j++) {
//		    	 
//		 	 	System.out.print(Daglist.get(i).suc_backup.size()  +",");
//		 	 	
//		 	 
//		  }
//			 
//		 
		}

	}

	public void cloneDag(ArrayList<Node> Dag1, ArrayList<Node> Dag2) {

		Dag2.removeAll(Dag2);

		for (int i = 0; i < Dag1.size(); i++) {

			Dag2.add(new Node(Dag1.get(i).getWCET(false), Dag1.get(i).getIndex()));

			Dag2.get(i).setPriority(Dag1.get(i).getPriority());

			Dag2.get(i).setTempf(Dag1.get(i).getTempf());

			Dag2.get(i).frontpath = Dag1.get(i).frontpath;

			Dag2.get(i).backpath = Dag1.get(i).backpath;
			Dag2.get(i).path = Dag1.get(i).path;

		}

		// successor

		for (int k = 0; k < Dag2.size(); k++) {
			for (int i = 0; i < Dag1.size(); i++) {
				// ----
				for (int j = 0; j < Dag1.get(i).successor.size(); j++) {

					if (Dag2.get(k).getIndex() == Dag1.get(i).successor.get(j).getIndex()) {

						Dag2.get(i).successor.add(Dag2.get(k));

					}
				}
				// --
				for (int j = 0; j < Dag1.get(i).predecessor.size(); j++) {

					if (Dag2.get(k).getIndex() == Dag1.get(i).predecessor.get(j).getIndex()) {

						Dag2.get(i).predecessor.add(Dag2.get(k));

					}
				}

				// --

				for (Node j : Dag1.get(i).Descendant) {

					if (Dag2.get(k).getIndex() == j.getIndex()) {

						Dag2.get(i).Descendant.add(Dag2.get(k));

					}
				}

				// --

				for (Node j : Dag1.get(i).Ancestor) {

					if (Dag2.get(k).getIndex() == j.getIndex()) {

						Dag2.get(i).Ancestor.add(Dag2.get(k));

					}
				}

			}
		}

//     System.out.print("\npredecessor1\n");
//		
//     for(int i = 0; i<  Dag1.size();i++) {
//    	 
//    	 	System.out.print(Dag1.get(i).predecessor.size() +",");
//    	 	
//    	 
//     }
//     System.out.print("\npredecessor2\n");
//		
//     for(int i = 0; i<  Dag2.size();i++) {
//    	 
// 	 	System.out.print(Dag2.get(i).predecessor.size()  +",");
// 	 	
// 	 
//  }

	}

//	
//	
//	public ArrayList<String> Stringmaker(DAG task){
//		
//		
//		
//		ArrayList<String> StringDAG = new ArrayList<String>() ;
//		
//		String G = "{";
//		String C = "{";
//		String P = "{";
//		
//		
//		
//		for(int i=0; i < task.DagList.size()-1; i++) {
//			
//			task.DagList.get(i).successor.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
//			
//			  List<Integer> collect = task.DagList.get(i).successor.stream().map(x -> x.getIndex()).collect(Collectors.toList());
//			
//			G += task.DagList.get(i).getIndex()+":[" + collect.stream().map( String::valueOf) .collect(Collectors.joining(","))+"],";
//			
//			
//			
//			C += task.DagList.get(i).getIndex()+":"+task.DagList.get(i).getWCET(false)+",";
//			
//			P += task.DagList.get(i).getIndex()+":"+task.DagList.get(i).getPriority()+",";
//			
//			
//			
//		}
//		
//		task.DagList.get(task.DagList.size()-1).successor.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
//		
//		  List<Integer> collect = task.DagList.get(task.DagList.size()-1).successor.stream().map(x -> x.getIndex()).collect(Collectors.toList());
//		
//		G += task.DagList.get(task.DagList.size()-1).getIndex()+":[" + collect.stream().map( String::valueOf) .collect(Collectors.joining(", "))+"]}";
//		
//		
//
//		C += task.DagList.get(task.DagList.size()-1).getIndex()+":"+task.DagList.get(task.DagList.size()-1).getWCET(false)+"}";
//		
//		
//		
//		P += task.DagList.get(task.DagList.size()-1).getIndex()+":"+task.DagList.get(task.DagList.size()-1).getPriority()+"}";
//		
//		
//		
//		
//		StringDAG.add(G);
//		StringDAG.add(C);
//		StringDAG.add(P);
//		
//
//		
//		
//		
//		return StringDAG;
//		
//		
//		
//		
//		
//		
//		
//		
//	}

	public void findPath(ArrayList<Node> DagList) {

		DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		for (int i = 0; i < DagList.size(); i++) {
			for (int j = 0; j < DagList.size(); j++) {

				if (DagList.get(i).successor.contains(DagList.get(j))) {

					DagList.get(i).groupsuc.add(DagList.get(j));

				}

			}

		}

		for (int i = 0; i < DagList.size(); i++) {
			for (int j = 0; j < DagList.size(); j++) {

				if (DagList.get(i).predecessor.contains(DagList.get(j))) {

					DagList.get(i).grouppre.add(DagList.get(j));

				}

			}

		}

		// frontpath

		for (int i = 0; i < DagList.size(); i++) {

			DagList.get(i).grouppre.sort((p1, p2) -> Long.compare(p1.groupfront, p2.groupfront));

			Collections.reverse(DagList.get(i).grouppre);

			DagList.get(i).groupfront = DagList.get(i).getWCET(false)
					+ (DagList.get(i).grouppre.isEmpty() ? 0 : DagList.get(i).grouppre.get(0).groupfront);

		}

		// backpath

		for (int i = DagList.size() - 1; i >= 0; i--) {

			DagList.get(i).groupsuc.sort((p1, p2) -> Long.compare(p1.groupback, p2.groupback));

			Collections.reverse(DagList.get(i).groupsuc);

			DagList.get(i).groupback = DagList.get(i).getWCET(false)
					+ (DagList.get(i).groupsuc.isEmpty() ? 0 : DagList.get(i).groupsuc.get(0).groupback);

		}

		// path

		for (int i = 0; i < DagList.size(); i++) {

			DagList.get(i).grouppath = DagList.get(i).groupback + DagList.get(i).groupfront
					- DagList.get(i).getWCET(false);

		}

	}

	public long getMakespan(ArrayList<Core> list) {
		
		list.sort((p1, p2) -> Long.compare(p2.getLoad(), p1.getLoad()));

		
		return list.get(0).getLoad();
		
		
		
	}

	public boolean getPath(ArrayList<Node> list, Node theOne, int number) {

		ArrayList<Node> remove = new ArrayList<Node>();

		// for those who release at the same time or later than theOne, if
		// it cannot block, its successor cannot block

		for (Node node1 : list) {

			if (node1.predecessor.containsAll(theOne.predecessor)) {

				remove.addAll(node1.Descendant);

			}

//			if(node1.getStart(true)> theOne.getStart(false)) {
//				
//				remove.add(node1);
//			}
//			

		}

		list.removeAll(remove);

		// create a 2 d list contains a series of nodes lists
		// all the node lists contains nodes that can be parallel to each other

		ArrayList<ArrayList<Node>> twoDList = new ArrayList<ArrayList<Node>>();

		for (Node node1 : list) {

			ArrayList<Node> conList = new ArrayList<Node>();
			conList.add(node1);
			twoDList.add(conList);
		}

		for (Node node1 : list) {

			for (ArrayList<Node> thelist : twoDList) {

				if (node1.concurrent.containsAll(thelist)) {

					thelist.add(node1);

				}

			}

		}

		// sort the list in decreasing order of the size
		twoDList.sort((l1, l2) -> l2.size() - l1.size());

		// sort the sublist with priority from high to low

		twoDList.forEach(list2 -> Collections.sort(list2, Comparator.comparingInt(Node::getPriority)));

		boolean interference = false;

		// we need certain number of nodes, and at least one high-priority node to form
		// the path

		for (ArrayList<Node> thelist : twoDList) {

			if (thelist.size() > number && thelist.get(0).getPriority() <= theOne.getPriority()) {

				ArrayList<Node> newParallel = new ArrayList<Node>(thelist);

				// further remove the scenario, when remove the nodes that starts earlier
				for (Node node1 : thelist) {

					if (node1.getStart(false) < theOne.getStart(false)) {

						newParallel.remove(node1);

					}
				}
				newParallel.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

				if (newParallel.get(0).getPriority() <= theOne.getPriority()) {

					interference = true;
				}

			}

		}

		return interference;

	}

	public int numPath(ArrayList<Node> list, Node theOne, int number) {

		ArrayList<Node> DagList = new ArrayList<Node>();

		DagList.addAll(list);
		int numI = 0;

		DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		while (!DagList.isEmpty()) {

			Node node1 = DagList.get(0);
			ArrayList<Node> Remove = new ArrayList<Node>();

			Remove.add(DagList.get(0));

			while (true) {
				boolean keepgo = false;

				for (int p = 0; p < DagList.size(); p++) {

					if (node1.successor.contains(DagList.get(p))) {

						Remove.add(DagList.get(p));
						node1 = DagList.get(p);
						keepgo = true;
						break;

					}

				}

				if (keepgo == false) {
					break;

				}

			}

			DagList.removeAll(Remove);

			Remove.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

			if (Remove.get(0).getPriority() < theOne.getPriority()) {
				numI++;

			} else {

				if (number > 0) {
					numI++;

					number--;

				}

			}

		}

		return numI;

	}

	public void printDAG(DAG task) {

//��ӡ       

		System.out.println("\n the priority of the DAG is: \n" + task.getPriority());

		System.out.println("\n node_list:\n");
		System.out.println(task.node_list);
		System.out.println("dag_node_group:\n");

		for (int i = 0; i < task.dag_node_group.size(); i++) {

			System.out.print("{");

			for (int j = 0; j < task.dag_node_group.get(i).size(); j++) {

				System.out.print(task.dag_node_group.get(i).get(j) + ",");

			}
			System.out.print("},");

		}

		System.out.println("\n���and��");

		out_put_Matrix(task.matrix_result);

		System.out.print("critical path" + "\n");

		System.out.print("{");

		for (Node a : task.Cpath) {

			System.out.print(a.getIndex() + ",");

		}

		System.out.print("}\n");

		System.out.print("WCET list \n");

		task.DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		System.out.print("{");
		for (int i = 0; i < task.DagList.size(); i++) {

			System.out.print(task.DagList.get(i).getWCET(false) + ", ");
		}

		System.out.print("}\n");

		System.out.print("priority list \n");

		task.DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		System.out.print("{");
		for (int i = 0; i < task.DagList.size(); i++) {

			System.out.print(task.DagList.get(i).getPriority() + ", ");
		}

		System.out.print("}\n");

//     

	}

	public void out_put_Matrix(double[][] M) {
//		int m_size = M.length;
		for (int i = 0; i < M.length; i++) {
			System.out.print("{");
			for (int j = 0; j < M[i].length; j++) {
				System.out.print((int) M[i][j] + ",");
			}
			System.out.println("},");
		}
	}

	public int ZhaoPath(ArrayList<Node> list) {

		ArrayList<Node> DagList = new ArrayList<Node>();

		DagList.addAll(list);
		int numI = 0;

		DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		while (!DagList.isEmpty()) {

			Node node1 = DagList.get(0);
			ArrayList<Node> Remove = new ArrayList<Node>();

			Remove.add(DagList.get(0));

			while (true) {
				boolean keepgo = false;

				for (int p = 0; p < DagList.size(); p++) {

					if (node1.successor.contains(DagList.get(p))) {

						Remove.add(DagList.get(p));
						node1 = DagList.get(p);
						keepgo = true;
						break;

					}

				}

				if (keepgo == false) {
					break;

				}

			}
			numI++;

			DagList.removeAll(Remove);

		}

		return numI;

	}

	public void PrintPriority(ArrayList<Node> DagList) {

		DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		System.out.print("\n");

		for (Node A : DagList) {

			System.out.print(A.getPriority() + ",");
		}

	}

	public Node NextNode(ArrayList<Node> DagList) {

		ArrayList<Node> readytask = new ArrayList<Node>();

		for (Node A : DagList) {

			if (A.getState() == true) {

				readytask.add(A);

			}
		}

		if (readytask.isEmpty()) {

			for (Node A : DagList) {

				System.out.print("\n" + A.getIndex());

			}
		}

		readytask.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

		return readytask.get(0);

	}

	public double getMedian(ArrayList<Long> sets) {

		Collections.sort(sets);

		double middle;

		if (sets.size() % 2 == 0) {
			middle = (sets.get(sets.size() / 2) + sets.get(sets.size() / 2 - 1)) / (double) 2;
		} else {
			middle = sets.get(sets.size() / 2);
		}

		return middle;

	}

	public boolean isSchedulable(ArrayList<DAG> tasks) {

		for (DAG task : tasks) {

			if (task.getR() > task.deadline) {
				return false;
			}

		}

		return true;

	}

	public long getInterference(long point, ArrayList<Node> Interference) {

		long interference = 0;

		for (Node inter : Interference) {

			interference += inter.getTempf() == 0 ? inter.getWCET(false)
					: Math.min(inter.getTempf() - point, inter.getWCET(false));

		}

		return interference;

	}

	public long sumWCET(ArrayList<Node> Interference) {

		long interference = 0;

		for (Node inter : Interference) {

			interference += inter.getWCET(false);

		}

		return interference;

	}

	public long getIII(long point, ArrayList<Node> Interference) {

		long interference = 0;

		for (Node inter : Interference) {

			if (inter.getTempf() == -1) {

				interference += inter.getWCET(false);

			} else {

				interference += Math.min(inter.getTempf() - point, inter.getWCET(false));
			}

		}

		return interference;

	}

	public long getLowI(ArrayList<Node> lowlist, int m) {

		for (int i = 0; i < lowlist.size(); i++) {

			for (int j = 0; j < lowlist.size(); j++) {

				if (!lowlist.get(i).Ancestor.contains(lowlist.get(j))
						&& !lowlist.get(i).Descendant.contains(lowlist.get(j))
						&& !lowlist.get(i).equals(lowlist.get(j))) {

					lowlist.get(i).lowcon.add(lowlist.get(j));

				}

			}

		}

		lowlist.sort((p1, p2) -> Long.compare(p1.getWCET(false), p2.getWCET(false)));

		Collections.reverse(lowlist);

		HashSet<Node> com = new HashSet<Node>();

		for (Node a : lowlist) {

			com = new HashSet<Node>();
			com.add(a);

			for (Node b : lowlist) {

				if (b.lowcon.containsAll(com)) {

					com.add(b);

				}
				if (com.size() == m) {
					break;

				}

			}

			if (com.size() == m) {
				break;

			}

		}

		return sum(com, m);

	}

	public long sum(HashSet<Node> list, int m) {

		ArrayList<Node> al = new ArrayList<>(list);

		int sum = 0;

		for (Node a : list) {

			sum += a.getWCET(false);

		}

		return sum;
	}

}
