package breakthrough;
import java.util.StringTokenizer;

import game.*;


public class BreakthroughMove extends GameMove {
	public int startRow, startCol;
	public int endingRow, endingCol;

	public BreakthroughMove()
	{
		super();
	}
	public static boolean indexOK(int v)
	{ return Util.inrange(v, 0, BreakthroughState.N-1); }
	public BreakthroughMove(int r1, int c1, int r2, int c2)
	{
		startRow = r1; startCol = c1; endingRow = r2; endingCol = c2;
		if (!indexOK(startRow) || !indexOK(startCol) ||
			!indexOK(endingRow) || !indexOK(endingCol)) {
				System.err.println("problem in Breakthrough ctor");
		}
	}
    public Object clone()
    { return new BreakthroughMove(startRow, startCol, endingRow, endingCol); }
	public String toString()
	{ return startRow + " " + startCol + " " + endingRow + " " + endingCol; }
	public void parseMove(String s)
	{
		StringTokenizer toks = new StringTokenizer(s);
		startRow = Integer.parseInt(toks.nextToken());
		startCol = Integer.parseInt(toks.nextToken());
		endingRow = Integer.parseInt(toks.nextToken());
		endingCol = Integer.parseInt(toks.nextToken());
	}
}
