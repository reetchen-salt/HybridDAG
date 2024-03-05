package Analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import Util.AnalysisUtil;
import entity.DAG;
import entity.Node;

public class Nonpreemptive {

	// �����DAG �� ������m
	public long WCmakespan(DAG t, int m) {

		for (Node A : t.DagList) {

			A.selfclean();

		}

		// ����DAG�ڵ��ռ�ÿ���ڵ�� I_remove
	
		for (Node A : t.DagList) {

			for (Node B : t.DagList) {
				// ���A�ڵ������B�ڵ��ȫ��ǰ��
				if (B.predecessor.containsAll(A.predecessor)) {
					// ����ԽС���ȼ�Խ�����B�����ȼ���A��
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

		// ��ʼ����ÿһ���ڵ��������ʱ��
		for (Node theNode : t.DagList) {
			// ���ȼ����Լ���WCET
			long WCfinish = theNode.getWCET();

			ArrayList<Node> realcon = new ArrayList<Node>();
			// ����ڲ��нڵ�concurrent ��ȥ�� I_remove
			for (Node con : theNode.concurrent) {

				if (!theNode.remove.contains(con)) {

					realcon.add(con);

				}

			}
			// �ж�ʣ�µĽڵ�����ܹ���������·���� �ǲ��Ǵ���M-1�����㹻�γɸ���
			if (new AnalysisUtil().numPath(realcon, theNode, (m - 1)) > m - 1) {

				// �����ж�˵��interference����

				long S = 0;

				// ����ǰ���ڵ㣬�������ĸ�ǰ�����������Ŀ�ʼʱ��S

				for (Node pre : theNode.predecessor) {

					ArrayList<Node> potential = new ArrayList<Node>();

					potential.addAll(realcon);
					// ��ȥǰ���ڵ�ĸ��ţ��γ�I_Potential
					potential.removeAll(pre.Interference);

					ArrayList<Node> Interference = new ArrayList<Node>();

					// �����и����ȼ��Ľڵ��ȼ���interference���ϣ���Ϊ�����ȼ��ڵ㶼����ɸ���
					for (Node con : potential) {

						if (con.getPriority() < theNode.getPriority()) {

							Interference.add(con);

						}

					}
					// ��ȥ�����Ƚڵ㣬 ʣ�¶��ǵ����ȼ�������ȡm-1�����ĵ����ȼ�
					potential.removeAll(Interference);

					// ʣ�µĽڵ㰴WCET����
					potential = (ArrayList<Node>) potential.stream().sorted(Comparator.comparing(Node::getWCET)
							.reversed().thenComparing(Comparator.comparing(Node::getIndex)))
							.collect(Collectors.toList());

					int num = Math.min(m - 1, potential.size());

					ArrayList<Node> lowlist = new ArrayList<Node>();

					// ��ǰm-1�����Ľڵ����lowlist��
					for (int j = 0; j < num; j++) {

						lowlist.add(potential.get(j));

					}
					// ��ȥlowlist
					potential.removeAll(lowlist);

					// ʣ�µĽڵ��п��ܴ���lowlist����ڵ�����Ƚڵ㣬Ϊ�˷���ִ��˳�򣬺ͱ�֤��ȫ�ԣ����ǰ�����Ҳ����interference
					// ����lowpre
					ArrayList<Node> lowPre = new ArrayList<Node>();

					for (Node a : lowlist) {

						for (Node b : potential) {

							if (a.Ancestor.contains(b)) {

								lowPre.add(b);

							}

						}

					}

					// interference ����ǰm-1�����ĵ����ȼ�
					Interference.addAll(lowlist);
					// interference ����lowlist������potential������Ƚڵ�
					Interference.addAll(lowPre);

					// ������ʼʱ��=ǰ����������ʱ��+interference/m
					long newS = pre.getTempf()
							+ (long) (Math.ceil(new AnalysisUtil().sumWCET(Interference) / (double) (m)));

					// �����滻���ҳ�����Ŀ�ʼʱ��
					if (newS > S) {

						S = newS;

						theNode.Interference.clear();
						theNode.Interference.addAll(Interference);
						theNode.Interference.addAll(pre.Interference);

					}

				}

				WCfinish += S;

				theNode.setTempf(WCfinish);

				// ������нڵ㲢���㹻�ൽ�γɸ��ţ���û��interference�����ʱ��������ǰ�����ʱ�䣬���������WCET
			} else {

				WCfinish += theNode.getLargestP();

				if (theNode.getP() != null) {

					theNode.Interference.clear();
					theNode.Interference.addAll(theNode.getP().Interference);

				}

				theNode.setTempf(WCfinish);

			}

		}

		// �����ҷ��������ɵĽڵ�

		t.DagList.sort((p1, p2) -> Long.compare(p1.getTempf(), p2.getTempf()));

		Collections.reverse(t.DagList);

		return t.DagList.get(0).getTempf();

	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
