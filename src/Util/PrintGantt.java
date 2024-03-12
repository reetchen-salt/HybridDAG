package Util;

import java.awt.*;
import javax.swing.*;
import entity.Core;
import entity.Node;
import java.util.ArrayList;

public class PrintGantt extends JFrame {
	
	boolean isEarly ;
    long makespan;
    ArrayList<Core> CoreList = new ArrayList<>();

    public PrintGantt(ArrayList<Core> CoreList, long makespan, boolean isEarly) {
    	
    	
    
    	
    	
        SwingUtilities.invokeLater(() -> {
            setSize(1000, 600);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setVisible(true);
        });
        this.CoreList = CoreList;
        this.makespan = makespan;
        this.isEarly = isEarly;

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Clear and prepare the component for drawing
        g.setColor(Color.black);

        if (CoreList == null || CoreList.isEmpty()) {
            return; // Early return if CoreList is not set or empty
        }

        int startX = 60;
        int startY = 90;
        int value = 500; // Scale factor for drawing
        int rectHeight = 30;
        int verticalSpacing = 60; // Space between rows of rectangles
        
        
        
        int minYForDetails = startY + CoreList.size() * verticalSpacing + rectHeight; // Start drawing details below all rectangles
     // Assume makespan, value, startX, startY, and verticalSpacing are defined as before
        // The Y position for the timeline will be below the last rectangle
        int timelineY = startY + CoreList.size() * verticalSpacing + rectHeight + 20; // Additional space for clarity

        // Determine the end of the timeline based on the maximum finish time of any rectangle
        long maxFinishTime = 0;
        for (Core core : CoreList) {
            for (Node node : core.Nodes) {
                long finishTime = node.getStart(isEarly) + node.getWCET(isEarly);
                if (finishTime > maxFinishTime) {
                    maxFinishTime = finishTime;
                }
            }
        }
        for (int i = 0; i < CoreList.size(); i++) {
            for (int j = 0; j < CoreList.get(i).Nodes.size(); j++) {
                Node node = CoreList.get(i).Nodes.get(j);
                // Scale the start time and WCET relative to the makespan to calculate position and size
                int rectStartX = startX + (int) ((double) node.getStart(isEarly) / makespan * value);
                int rectWidth = (int) ((double) node.getWCET(isEarly) / makespan * value);
                
                // Ensure there's no gap for consecutive tasks (e.g., v6 follows v2 without a gap)
                if (j > 0) {
                    Node prevNode = CoreList.get(i).Nodes.get(j - 1);
                    int prevEndX = startX + (int) ((double) (prevNode.getStart(isEarly) + prevNode.getWCET(isEarly)) / makespan * value);
                    if (rectStartX < prevEndX) { // Adjust rectStartX if it overlaps with the previous task
                        rectStartX = prevEndX;
                    }
                }

                // Drawing logic remains the same
                adjustFontSizeToFit(g, "v" + node.getIndex(), rectWidth, new Font("Serif", Font.BOLD, 14));
                g.drawRect(rectStartX, startY + i * verticalSpacing, rectWidth, rectHeight);
                drawCenteredText(g, "v" + node.getIndex(), rectStartX, startY + i * verticalSpacing, rectWidth, rectHeight);
            }
        }
        
        g.drawLine(startX, timelineY, startX + (int)(maxFinishTime * value / makespan), timelineY);
        int maxLabelHeight = 0; // To capture the maximum height of the time unit labels
        // Draw time units along the timeline
        for (int i = 0; i <= maxFinishTime; i++) {
            int timeUnitX = startX + (int)(i * value / makespan);
            // Draw small tick marks for each time unit
            g.drawLine(timeUnitX, timelineY, timeUnitX, timelineY + 5);

            // Draw the time unit label for every time unit or at specific intervals for clarity
            String label = String.valueOf(i);
            FontMetrics metrics = g.getFontMetrics();
            int labelWidth = metrics.stringWidth(label);
            int labelHeight = metrics.getHeight();
            maxLabelHeight = Math.max(maxLabelHeight, labelHeight);
            g.drawString(label, timeUnitX - labelWidth / 2, timelineY + 20);
        }

        // Calculate the starting Y position for the detailed information, considering the timeline and its labels
        int detailsStartY = timelineY + maxLabelHeight + 20; // Add space below the last time unit label for the details

        // Draw detailed information below the timeline
        drawDetails(g, startX, detailsStartY);
    }

    private void adjustFontSizeToFit(Graphics g, String text, int maxWidth, Font font) {
        g.setFont(font);
        int textWidth = g.getFontMetrics().stringWidth(text);
        if (textWidth > maxWidth) {
            float scaleFactor = (float) maxWidth / textWidth;
            float newSize = font.getSize() * scaleFactor;
            g.setFont(font.deriveFont(newSize));
        }
    }

    private void drawCenteredText(Graphics g, String text, int x, int y, int width, int height) {
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (width - fm.stringWidth(text)) / 2;
        int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent() - 2;
        g.drawString(text, textX, textY);
    }

    private void drawDetails(Graphics g, int startX, int startY) {
        g.setFont(new Font("Serif", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = fm.getHeight();

        int y = startY;
        for (Core core : CoreList) {
            for (Node node : core.Nodes) {
                if (y + lineHeight > this.getHeight() - 20) {
                    return; // Stop drawing if we're about to go out of the window bounds
                }
                String details = "id:" + node.getIndex() +
                                 ", start:" + node.getStart(isEarly) +
                                 ", WCET:" + node.getWCET(isEarly) +
                                 ", Tempf:" + node.getnewf(isEarly);
                g.drawString(details, startX, y);
                y += lineHeight + 5; // Add space between lines
            }
        }
    }
    

}
