package sciword2014;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread;
import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.io.*;
import javax.sound.sampled.*;


public class Sciword
{
	public static void main(String [] args)  throws Exception
	{
		JFrame j = new JFrame();  //JFrame is the window; window is a depricated class
		MyPanel m = new MyPanel();
		j.setSize(800,800);
		j.add(m);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class MyPanel extends JPanel implements MouseListener, KeyListener
{
	ArrayList<String> words;
	int i=0;
	int correct=0;

	public MyPanel() throws IOException
	{
		Scanner in=new Scanner(new File("words.in"));
		words=new ArrayList<String>();
		while(in.hasNext()){
			words.add(in.nextLine());
		}
		Collections.shuffle(words);
		setFocusable(true);
		this.addKeyListener(this);
		this.addMouseListener(this);

	}

	public void paintComponent(Graphics g)
	{
		g.clearRect(0,0,800,800);
		if (i<words.size()){
			g.setFont(new Font("Calibri", Font.PLAIN, 60));
			g.drawString(words.get(i),50,300);
			g.drawString(correct+"",300,500);
		}

		repaint();
	}

	public void keyTyped(KeyEvent e)
	{

	}
	public void keyReleased(KeyEvent e){
		if (e.getKeyCode()==89){
			correct++;
			i++;
		}}
	public void keyPressed(KeyEvent e){}

	public void mouseClicked(MouseEvent e)
	{
		i++;
		repaint();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
}