package ttt;
import game.*;

public class RogueTTTPlayer extends RandomTTTPlayer {
	public void init()
	{
		try {
			Thread.sleep(30*1000);
		}
		catch (Exception e) {
		}
	}
	public RogueTTTPlayer(String nname)
	{
		super(nname);
		gameState = new TTTState();
	}
	public GameMove getMove(GameState game, String lastMove)
	{
		try {
			//Thread.sleep(4970*1000);
		}
		catch (Exception e) {
		}
		return super.getMove(game, lastMove);
	}
	public static void main(String [] args)
	{
		GamePlayer p = new RogueTTTPlayer("Rogue player");
		p.compete(args);
	}
}
