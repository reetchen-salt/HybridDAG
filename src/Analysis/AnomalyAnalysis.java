package Analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import Util.AnalysisUtil;
import entity.DAG;
import entity.Node;

public class AnomalyAnalysis {

	// 输入个DAG 和 核心数m
	public long WCmakespan(DAG t, int m) {

		for (Node A : t.DagList) {

			A.selfclean();

		}

		// 遍历DAG节点收集每个节点的 I_remove
	
		for (Node A : t.DagList) {

			for (Node B : t.DagList) {
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

		t.DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		// 开始计算每一个节点的最差完成时间
		for (Node theNode : t.DagList) {
			// 首先加入自己得WCET
			long WCfinish = theNode.getWCET();

			ArrayList<Node> realcon = new ArrayList<Node>();
			// 其次在并行节点concurrent 中去除 I_remove
			for (Node con : theNode.concurrent) {

				if (!theNode.remove.contains(con)) {

					realcon.add(con);

				}

			}
			// 判断剩下的节点最多能够堵塞多少路径， 是不是大于M-1个以足够形成干扰
			if (new AnalysisUtil().numPath(realcon, theNode, (m - 1)) > m - 1) {

				// 进入判断说明interference存在

				long S = 0;

				// 遍历前驱节点，这里找哪个前驱能造成最晚的开始时间S

				for (Node pre : theNode.predecessor) {

					ArrayList<Node> potential = new ArrayList<Node>();

					potential.addAll(realcon);
					// 除去前驱节点的干扰，形成I_Potential
					potential.removeAll(pre.Interference);

					ArrayList<Node> Interference = new ArrayList<Node>();

					// 将所有高优先级的节点先加入interference集合，因为高优先级节点都会造成干扰
					for (Node con : potential) {

						if (con.getPriority() < theNode.getPriority()) {

							Interference.add(con);

						}

					}
					// 除去高优先节点， 剩下都是低优先级，再来取m-1个最大的低优先级
					potential.removeAll(Interference);

					// 剩下的节点按WCET排序
					potential = (ArrayList<Node>) potential.stream().sorted(Comparator.comparing(Node::getWCET)
							.reversed().thenComparing(Comparator.comparing(Node::getIndex)))
							.collect(Collectors.toList());

					int num = Math.min(m - 1, potential.size());

					ArrayList<Node> lowlist = new ArrayList<Node>();

					// 把前m-1个最大的节点存在lowlist中
					for (int j = 0; j < num; j++) {

						lowlist.add(potential.get(j));

					}
					// 除去lowlist
					potential.removeAll(lowlist);

					// 剩下的节点有可能存在lowlist里面节点的祖先节点，为了符合执行顺序，和保证安全性，我们把他们也算如interference
					// 存入lowpre
					ArrayList<Node> lowPre = new ArrayList<Node>();

					for (Node a : lowlist) {

						for (Node b : potential) {

							if (a.Ancestor.contains(b)) {

								lowPre.add(b);

							}

						}

					}

					// interference 存入前m-1个最大的低优先级
					Interference.addAll(lowlist);
					// interference 存入lowlist里面在potential里的祖先节点
					Interference.addAll(lowPre);

					// 计算最差开始时间=前驱的最差完成时间+interference/m
					long newS = pre.getTempf()
							+ (long) (Math.ceil(new AnalysisUtil().sumWCET(Interference) / (double) (m)));

					// 不断替换，找出最晚的开始时间
					if (newS > S) {

						S = newS;

						theNode.Interference.clear();
						theNode.Interference.addAll(Interference);
						theNode.Interference.addAll(pre.Interference);

					}

				}

				WCfinish += S;

				theNode.setTempf(WCfinish);

				// 如果并行节点并不足够多到形成干扰，则没有interference，完成时间就是最大前驱完成时间，加上自身的WCET
			} else {

				WCfinish += theNode.getLargestP();

				if (theNode.getP() != null) {

					theNode.Interference.clear();
					theNode.Interference.addAll(theNode.getP().Interference);

				}

				theNode.setTempf(WCfinish);

			}

		}

		// 排序并且返回最后完成的节点

		t.DagList.sort((p1, p2) -> Long.compare(p1.getTempf(), p2.getTempf()));

		Collections.reverse(t.DagList);

		return t.DagList.get(0).getTempf();

	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
