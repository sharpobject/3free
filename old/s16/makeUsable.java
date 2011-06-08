import java.io.*;
import java.util.*;

public class makeUsable
{
	static long[] reverse;
	public static void main(String[] fhqwgads) throws Throwable
	{
		//Make all 3APs of size 16.
		ArrayList<Integer> ap8=new ArrayList<Integer>();
		for(int i=1;2*i+1<=16;i++)
		{
			int t=1|(1<<i)|(1<<(2*i));
			while(t<65536)
			{
				ap8.add(t);
				t<<=1;
			}
		}
		System.out.println("These are the 3-APs that exist on 0...16");
		for(int i=0;i<ap8.size();i++)
			System.out.println(Integer.toString(ap8.get(i)&65535,2));
		int[] aps=new int[ap8.size()];
		for(int i=0;i<ap8.size();i++)
			aps[i]=ap8.get(i);
		ArrayList<Integer> goodones=new ArrayList<Integer>();
outer:	for(int i=0;i<65536;i++)
		{
			for(int j=0;j<aps.length;j++)
				if((i&aps[j])==aps[j])
					continue outer;
			goodones.add(i);
		}
		Collections.sort(goodones);
		PrintWriter out=new PrintWriter("goodoneslist.txt");
		for(Integer i:goodones)
			out.println(i);
		out.close();
		System.out.println(goodones.size());
	}
	static long reverse16(long a)
	{
		return (reverse[(int)(a>>8)])|(reverse[(int)(a&0xff)]<<8);
	}
}