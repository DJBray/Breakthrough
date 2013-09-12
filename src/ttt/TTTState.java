package ttt;
import game.*;

public class TTTState extends GameState {
	public static final Params gameParams = new Params("config" + SEP + "ttt.txt");
	public static final int N = gameParams.integer("ROWS");
	public static final char homeSym = gameParams.character("HOMESYM");
	public static final char awaySym = gameParams.character("AWAYSYM");
	public static final char emptySym = gameParams.character("EMPTYSYM");

	public char [][] board = new char [N][N];
	
	public TTTState()
	{ reset(); }
	public Object clone()
	{
		TTTState copy = new TTTState();
		copy.copyInfo(this);
		Util.copy(copy.board, board);
		return copy;
	}
	public void reset()
	{
		clear();
		Util.clear(board, emptySym);
	}
	public boolean moveOK(GameMove mv)
	{
		TTTMove tttmv = (TTTMove)mv;
		return status == Status.GAME_ON && mv != null && 
				Util.inrange(tttmv.row, N-1) && Util.inrange(tttmv.col, N-1) &&
			   board[tttmv.row][tttmv.col] == emptySym;
	}

	public boolean takebackMove(GameMove m)
	{
		boolean result = false;
		char emptySym = gameParams.character("EMPTYSYM");
		if (moveOK(m)) {
			TTTMove mv = (TTTMove)m;
			board[mv.row][mv.col] = emptySym;
			numMoves--;
			togglePlayer();
		}
		return result;
	}
	public boolean makeMove(GameMove mv)
	{
		boolean OK = false;
		TTTMove tttmv = (TTTMove)mv;
		if (moveOK(mv)) {
			board[tttmv.row][tttmv.col] = (who == Who.HOME ? homeSym : awaySym);
			super.newMove();
			computeStatus();
			OK = true;
		}
		return OK;
	}
	private Status winner(char who)
	{ return who == homeSym ? Status.HOME_WIN : Status.AWAY_WIN; }
	private void computeStatus()
	{
		for (int i=0; i<N; i++) {
			if (gameHasWinner(i, 0, 0, 1)) {
				status = winner(board[i][0]);
				return;
			} else if (gameHasWinner(0, i, 1, 0)) {
				status = winner(board[0][i]);
				return;
			}
		}
		
		if (gameHasWinner(0, 0, 1, 1)) {
			status = winner(board[0][0]);
		} else if (gameHasWinner(N-1, 0, -1, 1)) {
			status = winner(board[N-1][0]);
		} else if (numMoves == N*N) {
			status = Status.DRAW;
		} else {
			status = Status.GAME_ON;
		}
	}
	private boolean gameHasWinner(int startRow, int startCol, int dr, int dc)
	{
		int r = startRow;
		int c = startCol;
		for (int i=0; i<N-1; i++) {
			if (board[r][c] != board[r+dr][c+dc])
				return false;
			r += dr;
			c += dc;
		}
		return board[startRow][startCol] != emptySym;
	}
	public void parseMsgString(String s)
	{
		reset();
		Util.parseMsgString(s, board, emptySym);
		parseMsgSuffix(s.substring(s.indexOf('[')));
	}
	public String msgString() 
	{ return Util.msgString(board) + this.msgSuffix(); }
	public String toString() 
	{ return Util.toString(board) + msgSuffix();	}
}