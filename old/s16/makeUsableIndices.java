import java.io.*;
import java.util.*;

public class makeUsableIndices
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
		//OK! now size 16 is fine and dandy, let's make the size 32 stuff....
		ArrayList<Long> ap32l=new ArrayList<Long>();
		for(long i=1;2*i+1<=32;i++)
		{
			long t=1|(1<<i)|(1<<(2*i));
			while(t<4294967296L)
			{
				ap32l.add(t);
				t<<=1;
			}
		}
		//changes end here
		long[] ap32=new long[ap32l.size()];
		for(int i=0;i<ap32l.size();i++)
			ap32[i]=ap32l.get(i);
		reverse=new long[256];
		for(int i=0;i<256;i++)
			reverse[i]=((i&1)<<7)|((i&2)<<5)|((i&4)<<3)|((i&8)<<1)|((i&16)>>1)	
				|((i&32)>>3)|((i&64)>>5)|((i&128)>>7);
		long[] goodones16SortedNormally=new long[goodones16.length];
		for(int i=0;i<goodones16.length;i++)
			goodones16SortedNormally[i]=goodones16[i];
		Arrays.sort(goodones16SortedNormally);
		for(int i=0;i<goodones16.length;i++)
		{
			System.out.println("step "+i+" of "+goodones16.length);
			ArrayList<Long> compatible=new ArrayList<Long>();
	loopK:	for(int k=0;k<goodones16.length;k++)
			{
				
				long subseq=(goodones16SortedNormally[i]<<16)|goodones16[k];
				for(int j=0;j<ap32.length;j++)
						if((subseq&ap32[j])==ap32[j])
							continue loopK;
				compatible.add(goodones16[k]);
			}
			long[] comp=new long[compatible.size()];
			for(int j=0;j<comp.length;j++)
				comp[j]=compatible.get(j);
			HashMap<tehArray,Integer> map=new HashMap<tehArray,Integer>(100000);
			BufferedReader arrayIn=new BufferedReader(new InputStreamReader(new
				FileInputStream("F:\\sorted\\"+i+".txt"))),
				indexIn=new BufferedReader(new InputStreamReader(new
				FileInputStream("F:\\sortedindices\\"+i+".txt")));
			String s;
			System.out.println("Reading...");
			while((s=arrayIn.readLine())!=null)
				map.put(new tehArray(s),Integer.valueOf(indexIn.readLine()));
			System.out.println(map.containsValue(null)+" "+map.size());
			PrintWriter out=new PrintWriter(new BufferedOutputStream(new FileOutputStream
				("F:\\usableindices\\"+goodones16SortedNormally[i]+".txt")));
			System.out.println("Generating...");
			for(int b=0;b<65536;b++)
			{
				ArrayList<Integer> qq=new ArrayList<Integer>();
	loopC:		for(int c=0;c<comp.length;c++)
				{
					long word=comp[c];
					if((word&reverse16(b))!=0)
						continue loopC;
					qq.add((int)word);
				}
				int[] aa=new int[qq.size()];
				for(int q=0;q<aa.length;q++)
					aa[q]=qq.get(q);
				out.println(map.get(new tehArray(aa)));
			}
			out.close();
			arrayIn.close();
			indexIn.close();
		}
		System.out.println("All done.");
	}
	static long reverse16(long a)
	{
		return (reverse[(int)(a>>8)])|(reverse[(int)(a&0xff)]<<8);
	}
	static class tehArray
	{
		public tehArray(String s)
		{
			String[] sa=s.split(" ");
			data=new int[sa.length];
			for(int i=0;i<sa.length;i++)
				data[i]=Integer.parseInt(sa[i]);
		}
		public tehArray(int[] qq)
		{
			data=qq;
		}
		int[] data;
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
	}
}