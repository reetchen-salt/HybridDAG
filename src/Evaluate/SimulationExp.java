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

import Analysis.AnomalyAnalysis;
import Analysis.Makespan;
import entity.Core;
import entity.DAG;

public class SimulationExp {

	public static int MAX_PERIOD = 3000;
	public static int MIN_PERIOD = 1000;

	public static int SEED = 1000;
	static PrintWriter writer = null;
	static PrintWriter writer2 = null;
	int count = 0;
	static String filePath = "data/your_data.txt";

	public synchronized void countDown(CountDownLatch cd) {
		cd.countDown();
		count++;
	}

	public static void main(String args[]) throws Exception {
		
		
	
		
		
        try (FileWriter fileWriter = new FileWriter(filePath);
	             PrintWriter printWriter = new PrintWriter(fileWriter)) {
	            
    	    // Writing the header row with updated headers
		    printWriter.println("MaxMakespan,MinMakespan,MinWorkload,MaxWorkload,MaxWCET,MinWCET,MedianWCET,NumNodes,MaxParallel,CriticalPath,AvgInDegree,AvgOutDegree,CoreNum,IsAnomaly");


	        } catch (IOException e) {
	            System.err.println("An error occurred while writing the file.");
	            e.printStackTrace();
	        }
			

//		int[] Core = { 3,4,6,7,8,9 };
//
//		int[] Par = { 4,5,6,7,8,9,10 };
//
//		int[] Cri = { 4,5,6,7,8,9,10 };
//		
		

		int[] Core = { 3};

		int[] Par = { 4 };

		int[] Cri = { 4 };

		double[] ratio = { 0.2 };

		int times = 1;

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

//		for (int t = 0; t < Par.length; t++) {
//			
//			final int not=t;
//
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//				
//					ep.PriorityOrder(Core[0], Par[not], Cri[0], times,"NoPar", ratio[0]);
//					
//					ep.countDown(bd);
//				}
//			}).start();
//			
//		}
//		bd.await();
//		
//
//		for (int a = 0; a < Cri.length; a++) {
//			
//			final int noa=a;
//
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					ep.PriorityOrder(Core[0], Par[0], Cri[noa], times,"NoCri", ratio[0]);
//					
//					ep.countDown(cd);
//
//				}
//			}).start();
//			
//		}
//		cd.await();
//		

	}

//-------------------start the system, return RTA of MSRP and MrsP
	public void PriorityOrder(int core, int parallelism, int critical_path, int TOTAL_NUMBER_OF_DAGs, String name,
			double ratio) {

		// -----
		SimpleSystemGenerator generator = new SimpleSystemGenerator(MIN_PERIOD, MAX_PERIOD, 1, true, SEED, parallelism,
				critical_path, ratio);
		
		
		
		long makespan1 = 0;
		long makespan2 =0;
		
		long MaxMakespan = 0;
		long MinMakespan = 0;
		long MinWorkload = 0;
		long MaxWorkload = 0;
		long MaxWCET = 0;
		long MinWCET = 0;
		double MedianWCET =0;
		int NumNodes = 0;
		int MaxParallel = 0;
		int CriticalPath = 0;
		long AvgInDegree = 0;
		long AvgOutDegree = 0;
		int CoreNum = 0;
		int IsAnomaly =999;
		
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
			
			
			
			MaxMakespan = new AnalysisUtil().getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, core,"max"));
			
			MinMakespan = new AnalysisUtil().getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, core,"min"));
	
			MinWorkload = new AnalysisUtil().getWorkload(tasks.get(0).DagList, "min");
			
			MaxWorkload = new AnalysisUtil().getWorkload(tasks.get(0).DagList, "max");
			
			tasks.get(0).DagList.sort((p1, p2) -> Long.compare(p1.getWCET("max"), p2.getWCET("max")));
			
			MinWCET = tasks.get(0).DagList.get(0).getWCET("max");
			
			
			MaxWCET = tasks.get(0).DagList.get(tasks.get(0).DagList.size()-1).getWCET("max");
			
			MedianWCET =  new AnalysisUtil().findMedianWCET(tasks.get(0).DagList) ;
			 
			NumNodes = tasks.get(0).DagList.size();
			
			ArrayList<ArrayList<Node>> twoDList = new AnalysisUtil().GetParaList(tasks.get(0).DagList);;
			
			MaxParallel = twoDList.get(0).size();
			
			
			CriticalPath = tasks.get(0).Cpath.size();
			
			
			AvgInDegree = new AnalysisUtil().getAvgInDegree(tasks.get(0).DagList) ;
			
			AvgOutDegree = new AnalysisUtil().getAvgOutDegree(tasks.get(0).DagList);
			
			
			 CoreNum = core;
			
		
			
//			System.out.print("the bound is" + makespan2);
//	
//			System.out.print("the simulation is" + makespan1);
		    new Util.DrawDag(tasks.get(0), tasks.get(0).DagList);
//			

				if (new AnomalyAnalysis().AnalyzeAnomaly(tasks.get(0).DagList, core)) {

					
					
					ArrayList<Core> corelist = new Makespan().getMakespan(tasks.get(0).DagList, core, "max");

					
					makespan1 = new AnalysisUtil().getMakespan(corelist);
					
					new Util.PrintGantt(corelist, makespan1,"max");
					
					
					for (int j = 0; j < 100; j++) {

						makespan2 = new AnalysisUtil()
								.getMakespan(new Makespan().getMakespan(tasks.get(0).DagList, CoreNum, "random"));

						
						System.out.print("\n the maximum makespan is " + makespan1 + "the generated makespan is " + makespan2 );
			
						
						if (makespan2 > makespan1) {

							IsAnomaly = 1;
						}

					}

				} else {
					
					System.out.print("no anomaly") ;
				

					IsAnomaly = 0;

				}
				
				
				
				try (FileWriter fileWriter = new FileWriter(filePath, true);
					     PrintWriter printWriter = new PrintWriter(fileWriter)) {
					    
				
					    // Formatting and writing the data row
					    String dataRow = String.format("%d,%d,%d,%d,%d,%d,%f,%d,%d,%d,%d,%d,%d,%d",
					        MaxMakespan,
					        MinMakespan,
					        MinWorkload,
					        MaxWorkload,
					        MaxWCET,
					        MinWCET,
					        MedianWCET,
					        NumNodes,
					        MaxParallel,
					        CriticalPath,
					        AvgInDegree,
					        AvgOutDegree,
					        CoreNum,
					        IsAnomaly
					    );

					    // Writing the data row
					    printWriter.println(dataRow);

					} catch (IOException e) {
					    System.err.println("An error occurred while writing the file.");
					    e.printStackTrace();
					}
				
		    
		   

			

			

		}
		
		
		
		

		
		
//		System.out.print("\n ------------------ \n Core:"+ core+ " parallelism:" + parallelism+ "length:"+ critical_path+"\n anomaly: "+anomalyDetected+"\n tighter: "+tighter+ "\n advantage:"+ (tighter-anomalyDetected)+"\n percentage: "+ tighterP+"%"+"\n ------------------ \n");

	}


}
//--------------------------------------------------------------------
