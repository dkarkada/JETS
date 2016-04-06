package widi;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WidiApproxLength {
	public static void main(String [] args){
		JFrame j = new JFrame();
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		j.setSize((int)screenSize.getWidth(), (int)screenSize.getHeight());
		
		walPanel p = new walPanel();
		j.setContentPane(p);
		j.setVisible(true);
		j.validate();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class walPanel extends JPanel implements MouseWheelListener, KeyListener, ActionListener{
	boolean calibrating;
	int pp10cm;
	JLabel label, score;
	JTextField textbox;
	double length;
	double scoreval;
	int count;
	Point start, end;
	
	public walPanel(){
		setVisible(true);
		setOpaque(true);
		calibrating=false;
		setBackground(Color.WHITE);
		pp10cm = 369;
		start = new Point(250,250);
		end = new Point(250+pp10cm, 250);
		scoreval=0;
		count=0;
		
		label = new JLabel("pp10cm = " + pp10cm);
		if(!calibrating) label.setText("Estimate: ");
		label.setFont(new Font("Arial", Font.PLAIN, 24));
		add(label);
		
		textbox = new JTextField(6);
		add(textbox);
		textbox.setFont(new Font("Arial", Font.PLAIN, 24));
		if(calibrating) textbox.setVisible(false);
		
		score = new JLabel();
		score.setFont(new Font("Arial", Font.PLAIN, 24));
		add(score);
		if(calibrating) score.setVisible(false);
		
		addKeyListener(this);
		addMouseWheelListener(this);
		textbox.addActionListener(this);
		
		setFocusable(true);
		if(!calibrating) generate();
	}
	public Dimension getPreferredSize(){
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		return new Dimension((int)screenSize.getWidth(), (int)screenSize.getHeight());
		
	}
	public void generate(){
		count++;
		
		int rnd = (int)(Math.random()*10 + 1);
		switch (rnd){
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			length = Math.random()*5+.1;
			break;
		case 6:
		case 7:
		case 8:
			length = Math.random()*5 + 5;
			break;
		case 9:
		case 10:
			length = Math.random()*5 + 10;
			break;
		}
		length = ((int)(length*10))/10.0;
		
		int pxllength = (int) (length * pp10cm /10);
		start.x = getPreferredSize().width/2;
		start.y = getPreferredSize().height/2;
		double angle = Math.random()*2*Math.PI;
		end.x = (int) (start.x + pxllength * Math.cos(angle));
		end.y = (int) (start.y + pxllength * Math.sin(angle));
		
		repaint();
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(calibrating){
			end.x = 250+pp10cm;
		}
		g.drawLine(start.x, start.y, end.x, end.y);
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		int scroll = e.getWheelRotation();
		if(calibrating){
			pp10cm+=scroll;
			label.setText("pp10cm = " + pp10cm);
			repaint();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == e.VK_ENTER && calibrating){
			calibrating=false;
			textbox.setVisible(true);
			score.setVisible(true);
			label.setText("Estimate: ");
			validate();
			generate();
		}
	}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

	public void actionPerformed(ActionEvent e) {
		String input = ((JTextField)(e.getSource())).getText();
		try{
			double guess = Double.parseDouble(input);
			scoreval += Math.abs(guess-length)/length*100;
			DecimalFormat df = new DecimalFormat("#.##");
			double error = Double.parseDouble(df.format(scoreval/count));
			score.setText(length + "            " + error + " % error overall");
			textbox.setText("");
			generate();
		}catch(Exception ex){}
	}
}
