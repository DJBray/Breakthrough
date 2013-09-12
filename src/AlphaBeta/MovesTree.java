package AlphaBeta;

public class MovesTree {
	private Move root;
	
	public MovesTree(Move move){
		this.root = move;
	}
	
	public Move getRoot(){
		return this.root;
	}
	
	public Move getMove(int value){
		for(int i=0; i<root.getNumMoves(); i++){
			Move move = root.getMove(i);
			if(move.getValue() == value){
				return move;
			}
		}
		
		return null;
	}
}
