import java.io.*;
import java.util.*;

public class makeUsable
{
	public static void main(String[] fhqwgads) throws Throwable
	{
		//Make all 3APs of size 8.
		ArrayList<Integer> ap8l=new ArrayList<Integer>();
		for(int i=1;2*i+1<=8;i++)
		{
			int t=1|(1<<i)|(1<<(2*i));
			while(t<256)
			{
				ap8l.add(t);
				t<<=1;
			}
		}
		int[] ap8=new int[ap8l.size()];
		for(int i=0;i<ap8l.size();i++)
			ap8[i]=ap8l.get(i);
		ArrayList<Integer> goodones8l=new ArrayList<Integer>();
outer:	for(int i=0;i<256;i++)
		{
			for(int j=0;j<ap8.length;j++)
				if((i&ap8[j])==ap8[j])
					continue outer;
			goodones8l.add(i);
		}
		Collections.sort(goodones8l,new Comparator(){
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
				if(ones[ia]>ones[ib])
					return -1;
				if(ones[ia]<ones[ib])
					return 1;
				if(ia>ib)
					return -1;
				if(ia<ib)
					return 1;
				return 0;
			}
		});
		int[] goodones8=new int[goodones8l.size()];
		for(int i=0;i<goodones8.length;i++)
			goodones8[i]=goodones8l.get(i);
			
		for(int i=0;i<goodones8.length;i++)
			System.out.println(Integer.toString(goodones8[i],2));
		//OK! now size 8 is fine and dandy, let's make the size 16 stuff....
		ArrayList<Integer> ap16l=new ArrayList<Integer>();
		for(int i=1;2*i+1<=16;i++)
		{
			int t=1|(1<<i)|(1<<(2*i));
			while(t<65536)
			{
				ap16l.add(t);
				t<<=1;
			}
		}
		int[] ap16=new int[ap16l.size()];
		for(int i=0;i<ap16l.size();i++)
			ap16[i]=ap16l.get(i);
		ArrayList<Integer>[][] usable=new ArrayList[256][256];
		for(int i=0;i<256;i++)
			for(int j=0;j<256;j++)
				usable[i][j]=new ArrayList<Integer>();
		int[] reverse=new int[256];
		for(int i=0;i<256;i++)
			reverse[i]=((i&1)<<7)|((i&2)<<5)|((i&4)<<3)|((i&8)<<1)|((i&16)>>1)	
				|((i&32)>>3)|((i&64)>>5)|((i&128)>>7);
		for(int a=0;a<256;a++)
			for(int b=0;b<256;b++)
	loopC:		for(int c=0;c<goodones8.length;c++)
				{
					int word=goodones8[c];
					if((word&reverse[b])!=0)
						continue loopC;
					int subseq=(a<<8)|word;
					for(int i=0;i<ap16.length;i++)
						if((subseq&ap16[i])==ap16[i])
							continue loopC;
					usable[a][b].add(word);
				}
		PrintWriter out=new PrintWriter("usableSize.txt");
		for(int i=0;i<256;i++)
			for(int j=0;j<256;j++)
				if(j==255)
					out.println(usable[i][j].size());
				else
					out.print(usable[i][j].size()+" ");
		out.close();
		out=new PrintWriter("usable.txt");
		for(int i=0;i<256;i++)
			for(int j=0;j<256;j++)
				for(Integer q:usable[i][j])
					out.print(q+" ");
		out.println();
		out.close();
	}
}