import java.io.*;
import java.util.*;

public class unionThemAll
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
		Collections.sort(goodones,new Comparator(){
			public int compare(Object a,Object b)
			{
				int ia=(Integer)a,ib=(Integer)b;
				int[] ones={0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3,
							3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5,
							2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3,
							3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4,
							3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6,
							6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5,
							3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4,
							4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5,
							4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5,
							5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8};
				if(ones[ia&0xff]+ones[ia>>8]>ones[ib&0xff]+ones[ib>>8])
					return -1;
				if(ones[ia&0xff]+ones[ia>>8]<ones[ib&0xff]+ones[ib>>8])
					return 1;
				if(ia>ib)
					return -1;
				if(ia<ib)
					return 1;
				return 0;
			}
		});
		long[] goodones16=new long[goodones.size()];
		for(int i=0;i<goodones16.length;i++)
			goodones16[i]=goodones.get(i);
		TreeSet<tehArray> set=new TreeSet<tehArray>();
		for(int i=0;i<goodones16.length;i++)
		{
			BufferedReader in=new BufferedReader(new InputStreamReader(
				new FileInputStream(new File("usable/"+goodones16[i]+".txt"))));
			System.out.println("Adding "+"usable/"+goodones16[i]+".txt"+" #"+
				(i+1)+" of "+goodones16.length);
			String s;
			while(!((s=in.readLine())==null))
				set.add(new tehArray(s));
		}
		PrintWriter out=new PrintWriter("allarrays.txt");
		for(tehArray t:set)
			out.println(t);
	}
	static class tehArray implements Comparable
	{
		public tehArray(char[] qq)
		{
			data=qq;
		}
		public tehArray(String s)
		{
			String[] sa=s.split(" ");
			data=new char[sa.length];
			for(int i=0;i<sa.length;i++)
				data[i]=(char)Integer.parseInt(sa[i]);
		}
		char[] data;
		public int hashCode()
		{
			int res=1337;
			for(int i=0;i<data.length;i++)
				res=33*res+data[i];
			return res;
		}
		public boolean equals(Object o)
		{
			return Arrays.equals(data,((tehArray)o).data);
		}
		public int compareTo(Object o)
		{
			char[] other=((tehArray)o).data;
			for(int i=0;i<Math.min(other.length,data.length);i++)
				if(data[i]<other[i])
					return -1;
				else if(data[i]>other[i])
					return 1;
			if(data.length<other.length)
				return -1;
			else if(data.length>other.length)
				return 1;
			else
				return 0;
		}
		public String toString()
		{
			if(data.length==0)
				return "";
			String s=(int)data[0]+"";
			for(int i=1;i<data.length;i++)
				s+=(" "+((int)data[i]));
			return s;
		}
	}
}