import java.io.*;
import java.util.*;

public class eatMemory
{
	public static void main(String[] fhqwgads) throws Throwable
	{
		int[][] a=new int[65536][];
		int i=0;
		while(true)
		{
			a[i++]=new int[1000000];
			System.in.read();
			System.out.println(i);
		}
	}
}