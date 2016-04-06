package sciword2014;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class Sciwordoso {
	static boolean isRunning=true;
	public static void main(String[] args) throws FileNotFoundException{
		File sciword = new File("resources/sciwordosotxt.txt");
		Scanner scan = new Scanner(sciword);
		
		ArrayList<String> wordsOrig = new ArrayList<String>();
		ArrayList<String> cluesOrig = new ArrayList<String>();
		
		String line; boolean done=false;
		while(scan.hasNextLine()){
			line = scan.nextLine();
			String[] THING = line.split("\t");
			wordsOrig.add(THING[0]);
			cluesOrig.add(THING[1]);
		}
		
		String[] errors = getErrors();
		Scanner input = new Scanner(System.in);
		Timer clock = new Timer();
		while(1<2){
			System.out.print("Time limit:\t");
			int time=0;
			while(!done){											//Input time
				String timeInp = input.next();
				try{
					time = Integer.parseInt(timeInp);
					done=true;
				}
				catch(Exception e){System.out.print("Try again");}
			}
			done=false;
			
			int gameType=0;
			System.out.println("Enter 1 to prompt with clues. Enter 2 to prompt with words.\t");
			while(!done){										 									//Input game type
				String inp = input.next();
				try{
					gameType = Integer.parseInt(inp);
					if(gameType==1 || gameType==2) done=true;
				}
				catch(Exception e){System.out.print("Try again");}
			}
			done=false;
			
			System.out.print("Enter 'a' to start");
			input.next();
			
			if(time!=0){
				clock.schedule(new TimerTask(){
					public void run(){
						isRunning=false;
					}
				}, ((long)time*1000));
			}
			int correct=0, wrong=0, skip = 0, ind=-1;
			ArrayList<String> words = (ArrayList<String>) wordsOrig.clone();
			ArrayList<String> clues = (ArrayList<String>) cluesOrig.clone();
			
			input.nextLine();
			while(isRunning){
				if(gameType==1){
					ind = (int)(Math.random()*words.size());
					System.out.println(clues.get(ind));
					String inp = input.nextLine();
					String key = words.get(ind);
					if(!equal(inp, key)){
						int rnd = (int)(Math.random()*errors.length);
						System.out.println(errors[rnd] + words.get(ind) + ". retype or die.");
						while(!equal(input.nextLine(), words.get(ind))){}
						wrong++;
					}
					else{ correct++; words.remove(ind); clues.remove(ind);}
					if((correct+wrong+skip)%10==0) System.out.println("Correct: " + correct + "    Wrong: " + wrong + "    Skip: " + skip);
				}
				if(gameType==2){
					ind = (int)(Math.random()*words.size());
					System.out.println(words.get(ind));
					String inp = input.nextLine();
					String key = clues.get(ind);
					if(!equal(inp, key)){
						int rnd = (int)(Math.random()*errors.length);
						System.out.println(errors[rnd] + clues.get(ind) + ". retype or die.");
						while(!equal(input.nextLine(), clues.get(ind))){}
						wrong++;
					}
					else{ correct++; words.remove(ind); clues.remove(ind);}
					if((correct+wrong+skip)%10==0) System.out.println("Correct: " + correct + "    Wrong: " + wrong + "    Skip: " + skip);
				}
			}
			isRunning=true;
			System.out.println("Correct: " + correct + "    Wrong: " + wrong + "    Skip: " + skip);

			System.out.println();
			clock.purge();
		}
	}
	public static boolean equal(String in, String key){
		in = process(in); key = process(key);
		if (in.equals(key)) return true;
		String[] inwords = in.split(" ");
		String[] keywords = key.split(" ");
		String in1="", in2="", key1="";
		for(int i=0; i<inwords.length; i++){
			inwords[i] = process(inwords[i]);
			in1 += inwords[i];
		}
		for(int i=inwords.length-1; i>=0; i--){
			in2 += inwords[i];
		}
		for(int i=0; i<keywords.length; i++){
			keywords[i] = process(keywords[i]);
			key1 += keywords[i];
		}
		return (in1.equals(key1) || in2.equals(key1));
	}
	public static String process(String s){
		if (s.equals("")) return "";
		s=s.toLowerCase();
		while(s.length()>=1 && s.charAt(s.length()-1)==' ')
			s = s.substring(0,s.length()-1);
		if (s.equals("")) return "";
		if (s.charAt(s.length()-1)=='\\')
			s = s.substring(0,s.length()-1);
		if (s.equals("")) return "";
		if (s.length()>=3 && s.substring(s.length()-3).equals("ing"))
			s = s.substring(0,s.length()-3);
		if (s.equals("")) return "";
		if (s.charAt(s.length()-1)=='s')
			s = s.substring(0,s.length()-1);
		return s;
	}
	public static String[] getErrors(){
		String[] s = {"haha die. ", "killarte immediately. ", "wow u suck. ", "can u not be stupid. ", "la que es incorrect. ", "SAARANG U SUCK. ", "holy crap ur bad. ",
				"dejar de ser tanto stupid. ", "NO. ", "lmao wrong. ", "u ser are baaad. ", "ur a moron. ", "ur a terrible partner. ", "u suck. ", "stop being wrong please. "};
		return s;
	}

}

