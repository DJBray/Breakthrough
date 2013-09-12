package ttt;
import game.*;

public class BeginnerTTTPlayer extends GamePlayer {
	public BeginnerTTTPlayer(String nname)
	{ super(nname, new TTTState(), true); }
	public GameMove getMove(GameState game, String lastMove)
	{
		TTTState brd = (TTTState)game;
		int row=0, col=0;
		
		if (brd.board[1][1] == TTTState.emptySym) {
			return new TTTMove(1, 1);
		} else if (brd.getNumMoves() == 1) {
			return new TTTMove(0, 0);
		} else if (brd.getNumMoves() == 2) {
			if (brd.board[0][0] == TTTState.emptySym) {
				return new TTTMove(0, 0);
			} else {
				return new TTTMove(0, 2);
			}
		} else {
			for (int r=0; r<TTTState.N; r++) {
				for (int c=0; c<TTTState.N; c++) {
					if (brd.board[r][c] == TTTState.emptySym) {
						row = r;
						col = c;
						TTTMove mv = new TTTMove(r, c);
						brd.makeMove((GameMove)mv);
						GameState.Status status = brd.getStatus();
						brd.board[mv.row][mv.col] = TTTState.emptySym;
						if (status != GameState.Status.GAME_ON) {
							return new TTTMove(r, c);
						}
					}
				}
			}
			return new TTTMove(row, col);
		}
	}
	public static void main(String [] args)
	{
		GamePlayer p = new BeginnerTTTPlayer("TTT beginner");
		p.compete(args);
	}
}
