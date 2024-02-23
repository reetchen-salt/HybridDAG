package Analysis;

import java.util.ArrayList;

import Util.AnalysisUtil;

import entity.Node;

public class AnomalyAnalysis {

	// 输入个DAG 和 核心数m
	public void AnalyzeAnomaly(ArrayList<Node> DagList, int m) {

		boolean anomaly = false;

//		for (Node A : t.DagList) {
//
//			A.selfclean();
//
//		}

		// 遍历DAG节点收集每个节点的 I_remove

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

		// 开始计算每一个节点的最差完成时间
		for (Node theNode : DagList) {
//			// 首先加入自己得WCET
//			long WCfinish = theNode.getWCET();
//
			ArrayList<Node> realcon = new ArrayList<Node>();
			// 其次在并行节点concurrent 中去除 I_remove
			for (Node con : theNode.concurrent) {

				if (!theNode.remove.contains(con)) {

					realcon.add(con);

				}

			}

			ArrayList<Node> interference = new ArrayList<Node>();
			for (Node DelayNode : realcon) {

				if (DelayNode.getTempf() <= theNode.getStart()) {

					interference.add(DelayNode);

				}

			}
			realcon.removeAll(interference);
			

			// 判断剩下的节点最多能够堵塞多少路径， 是不是大于M-1个以足够形成干扰
			if (new AnalysisUtil().numPath(realcon, theNode, (m - 1)) > m - 1) {

				anomaly = true;

				System.out.print("Anomaly of V" + theNode.getIndex() + " is: ");

				for (Node AnomalyNode : realcon) {

					System.out.print("V" + AnomalyNode.getIndex() + ", ");

				}

				System.out.print("\n");
			}

		}

		if (anomaly == false) {

			System.out.print("no anomaly\n");

		}

	}

}
