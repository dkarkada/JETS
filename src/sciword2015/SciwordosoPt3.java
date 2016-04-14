package sciword2015;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;

public class SciwordosoPt3{
	public static void main(String[] args) throws Exception{
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.PLAIN, 24))); 
		
		JFrame j = new JFrame();
		j.setTitle("Sciwordoso Pt 3");
		ImageIcon img = new ImageIcon("resources/saxophone.png");
		j.setIconImage(img.getImage());
		Sciwordoso3Panel p = new Sciwordoso3Panel();
		j.setSize(p.getPreferredSize());
		j.add(p);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		j.setLocation(dim.width/2-j.getSize().width/2, dim.height/2-j.getSize().height*2/3);
		j.pack();
		p.input.requestFocusInWindow();
		j.setVisible(true);
		
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class Sciwordoso3Panel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	boolean paused = false, done = false;
	int origtime = 0, time = 0, stage = 0, gameDiff = 1;
	Entry ent = null;
	WeightedRandom wRnd;
	ArrayList<Entry> entries = new ArrayList<Entry>();
	ArrayList<Entry> quizFirst = new ArrayList<Entry>();
	ArrayList<Entry> right = new ArrayList<Entry>();
	ArrayList<Entry> wrong = new ArrayList<Entry>();
	ArrayList<Entry> skipped = new ArrayList<Entry>();
	ArrayList<String> insults = new ArrayList<String>();
	Timer refresher;
	JTextField input = new JTextField(25);
	JLabel scoreLabel = new JLabel("0 / 0 / 0 / 0"),
			promptLabel = new JLabel("Enter time (min):");
	JButton timeButton = new JButton("Set difficulty");
	JSlider diffSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 1);
	
	public Sciwordoso3Panel(){		
		setBackground(new Color(230,240,255));
		GridBagLayout gridbag = new GridBagLayout();
		setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		
		scoreLabel.setFont(new Font("Arial",Font.PLAIN,36));
		scoreLabel.setHorizontalAlignment(JLabel.CENTER);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0,0,0,0);
		gridbag.setConstraints(scoreLabel, c);
		add(scoreLabel);
		
		timeButton.setFont(new Font("Arial",Font.PLAIN,72));
		timeButton.setHorizontalAlignment(JLabel.CENTER);
		timeButton.setMaximumSize(new Dimension(200,100));
		timeButton.addActionListener(this);
		c.insets = new Insets(100,0,0,0);
		gridbag.setConstraints(timeButton, c);
		add(timeButton);
		
		diffSlider.setMajorTickSpacing(1);
		diffSlider.setPaintTicks(true);
		diffSlider.createStandardLabels(1);
		diffSlider.setPaintLabels(true);
		c.insets = new Insets(40,0,0,0);
		gridbag.setConstraints(diffSlider,c);
		add(diffSlider);
		
		promptLabel.setFont(new Font("Arial",Font.PLAIN,48));
		promptLabel.setHorizontalAlignment(JLabel.CENTER);
		c.insets = new Insets(75,0,0,0);
		gridbag.setConstraints(promptLabel, c);
		add(promptLabel);
			
		input.addActionListener(this);
		input.setFont(new Font("Arial",Font.PLAIN,48));
		input.setHorizontalAlignment(JTextField.CENTER);
		c.insets = new Insets(40,0,0,0);
		gridbag.setConstraints(input, c);
		add(input);

		setVisible(true);
				
		refresher = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(!paused){
					time--;
					timeButton.setText(String.format("%01d:%02d", time/60, time%60));	
					if(time<0){
						end();
					}
				}
			}
		});

		setInsults();
		getWords(new File("resources/sciword3.txt"), entries);
		getWords(new File("resources/missed.txt"), quizFirst);
		Collections.shuffle(entries);
		Collections.shuffle(quizFirst);
	}
	public Dimension getPreferredSize(){
		return new Dimension(1200,600);
	}
	private void setInsults(){
		insults.add("Holy crap u SUCK.");
		insults.add("stop being bad.");
		insults.add("u r actually trash.");
		insults.add("kys immediately thx!");
		insults.add("fenk yaself,,,");
		insults.add("wow, wrong!!!");
		insults.add("100% GARBAGE,");
		insults.add("stop being wrong thx,");
		insults.add("wow haha die!");
		insults.add("incORRECt!");
		insults.add("get ur crap together pls thx!");
		insults.add("CAN U DO SOME WORK!?");
		insults.add("Wrong! what a surprise.");
		insults.add("ur a special breed of trash.");
		insults.add("lOl wrong haha,,,");
		insults.add("i LITERALLY hate u.");
		insults.add("you are, unsurprisingly, wrong!");
	}
	private void getWords(File f, ArrayList<Entry> list){
		try{
			Scanner scan  = new Scanner(f);
			
			while(scan.hasNextLine()){
				String id = scan.nextLine();
				String[] line = id.split("\t");
				if(line.length>2){
					int d = Integer.parseInt(line[0]);
					int type = Integer.parseInt(line[1]);
					String w1 = line[2].trim();
					String  w2 = line[3].trim();
					switch(type){
					case 1:
						list.add(new Entry(w1,w2,null,null,d,id));
						break;
					case 2:
						list.add(new Entry(null,w1,w2,null,d,id));
						break;
					case 3:
						list.add(new Entry(null,w1,w2,line[4].trim(),d,id));
					}
				}
			}			
			scan.close();
		}catch(FileNotFoundException e) {e.printStackTrace();}		
	}
	private void generate(){
		int c = right.size();
		int m = wrong.size();
		int s = skipped.size();
		scoreLabel.setText(c + " / " + m + " / " + s + " / " + (s+m+c));
		
		if(entries.isEmpty()) end();
		else{
			ent = null;
			if(! quizFirst.isEmpty())
				ent = quizFirst.remove(0);
			while(ent==null){
				int ind=0;
				int d = wRnd.getValue();
				while(ent==null && ind<entries.size()){
					if(entries.get(ind).difficulty == d)
						ent = entries.remove(ind);
					else ind++;
				}
				if(ent==null) wRnd.remove(d);
			}
			promptLabel.setText(ent.getPrompt());
		}
	}
	private void end(){
		refresher.stop();
		timeButton.setText("Restart");
		stage = 3;
		String msg = scoreLabel.getText() + "\n" +
				new SimpleDateFormat("MM/dd \t hh:mm a").format(Calendar.getInstance().getTime()) + "\n" +
				origtime/60 + " mins";
		JOptionPane.showMessageDialog(null, msg, "Report", JOptionPane.INFORMATION_MESSAGE);
		
		try {
			FileWriter fw = new FileWriter("resources/missed.txt");
			for(Entry m : wrong){
				fw.write(m.ID + System.lineSeparator());
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void restart(){
		stage=0;
		ent=null;
		int[] v = {1,2,3,4,5};
		int[][] mat = {{5,4,3,2,1}, {3,6,3,2,1}, {2,3,6,3,1}, {1,2,4,5,3}, {1,2,3,4,5}};
		int[] p = mat[gameDiff-1];
		wRnd = new WeightedRandom(v,p);
		entries.clear();
		quizFirst.clear();
		right.clear();
		wrong.clear();
		getWords(new File("resources/sciword3.txt"), entries);
		getWords(new File("resources/missed.txt"), quizFirst);
		Collections.shuffle(entries);
		Collections.shuffle(quizFirst);
		diffSlider.setVisible(true);
		promptLabel.setText("Enter time (min):");
		timeButton.setText("Time not set");
		scoreLabel.setText("0 / 0 / 0 / 0");
		input.setText("");
	}

	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(input)){
			String in = input.getText();
			input.setText("");
			if(stage==0){
				try{
					origtime = time = 60 * Integer.parseInt(in);
					stage = 1;
					timeButton.setText("Start");
					promptLabel.setText("Press Start to begin");
					timeButton.requestFocusInWindow();
				} catch(Exception ex){}
			}
			else if (stage==2 && in.length()>0){
				if(ent.check(in)){
					ent.nextClue();
					if(! wrong.contains(ent))
						right.add(ent);
					if(! ent.isEmpty())
						entries.add(ent);
					generate();
				}
				else{
					wrong.add(ent);
					String msg = insults.get((int)(Math.random()*insults.size()))
							+ " " + ent.getSolution(ent.getPrompt());
					promptLabel.setText(msg);
				}
			}
		}
		else if(e.getSource().equals(timeButton)){
			if(stage==1){
				timeButton.setText(String.format("%01d:%02d", time/60, time%60));	
				timeButton.setBackground(new JButton().getBackground());
				refresher.start();
				gameDiff = diffSlider.getValue();
				diffSlider.setVisible(false);
				int[] v = {1,2,3,4,5};
				int[][] mat = {{5,4,3,2,1}, {3,6,3,2,1}, {2,3,6,3,1}, {1,2,4,5,3}, {1,2,3,4,5}};
				int[] p = mat[gameDiff-1];
				wRnd = new WeightedRandom(v,p);
				generate();
				stage = 2;
			}
			else if(stage==2){
				if(!paused){
					paused=true;
					promptLabel.setText("Paused");
					timeButton.setText(String.format("%01d:%02d", time/60, time%60)+ " remaining");
				}
				else{
					paused=false;
					timeButton.setText(String.format("%01d:%02d", time/60, time%60));	
					timeButton.setBackground(new JButton().getBackground());
					skipped.add(ent);
					generate();
				}
			}
			else if(stage==3){
				restart();
			}
		}
	}
	
	class Entry{
		ArrayList<String> words = new ArrayList<String>(3);
		ArrayList<String> clues = new ArrayList<String>(3);
		String name = "", ID;
		int difficulty;
		
		public Entry(String c, String w1, String w2, String w3, int d, String id){
			ID=id;
			if(c !=null){
				clues.add(c);
				name+= c + " - ";
			}
			if(w1!=null){
				words.add(w1);
				name+= w1 + " - ";
			}
			if(w2!=null){
				words.add(w2);
				name+= w2 + " - ";
			}
			if(w3!=null){
				words.add(w3);
				name+= w3 + " - ";
			}
			name = name.substring(0, name.length()-3);
			Collections.shuffle(words);
			if(clues.isEmpty()){
				for(String w: words)
					clues.add(w);
			}
			difficulty = d;
		}
		public String getPrompt(){
			return clues.get(0);
		}
		public void nextClue(){
			clues.remove(0);
		}
		public boolean check(String in){
			in = in.trim();
			String key = getSolution(getPrompt());
			int errorThreshold = key.length() / 5;
			if (errorThreshold<2) errorThreshold=2;
			if(eDist(process(in),process(key)) <= errorThreshold) return true;
			
			String[] keySplit = key.split(" ");
			if(keySplit.length==1) return false;
			String[] inSplit = in.split(" ");
			if(inSplit.length>2){
				String concat="";
				String last= inSplit[inSplit.length-1];
				for(int i=0; i<inSplit.length -1; i++)
					concat+= inSplit[i];
				inSplit = new String[2];
				inSplit[0] = concat;
				inSplit[1] = last;
				
			}
			in = "";
			for(int i=0; i<inSplit.length; i++)
				in+= process(inSplit[i]) + " ";
			in.trim();
			
			String[] keyPerms = new String[keySplit.length];
			keyPerms[0] = process(keySplit[0]) + " " + process(keySplit[1]);
			keyPerms[1] = process(keySplit[1]) + " " + process(keySplit[0]);
			return eDist(in,keyPerms[0]) <= errorThreshold || 
					eDist(in,keyPerms[1]) <= errorThreshold;
		}
		public String getSolution(String s){
			String out = "";
			for(String w: words){
				if(! w.equals(s))
					out+= w + " ";
			}
			return out.trim();
		}
		public String process(String s){
			s = s.toLowerCase();
			while(s.length()>=1 && (s.charAt(s.length()-1)=='\\' || s.charAt(s.length()-1)=='s'))
				s = s.substring(0,s.length()-1);
			if (s.length()>=3 && s.substring(s.length()-3).equals("ing"))
				s = s.substring(0,s.length()-3);
			return s;
		}
		public int eDist(String s1, String s2){
			if(s1.length()==0 || s2.length()==0) return Math.max(s1.length(), s2.length());
			int[][] mat = new int[s1.length()+1][s2.length()+1];
			mat[s1.length()-1][s2.length()-1] =
					s1.charAt(s1.length()-1) == s2.charAt(s2.length()-1) ? 0:1;
			for(int i = s1.length()-1; i>=0; i--)
				mat[i][s2.length()] = s1.length()-i;
			for(int j = s2.length()-1; j>=0; j--)
				mat[s1.length()][j] = s2.length()-j;
			for(int i=s1.length()-1; i>=0; i--){
				for(int j=s2.length()-1; j>=0; j--){
					char c1 = s1.charAt(i);
					char c2 = s2.charAt(j);
					if(c1==c2){
						mat[i][j] = mat[i+1][j+1];
					}
					else{
						mat[i][j] = 1 + Math.min(mat[i][j+1], 
								Math.min(mat[i+1][j], mat[i+1][j+1]));
					}
				}
			}
			return mat[0][0];
		}
		public boolean isEmpty(){
			return clues.isEmpty();
		}
		public String toString(){
			return name;
		}
	}

}

class WeightedRandom{
	ArrayList<Integer> values = new ArrayList<Integer>();
	ArrayList<Integer> relativeProbabilities = new ArrayList<Integer>();
	int total=0;
	ArrayList<Double> probMarkers = new ArrayList<Double>();
	
	public WeightedRandom(){}
	public WeightedRandom(int[] v, int[] p){
		if(v.length == p.length){
			for(int val : v)
				values.add(val);
			for(int prob : p){
				relativeProbabilities.add(prob);
				total += prob;
			}
		}
		calcProbs();
	}
	public void add(int v, int p){
		values.add(v);
		relativeProbabilities.add(p);
		total += p;
		calcProbs();
	}
	public void remove(int v){
		int ind = values.indexOf(v);
		if(ind!=-1){
			values.remove(ind);
			total -= relativeProbabilities.remove(ind);
		}
		calcProbs();
	}
	private void calcProbs(){
		probMarkers.clear();
		if(values.isEmpty()) return;
		probMarkers.add(0.0);
		for(int ind=0; ind<values.size()-1; ind++){
			double prob = ((double) relativeProbabilities.get(ind)) / total;
			probMarkers.add(probMarkers.get(ind) + prob);
		}
	}
	public int getValue(){
		if(values.isEmpty()) return Integer.MIN_VALUE;
		double rnd = Math.random();
		int ind = values.size()-1;
		while(rnd < probMarkers.get(ind))
			ind--;
		return values.get(ind);
	}
}



