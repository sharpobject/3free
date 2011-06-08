import java.io.*;
import java.util.*;

public class SortEachFile
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
		for(int i=0;i<good.length;i++)
		{
			System.out.println("Sorting #"+i);
			BufferedReader in=new BufferedReader(new InputStreamReader(new 
				FileInputStream("usable/"+good[i]+".txt")));
			String s="";
			TreeSet<tehArray> tree=new TreeSet<tehArray>();
			while((s=in.readLine())!=null)
				tree.add(new tehArray(s));
			PrintWriter out=new PrintWriter("F:\\sorted\\"+i+".txt");
			for(tehArray t:tree)
				out.println(t);
			out.close();
			in.close();
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