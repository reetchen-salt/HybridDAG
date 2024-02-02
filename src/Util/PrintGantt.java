package Util;

import java.awt.*;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import entity.Core;


public class PrintGantt extends JFrame {
	 long makespan;
	 ArrayList<Core> CoreList = new ArrayList<Core> ();

	public PrintGantt(ArrayList<Core> CoreList, long makespan) {
		setSize(1000, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setVisible(true);
		this.CoreList = CoreList;
		this.makespan = makespan;
	}

	public void paint(Graphics g) {
		g.setColor(Color.blue);
		g.setFont(new Font("ºÚÌå", Font.BOLD, 14));
		String string = "";
		int t = 0;
		for (int i = 0; i < CoreList.size(); i++) {

			string += "{";
			for (int j = 0; j < CoreList.get(i).Nodes.size(); j++) {
			
				int value = 500;
				g.drawRect(60 + (int) ((double) CoreList.get(i).Nodes.get(j).getStart() / makespan * value),
						90 + i * 60, (int) ((double) CoreList.get(i).Nodes.get(j).getWCET() / makespan * value), 30);
				g.drawString(CoreList.get(i).Nodes.get(j).getIndex() + "",
						60+ (int) ((double) CoreList.get(i).Nodes.get(j).getStart() / makespan * value), 107 + i * 60);
				g.drawString("id:" + CoreList.get(i).Nodes.get(j).getIndex() + ",start:"
						+ CoreList.get(i).Nodes.get(j).getStart() + ",WCET:" + CoreList.get(i).Nodes.get(j).getWCET() + ",Tempf:" + CoreList.get(i).Nodes.get(j).getTempf(),
						50, 150 + CoreList.size() * 30 + t++ * 30);
				string += CoreList.get(i).Nodes.get(j).getIndex() + ",";
			
			}
			string += "}";
		}
		g.drawString(string, 50, 50);
		g.drawString("makespan:" + makespan, 50, 75);
		
		


	}


}
