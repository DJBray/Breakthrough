package AlphaBeta;

import breakthrough.BreakthroughMove;
import breakthrough.BreakthroughState;

public class Move {
	private int value;
	private BreakthroughMove move;
	private BreakthroughState state;
	private Move previous;
	private int numMoves;
	private Move[] moves;
	private boolean took;
	
	public Move(BreakthroughState state){
		this.value = 0;
		this.move = null;
		this.moves = new Move[BreakthroughState.N*2*3];
		this.state = state;
		this.previous = null;
		this.numMoves = 0;
		this.took = false;
	}
	
	public Move(int value, boolean took, BreakthroughMove move, BreakthroughState state, Move previous){
		this.value = value;
		this.move = move;
		this.moves = new Move[BreakthroughState.N*2*3]; //number of players * different ways to move
		this.state = state;
		this.previous = previous;
		this.numMoves = 0;
		this.took = took;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public boolean getTook(){
		return this.took;
	}
	
	public BreakthroughMove getMove(){
		return this.move;
	}
	
	public Move getPreviousMove(){
		return this.previous;
	}
	
	public int getNumMoves(){
		return this.numMoves;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public void addMove(Move move){
		this.moves[numMoves] = move;
		numMoves++;
	}
	
	public Move getMove(int index){
		return this.moves[index];
	}
}
