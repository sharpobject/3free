import java.io.*;
import java.util.*;

public class CountLines
{
	static long[] reverse;
	public static void main(String[] fhqwgads) throws Throwable
	{
		BufferedReader in=new BufferedReader(new InputStreamReader(new
				FileInputStream("f:\\onesortedfile\\sortedfile.txt")));
		int nLines=0;
		while(in.readLine()!=null)
		{
			nLines++;
			if(nLines%10000==0)
				System.out.println(nLines);
		}
		System.out.println("FFFFF "+nLines+" FFFFF");
	}
}