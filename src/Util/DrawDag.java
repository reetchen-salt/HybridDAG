package Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import entity.DAG;
import entity.Node;

public class DrawDag extends JFrame {
	DAG task;
	ArrayList<Node> plist;

	public DrawDag(DAG task, ArrayList<Node> plist) {
		this.task = task;
		this.plist = plist;
		initUI();
	}

	private void initUI() {
		setTitle("DrawDag");
		setSize(1200, 1000);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		// Add custom drawing panel
		add(new DrawingPanel(task, plist));
		setVisible(true);
	}

	class DrawingPanel extends JPanel {
		DAG task;
		ArrayList<Node> plist;

		public DrawingPanel(DAG task, ArrayList<Node> plist) {
			this.task = task;
			this.plist = plist;
			setBackground(Color.white);
		}

		public void paint(Graphics g) {

			super.paint(g);

            Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D for more control
            // Enable anti-aliasing for smoother lines and shapes
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


			int x = 200;

			task.Cpath.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

			int[] critical = new int[task.Cpath.size()];

			for (int i = 0; i < task.Cpath.size(); i++) {

				critical[i] = task.Cpath.get(i).getIndex();

			}

			int[][] daglist = task.dag_node_group.stream().map(u -> u.stream().mapToInt(i -> i).toArray())
					.toArray(int[][]::new);

			long[] WCET = task.WCETList;

			int[] priority = new int[plist.size()];

			plist.sort((p1, p2) -> Integer.compare(p1.getIndex(), p2.getIndex()));

			for (int i = 0; i < plist.size(); i++) {

				priority[i] = plist.get(i).getPriority();

			}

			double[][] matrix = task.matrix_result;

//					

			ArrayList<ArrayList<Integer>> axis = new ArrayList<ArrayList<Integer>>();

			int number = 0;
			for (int i = 0; i < daglist.length; i++) {

				int y = 800;

				for (int j = 0; j < daglist[i].length; j++) {

					g.setColor(Color.black);

					g.drawOval(x, y, 50, 50);

					for (int k = 0; k < critical.length; k++) {

						if (daglist[i][j] == critical[k]) {

							g.drawOval(x + 15, y, 10, 10);
							g.fillOval(x + 15, y, 10, 10);
						}

					}

					g.drawString("V" + String.valueOf(daglist[i][j]), x + 20, y + 30);

					g.drawString("WCET=" + String.valueOf(WCET[number]), x - 20, y + 60);

					g.setColor(Color.blue);

//			 g.drawString("Path="+String.valueOf(path[number]),x,y+80);

					g.setColor(Color.red);
					g.drawString("P=" + String.valueOf(priority[number]), x, y);

					number++;

					y = y - 120;

					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.add(x + 20);
					temp.add(y + 100);

					axis.add(temp);

				}

				x = x + 200;
			}

			g.setColor(Color.black);

			for (int i = 0; i < matrix.length; i++) {

				for (int j = 0; j < matrix[i].length; j++) {

					if (matrix[i][j] == 1) {

						drawArrow(g, axis.get(i).get(0) + 30, axis.get(i).get(1) + 50, axis.get(j).get(0) - 20,
								axis.get(j).get(1) + 50, 10, 45);

					}

				}

			}

		}

	}

	static public void drawArrow(Graphics g, int x0, int y0, int x1, int y1, int headLength, int headAngle) {
		double offs = headAngle * Math.PI / 180.0;
		double angle = Math.atan2(y0 - y1, x0 - x1);
		int[] xs = { x1 + (int) (headLength * Math.cos(angle + offs)), x1,
				x1 + (int) (headLength * Math.cos(angle - offs)) };
		int[] ys = { y1 + (int) (headLength * Math.sin(angle + offs)), y1,
				y1 + (int) (headLength * Math.sin(angle - offs)) };
		g.drawLine(x0, y0, x1, y1);
		g.drawPolyline(xs, ys, 3);
	}

}
	
	


