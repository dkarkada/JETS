package sciword2014;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;


public class SciwordosoTeam{
	public static void main(String [] args) throws FileNotFoundException{
		File sciword = new File("resources/sciwordosotxt.txt");
		Scanner scan = new Scanner(sciword);
				
		ArrayList<String> words = new ArrayList<String>();
		String line; boolean done=false;
		while(scan.hasNextLine()){
			line = scan.nextLine();
			String[] THING = line.split("\t");
			words.add(THING[0]);
		}
		
		SciwordScreen mg = new SciwordScreen(words);
		
		JFrame j = new JFrame();  //JFrame is the window; window is a depricated class
		j.setSize(mg.getSize());
		j.add(mg);
		j.setVisible(true);
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
	}

}

class SciwordScreen extends JPanel implements ActionListener, KeyListener{
	ArrayList<String> words, mistakes;
	javax.swing.Timer update;
	Timer time;
	int correct, wrong, ind, wordnum;
	Boolean started, competition, finished;
	String word;
	
	public SciwordScreen(ArrayList<String> w){
		setSize(1100, 900);
		setVisible(true);
		words=w;
		mistakes = new ArrayList<String>();
		update = new javax.swing.Timer(100, this);
		update.start();
		time = new Timer();
		correct = wrong = ind = wordnum = 0;
		word = "Spacebar-Competition enter-Endless";
		started =false;
		finished=false;
		
		addKeyListener(this);
		setFocusable(true);
	}
	public void paintComponent(Graphics g){
		g.setFont(new Font("Arial", Font.PLAIN, 48));
		g.setColor(Color.WHITE);
		g.fillRect(0,0,1100,900);
		g.setColor(Color.BLACK);
		int x=200, y=400;
		for(String s: word.split(" ")){
			g.drawString(s,x, y);
			y+=100;
		}
	}
	public void keyPressed(KeyEvent e) {
	}
	public void keyReleased(KeyEvent e) {
		if(!started){
			if(e.getKeyCode()==e.VK_SPACE) competition=true;
			else if(e.getKeyCode()==e.VK_ENTER) competition=false;
			if(competition!=null) start();
		}
		else if(e.getKeyCode()==e.VK_W || e.getKeyCode()==e.VK_UP){
			correct++; again(wordnum++);
		}
		else if(e.getKeyCode()==e.VK_S || e.getKeyCode()==e.VK_DOWN){
			wrong++; mistakes.add(words.get(ind)); again(wordnum++); 
		}
	}
	public void again(int i){
		words.remove(ind);
		ind = (int)(Math.random()*words.size());
		if (!competition){
			word = words.get(ind);
		}
		else if(i<40 && !finished) {
			word = words.get(ind);
		}
		else{
			word = "Done";
			System.out.println(correct);
			System.out.println(mistakes);
		}
	}
	public void start(){
		started=true;
		if(competition){
			time.schedule(new TimerTask(){
				public void run(){
					finished=true;
				}
			}, ((long)240000));
		}
		again(wordnum++);
	}
	public void keyTyped(KeyEvent e) {
	}
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	
	
	
	
}
