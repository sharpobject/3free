import java.io.*;
import java.util.*;

public class calculateSpace
{
	public static void main(String[] fhwq) throws Throwable
	{
		BufferedReader in=new BufferedReader(new InputStreamReader(new
			FileInputStream("F:\\onesortedfile\\sortedfile.txt")));
		int counter=0;
		String s="";
		long bytesRead=0;
		long nextReport=10000000;
		while((s=in.readLine())!=null)
		{
			bytesRead+=2+s.length();
			counter+=s.split(" ").length;
			if(bytesRead>nextReport)
			{
				System.out.println(nextReport/1000000+" MB read.");
				nextReport+=10000000;
			}
		}
		System.out.println("The amount of 16-bit integers in the data is: "+
			counter+" YA RLY.");
	}
}