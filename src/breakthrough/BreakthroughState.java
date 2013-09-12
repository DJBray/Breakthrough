package breakthrough;
import game.*;

import java.io.File;
import java.util.*;

public class BreakthroughState extends GameState {
	public static final Params gameParams = new Params("config/breakthrough.txt");
	public static final int N = gameParams.integer("ROWS");
	public static final char homeSym = gameParams.character("HOMESYM");
	public static final char awaySym = gameParams.character("AWAYSYM");
	public static final char emptySym = gameParams.character("EMPTYSYM");

	public char [][] board;

	public BreakthroughState()
	{
		super();
		board = new char [N][N];
		reset();
	}
	public Object clone()
	{
		BreakthroughState res = new BreakthroughState();
		res.copyInfo(this);
		Util.copy(res.board, board);
		return res;
	}
	public void reset()
	{
		clear();
		Util.clear(board, emptySym);
		Arrays.fill(board[0], homeSym);
		Arrays.fill(board[1], homeSym);
		Arrays.fill(board[N-2], awaySym);
		Arrays.fill(board[N-1], awaySym);
	}
	public boolean moveOK(GameMove m)
	{
		BreakthroughMove mv = (BreakthroughMove)m;
		boolean OK = false;
		char PLAYER = who == GameState.Who.HOME ? homeSym : awaySym;
		char OPP = who == GameState.Who.HOME ? awaySym : homeSym;
		int diff = who == GameState.Who.HOME ? 1 : -1;
		if (status == Status.GAME_ON && mv != null &&
			Util.inrange(mv.startRow, 0, N-1) && Util.inrange(mv.endingRow, 0, N-1) &&
			Util.inrange(mv.startCol, 0, N-1) && Util.inrange(mv.endingCol, 0, N-1) &&
			mv.startRow + diff == mv.endingRow && Math.abs(mv.startCol - mv.endingCol) <= 1 &&
			board[mv.startRow][mv.startCol] == PLAYER) {
						if (board[mv.endingRow][mv.endingCol] == emptySym ||
							Math.abs(mv.startCol-mv.endingCol) == 1 &&
							board[mv.endingRow][mv.endingCol] == OPP) {
								OK = true;
						}
		}
		return OK;
	}
	private boolean oneSideEliminated()
	{
		int home = 0, away = 0;
		for (int r=0; r<N; r++) {
			for (int c=0; c<N; c++) {
				char ch = board[r][c];
				if (ch == homeSym) {
					home++;
				} else if (ch == awaySym) {
					away++;
				}
			}
		}
		return home == 0 || away == 0;
	}
	public boolean makeMove(GameMove m)
	{
		BreakthroughMove mv = (BreakthroughMove)m;
		boolean OK = false;
		char PLAYER = who == GameState.Who.HOME ? homeSym : awaySym;
		int goal = who == GameState.Who.HOME ? N - 1 : 0;
		GameState.Status possibleStatus = 
				who == GameState.Who.HOME ? GameState.Status.HOME_WIN: GameState.Status.AWAY_WIN;
		if (moveOK(m)) {
			board[mv.startRow][mv.startCol] = emptySym;
			board[mv.endingRow][mv.endingCol] = PLAYER;
			OK = true;
			super.newMove();
			status = (mv.endingRow == goal || oneSideEliminated()) ?
								possibleStatus :  GameState.Status.GAME_ON;
		}
		return OK;
	}
	public void parseMsgString(String s)
	{
		reset();
		Util.parseMsgString(s, board, emptySym);
		parseMsgSuffix(s.substring(s.indexOf('[')));
	}
	public String toString()
	{ return Util.toString(board) + msgSuffix(); }
	public String msgString()
	{ return Util.msgString(board) + this.msgSuffix(); }
}
