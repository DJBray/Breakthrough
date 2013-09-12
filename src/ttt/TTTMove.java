package ttt;
import game.GameMove;

import java.util.*;

public class TTTMove extends GameMove {
	public int row, col;
	public TTTMove(int r, int c)
	{
		row = r; col = c;
		if (!indexOK(row) || !indexOK(col)) {
			row = col = 0;
		}
	}
    public Object clone()
    { return new TTTMove(row, col); }
	public String toString()
	{ return row + " " + col; }
	public void parseMove(String s)
	{
		StringTokenizer toks = new StringTokenizer(s);
		row = Integer.parseInt(toks.nextToken());
		col = Integer.parseInt(toks.nextToken());
		if (!indexOK(row) || !indexOK(col)) {
			row = col = 0;
		}
	}

	public static boolean indexOK(int v)
	{ return v >= 0 && v < TTTState.N; }
}
