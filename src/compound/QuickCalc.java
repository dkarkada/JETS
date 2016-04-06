package compound;
import java.util.*;

public class QuickCalc {
	public static void main(String[]args){
		Scanner scan= new Scanner(System.in);
		
		int[] angs = {127,130,153,141,124, 213, 122, 235, 239, 225, 189, 233, 240};//{-28, 0, 35, -28, 17, 36, 45, 51, -33, 40, 50, 28, -20, 50, 57, 53, 42, 31};
		double[] res = {.232, .268, .43, .366, .195, .585, .156, .844, .985, .681, .504, .809, .996};//{.436, .498, .619, .435, .532, .629, .726, .823, .412, .660, .794, .574, .461, .826, 1.032, .885, .681, .592};
		for(int i=0; i<angs.length; i++){
	//		double calcd =  format(Compound.calc(angs[i]));
	//		System.out.println(angs[i] + "\t" + calcd + "\t" + format(calcd-res[i]));
		}
		
		while(1<2){
			System.out.print("m1\t");
			double m =scan.nextDouble();
			System.out.print("m2\t");
			double n =scan.nextDouble();
			System.out.print("ang\t");
			int i = scan.nextInt();
			System.out.println(format(Compound.calc(i)) + "\t" + format(m/n));
			System.out.println();
		}
	}
	public static double format(double d){
		return ((int)((d)*1000))/1000.0;
	}
}
