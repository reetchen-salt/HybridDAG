package Evaluate;



import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import entity.Node;

import GenerationTool.PriorityGenerator;
import GenerationTool.SimpleSystemGenerator;
import Util.AnalysisUtil;
import Analysis.Nonpreemptive;
import Analysis.AnomalyAnalysis;
import Analysis.Makespan;
import entity.DAG;

public class SingleDAG {

	public static int MAX_PERIOD = 2000;
	public static int MIN_PERIOD = 1000;

	public static int SEED = 1000;
	static PrintWriter writer = null;
	static PrintWriter writer2 = null;
	int count = 0;

	public synchronized void countDown(CountDownLatch cd) {
		cd.countDown();
		count++;
	}

	public static void main(String args[]) throws Exception {

		int[] Core = { 3 };

		int[] Par = { 5 };

		int[] Cri = { 5 };

		double[] ratio = { 0.2 };

		int times = 1;

		SingleDAG ep = new SingleDAG();

		CountDownLatch ad = new CountDownLatch(Core.length);
		CountDownLatch bd = new CountDownLatch(Par.length);
		CountDownLatch cd = new CountDownLatch(Cri.length);

		for (int p = 0; p < Core.length; p++) {

			final int nop = p;

			new Thread(new Runnable() {
				@Override
				public void run() {
					ep.PriorityOrder(Core[nop], Par[0], Cri[0], times, "NoCore", ratio[0]);

					ep.countDown(ad);
				}

			}).start();

		}

		ad.await();

	

	}

//-------------------start the system, return RTA of MSRP and MrsP
	public void PriorityOrder(int core, int parallelism, int critical_path, int TOTAL_NUMBER_OF_SYSTEMS, String name,
			double ratio) {

		// -----
		SimpleSystemGenerator generator = new SimpleSystemGenerator(MIN_PERIOD, MAX_PERIOD, 1, true, SEED, parallelism,
				critical_path, ratio);

		long makespan1 = 0;



		int anomalyDetected =0;
		

		for (int i = 0; i < TOTAL_NUMBER_OF_SYSTEMS; i++) {

			ArrayList<DAG> tasks = generator.generateTasks();

			tasks.get(0).setPriority(0);



			new PriorityGenerator().MyAssignment(tasks.get(0));
	

			makespan1 = new Makespan().getMakespan(tasks.get(0).DagList, core);
			

		    new Util.DrawDag(tasks.get(0), tasks.get(0).DagList);
			


		    if(new AnomalyAnalysis().AnalyzeAnomaly(tasks.get(0).DagList, core)) {
		    	
		    	anomalyDetected++;
		    	
		    }
			
	
			

		}
		
		

		

		

	}


}
//--------------------------------------------------------------------



