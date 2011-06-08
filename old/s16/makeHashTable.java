import java.io.*;
import java.util.*;

public class makeHashTable
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
			//RandomAccessFile(pfile,"rw")
		RandomAccessFile[] pointers=new RandomAccessFile(new File("D:\\pointers.bin"),"rw"),
			data=new RandomAccessFile(new File("I:\\data.bin"),"rw");
		byte[] toOut=new byte[400000];
		byte[] intMinVal={(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x00};
		for(int i=0;i<1000;i++)
		{
			System.out.println(i);
			pointers.write(toOut);
		}
		toOut=null;
		intMinVal=null;
		BufferedReader in=new BufferedReader(new InputStreamReader(
			new FileInputStream("F:\\onesortedfile\\sortedfile.txt")));
		String s;
		int i=0;
thisFile:while((s=in.readLine())!=null)
		{
			if(i%10000==0)
				System.out.println("Handling line #"+(i+1));
			tehArray t=new tehArray(s);
			long hc=t.hashCode();
			int whichFile=(int)(hc%100);
			hc/=100;
			pointers[whichFile].seek(hc*4);
			int node=pointers[whichFile].readInt();
/*Node structure:
 *32-bit int pointing to next node
 *32-bit int representing the position of this node in sortedfile.txt
 *16-bit int representing the number of values in this node
 *array of values, 16 bits each.*/
			if(node==Integer.MIN_VALUE)
			{
				pointers[whichFile].seek(hc*4);
				pointers[whichFile].writeInt((int)data[whichFile].length());
				data[whichFile].seek((int)data[whichFile].length());
				data[whichFile].writeInt(Integer.MIN_VALUE);
				data[whichFile].writeInt(i);
				char[] arr=t.data;
				if(arr.length>30000)
				{
					System.out.println("SOMETHING IS FUCKED.");
					System.in.read();
					return;
				}
				data[whichFile].writeChar((char)arr.length);
				for(int w=0;w<arr.length;w++)
					data[whichFile].writeChar(arr[w]);
			}
			else
			{
				char[] arr=t.data;
				int oldnode=0;
tehloop:		while(node!=Integer.MIN_VALUE)
				{
					oldnode=node;
					data[whichFile].seek(node);
					node=data[whichFile].readInt();
				}
				data[whichFile].seek(oldnode);
				data[whichFile].writeInt((int)data[whichFile].length());
				data[whichFile].seek(data[whichFile].length());
/*Node structure:
 *64-bit int pointing to next node
 *16-bit int representing the number of values in this node
 *array of values, 16 bits each.*/
				data[whichFile].writeInt(Integer.MIN_VALUE);
				data[whichFile].writeInt(i);
				if(arr.length>30000)
				{
					System.out.println("SOMETHING IS FUCKED.");
					System.in.read();
					return;
				}
				data[whichFile].writeChar((char)arr.length);
				for(int w=0;w<arr.length;w++)
					data[whichFile].writeChar(arr[w]);
			}
			i++;
		}
		in.close();
	}
	static long reverse16(long a)
	{
		return (reverse[(int)(a>>8)])|(reverse[(int)(a&0xff)]<<8);
	}
	static class tehArray
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
		public int hashCode()
		{
			long res=5687693;
			for(int i=0;i<data.length;i++)
				res=(33*res+data[i])%1000000000;
			return (int)res;
		}
		public boolean equals(Object o)
		{
			return Arrays.equals(data,((tehArray)o).data);
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