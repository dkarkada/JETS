package sciword2015;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Extractor {
	public static void main(String[] args){
		
		Scanner in, in2;
		try {
			in = new Scanner(new File("resources/alldouble.txt"));
			in2 = new Scanner(new File("resources/asdasd.txt"));
		} catch (FileNotFoundException e) {
			in = new Scanner(System.in);
			in2 = new Scanner(System.in);
			System.out.println(e);
		}
		xcheck(in,in2);
	}
	public static void test1(Scanner in){
		
		int count=0;
		while(in.hasNext()){
			String word = in.nextLine();
			if(count%4==0){
				if(word.charAt(word.length()-1)==' ')
					word = word.substring(0,word.length()-1);
				if(word.charAt(word.length()-1)=='–' || word.charAt(word.length()-1)=='-')
					word = word.substring(0,word.length()-1);
				else
					word="WTF";
				if(word.charAt(word.length()-1)==' ')
					word = word.substring(0,word.length()-1);
				if(word.split(" ").length<3)
					System.out.println(word);
				
			}
			count++;
		}
	}
	public static void test2(Scanner in){
		while(in.hasNext()){
			String[] thing = in.nextLine().split(" - ");
			if(thing.length==1){
				thing = thing[0].split(" – ");
			}
			if(thing.length>1){
				String outPt1 = thing[0];
				String[] xd = outPt1.split("\\(");
				String xdOut = xd[0];
				if(xdOut.charAt(xdOut.length()-1)==' ')
					xdOut = xdOut.substring(0, xdOut.length()-1);
				String[] word = xdOut.split(" ");
				if(word.length<3)
					System.out.println(xdOut);
			}
		}
	}
	public static void test3(Scanner in){
		int i=0;
		while(in.hasNext()){
			if(i%2==0){
				String word = in.nextLine();
				if(word.length()>1){
					if(word.charAt(word.length()-1)==' ')
						word = word.substring(0,word.length()-1);
					if(word.split(" ").length<3)
						System.out.println(word);
				}
			}
			i++;
		}
	}
	public static void test4(Scanner in){
		while(in.hasNext()){
				String word = in.nextLine();
				if(word.length()>1){
					if(word.split(":").length>1)
						word = word.split(":")[1];
					String[] things = word.split(",");
					for(String term: things){
						if(term.charAt(term.length()-1)==' ')
							term = term.substring(0,term.length()-1);
						if(term.charAt(0)==' ')
							term = term.substring(1);
						if(term.split(" ").length<3)
							System.out.println(term);
					}
				}
		}
	}
	public static void clean(Scanner in){
		TreeSet<String> words = new TreeSet<String>();
		while(in.hasNext()){
			words.add(in.nextLine().toLowerCase());
		}
		for(Iterator<String> it=words.iterator(); it.hasNext();)
			System.out.println(it.next());
	}
	public static void separate(Scanner in){
		ArrayList<String> oneword = new ArrayList<String>();
		while(in.hasNext()){
			String term = in.nextLine();
			if(term.split(" ").length ==1)
				oneword.add(term);
			else
				System.out.println(term);
		}
		System.out.println("-------------------------");
		for(String s:oneword)
			System.out.println(s);
	}
	public static void countFreq(Scanner in){
		ArrayList<String> terms = new ArrayList<String>();
		TreeMap<String,Integer> words = new TreeMap<String,Integer>();
		while(in.hasNext()){
			String term = in.nextLine();
			terms.add(term);
			String[] split = term.split(" ");
			String w1 = split[0];
			String w2 = split[1];
			if(words.containsKey(w1))
				words.put(w1, words.get(w1)+1);
			else
				words.put(w1, 1);
			if(words.containsKey(w2))
				words.put(w2, words.get(w2)+1);
			else
				words.put(w2, 1);
		}
		TreeSet<String> ultrawords = new TreeSet<String>();
		for(String t : terms){
			String[] split = t.split(" ");
			String w1 = split[0];
			String w2 = split[1];
			double freq = (words.get(w1) + words.get(w2))/2.0;
			if(!(words.get(w2)==1 || words.get(w1)==1)){
				ultrawords.add(w1);
				ultrawords.add(w2);
			}
		}
		for(String s: ultrawords)
			System.out.println(s + " : " +words.get(s));
		for(String s: words.keySet());
			//System.out.println(s + " : " +words.get(s));
		
	}
	public static void combine(Scanner in){
		TreeSet<String> words = new TreeSet<String>();
		ArrayList<String> add = new ArrayList<String>();
		boolean nextpart=false;
		while(in.hasNext()){
			String nxtln = in.nextLine();
			if(nxtln.charAt(0)=='-')
				nextpart=true;
			else if(!nextpart){
				String[] line = nxtln.split(" ");
				add.add(line[0]);
			}
			else
				words.add(nxtln.toLowerCase());
		}
		for(String s:add){
			words.add(s);
		}
		for(String s:words)
			System.out.println(s);
	}
	public static void xcheck(Scanner in, Scanner in2){
		ArrayList<String> full = new ArrayList<String>();
		ArrayList<String> cur = new ArrayList<String>();
		ArrayList<String> diff = new ArrayList<String>();
		
		while(in.hasNextLine()) full.add(in.nextLine());
		while(in2.hasNextLine()) cur.add(in2.nextLine());
		for(String s: full)
			if(!cur.contains(s))
				diff.add(s);
		for(String s: diff)
			System.out.println(s);
	}
}
