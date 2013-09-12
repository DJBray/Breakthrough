package AlphaBeta;

import game.GameState;

import breakthrough.BreakthroughMove;
import breakthrough.BreakthroughState;


public class AlphaBeta {

	public static final int INITIAL_DEPTH = 0;
	public static final int MAX_DEPTH = 4; 
	
	private static boolean isHome;
	
	public static BreakthroughMove alpha_beta_search(BreakthroughState state){
		/**
		 * 	function ALPHA-BETA-SEARCH(state) returns an action
		 * 		v <- MAX-VALUE(state, -INFINITY, +INFINITY)
		 * 		return the action in ACTIONS(state) with value v
		 */
		
		//direction to move
		int dir = (state.who == GameState.Who.HOME) ? +1 : -1;
		if(state.who == GameState.Who.HOME){
			isHome = true;
		}
		else{
			isHome = false;
		}

		//create tree and set root
		Move root = new Move(state);
		MovesTree movesTree = new MovesTree(root);
		
		//get max value. 
		int value = AlphaBeta.min_max_value(true, state, dir, INITIAL_DEPTH, root, Integer.MIN_VALUE, Integer.MAX_VALUE);

		//find out which node that came from
		Move move = movesTree.getMove(value);

		//return its move
		return move.getMove();
	}

	public static int min_max_value(boolean type, BreakthroughState state, int dir, int depth, Move move, int alpha, int beta){
		/**
		 * 	function MIN-VALUE(state, A, B) returns a utility value
		 * 		if TERMINAL-TEST(state) then return UTILITY(state)
		 * 		v <- +INFINITY
		 * 		for each a in ACTIONS(state) do
		 * 			v <- MIN(v, MAX-VALUE(RESULT(state, a), A, B))
		 * 			if v <= A then return v
		 * 			B <- MIN(B, v)
		 * 		return v
		 */
		
		if(depth > MAX_DEPTH)

			return utility(state, move);

		int v = (type) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int temp;
		for(int r=0; r<BreakthroughState.N; r++){
			for(int c=0; c<BreakthroughState.N; c++){
				//check left diagonal
				temp = movePlayer(r, c, -1, type, state, dir, depth, move, v, alpha, beta);
				if(type){
					v = Math.max(v, temp);
					move.setValue(v);
					if(v >= beta)
						return v;
					alpha = Math.max(alpha, v);
				}else{
					v = Math.min(v,  temp);
					move.setValue(v);
					if(v <= alpha)
						return v;
					beta = Math.max(beta, v);
				}
				
				//check straight
				temp = movePlayer(r, c, 0, type, state, dir, depth, move, v, alpha, beta);
				if(type){
					v = Math.max(v, temp);
					move.setValue(v);
					if(v >= beta)
						return v;
					alpha = Math.max(alpha, v);
				}else{
					v = Math.min(v,  temp);
					move.setValue(v);
					if(v <= alpha)
						return v;
					beta = Math.max(beta, v);
				}
				
				//check right diagonal
				temp = movePlayer(r, c, 1, type, state, dir, depth, move, v, alpha, beta);
				if(type){
					v = Math.max(v, temp);
					move.setValue(v);
					if(v >= beta)
						return v;
					alpha = Math.max(alpha, v);
				}else{
					v = Math.min(v,  temp);
					move.setValue(v);
					if(v <= alpha)
						return v;
					beta = Math.max(beta, v);
				}
			}
		}

		return v;
	}
	
	public static int movePlayer(int r, int c, int shift, boolean type, BreakthroughState state, int dir, int depth, Move move, int v, int alpha, int beta){
		//figure out sides
		char player = state.who == GameState.Who.HOME ? BreakthroughState.homeSym : BreakthroughState.awaySym;
		char opp = state.who == GameState.Who.HOME ? BreakthroughState.awaySym : BreakthroughState.homeSym;
		
		//Alt. way I'd like to use to figure out sides
		//char player, opp;
		//if(isHome){
		//	player = BreakthroughState.homeSym;
		//	opp = BreakthroughState.awaySym;
		//}
		//else{
		//	player = BreakthroughState.awaySym;
		//	opp = BreakthroughState.homeSym;
		//}
		
		
		//move the player according to the shift
		BreakthroughMove mv = new BreakthroughMove();
		mv.startRow = r;
		mv.startCol = c;
		mv.endingRow = r+dir; 
		mv.endingCol = c+shift;

		//check to see if a piece is Taken
		char prev = '.';
		if(mv.endingRow > -1 && mv.endingRow < BreakthroughState.N 
				&& mv.endingCol > -1 && mv.endingCol < BreakthroughState.N){
			if(state.board[mv.endingRow][mv.endingCol] == opp){
				prev = opp;
			}
		}
		
		//check if move is valid
		if (state.moveOK(mv)) {
			//save properties before move is made (as it will have to be changed back)
			GameState.Who who = state.who;
			GameState.Status status = state.status;
			
			//valid move - make move on board
			if(state.makeMove(mv) && state.status != GameState.Status.GAME_ON)
				return (state.who == GameState.Who.HOME) ? 
						(state.status == GameState.Status.HOME_WIN ? Integer.MAX_VALUE : Integer.MIN_VALUE)
						: (state.status == GameState.Status.AWAY_WIN ? Integer.MAX_VALUE : Integer.MIN_VALUE);

			//add move to tree
			Move nextMove = new Move(v, (prev != '.'), mv, state, move);
			move.addMove(nextMove);
			v = (type) ?
					Math.max(v, AlphaBeta.min_max_value(!type, state, -dir, depth+1, nextMove, alpha, beta))
					: Math.min(v, AlphaBeta.min_max_value(!type, state, -dir, depth+1, nextMove, alpha, beta)); 
			
			//reverse move
			state.board[mv.endingRow][mv.endingCol] = prev;
			state.board[mv.startRow][mv.startCol] = player;
			state.who = who;
			state.status = status;
			state.numMoves--;
			
		}
		return v;
	}
	
	public static int utility(BreakthroughState state, Move currMove){
		//combine the results of a bunch of heuristics. 
		int utility = 0;
		utility += utility_BasicStrat(state, currMove);
		//utility += AlphaBeta.utility_progression(state);
		//utility += AlphaBeta.utility_defense(state)*2;
		return utility;
	}
	
	public static int utility_BasicStrat(BreakthroughState state, Move currMove){
		int utility = 0;
		if(currMove.getTook()){ //TODO is this working properly?
			utility+=2;
		}
		if(isInDanger(state, currMove)){
			utility-=-1; //TODO change so that it is multiplied by a ratio between your and opp pieces
			//if opens losing path
			//		utility = -inf
		}
		else{
			//if( can outrun opponent ) 
			//		utility = inf - 1
		}
		return utility;
	}
	
	/**
	 * isInDanger
	 * 
	 * Checks the diagonals of the currently moving piece to see if it can be taken by an opponent next
	 * turn.
	 * @param state - the state of the board
	 * @param currMove - the current move that was just made on the board
	 * @return true if the piece is in a dangerous location, else return false
	 */
	private static boolean isInDanger(BreakthroughState state, Move currMove){
		char home = state.who == GameState.Who.HOME ? BreakthroughState.homeSym : BreakthroughState.awaySym;
		char away = state.who == GameState.Who.HOME ? BreakthroughState.awaySym : BreakthroughState.homeSym;
		
		int row = currMove.getMove().endingRow;
		int col = currMove.getMove().endingCol;
		int dirToCheck = (row - currMove.getMove().startRow > 0) ? 1 : -1;
		
		//Finds out if the opponent of the current move is home or away
		char opp = (state.board[row][col] == home) ? away : home;
		
		//check to see if next row is even a valid row to move to (if not then we can assume we won)
		if(row + dirToCheck > state.N - 1 || row + dirToCheck < 0){
			return false;
		}
		//if neither diagonal is off the side of the board, check them both for an opponent
		if(col + 1 < state.N && col - 1 >= 0){
			return (state.board[row + dirToCheck][col + 1] == opp || 
				state.board[row + dirToCheck][col - 1] == opp);
		}
		
		//else if col + 1 is a valid location then check it for an opponent.
		else if(col + 1 < state.N){
			return (state.board[row + dirToCheck][col + 1] == opp);
		}
		//else if col - 1 is a valid location then check it for an opponent
		else if(col - 1 >= 0){
			return (state.board[row + dirToCheck][col - 1] == opp);
		}
		
		//else the board has been resized such that there can exist no opponents in a dangerous location
		return false;
	}
	
	public static int utility_numPieces(BreakthroughState state){
		int value = BreakthroughState.N;
		for(int r=0; r<BreakthroughState.N; r++){
			for(int c=0; c<BreakthroughState.N; c++){
				if(state.who == GameState.Who.HOME)
					value += (state.board[r][c] == BreakthroughState.homeSym) 
						? 1 : ((state.board[r][c] == BreakthroughState.awaySym) ? -1 : 0);
				else
					value += (state.board[r][c] == BreakthroughState.awaySym) 
						? 1 : ((state.board[r][c] == BreakthroughState.homeSym) ? -1 : 0);
			}
		}
		return value;
	}
	
	public static int utility_progression(BreakthroughState state){
		//get player
		char player = state.who == GameState.Who.HOME ? BreakthroughState.homeSym : BreakthroughState.awaySym;
		
		//get direction of travel
		int dir = (state.who == GameState.Who.HOME) ? +1 : -1;
		
		//value to be returned
		int value = 0;
		
		//whether to start at the top or the bottom
		int r = (dir > 0) ? 0: BreakthroughState.N-1;
		
		//factor to add to the value
		int factor = 1;
		
		//go through the board
		while(r > -1 && r < BreakthroughState.N){
			for(int j=0; j<BreakthroughState.N; j++){
				if(state.board[r][j] == player)
					value += factor;
			}
			r += dir;
			factor *= 2;
		}
		
		return value;
	}
	
	public static int utility_defense(BreakthroughState state){
		//get opponent
		char opp = state.who == GameState.Who.HOME ? BreakthroughState.awaySym : BreakthroughState.homeSym;
		
		//get direction of travel
		int dir = (state.who == GameState.Who.HOME) ? 1 : -1;
		
		//value to be returned
		int value = 0;
		
		//whether to start at the top or the bottom
		int r = (dir > 0) ? BreakthroughState.N-1 : 0;
		
		//factor to add to the value
		int factor = 1;
		
		//go through the board
		while(r > -1 && r < BreakthroughState.N){
			for(int j=0; j<BreakthroughState.N; j++){
				if(state.board[r][j] == opp)
					value -= factor;
			}
			r += dir;
			factor *= 2;
		}
		
		return value;
	}
}
