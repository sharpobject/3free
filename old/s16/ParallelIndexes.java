import java.io.*;
import java.util.*;

public class ParallelIndexes
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
		int[] good=new int[goodones.size()];
		for(int i=0;i<good.length;i++)
			good[i]=goodones.get(i);
			
		BufferedReader[] in=new BufferedReader[good.length];
		for(int i=0;i<good.length;i++)
		{
			in[i]=new BufferedReader(new InputStreamReader(new
				FileInputStream("F:\\sorted\\"+i+".txt")));
			System.out.println(i);
		}
		BufferedReader union=new BufferedReader(new InputStreamReader(new
				FileInputStream("F:\\onesortedfile\\sortedfile.txt")));
		PrintWriter[] out=new PrintWriter[good.length];
		for(int i=0;i<good.length;i++)
		{
			out[i]=new PrintWriter(new BufferedOutputStream(new FileOutputStream
				("F:\\sortedindices\\"+i+".txt")));
			System.out.println(i);
		}
		String[] arrays=new String[good.length];
		for(int i=0;i<good.length;i++)
			arrays[i]=in[i].readLine();
		int nextReport=10000;
		long start=System.currentTimeMillis();
		int linesRead=0;
		System.out.println("It begins.");
		String s;
		int index=0;
		while((s=union.readLine())!=null)
		{
			for(int i=0;i<good.length;i++)
				if(arrays[i]!=null&&s.equals(arrays[i]))
				{
					out[i].println(index);
					arrays[i]=in[i].readLine();
				}
			linesRead++;
			if(linesRead>nextReport)
			{
				System.out.println("Lines read so far: "+linesRead);
				System.out.println("Total time: "+(System.currentTimeMillis()
					-start)/60000.0+" minutes.");
				System.out.println();
				nextReport+=10000;
			}
			index++;
		}
		for(int i=0;i<good.length;i++)
		{
			System.out.println("Closing #"+i);
			out[i].close();
		}
	}
	static class tehArray implements Comparable
	{
		public tehArray(String s)
		{
			tehstring=s;
			String[] sa=s.split(" ");
			data=new char[sa.length];
			for(int i=0;i<sa.length;i++)
				data[i]=(char)Integer.parseInt(sa[i]);
		}
		char[] data;
		public String tehstring;
		public boolean equals(Object o)
		{
			if(o==null)
				return false;
			return Arrays.equals(data,((tehArray)o).data);
		}
		public int compareTo(Object o)
		{
			char[] other=((tehArray)o).data;
			int lim=Math.min(other.length,data.length);
			for(int i=0;i<lim;i++)
				if(data[i]<other[i])
					return -1;
				else if(data[i]>other[i])
					return 1;
			return data.length-other.length;
		}
		public String toString()
		{
			return tehstring;
		}
	}
}