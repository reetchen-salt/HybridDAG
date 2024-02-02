package Util;




import java.util.ArrayList;
import java.awt.Color;

import java.awt.Graphics;

import javax.swing.JFrame;

import entity.DAG;
import entity.Node;



public class DrawDag extends JFrame{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	DAG task ;
	
	
	ArrayList<Node> plist;
	 
	 
	public DrawDag( DAG task, ArrayList<Node> plist) {
		
		this.task = task;
		
		this.plist = plist;
		setTitle("DrawDag");		
		setSize(1200,1000);
		setVisible(true);
		
		 getContentPane().setBackground(Color.BLUE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	

		
	}
	
	
	public void paint(Graphics g) {
		
	
		
		
//		
//		 
//		 drawArrow(g, 200, 200,500, 500, 10, 45);
//		    
		 int x = 200;
		 
		 
//		 int [] critical = {1, 3, 7, 11};
//		 
//		 int [][] daglist = {{1},{2,3,4,5,6},{7,8,9,10},{11}};
//		 
//		 int [] WCET = {17, 5, 12, 4, 4, 9, 18, 15, 5, 7, 4};
//		 
//		 int [] priority = {0,4,1,2,5,3,6,7,9,8,10};
//		 
////		 int [] path = {61598, 60304, 61598, 54225, 61598, 48778, 58883, 58698, 61598, 61263, 56417, 59438, 61598};
//		 
//		 int [][] matrix = {{0,1,1,1,1,1,0,0,0,0,0},
//				 {0,0,0,0,0,0,1,1,1,1,0},
//				 {0,0,0,0,0,0,1,0,1,1,0},
//				 {0,0,0,0,0,0,1,1,1,1,0},
//				 {0,0,0,0,0,0,1,1,0,1,0},
//				 {0,0,0,0,0,0,1,1,0,1,0},
//				 {0,0,0,0,0,0,0,0,0,0,1},
//				 {0,0,0,0,0,0,0,0,0,0,1},
//				 {0,0,0,0,0,0,0,0,0,0,1},
//				 {0,0,0,0,0,0,0,0,0,0,1},
//				 {0,0,0,0,0,0,0,0,0,0,0}};
		 
		 task.Cpath.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		 
		 int [] critical = new int[task.Cpath.size()];
		 
		for(int i = 0; i< task.Cpath.size();i++) {
			
			 critical[i]= task.Cpath.get(i).getIndex();
			
			
		}
		 
		
		 
		int [][] daglist = task.dag_node_group.stream().map(  u  ->  u.stream().mapToInt(i->i).toArray()  ).toArray(int[][]::new);
		
		
		
		
		

		 
		long [] WCET = task.WCETList;
		
		 
		int [] priority = new int[plist.size()];
		
		plist.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));
		
		

		
		for(int i = 0; i<  plist.size();i++) {
			
			priority[i]= plist.get(i).getPriority();
			
			
			
			
		}
		 
		 
		double[][] matrix = task.matrix_result;
		 
		 
		 
//					
		 
		 
		 
		 ArrayList<ArrayList<Integer>> axis = new ArrayList<ArrayList<Integer>>();
		 
		 int number = 0;
		 for(int i = 0; i< daglist.length;i++) {
			 
			 int y = 800 ;
			 
			 
			 
			 
			 for(int j = 0; j< daglist[i].length;j++) {
			 
			 g.setColor(Color.black);
			 
			 
			 g.drawOval(x,y,50,50);
			 
			 
			 for( int k=0; k < critical.length; k++) {
				 
				if(daglist[i][j]==critical[k]) {
					
					
					g.drawOval(x+15,y,10,10);
					 g.fillOval(x+15,y,10,10);
				}
				 
				 
				 
				 
			 }
			 
			 
			 
			 
			 
			 
			 g.drawString("V"+String.valueOf(daglist[i][j]),x+20,y+30);
			 
			 g.drawString("WCET="+String.valueOf(WCET[number]),x-20,y+60);
			 
			 g.setColor(Color.blue);
			 
//			 g.drawString("Path="+String.valueOf(path[number]),x,y+80);
			 
			 g.setColor(Color.red); 
			 g.drawString("P="+String.valueOf(priority[number]),x,y);
			 
			
			 
			 number++;
			 
			 
			 y= y-120; 
			 
			 ArrayList<Integer> temp  = new ArrayList<Integer>();
			 temp.add(x+20);
			 temp.add(y+100);
			
			 axis.add(temp);
			 
		
			 
		 }
			 
			 x = x+200;
		 }
		 
		 
		 
		 g.setColor(Color.black);
		 
		 for(int i = 0; i< matrix.length;i++) {
			 
			 
			
			 for(int j = 0; j<matrix[i].length;j++) {
				 
				 if(matrix[i][j]==1) {
					 
					 
					 drawArrow(g, axis.get(i).get(0)+30,  axis.get(i).get(1)+50, axis.get(j).get(0)-20, axis.get(j).get(1)+50, 10, 45);
					 
				 }
				 
		 
		 
		 
		 
			 }
			 
		 }
		 
	 
		 
		 
		 
		 
	}
	
	
	
	
	
	
	

	
	
	
    static public void drawArrow(Graphics g, int x0, int y0, int x1,
            int y1, int headLength, int headAngle) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y0 - y1, x0 - x1);
        int[] xs = { x1 + (int) (headLength * Math.cos(angle + offs)), x1,
                x1 + (int) (headLength * Math.cos(angle - offs)) };
        int[] ys = { y1 + (int) (headLength * Math.sin(angle + offs)), y1,
                y1 + (int) (headLength * Math.sin(angle - offs)) };
        g.drawLine(x0, y0, x1, y1);
        g.drawPolyline(xs, ys, 3);
    }
	
	
//    
//	public static void main(String[] args) {
//		
//		
//		
//		
//
//		
//		
//		DrawDag t = new DrawDag(); 
//		
//		Graphics g = null ;
//		
//		
//		t.paint(g);
//		
//
//        
//		
//	}
//	
//    
	
	
	
	
	
	

}
