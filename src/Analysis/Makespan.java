package Analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import Util.AnalysisUtil;
import entity.Core;
import entity.Node;

public class Makespan {

	long makespan = 0;

	public ArrayList<Core> getMakespan(ArrayList<Node> tdag, int Ncore, boolean isEarly) {
		


		for (Node A : tdag) {

			A.selfclean();
			if(isEarly==true) {
				A.setNewWCET();}

		}

		// �������нڵ�
		ArrayList<Node> tempdag = new ArrayList<Node>();

		tempdag.addAll(tdag);

		// �������
		ArrayList<Core> CoreList = new ArrayList<Core>();

		for (int i = 0; i < Ncore; i++) {

			CoreList.add(new Core(i));

		}

		while (!tempdag.isEmpty()) {

			boolean allocated = false;

			//non-decreasing order
			CoreList.sort((p1, p2) -> Long.compare(p1.getLoad(), p2.getLoad()));

			ArrayList<Node> readytask = new ArrayList<Node>();
			
			for (int i = 0; i < tempdag.size(); i++) {
				
				if (tempdag.get(i).getState() == true) {

					readytask.add(tempdag.get(i));

				}

			}
			
	// debug		
//			if(readytask.size() == 0) {
//				
//				System.out.print("\n the size of whole DAG is" + tdag.size());
//			
//				System.out.print("\nthe size of DAG is" + tempdag.size());
//				
//				System.err.print("\something wrong");
//				
//				for (int i = 0; i < tempdag.size(); i++) {
//					
//					if (tempdag.get(i).getState() == false) {
//
//						if (tempdag.get(i).predecessor.isEmpty()) {
//
//							System.out.print("\n the predecessor empty" + tempdag.get(i).getIndex());
//
//						} else {
//							
//							
//							for(Node thenode:tempdag.get(i).predecessor) {
//								
//								
//								System.out.print("\n the predecessor of  " +tempdag.get(i).getIndex()+ "is : " + thenode.getIndex());
//
//								
//							}
//							
//							
//
//							tempdag.get(i).predecessor.sort((p1, p2) -> Long.compare(p1.getTempf(), p2.getTempf()));
//
//							System.out.print("\n the predecessor tempf is " + tempdag.get(i).predecessor.get(0).getTempf());
//
//						}
//
//					}
//
//				}
//				
//
//				
//			}
			readytask.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

			for (int i = 0; i < readytask.size(); i++) {

				if (CoreList.get(0).getLoad() >= readytask.get(i).getLargestP()) {

					CoreList.get(0).Nodes.add(readytask.get(i));
					readytask.get(i).setTempf(CoreList.get(0).getLoad() + readytask.get(i).getWCET(isEarly));

					CoreList.get(0).setLoad(readytask.get(i).getTempf());

					tempdag.remove(readytask.get(i));

					allocated = true;
					break;

				}

			}

			if (allocated == false) {

				readytask.sort((p1, p2) -> Long.compare(p1.getLargestP(), p2.getLargestP()));

				CoreList.get(0).Nodes.add(readytask.get(0));

				readytask.get(0).setTempf(readytask.get(0).getLargestP() + readytask.get(0).getWCET(isEarly));

				CoreList.get(0).setLoad(readytask.get(0).getTempf());

				tempdag.remove(readytask.get(0));

			}

		}

		CoreList.sort((p1, p2) -> Long.compare(p1.getLoad(), p2.getLoad()));

		makespan = CoreList.get(CoreList.size() - 1).getLoad();

		
		
		storeData(isEarly,tdag);
		
//		new Util.PrintGantt(CoreList, makespan, isEarly);

		return CoreList;

	}
	
	private void storeData(boolean isEarly, ArrayList<Node> dagList) {

		if (isEarly) {

			for (Node theNode : dagList) {

				theNode.setEarlyT(theNode.getTempf());

			}

		} else {

			for (Node theNode : dagList) {

				theNode.setLateT(theNode.getTempf());

			}

		}

	}
	

	
	
	
	
	
	
	
	
	
	
	
	

}
