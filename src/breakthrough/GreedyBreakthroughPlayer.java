package breakthrough;
import game.*;
import java.util.*;

public class GreedyBreakthroughPlayer extends GamePlayer {
	public GreedyBreakthroughPlayer(String n) 
	{
		super(n, new BreakthroughState(), false);
	}
	public void messageFromOpponent(String m)
	{ System.out.println(m); }
	public GameMove getMove(GameState state, String lastMove)
	{
		//try { Thread.sleep(50010); } catch (Exception e) {}
		BreakthroughState board = (BreakthroughState)state;
		ArrayList<BreakthroughMove> moves = new ArrayList<BreakthroughMove>();  
		ArrayList<BreakthroughMove> takes = new ArrayList<BreakthroughMove>();  
		BreakthroughMove mv = new BreakthroughMove();
		char opp = state.who == GameState.Who.HOME ?
				BreakthroughState.awaySym : BreakthroughState.homeSym;
		int dir = state.who == GameState.Who.HOME ? +1 : -1;
		for (int r=0; r<BreakthroughState.N; r++) {
			for (int c=0; c<BreakthroughState.N; c++) {
				mv.startRow = r;
				mv.startCol = c;
				mv.endingRow = r+dir; mv.endingCol = c;
				if (board.moveOK(mv)) {
					if (board.board[mv.endingRow][mv.endingCol] == opp) {
						takes.add((BreakthroughMove)mv.clone());
					} else {
						moves.add((BreakthroughMove)mv.clone());
					}
				}
				mv.endingRow = r+dir; mv.endingCol = c+1;
				if (board.moveOK(mv)) {
					if (board.board[mv.endingRow][mv.endingCol] == opp) {
						takes.add((BreakthroughMove)mv.clone());
					} else {
						moves.add((BreakthroughMove)mv.clone());
					}
				}
				mv.endingRow = r+dir; mv.endingCol = c-1;
				if (board.moveOK(mv)) {
					if (board.board[mv.endingRow][mv.endingCol] == opp) {
						takes.add((BreakthroughMove)mv.clone());
					} else {
						moves.add((BreakthroughMove)mv.clone());
					}
				}
			}
		}
		ArrayList<BreakthroughMove> list;  
		if (takes.size() > 0) {
			list = takes;
		} else {
			list = moves;
		}
		int which = Util.randInt(0, list.size()-1);
		return list.get(which);
	}
	public static void main(String [] args)
	{
		GamePlayer p = new GreedyBreakthroughPlayer("Greedy BT+");
		p.compete(args);
	}
}
