package compound;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Compound{
	public static void main (String...args) throws Exception 
	{
		JFrame j = new JFrame();  //JFrame is the window; window is a depricated class
		MyPanelc m = new MyPanelc();
		j.setSize(m.getSize());
		j.add(m); //adds the panel to the frame so that the picture will be drawn
			      //use setContentPane() sometimes works better then just add b/c of greater efficiency.

		j.setVisible(true); //allows the frame to be shown.
		
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the dialog box exit when you click the "x" button.
	}
	public static double calc(double a){
		//5.9, 13.5, .301, 13, 18.7
		double r = 5.9;//4.4;//5.9;//5        
		double s = 13.5;//12.4;//13  flat part pusher
		double h = .301;//.001  flat
		double x = 13;//12  			pushes all, flat part most
		double y = 18.7;//Math.sqrt(sqr(h)+sqr(r+s));
				
		double theta = conv(Math.asin(h/y));
		double X = a + theta;
		double f = Math.sqrt(sqr(r)+sqr(y)-2*r*y*(Math.cos(X/180*Math.PI)));
//		System.out.println(y);
		
		double c = law(f,s,x);
		double b = Math.signum(X) * law(y,r,f) - law(s,x,f);
		double d = Math.signum(X) * law(r,y,f) - law(x,s,f) + theta;
//		System.out.print(a + " " + b + " " + c + " " + d);
		
		double ratio = (Math.sin(b/180*Math.PI) * Math.cos(d/180*Math.PI))  /  (1.5 * Math.cos(a/180*Math.PI) * Math.sin(c/180*Math.PI)); 
//		System.out.println(ratio);
		return ratio;
	}
	public static double calc2(double a){
		if (a<38) return (a*.00831956)+ 0.382;
		else return (calc(a));
	}
	public static double sqr(double a){
		return a*a;
	}
	public static double law(double a, double b, double c){
		return conv(Math.acos( (sqr(a)-sqr(b)-sqr(c)) / (-2*b*c) ));
	}
	public static double conv(double a){
		return a * 180 / Math.PI;
	}

}

class MyPanelc extends JPanel implements ActionListener, MouseListener
{
	
	private BufferedImage buf; //uses buffering to eliminate flickering
	Graphics2D gBuf;
	private Timer time;
	double ang=0,rat=0;
		
	MyPanelc()
	{
		//time = new Timer(30, this); //sets delay to 30 millis and calls the actionPerformed of this class.
		setSize(1100, 900);
		setVisible(true); //it's like calling the repaint method.
		//time.start();
		buf = new BufferedImage(1650,900, BufferedImage.TYPE_INT_RGB);
		gBuf = buf.createGraphics();		//create Graphics2D object
		
		
		addMouseListener(this);		//add listeners
		setFocusable(true);
	}
	
	public void paintComponent(Graphics g)
	{
		gBuf.setColor(Color.WHITE);
		gBuf.fillRect(0, 0, 1100, 900);
		gBuf.setColor(Color.BLACK);
		gBuf.drawLine(100, 600, 1000, 600);
		gBuf.drawLine(550, 100, 550, 600);
		gBuf.drawString(ang + "," + rat,10,10);

		int prevx = 100; double prevy = 5000; double minSlope=1000; boolean done=false;
		for(double a = -90; a<=90; a+=.2){
			int x = (int)(550 + a*5);
			double y = 600 - Compound.calc(a)*500;
						
			if(y!=0){ if(prevy!=0) gBuf.drawLine(x,(int)y,prevx,(int)prevy); prevy=y;}
			prevx=x;
		}
		//                                                                                                                 112.5      24.0
		double[] angs = {90, 112, 124.5, 40, 81, 52, 70.5, 77, 25, 30.5, 27, 33, 44.5, 57, 64, 86, 95, 105, 118, 58, 83.5, 111.5, 48, 26, 109};
		//
		double[] res = {.382, .175, .064, .771, .462, .67, .54, .492, .984, .875, 937, .848, .719, .633, .586, .413, .333, .245, .125, .639, .445, .180, .694, .954, .197};
		for (int i=0;i<25;i++){ res[i] = res[i]/1.005;}
		System.out.println(res[0]);

		for(int i=0; i<angs.length; i++){
			int x = (int)(550 + (90-angs[i])*5);
			int y = (int)(600 - (res[i])*500);
			gBuf.fillRect(x-1,y-1,3,3);
		}
		
		done=false;
		g.drawImage(buf, 0, 0, this);						//draw Buffered image onto MyPanelb
		
		
		for(double a = 90; a>=-90; a-=.5){
			double ys = ((int)(Compound.calc(a)*1000))/1000.0;
			System.out.println((int)((90-a)*10)/10.0 + "\t" + ys + "\t" + (int)((1/ys)*1000)/1000.0);
		}
		
	}

	public void actionPerformed(ActionEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){
		gBuf.setColor(Color.BLACK);
		ang=(e.getX()-550)/5.0;
		rat =(600-e.getY())/500.0;
		repaint();	
	}
	public void mouseClicked(MouseEvent e){} 
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseMoved(MouseEvent e){}
}
	
