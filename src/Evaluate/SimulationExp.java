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

public class SimulationExp {

	public static int MAX_PERIOD = 3000;
	public static int MIN_PERIOD = 1000;

	public static int SEED = 1000;
	static PrintWriter writer = null;
	static PrintWriter writer2 = null;
	int count = 0;
	static String filePath = "your_data.csv";

	public synchronized void countDown(CountDownLatch cd) {
		cd.countDown();
		count++;
	}

	public static void main(String args[]) throws Exception {
		
		
	
		
		
        try (FileWriter fileWriter = new FileWriter(filePath);
	             PrintWriter printWriter = new PrintWriter(fileWriter)) {
	            
	            // Writing the header row
	            printWriter.println("AvgCfactor,WorkloadPercentage, CurrentWorkload,MinWorkload,MaxWorkload,TempMakespan,WCmakespan,NumNodes,AvgInDegree,AvgOutDegree,CoreNum, isLonger");
	            


	        } catch (IOException e) {
	            System.err.println("An error occurred while writing the file.");
	            e.printStackTrace();
	        }
			

		int[] Core = { 3,4,6,7,8,9 };

		int[] Par = { 4,5,6,7,8,9,10 };

		int[] Cri = { 4,5,6,7,8,9,10 };

		double[] ratio = { 0.2 };

		int times = 1000;

		SimulationExp ep = new SimulationExp();

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

		for (int t = 0; t < Par.length; t++) {
			
			final int not=t;

			new Thread(new Runnable() {
				@Override
				public void run() {
				
					ep.PriorityOrder(Core[0], Par[not], Cri[0], times,"NoPar", ratio[0]);
					
					ep.countDown(bd);
				}
			}).start();
			
		}
		bd.await();
		

		for (int a = 0; a < Cri.length; a++) {
			
			final int noa=a;

			new Thread(new Runnable() {
				@Override
				public void run() {
					ep.PriorityOrder(Core[0], Par[0], Cri[noa], times,"NoCri", ratio[0]);
					
					ep.countDown(cd);

				}
			}).start();
			
		}
		cd.await();
//		

	}

//-------------------start the system, return RTA of MSRP and MrsP
	public void PriorityOrder(int core, int parallelism, int critical_path, int TOTAL_NUMBER_OF_DAGs, String name,
			double ratio) {

		// -----
		SimpleSystemGenerator generator = new SimpleSystemGenerator(MIN_PERIOD, MAX_PERIOD, 1, true, SEED, parallelism,
				critical_path, ratio);

		long MaxMakespan = 0;
		long MinMakespan = 0;
		long MinWorkload = 0;
		long MaxWordload = 0;
		long MaxWCET = 0;
		long MinWCET = 0;
		double MedianWCET =0;
		int NumNodes = 0;
		int MaxParallel = 0;
		int CriticalPath = 0;
		int AvgInDegree = 0;
		int AvgOutDegree = 0;
		int CoreNum = 0;
		int IsAnomaly =0;
		
//		
//		
//		double tighter =0;
//		
//		double tighterP= 0;

		
		for (int i = 0; i < TOTAL_NUMBER_OF_DAGs; i++) {

			ArrayList<DAG> tasks = generator.generateTasks();

			tasks.get(0).setPriority(0);

			new PriorityGenerator().MyAssignment(tasks.get(0));
			
			
	
			
//			makespan2 = new Analysis.Nonpreemptive().WCmakespan(tasks.get(0), core);
			
			
			
			MaxMakespan = new AnalysisUtil().getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, core,false));
			
			MinMakespan = new AnalysisUtil().getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, core,true));
	
			MinWorkload = new AnalysisUtil().getWorkload(tasks.get(0).DagList, true);
			
			MaxWordload = new AnalysisUtil().getWorkload(tasks.get(0).DagList, false);
			
			tasks.get(0).DagList.sort((p1, p2) -> Long.compare(p1.getWCET(false), p2.getWCET(false)));
			
			MinWCET = tasks.get(0).DagList.get(0).getWCET(false);
			
			
			MaxWCET = tasks.get(0).DagList.get(tasks.get(0).DagList.size()-1).getWCET(false);
			
			MedianWCET =  new AnalysisUtil().findMedianWCET(tasks.get(0).DagList) ;
			 
			NumNodes = tasks.get(0).DagList.size();
			
			
			
			
			
			
			
//			System.out.print("the bound is" + makespan2);
//	
//			System.out.print("the simulation is" + makespan1);
//		    new Util.DrawDag(tasks.get(0), tasks.get(0).DagList);
//			


		    if(new AnomalyAnalysis().AnalyzeAnomaly(tasks.get(0).DagList, core)) {
		    	
		    	for(int j = 0; j < 10000; j++) {
		    	
		    		makespan2 =  new AnalysisUtil().getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, core,false));
				
					if(makespan1 < makespan2) {
					
					
						tighterP += (makespan2-makespan1)/(double)makespan2;
						
						tighter++;
					
					
					
					}
				
		    	}
		    	
		    }

			
		
			if(makespan2 < makespan1) {
				
				System.err.print("the bound is smaller than simulation");
				
			}
			
			
	
			

		}
		
		tighterP = (tighterP/(double)tighter)*100;
		

		
		
		System.out.print("\n ------------------ \n Core:"+ core+ " parallelism:" + parallelism+ "length:"+ critical_path+"\n anomaly: "+anomalyDetected+"\n tighter: "+tighter+ "\n advantage:"+ (tighter-anomalyDetected)+"\n percentage: "+ tighterP+"%"+"\n ------------------ \n");

	}


}
//--------------------------------------------------------------------
