package ttt;
import game.*;

import java.util.*;

public class RandomTTTPlayer extends GamePlayer {
	public RandomTTTPlayer(String nname)
	{ super(nname, new TTTState(), false); }
	public GameMove getMove(GameState game, String lastMove)
	{
		ArrayList<GameMove> possibleMoves = new ArrayList<GameMove>();
		TTTState brd = (TTTState)game;
		for (int r=0; r<TTTState.N; r++) {
			for (int c=0; c<TTTState.N; c++) {
				if (brd.board[r][c] == TTTState.emptySym) {
					possibleMoves.add(new TTTMove(r, c));
				}
			}
		}

		int numPossibleMoves = possibleMoves.size();
		int which = (int)(Math.random() * numPossibleMoves);
		if (numPossibleMoves == 0) {
			return null;
		} else {
			return (GameMove)possibleMoves.get(which);
		}
	}
	public static void main(String [] args)
	{
		GamePlayer p = new RandomTTTPlayer("TTT randomizer");
		p.compete(args);
	}
}
