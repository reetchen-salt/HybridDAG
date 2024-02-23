package Analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import Util.AnalysisUtil;
import entity.Core;
import entity.Node;

public class Makespan {

	long makespan = 0;

	public long getMakespan(ArrayList<Node> tdag, int Ncore) {

//		for (Node A : tdag) {
//
//			A.selfclean();
//
//		}

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

			readytask.sort((p1, p2) -> Integer.compare(p1.getPriority(), p2.getPriority()));

			for (int i = 0; i < readytask.size(); i++) {

				if (CoreList.get(0).getLoad() >= readytask.get(i).getLargestP()) {

					CoreList.get(0).Nodes.add(readytask.get(i));
					readytask.get(i).setTempf(CoreList.get(0).getLoad() + readytask.get(i).getWCET());

					CoreList.get(0).setLoad(readytask.get(i).getTempf());

					tempdag.remove(readytask.get(i));

					allocated = true;
					break;

				}

			}

			if (allocated == false) {

				readytask.sort((p1, p2) -> Long.compare(p1.getLargestP(), p2.getLargestP()));

				CoreList.get(0).Nodes.add(readytask.get(0));

				readytask.get(0).setTempf(readytask.get(0).getLargestP() + readytask.get(0).getWCET());

				CoreList.get(0).setLoad(readytask.get(0).getTempf());

				tempdag.remove(readytask.get(0));

			}

		}

		CoreList.sort((p1, p2) -> Long.compare(p1.getLoad(), p2.getLoad()));

		makespan = CoreList.get(CoreList.size() - 1).getLoad();

		
		
		new Util.PrintGantt(CoreList, makespan);

		return makespan;

	}

}
