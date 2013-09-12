package breakthrough;
import game.*;
import java.util.*;

import AlphaBeta.AlphaBeta;

public class AwesomeBreakthroughPlayer extends GamePlayer {
	public AwesomeBreakthroughPlayer(String n) {
		super(n, new BreakthroughState(), false);
	}
	
	public void messageFromOpponent(String m){ 
		System.out.println(m); }
	
	public GameMove getMove(GameState state, String lastMove){
		return AlphaBeta.alpha_beta_search((BreakthroughState)state);
	}
		
		
	public GameMove Greedy(BreakthroughState state){
		//get current state of the game
		BreakthroughState board = (BreakthroughState)state;
		
		//moves that are valid but don't take a piece
		ArrayList<BreakthroughMove> moves = new ArrayList<BreakthroughMove>();  
		
		//moves that can take a piece
		ArrayList<BreakthroughMove> takes = new ArrayList<BreakthroughMove>();  
		
		//possible move to make
		BreakthroughMove mv = new BreakthroughMove();
		
		//if opponent is away or home
		char opp = (state.who == GameState.Who.HOME) ? BreakthroughState.awaySym : BreakthroughState.homeSym;
		
		//direction to move
		int dir = (state.who == GameState.Who.HOME) ? +1 : -1;
		
		//go through each tile on the board
		for (int r=0; r<BreakthroughState.N; r++) {
			for (int c=0; c<BreakthroughState.N; c++) {
				
				//current position
				mv.startRow = r;
				mv.startCol = c;
				
				//moving straight
				mv.endingRow = r+dir;
				mv.endingCol = c;
				
				//check if move is valid
				if (board.moveOK(mv)) {
					//check if an opponent is on the tile being moved to
					if (board.board[mv.endingRow][mv.endingCol] == opp) {
						//if so, add that as a move that will take a piece
						takes.add((BreakthroughMove)mv.clone());
					} else {
						//else add that as a valid move
						moves.add((BreakthroughMove)mv.clone());
					}
				}
				
				//moving along a right-diagonal
				mv.endingRow = r+dir; mv.endingCol = c+1;
				
				//check if move is valid
				if (board.moveOK(mv)) {
					//check if an opponent is on the tile being moved to
					if (board.board[mv.endingRow][mv.endingCol] == opp) {
						//if so, add that as a move that will take a piece
						takes.add((BreakthroughMove)mv.clone());
					} else {
						//else, add that as a valid move
						moves.add((BreakthroughMove)mv.clone());
					}
				}
				
				//moving along a left-diagonal
				mv.endingRow = r+dir; mv.endingCol = c-1;
				
				//check if move is valid
				if (board.moveOK(mv)) {
					//check if an opponent is on the tile being moved to
					if (board.board[mv.endingRow][mv.endingCol] == opp) {
						//if so, add that as a move that will take a piece
						takes.add((BreakthroughMove)mv.clone());
					} else {
						//else, add that as a valid move
						moves.add((BreakthroughMove)mv.clone());
					}
				}
			}
		}
		
		//the list of moves to use
		ArrayList<BreakthroughMove> list;
		
		//check if the array of moves that take pieces has at least one move
		if (takes.size() > 0) {
			//if it does, use that list for picking a move
			list = takes;
		}else{
			//else, use the array that lists all possible moves that don't take a piece as the list for picking a move
			list = moves;
		}
		
		//randomly decide which move to make from the list
		int which = Util.randInt(0, list.size()-1);
		
		//return the move chosen to make
		return list.get(which);
	}
		
	public static int heuristic(BreakthroughState state, int r, int c){
		return 1;
	}
	
	public static int Min_Value(){
		
		return 0;
	}
	
	public static void main(String[] args){
		//enter the game
		GamePlayer p = new AwesomeBreakthroughPlayer("Awesome+");
		
		//game is om
		p.compete(args);
	}
}
