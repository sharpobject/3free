import java.io.*;
import java.util.*;

public class MergeSortedFiles
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
			in[i]=new BufferedReader(new InputStreamReader(new
				FileInputStream("F:\\sorted\\"+i+".txt")));
		PrintWriter out=new PrintWriter("F:\\onesortedfile\\sortedfile.txt");
		tehArray[] arrays=new tehArray[good.length];
		for(int i=0;i<good.length;i++)
			arrays[i]=new tehArray(in[i].readLine());
		int nextReport=100000;
		long start=System.currentTimeMillis();
		int linesRead=good.length;
		System.out.println("It begins.");
		while(true)
		{
			tehArray min=null;
			for(int i=0;i<good.length;i++)
			{
				if(arrays[i]==null)
					continue;
				if(min==null)
				{
					min=arrays[i];
					continue;
				}
				if(min.compareTo(arrays[i])>0)
					min=arrays[i];
			}
			if(min==null)
			{
				for(int i=0;i<good.length;i++)
					in[i].close();
				out.close();
				System.out.println("All done.");
				return;
			}
			out.println(min);
			for(int i=0;i<good.length;i++)
			{
				if(min.equals(arrays[i]))
				{
					String s=in[i].readLine();
					if(s==null)
						arrays[i]=null;
					else
					{
						arrays[i]=new tehArray(s);
						linesRead++;
					}
				}
			}
			if(linesRead>nextReport)
			{
				System.out.println("Lines read so far: "+linesRead);
				System.out.println("Total time: "+(System.currentTimeMillis()
					-start)/60000.0+" minutes.");
				System.out.println();
				if(nextReport==100000)
					nextReport=1000000;
				else
					nextReport+=1000000;
			}
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