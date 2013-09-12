package game;
import java.awt.*;

public class GameFrame extends Frame {
	public GameCanvas canvas;
	public static final long serialVersionUID = 0;
    public GameFrame(String name, GameCanvas can)
    {
        super(name);
        canvas = can;
        setSize(canvas.getW(), canvas.getH());
        addWindowListener(new WindowCloser());
        add(canvas);
    }
}
