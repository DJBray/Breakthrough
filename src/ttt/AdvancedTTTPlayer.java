package ttt;
import game.*;

public class AdvancedTTTPlayer extends GamePlayer {
	
	private class ScoredTTTMove extends TTTMove {
		public ScoredTTTMove(int r, int c, double s)
		{
			super(r, c);
			score = s;
		}
		public double score;
	}

	private TTTMove tempMv = new TTTMove(0,0);
	public AdvancedTTTPlayer(String nname)
	{ super(nname, new TTTState(), true); }
	
	private ScoredTTTMove terminalValue(GameState brd)
	{
		GameState.Status status = brd.getStatus();
		if (status == GameState.Status.HOME_WIN) {
			return new ScoredTTTMove(0, 0, +1);
		} else if (status == GameState.Status.AWAY_WIN) {
			return new ScoredTTTMove(0, 0, -1);
		} else if (status == GameState.Status.DRAW) {
			return new ScoredTTTMove(0, 0, 0);
		} else {
			return null;
		}
	}
	
	/**
	 * Minimax without a depth limit. Goes all the way to terminal
	 * leaves
	 * @param brd
	 * @return Move recommendation
	 */
	private ScoredTTTMove minimax(TTTState brd)
	{
		double bestScore = (brd.getWho() == GameState.Who.HOME ? Double.NEGATIVE_INFINITY :
															Double.POSITIVE_INFINITY);
		boolean toMaximize = (brd.getWho() == GameState.Who.HOME);
		
		ScoredTTTMove terminal = terminalValue(brd);
		if (terminal != null) {
			return terminal;
		} else {
			ScoredTTTMove bestMove = new ScoredTTTMove(0, -10, bestScore);
			GameState.Who currTurn = brd.getWho();

			for (int r=0; r<TTTState.N; r++) {
				for (int c=0; c<TTTState.N; c++) {
					if (brd.board[r][c] == TTTState.emptySym) {
						
						tempMv.row = r; tempMv.col = c;
						brd.makeMove(tempMv);
						ScoredTTTMove moveAttempt = minimax(brd);
						
						brd.board[r][c] = TTTState.emptySym;
						brd.numMoves--;
						brd.status = GameState.Status.GAME_ON;
						brd.who = currTurn;
						
						if (toMaximize && moveAttempt.score > bestMove.score) {
							bestMove = new ScoredTTTMove(r, c, moveAttempt.score);
						} else if (!toMaximize && moveAttempt.score < bestMove.score) {
							bestMove = new ScoredTTTMove(r, c, moveAttempt.score);
						}
					}
				}
			}
			return bestMove;
		}
	}
	public GameMove getMove(GameState brd, String lastMove)
	{ return minimax((TTTState)brd); }
	
	public static void main(String [] args)
	{
		GamePlayer p = new AdvancedTTTPlayer("TTT genius");
		p.compete(args);
	}
}
