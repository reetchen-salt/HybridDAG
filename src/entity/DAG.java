
package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Util.AnalysisUtil;

import java.util.Collections;

public class DAG {

	public ArrayList<Integer> node_list = new ArrayList<Integer>();
	public ArrayList<ArrayList<Integer>> dag_node_group = new ArrayList<ArrayList<Integer>>();

	public int parallelism;
	public int critical_path;

	public ArrayList<Node> DagList = new ArrayList<Node>();

	public ArrayList<Node> Cpath = new ArrayList<Node>();

	public boolean legal = true;

	public int priority;

	public long period;
	public long deadline;
	public long TotalWCET;
	public long[] WCETList;

	public double matrix_result[][];

	public long R;

	int task_id;

	public double util;

	public DAG(int parallelism, int critical_path, int priority, long period, long deadline, long TotalWCET,
			int task_id, double util) {

		this.parallelism = parallelism;
		this.critical_path = critical_path;
		this.priority = priority;
		this.period = period;
		this.deadline = deadline;
		this.TotalWCET = TotalWCET;
		this.task_id = task_id;
		this.util = util;

//			critical_path= (int) Math.max(3,Math.random() *critical_path );
//			parallelism = (int) Math.max(3,Math.random() *parallelism );

		// 步骤1 构建DAG每层的节点数量
		int node_num = 0; // 当前节点编号，输入完成即变成节点数量参数
		node_list = new ArrayList<Integer>();
		node_list.add(1); // 头加一个节点
		dag_node_group = new ArrayList<ArrayList<Integer>>();
		dag_node_group.add(new ArrayList<Integer>());
		dag_node_group.get(0).add(++node_num); // 输入第一层节点

		for (int i = 1; i < critical_path - 1; i++) {
			node_list.add(parallelism); // 随机每次节点数量，不可超过并行度大小
			dag_node_group.add(new ArrayList<Integer>());
			for (int j = 0; j < node_list.get(i); j++) {
				dag_node_group.get(i).add(++node_num);
			}
		}
		node_list.add(1); // 尾加一个节点
		dag_node_group.add(new ArrayList<Integer>());
		dag_node_group.get(critical_path - 1).add(++node_num);

		// 步骤2 开始生成路径
		List<Integer> suce_list = new ArrayList<Integer>(); // 所有后继层的节点
		List<Integer> ance_list = new ArrayList<Integer>(); // 所有除了前驱层的祖先节点
		List<Integer> self_list = new ArrayList<Integer>();
		List<Integer> desc_list = new ArrayList<Integer>();
		List<List<Integer>> edge_list = new ArrayList<List<Integer>>(); // 记录边，相邻两个元素之间有边
		List<Integer> t_group = new ArrayList<Integer>();

		for (int i = 1; i < critical_path - 1; i++) {
			self_list.clear();
			suce_list.clear();
			ance_list.clear();
			desc_list.clear();

			self_list.addAll(dag_node_group.get(i));
			suce_list.addAll(dag_node_group.get(i + 1));
			for (int j = 0; j < i; j++) {
				ance_list.addAll(dag_node_group.get(j));
			}
			for (int j = i + 1; j < critical_path; j++) {
				desc_list.addAll(dag_node_group.get(j));
			}
			for (int j = 0; j < self_list.size(); j++) {
				// int k1 = Math.max((int)(Math.random() * ance_list.size()), 1);
				// //随机抽取m个前驱节点,1~size
				// int k2 = Math.max((int)(Math.random() * desc_list.size()), 1);
				// //随机抽取m个前驱节点,1~size
				//
				int k1 = Math.max((int) (Math.random() * Math.ceil(ance_list.size() / 2)), 1); // 随机抽取m个前驱节点,1~size
				int k2 = Math.max((int) (Math.random() * Math.ceil(desc_list.size() / 2)), 1); // 随机抽取m个前驱节点,1~size

				t_group.clear();
				while (t_group.size() < k1) { // 随机抽取m个前驱节点
					int t = (int) (Math.random() * ance_list.size());// 1 - m
					if (!t_group.contains(t)) {
						t_group.add(t);
					}
				}
				Collections.sort(t_group);
				for (int k = 0; k < t_group.size(); k++) {
					edge_list.add(new ArrayList<Integer>());
					edge_list.get(edge_list.size() - 1).add(ance_list.get(t_group.get(k)));
					edge_list.get(edge_list.size() - 1).add(self_list.get(j));
				}

				t_group.clear();
				while (t_group.size() < k2) { // 随机抽取m个前驱节点
					int t = (int) (Math.random() * desc_list.size());// 1 - m
					if (!t_group.contains(t)) {
						t_group.add(t);
					}
				}
				Collections.sort(t_group);
				for (int k = 0; k < t_group.size(); k++) {
					edge_list.add(new ArrayList<Integer>());
					edge_list.get(edge_list.size() - 1).add(self_list.get(j));
					edge_list.get(edge_list.size() - 1).add(desc_list.get(t_group.get(k)));
				}
			}
			edge_list.add(new ArrayList<Integer>());
			edge_list.get(edge_list.size() - 1).add(self_list.get(0));
			edge_list.get(edge_list.size() - 1).add(desc_list.get(0));
		}
		// System.out.println(edge_list);
		double path[][] = new double[node_num][node_num]; // 生成邻接矩阵
		for (int i = 0; i < edge_list.size(); i++) {
			path[(int) edge_list.get(i).get(0) - 1][(int) edge_list.get(i).get(1) - 1] = 1;
		}
		// System.out.println("输出原始邻接矩阵");
		// out_put_Matrix(path);
		double iden_d[][] = new double[node_num][node_num];
		iden_d = getIdentity(node_num);
		double pow[][] = new double[node_num][node_num];
		pow = matrix_pow_bool(matrix_or_bool(path, iden_d), node_num);
		double inver_d[][] = new double[node_num][node_num];
		inver_d = matrix_inverse(iden_d);

		double D_KK[][] = new double[node_num][node_num];
		D_KK = matrix_multiplication_bool(matrix_and_bool(pow, inver_d), path);

		matrix_result = new double[node_num][node_num];
		matrix_result = matrix_and_bool(matrix_inverse(D_KK), path);

		for (int i = 0; i < dag_node_group.size(); i++) {

			for (int j = 0; j < dag_node_group.get(i).size(); j++) {

				DagList.add(new Node(0, dag_node_group.get(i).get(j)));
			}
		}

		// successor and predecessor

		for (int i = 0; i < matrix_result.length; i++) {
			for (int j = 0; j < matrix_result[i].length; j++) {

				if (matrix_result[i][j] == 1) {

					DagList.get(i).successor.add(DagList.get(j));
					DagList.get(j).predecessor.add(DagList.get(i));
					DagList.get(i).suc_backup.add(DagList.get(j));
					DagList.get(j).pre_backup.add(DagList.get(i));

				}
			}
		}

		// descendants
		for (int i = DagList.size() - 1; i >= 0; i--) {

			for (int j = 0; j < DagList.get(i).predecessor.size(); j++) {

				DagList.get(i).predecessor.get(j).Descendant.add(DagList.get(i));
				DagList.get(i).predecessor.get(j).Descendant.addAll(DagList.get(i).Descendant);
				DagList.get(i).predecessor.get(j).Des_backup.add(DagList.get(i));
				DagList.get(i).predecessor.get(j).Des_backup.addAll(DagList.get(i).Descendant);

			}
		}

		// ancestors

		for (int i = 0; i < DagList.size(); i++) {

			for (int j = 0; j < DagList.get(i).successor.size(); j++) {

				DagList.get(i).successor.get(j).Ancestor.add(DagList.get(i));

				DagList.get(i).successor.get(j).Ancestor.addAll(DagList.get(i).Ancestor);

				DagList.get(i).successor.get(j).Anc_backup.add(DagList.get(i));

				DagList.get(i).successor.get(j).Anc_backup.addAll(DagList.get(i).Ancestor);

			}
		}

//		System.out.print(" total util  is " + this.util+ "\n" );

//	      TotalWCET =100;

        // List to store the utilization values for each task
        List<Double> utilList = new ArrayList<>();
        Random rand = new Random();
        
        // Remaining total utilization to be distributed
        double remainingUtilization = this.util;
        
        // Randomly distribute utilization to numTasks - 1 tasks
        for (int i = 1; i < DagList.size(); i++) {
            // Generate a random number between 0 and 1
            double randVal = rand.nextDouble();
            
            // Calculate the utilization for the current task based on remaining utilization
            double nextUtil = remainingUtilization * (1 - Math.pow(randVal, 1.0 / ( DagList.size() - i)));
            
            // Add the calculated utilization to the list
            utilList.add(nextUtil);
            
            // Reduce the remaining total utilization by the assigned amount
            remainingUtilization -= nextUtil;
        }
        
        // Assign the remaining utilization to the last task
        utilList.add(remainingUtilization);
        
        
        
        
        

//		long mean = (long) Math.floor(TotalWCET / (double) DagList.size());
//
//		if (mean / 2 < 1) {
//
//			legal = false;
//
//		}
//
//		for (int i = 0; i < DagList.size(); i++) {
//
//			timeList.add(mean / 2);
//
//			TotalWCET -= mean / 2;
//
//		}
//
//		while (TotalWCET != 0) {
//
//			for (int j = 0; j < timeList.size(); j++) {
//
//				long extra = (long) (Math.random() * (TotalWCET + 1));
//
//				timeList.set(j, timeList.get(j)+extra);
//
//				TotalWCET -= extra;
//
//				if (TotalWCET == 0) {
//					break;
//
//				}
//
//			}
//
//		}
//		
//		
//
//		Collections.shuffle(timeList);

		WCETList = new long[DagList.size()];

		for (int i = 0; i < utilList.size(); i++) {
			
			
			
			long theWCET = (long)(utilList.get(i)*this.period);
			
			if(theWCET < 5) {
				
				theWCET = 10;
			}

			WCETList[i] = theWCET ;

			DagList.get(i).setWCET(theWCET );

		}
		

		
		
		
		List<Integer> critical_temp = critical_path(matrix_result, WCETList);

		// critical path

		for (int i = 0; i < critical_temp.size(); i++) {

			for (int j = 0; j < DagList.size(); j++) {

				if (critical_temp.get(i) == DagList.get(j).getIndex()) {

					Cpath.add(DagList.get(j));

				}

			}

		}

		// frontpath

		DagList.get(0).frontpath = DagList.get(0).getWCET("max");

		for (int i = 1; i < DagList.size(); i++) {

			DagList.get(i).predecessor.sort((p1, p2) -> Long.compare(p1.frontpath, p2.frontpath));

			Collections.reverse(DagList.get(i).predecessor);

			DagList.get(i).frontpath = DagList.get(i).getWCET("max") + DagList.get(i).predecessor.get(0).frontpath;

		}

		// backpath

		DagList.get(DagList.size() - 1).backpath = DagList.get(DagList.size() - 1).getWCET("max");

		for (int i = DagList.size() - 2; i >= 0; i--) {

			DagList.get(i).successor.sort((p1, p2) -> Long.compare(p1.backpath, p2.backpath));

			Collections.reverse(DagList.get(i).successor);

			DagList.get(i).backpath = DagList.get(i).getWCET("max") + DagList.get(i).successor.get(0).backpath;

		}

		// path

		for (int i = 0; i < DagList.size(); i++) {

			DagList.get(i).path = DagList.get(i).backpath + DagList.get(i).frontpath - DagList.get(i).getWCET("max");

		}


		// concurrent
		
		
		
		

		// 计算concurrent node

		for (int i = 0; i < DagList.size(); i++) {

			for (int j = 0; j < DagList.size(); j++) {

				if (!DagList.get(i).Ancestor.contains(DagList.get(j))
						&& !DagList.get(i).Descendant.contains(DagList.get(j))
						&& !DagList.get(i).equals(DagList.get(j))) {

					DagList.get(i).concurrent.add(DagList.get(j));

				}

			}

		}
		
		
	

	}

	public void setPriority(int P) {

		this.priority = P;
	}

	public int getPriority() {

		return priority;
	}

	public void setR(long R) {

		this.R = R;
	}

	public long getR() {

		return R;
	}

	public void setIndex(int id) {

		this.task_id = id;
	}

	public int getIndex() {

		return task_id;
	}

	public double getAverage() {

		ArrayList<Long> WCET = new ArrayList<Long>();

		for (Node A : this.DagList) {

			WCET.add(A.getWCET("max"));

		}

		double median = new AnalysisUtil().getMedian(WCET);

		return median;

	}

//测试组	

//	public int parallelism	;
//	public int critical_path;
//	
//	public ArrayList<Node> DagList = new ArrayList<Node>() ;
//	
//	public ArrayList<Node> Cpath = new ArrayList<Node>() ;
//	public int priority;
//	
//	public long period;
//	public long deadline;
//	public long TotalWCET;
//	
//	public double matrix_result [][];
//	
//	 
//	 int task_id;
//	 
//	public double util;
//	
//	
//	public DAG (int parallelism, int critical_path, int priority, long period,long deadline, long TotalWCET, int task_id, double util ) {
//
//		this.parallelism = parallelism;
//		this.critical_path = critical_path;
//		this.priority = priority;
//		this.period = period;
//		this.deadline =  deadline;
//		this.TotalWCET = TotalWCET ;
//		this.task_id = task_id;
//		this.util = util;
//		
//
//		 int [] critical = {1, 2, 3, 6, 10};
//		 
//		 int [][] daglist = {{1},{2},{3,4,5},{6,7,8,9},{10}};
//		 
//		 int [] WCET = {15120, 14861, 14824, 8848, 8153, 8315, 4546, 5667, 3320, 24346};
//		 
//
//		 
//		 int [] path = {61598, 60304, 61598, 54225, 61598, 48778, 58883, 58698, 61598, 61263, 56417, 59438, 61598};
//		 
//		 int [][] matrix_result = {{0,1,0,0,0,0,0,0,0,0},
//									 {0,0,1,1,1,0,0,0,0,0},
//									 {0,0,0,0,0,1,0,1,0,0},
//									 {0,0,0,0,0,1,0,1,1,0},
//									 {0,0,0,0,0,0,1,0,1,0},
//									 {0,0,0,0,0,0,0,0,0,1},
//									 {0,0,0,0,0,0,0,0,0,1},
//									 {0,0,0,0,0,0,0,0,0,1},
//									 {0,0,0,0,0,0,0,0,0,1},
//									 {0,0,0,0,0,0,0,0,0,0}};
//
//        
//
//        for(int i = 0; i< daglist.length; i++) {
//
//        for(int j = 0; j <  daglist[i].length ; j++) {
//        	
//        	DagList.add(new Node(0,daglist[i][j]));
//        	}
//        }
//
//
//        	// successor and predecessor
//
//        for(int i = 0; i< matrix_result.length; i++) {
//        	for(int j = 0; j< matrix_result[i].length; j++) {
//
//        	if(matrix_result[i][j] == 1 ) {
//        		
//        		DagList.get(i).successor.add(DagList.get(j));
//        		DagList.get(j).predecessor.add(DagList.get(i));
//        		
//        	} } }
//        
//        
//        //descendants
//        for(int i = DagList.size()-1; i >=  0 ; i--) {
//        	
//        	for(int j = 0; j<  DagList.get(i).predecessor.size() ; j++) {
//        		
//        		
//        		DagList.get(i).predecessor.get(j).Descendant.add(DagList.get(i));
//        		
//        		DagList.get(i).predecessor.get(j).Descendant.addAll(DagList.get(i).Descendant);
//
//        	} 
//        }
//        
//        
//        //ancestors
//        
//        for(int i = 0; i <  DagList.size() ; i++) {
//        	
//        	for(int j = 0;  j< DagList.get(i).successor.size() ; j++) {
//        		
//        		
//        		DagList.get(i).successor.get(j).Ancestor.add(DagList.get(i));
//        		
//        		DagList.get(i).successor.get(j).Ancestor.addAll(DagList.get(i).Ancestor);
//
//        	} 
//        }
//        
//        
//
//        
//        
//        
////        System.out.print(" total WCET list is " + TotalWCET+ "\n" );
//        
//
//       
//        for (int i = 0; i < WCET.length; i++) {
//        	
//
//        			 
//        	 DagList.get(i).setWCET( WCET[i]);
//        	
//        			 
//       
//
//        	
//        	
//        }
//        
//        
//
//
//        
//        
//        // critical path
//        for (int i = 0; i <  critical.length; i++) {
//        	for (int j = 0; j < DagList.size(); j++) {
//        	if(DagList.get(j).getIndex()==critical[i]) {
//
//        			
//        			Cpath.add(DagList.get(j));    	
//        	}        			
//
//        }
//        
//        
//        }
//        
//      
//    // frontpath
//      
//      DagList.get(0).frontpath =  DagList.get(0).getWCET();
//      
//      
//      for(int i = 1; i<  DagList.size();i++) {
//      	
//      	
//      	
//      	
//      	DagList.get(i).predecessor.sort((p1, p2) -> Long.compare(p1.frontpath, p2.frontpath));
//      	
//      	
//      	Collections.reverse(DagList.get(i).predecessor);
//      	
//      	DagList.get(i).frontpath = DagList.get(i).getWCET()+DagList.get(i).predecessor.get(0).frontpath;
//      	
//      	
//      	
//      	
//      }
//      
//   
//      // backpath
//      
//      
//      
//      DagList.get(DagList.size()-1).backpath =  DagList.get(DagList.size()-1).getWCET();
//      
//      
//      for(int i = DagList.size()-2; i >= 0 ;i--) {
//      	
//      	
//      	
//      	
//      	DagList.get(i).successor.sort((p1, p2) -> Long.compare(p1.backpath, p2.backpath));
//      	
//      	
//      	Collections.reverse(DagList.get(i).successor);
//      	
//      	DagList.get(i).backpath = DagList.get(i).getWCET()+DagList.get(i).successor.get(0).backpath;
//      	
//      	
//      	
//      	
//      }
//      
//      
//      
//      
//   // path 
//      
//      
//      
//      for(int i = 0; i<  DagList.size();i++) {
//      	
//      	
//      	DagList.get(i).path = DagList.get(i).backpath+ DagList.get(i).frontpath-DagList.get(i).getWCET();
//      	
//      	
//      	
//      	
//      	
//      }
//        
//
//            
//        
//
//}

	/* 创建单位矩阵 */
	public double[][] getIdentity(int size) {
		double[][] matrix = new double[size][size];
		for (int i = 0; i < size; i++)
			matrix[i][i] = 1;
		return matrix;
	}

	/* 矩阵相乘 */
	public double[][] matrix_multiplication_bool(double[][] M1, double[][] M2) {
		int m_size = M1.length;
		double[][] matrix = new double[m_size][m_size];
		double[][] matrix_1 = new double[m_size][m_size];
		double[][] matrix_2 = new double[m_size][m_size];
		matrix_1 = M1.clone();
		matrix_2 = M2.clone();
		for (int i = 0; i < m_size; i++) {
			for (int j = 0; j < m_size; j++) {
				matrix[i][j] = 0;
				for (int k = 0; k < m_size; k++) {
					if ((matrix_1[i][k] != 0 && matrix_2[k][j] != 0))
						matrix[i][j] = 1;
				}
			}
		}
		return matrix;
	}

	/* 矩阵求幂 */
	public double[][] matrix_pow_bool(double[][] M, int pow_num) {
		int m_size = M.length;
		double[][] matrix_1 = new double[m_size][m_size];
		double[][] matrix_2 = new double[m_size][m_size];
		matrix_1 = M.clone();
		matrix_2 = M.clone();

		for (int i = 0; i < pow_num; i++) {
			matrix_1 = matrix_multiplication_bool(matrix_1, matrix_2);
		}
		return matrix_1;
	}

	/* 矩阵取或 */
	public double[][] matrix_or_bool(double[][] M1, double[][] M2) {
		int m_size = M1.length;
		double[][] matrix = new double[m_size][m_size];
		double[][] matrix_1 = new double[m_size][m_size];
		double[][] matrix_2 = new double[m_size][m_size];
		matrix_1 = M1.clone();
		matrix_2 = M2.clone();
		for (int i = 0; i < m_size; i++) {
			for (int j = 0; j < m_size; j++) {
				if ((matrix_1[i][j] != 0 || matrix_2[i][j] != 0))
					matrix[i][j] = 1;
				else
					matrix[i][j] = 0;
			}
		}
		return matrix;
	}

	/* 矩阵取与 */
	public double[][] matrix_and_bool(double[][] M1, double[][] M2) {
		int m_size = M1.length;
		double[][] matrix = new double[m_size][m_size];
		double[][] matrix_1 = new double[m_size][m_size];
		double[][] matrix_2 = new double[m_size][m_size];
		matrix_1 = M1.clone();
		matrix_2 = M2.clone();
		for (int i = 0; i < m_size; i++) {
			for (int j = 0; j < m_size; j++) {
				if ((matrix_1[i][j] != 0 && matrix_2[i][j] != 0))
					matrix[i][j] = 1;
				else
					matrix[i][j] = 0;
			}
		}
		return matrix;
	}

	/* 矩阵取逆 */
	public double[][] matrix_inverse(double[][] M) {
		int m_size = M.length;
		double[][] matrix = new double[m_size][m_size];
		double[][] matrix1 = new double[m_size][m_size];
		matrix = M.clone();
		for (int i = 0; i < m_size; i++) {
			for (int j = 0; j < m_size; j++) {
				if (matrix[i][j] == 0)
					matrix1[i][j] = 1;
				else
					matrix1[i][j] = 0;
			}
		}

		return matrix1;
	}

	/* 打印矩阵 */
	public void out_put_Matrix(double[][] M) {
//		int m_size = M.length;
		for (int i = 0; i < M.length; i++) {
			System.out.print("{");
			for (int j = 0; j < M[i].length; j++) {
				System.out.print((int) M[i][j] + ",");
			}
			System.out.println("},");
		}
	}

	/* work_constrain */
//	public static int[] work_constrain(int core_num, int[] WCET, double adj_matrix [][]) { 
//    	int C_Path [] = new int [WCET.length];
//		return []; 
//    } 

	/* critical_path */
	public List<Integer> critical_path(double adj_matrix[][], long[] WCET) {

		int m_size = adj_matrix.length;
		double[][] critic_p = new double[m_size][2];
		double[] critic_buf = new double[2];
		List<Integer> critic_list = new ArrayList<Integer>(); // 所有后继层的节点
		critic_p[0][0] = 0;
		critic_p[0][1] = WCET[0];
		for (int i = 1; i < m_size; i++) {
			critic_p[i][0] = 0;
			critic_p[i][1] = WCET[i];
			for (int j = 0; j < i; j++) {
				if (adj_matrix[j][i] != 0) {
					if (critic_p[j][1] + WCET[i] > critic_p[i][1]) {
						critic_p[i][0] = j + 1;
						critic_p[i][1] = critic_p[j][1] + WCET[i];
					}
				}
			}
		}
		critic_buf = critic_p[m_size - 1];
		critic_list.add(m_size);
		while (critic_buf[0] != 0) {
			critic_list.add((int) critic_buf[0]);
			critic_buf = critic_p[(int) critic_buf[0] - 1];
		}
		Collections.sort(critic_list);

		return critic_list;
	}
	
	
	
	
	
	

}
