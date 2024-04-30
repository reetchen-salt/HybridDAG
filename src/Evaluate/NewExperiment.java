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
import entity.Core;
import entity.DAG;

public class NewExperiment{

	public static int MAX_PERIOD = 3000;
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

		int[] Core = { 3, 4, 6, 7, 8, 9 };
		int[] Par = { 4, 5, 6, 7, 8, 9, 10 };
		int[] Cri = { 4, 5, 6, 7, 8, 9, 10 };
		double[] ratio = { 0.2 };
		int times = 1000;

		NewExperiment ep = new NewExperiment();

		// Thread to vary Core, keeping Par and Cri constant
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		        for (int core : Core) {
		            ep.PriorityOrder(core, Par[1], Cri[1], times, "core", ratio[0]);
		        }
		        System.out.println("Core simulation completed.");
		    }
		}).start();

		// Thread to vary Par, keeping Core and Cri constant
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		        for (int par : Par) {
		            ep.PriorityOrder(Core[1], par, Cri[1], times, "parallelism", ratio[0]);
		        }
		        System.out.println("Par simulation completed.");
		    }
		}).start();

		// Thread to vary Cri, keeping Core and Par constant
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		        for (int cri : Cri) {
		            ep.PriorityOrder(Core[1], Par[1], cri, times, "length", ratio[0]);
		        }
		        System.out.println("Cri simulation completed.");
		    }
		}).start();
//


	}

//-------------------start the system, return RTA of MSRP and MrsP
	public void PriorityOrder(int core, int parallelism, int critical_path, int TOTAL_NUMBER_OF_SYSTEMS, String name,
			double ratio) {

		// -----
		SimpleSystemGenerator generator = new SimpleSystemGenerator(MIN_PERIOD, MAX_PERIOD, 1, true, SEED, parallelism,
				critical_path, ratio);
		
		
		long WCRT=0;
		
		double advantage=0;
		
		double meanAdvantage = 0;

		long makespan1 = 0;

		long makespan2 = 0;

		int anomalyYes = 0;

		int anomalyNo = 0;

		int unlabelled = 0;

		for (int i = 0; i < TOTAL_NUMBER_OF_SYSTEMS; i++) {

			ArrayList<DAG> tasks = generator.generateTasks();

			tasks.get(0).setPriority(0);

			new PriorityGenerator().MyAssignment(tasks.get(0));

			
			WCRT = new Nonpreemptive().WCmakespan(tasks.get(0), core);
//
//			makespan1 = new Makespan().getMakespan(tasks.get(0).DagList, core,true);

			makespan1 = new AnalysisUtil().getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, core, "max"));

//			
			if (new AnomalyAnalysis().AnalyzeAnomaly(tasks.get(0).DagList, core)) {

				boolean detected = false;

				for (int j = 0; j < 10000; j++) {

					ArrayList<Core> corelist = new Makespan().getMakespan(tasks.get(0).DagList, core, "random");

					makespan2 = new AnalysisUtil().getMakespan(corelist);

					if (makespan2 > makespan1) {

						detected = true;

					}

				}

				if (detected == false) {

					unlabelled++;
				} else {
					anomalyYes++;

				}

			} else {
				
				advantage += (WCRT - makespan1)/(double)makespan1 ;
				
//				System.out.print("\n we have WCRT: "+ WCRT + " we have simulated makepsan: " + makespan1);
//				
//				System.out.print(" \n the advantage is " + advantage);
				anomalyNo++;

			}
			
			
			
			meanAdvantage = Math.round(advantage*10000/(double)anomalyNo)/100;
			
//			System.out.print("\n we have WCRT: "+ WCRT + " we have simulated makepsan: " + makespan1);

		}

		System.out.print("\n For Core: " + core + ", Parallelism: " + parallelism + ", Length: " + critical_path);


		System.out.print(
				"\n We have Anomaly: " + anomalyYes + ", Anomaly-free: " + anomalyNo + ", unlabelled: " + unlabelled);
		
		System.out.print(
				"\n We have meanAdvantage: " + meanAdvantage);
	}


}
//--------------------------------------------------------------------



