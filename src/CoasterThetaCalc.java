import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class CoasterThetaCalc {
	public static void main(String args[]){
		JFrame j = new JFrame();
		CTCPanel p = new CTCPanel();
		j.setSize(p.getPreferredSize());
		j.add(p);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		j.setLocation(dim.width/2-j.getSize().width/2, dim.height/2-j.getSize().height/2-36);
		j.pack();
		j.setVisible(true);
		
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class CTCPanel extends JPanel{
	int height=900, width=1600;
	int initx=-5,
			endx=100,
			inity=300,
			endy=-100;
	
	public CTCPanel(){
		
	}
	public Dimension getPreferredSize(){
		return new Dimension(width, height);
	}
	public void paintComponent(Graphics g){
		g.drawLine(convertX(0), convertY(endy),
				convertX(0), convertY(inity));
		g.drawLine(convertX(initx), convertY(0),
				convertX(endx), convertY(0));
		for(double x=0; x<90; x+=.1){
			//g.fillRect(convertX(x), convertY(calculate(x)), 2, 2);
		}
		double max = 0;
		String params="";
		for(double a=90; a>63; a--){
			for(double b=a; b>45; b--){
				for(double c=b; c>45; c--){
					for(double d=c; d>45; d--){
						for(double e=d; e>=45; e--){
							double result = calculate(a,b,c,d,e);
							if(result>max){
								max=result;
								params = a+" "+b+" "+c+" "+d+" "+e;
							}
						}
					}
				}
			}
		}
		System.out.println(max);
		System.out.println(params);
	}
	public double calculate(double a, double b, double c, double d, double e){
		double h=100;
		double dx1,dx2,dx3,dx4,dx5;
		dx1 = dx(h,a);
		h=reduceH(h,a);
		dx2 = dx(h,b);
		h=reduceH(h,b);
		dx3 = dx(h,c);
		h=reduceH(h,c);
		dx4 = dx(h,d);
		h=reduceH(h,d);
		dx5 = dx(h,e);
		return dx1 + dx2 + dx3 + dx4 + dx5;
	}
	public double calculate(double a){
		double h=100;
		double b,  c,  d,  e;
		b=c=d=e=45;
		double dx1,dx2,dx3,dx4,dx5;
		dx1 = dx(h,a);
		h=reduceH(h,a);
		dx2 = dx(h,b);
		h=reduceH(h,b);
		dx3 = dx(h,c);
		h=reduceH(h,c);
		dx4 = dx(h,d);
		h=reduceH(h,d);
		dx5 = dx(h,e);
		return dx1 + dx2 + dx3 + dx4 + dx5;
	}
	public double dx(double h, double t){
		return h * Math.sin(Math.toRadians(t)) * Math.cos(Math.toRadians(t));
	}
	public double reduceH(double h, double t){
		return h * Math.sin(Math.toRadians(t)) * Math.sin(Math.toRadians(t));
	}
	public Point convert(int x, int y){
		int fx = convertX(x);
		int fy = convertY(y);
		return new Point(fx,fy);
	}
	public int convertX(double x){
		return (int) (width * (x-initx)/(endx-initx));
	}
	public int convertY(double y){
		return (int) (height * (y-inity)/(endy-inity));
	}
}