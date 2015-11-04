
public class Ctx {
	int[] R = new int[160];
	int[] S = new int[160];
	int[] key = new int[160];
	long ivsize;
	
	public Ctx()
	{
		initR();
		initS();
	}
	
	public void initR()
	{
		for(int i=0;i<160;i++)
		{
			R[i] = 0;
		}
	}
	
	public void initS()
	{
		for(int i=0;i<160;i++)
		{
			R[i] = 0;
		}
	}
}
