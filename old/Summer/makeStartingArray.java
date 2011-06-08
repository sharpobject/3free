import java.io.*;
import java.util.*;

public class makeStartingArray
{
	public static void main(String[] fhqwgads)
	{
		//Make all 3APs of size 8.
		ArrayList<Integer> ap8=new ArrayList<Integer>();
		for(int i=1;2*i+1<=8;i++)
		{
			int t=1|(1<<i)|(1<<(2*i));
			while(t<256)
			{
				ap8.add(t);
				t<<=1;
			}
		}
		System.out.println("These are the 3-APs that exist on 0...7");
		for(int i=0;i<ap8.size();i++)
			System.out.println(Integer.toString(ap8.get(i)&255,2));
		int[] aps=new int[ap8.size()];
		for(int i=0;i<ap8.size();i++)
			aps[i]=ap8.get(i);
		ArrayList<Integer> goodones=new ArrayList<Integer>();
outer:	for(int i=0;i<256;i++)
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
		System.out.println("Here is the array, for hard coding purposes "+goodones);
		System.out.println("And here are the sets it contains in bitmask form, so you can check them.");
		for(int i=0;i<goodones.size();i++)
			System.out.println(Integer.toString(goodones.get(i)&255,2));
		System.out.println("The size of the array is "+goodones.size());
	}
}