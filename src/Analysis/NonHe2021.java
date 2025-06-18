package Analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import Util.AnalysisUtil;
import entity.DAG;
import entity.Node;

public class NonHe2021 {

	public long WCmakespan(DAG t, int m) {

		for (Node A : t.DagList) {

			A.selfclean();

		}

		

		t.DagList.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

		// ��������
		for (Node theNode : t.DagList) {

			long WCfinish = theNode.getWCET("max");

			long S = 0;

			for (Node pre : theNode.predecessor) {

				ArrayList<Node> potential = new ArrayList<Node>();

				potential.addAll(theNode.concurrent);
				potential.removeAll(pre.Interference);

				ArrayList<Node> Interference = new ArrayList<Node>();

				for (Node con : potential) {

					if (con.getPriority() < theNode.getPriority()) {

						Interference.add(con);

					}

				}

				potential.removeAll(Interference);

				potential = (ArrayList<Node>) potential.stream()
					    .sorted(Comparator.comparing((Node n) -> n.getWCET("max")).reversed()
					    .thenComparing(Comparator.comparing(Node::getIndex)))
					    .collect(Collectors.toList());


				int num = Math.min(m - 1, potential.size());

				ArrayList<Node> lowlist = new ArrayList<Node>();

				for (int j = 0; j < num; j++) {

					lowlist.add(potential.get(j));

				}

				potential.removeAll(lowlist);

				ArrayList<Node> lowPre = new ArrayList<Node>();

				for (Node a : lowlist) {

					for (Node b : potential) {

						if (a.Ancestor.contains(b)) {

							lowPre.add(b);

						}

					}

				}

				Interference.addAll(lowlist);
				Interference.addAll(lowPre);

				long newS = pre.getTempf()
						+ (long) (Math.ceil(new AnalysisUtil().sumWCET(Interference) / (double) (m)));

				if (newS > S) {

					S = newS;

					theNode.Interference.clear();
					theNode.Interference.addAll(Interference);
					theNode.Interference.addAll(pre.Interference);

				}

			}

			WCfinish += S;

			theNode.setTempf(WCfinish);

		}

		t.DagList.sort((p1, p2) -> Long.compare(p1.getTempf(), p2.getTempf()));

		Collections.reverse(t.DagList);

		return t.DagList.get(0).getTempf();

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
