package Analysis;

import java.util.ArrayList;

import Util.AnalysisUtil;

import entity.Node;

public class AnomalyAnalysis {

	// 输入个DAG 和 核心数m
	public boolean AnalyzeAnomaly(ArrayList<Node> DagList, int m) {

		boolean anomaly = false;


		getRemove(DagList);

		for (Node theNode : DagList) {

			ArrayList<Node> realcon =  getRealcon(theNode);
			


			// 判断剩下的节点最多能够堵塞多少路径， 是不是大于M-1个以足够形成干扰
			if (new AnalysisUtil().getPath(realcon, theNode, (m - 1))) {
				
// at this stage we still cannot guarantee the anomaly
//					
				anomaly= true;
				
//				System.out.print("Anomaly of V" + theNode.getIndex() + " is: ");
//	
//				for (Node AnomalyNode : realcon) {
//	
//					System.out.print("V" + AnomalyNode.getIndex() + ", ");
//	
//				}
//	
//				System.out.print("\n");
//				

//				theNode.anomaly = true;

			}
			
			
	
			

		}
		
		
// further anomaly analysis this time we include further analysis of 		
		
//		for (Node theNode : DagList) {
//
//			ArrayList<Node> realcon = getRealcon(theNode);
//
//			ArrayList<Node> fakeAnomaly = new ArrayList<Node>();
//		
//
//			for (Node conNode : realcon) {
////
////				if (conNode.getStart(true) >= theNode.getStart(false)) {
//				if (conNode.anomaly == false && conNode.getStart(true) >= theNode.getStart(false)) {
//					fakeAnomaly.add(conNode);
//
//				}
//
//			}
//
//			realcon.remove(fakeAnomaly);
//
//			if (new AnalysisUtil().getPath(realcon, theNode, (m - 1))) {
//
//				anomaly = true;
//
////				System.out.print("Anomaly of V" + theNode.getIndex() + " is: ");
////
////				for (Node AnomalyNode : realcon) {
////
////					System.out.print("V" + AnomalyNode.getIndex() + ", ");
////
////				}
////
////				System.out.print("\n");
//
//			}
//
//		}

		if (anomaly == false) {

			System.out.print("no anomaly\n");

		}

		
		return anomaly;
	}
	
	
	
	public void getRemove(ArrayList<Node> DagList) {

		for (Node A : DagList) {

			for (Node B : DagList) {
				// 如果A节点包括了B节点的全部前驱
				if (B.predecessor.containsAll(A.predecessor)) {
					// 数字越小优先级越大，如果B的优先级比A低
					if (B.getPriority() > A.getPriority()) {

						A.remove.add(B);
						A.remove.addAll(B.Descendant);

					}

					if (B.getPriority() < A.getPriority()) {

						for (Node C : B.Descendant) {

							if (C.getPriority() > A.getPriority()) {

								A.remove.add(C);

								A.remove.addAll(C.Descendant);

							}
						}
					}
				}
			}
		}

		DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

	}

	public ArrayList<Node> getRealcon(Node theNode) {

		ArrayList<Node> realcon = new ArrayList<Node>();
		// 其次在并行节点concurrent 中去除 I_remove
		for (Node con : theNode.concurrent) {

			if (!theNode.remove.contains(con)) {

				realcon.add(con);

			}

		}

		ArrayList<Node> interference = new ArrayList<Node>();
		for (Node DelayNode : realcon) {

			if (DelayNode.getNewf("max") <= theNode.getStart("max")) {

				interference.add(DelayNode);

			}

		}
		realcon.removeAll(interference);

		return realcon;

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	


}
