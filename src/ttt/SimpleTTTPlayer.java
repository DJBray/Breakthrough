package ttt;
import game.*;

public class SimpleTTTPlayer extends GamePlayer {
	public SimpleTTTPlayer(String nname)
	{ super(nname, new TTTState(), true); }
	public GameMove getMove(GameState game, String lastMove)
	{
		TTTState brd = (TTTState)game;
		for (int r=0; r<TTTState.N; r++) {
			for (int c=0; c<TTTState.N; c++) {
				if (brd.board[r][c] == TTTState.emptySym) {
					return new TTTMove(r, c);
				}
			}
		}
		return null;
	}
	public static void main(String [] args)
	{
		GamePlayer p = new SimpleTTTPlayer("TTT simpleton");
		p.compete(args);
	}
}
