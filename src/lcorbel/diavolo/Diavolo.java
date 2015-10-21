package lcorbel.diavolo;

public class Diavolo
{		
	private boolean alive;
	private boolean stop;
	
	private boolean colored;
	private boolean chooseColor;
	
	public boolean firstTurn;
	
	public Gameboard g;
	
	public Diavolo(int size)
	{
		this.alive = true;
		this.stop = false;
		
		this.colored = true;
		this.chooseColor = false;
		
		this.firstTurn = true;
		
		this.g = new Gameboard(size);
	}
	
	public boolean isRunning()
	{
		return alive;
	}
	
	public void nextAction(Action ac)
	{
		int x1 = ac.pos[0][0];
		int y1 = ac.pos[0][1];
		int x2 = ac.pos[1][0];
		int y2 = ac.pos[1][1];
		
		switch(ac.type)
		{
			case FIRST:
				colored = false;
				break;
			case SECOND:
				chooseColor = true;
				colored = true;
				break;
			case DARK:
				chooseColor = true;
				break;
			case LIGHT:
				if(!colored)
				{
					colored = true;
					System.out.println("foncé");
				}
				else
				{
					System.out.println("clair");
					colored = false;
				}
				break;
			case BRIDGE:
				g.addBridge(x1, y1, x2, y2);
				g.switchPlayer();
				break;
			case PAWNS:
				g.addPawn(x1, y1);
				g.addPawn(x2, y2);
				g.switchPlayer();
				break;
			case STOP:
				if(!colored)
				{
					stop = true;
				}
				else
				{
					System.out.println("ok");
					g.switchPlayer();
				}
				break;
			case END:
				alive = false;
				break;
		}
	}
	
	public Action play(Action ac)
	{		
		if(ac != null)
		{
			nextAction(ac);
			
			if(chooseColor)
			{
				chooseColor = false;
				return null;
			}
			
			if(!alive)
			{
				return null;
			}
			
			// DEFAULT
			return nextAction();
		}
		
		return null;
	}
	
	public Action nextAction()
	{
		Action ac = null;
		
		if(!stop)
		{
			firstTurn = false;
			
			// NEXT MOVE IA
			g.nextMove();
			
			// Get the move
			int x1 = g.lastMove[0][0];
			int y1 = g.lastMove[0][1];
			int x2 = g.lastMove[1][0];
			int y2 = g.lastMove[1][1];
				
			System.out.println(x1+":"+y1+"|"+x2+":"+y2);
			
			// If i can play
			if(g.lastMove[0][0] != 0)
			{
				// pawns
				if(g.lastMovePawn)
				{
					g.addPawn(x1, y1);
					g.addPawn(x2, y2);
						
					ac = new Action(Type.PAWNS, x1, y1, x2, y2);
				}
				// bridge
				else
				{
					g.addBridge(x1, y1, x2, y2);
						
					ac = new Action(Type.BRIDGE, x1, y1, x2, y2);
				}
			}
			// Else STOP
			else
			{
				System.out.println("stop");
				stop = true;
				ac = new Action(Type.STOP);
			}
			
			// -- SWITCH PLAYER --
			g.switchPlayer();

			// If you can, take the white !
			if(colored && firstTurn)
			{
				ac = new Action(Type.LIGHT);
				
				firstTurn = false;
				colored = false;
			}
		}
		else
		{	
			// Game STOP
			// waiting...
			if(colored)
			{
				System.out.println("stop");
				ac = new Action(Type.STOP);
			}
			else
			{
				ac = null;
			}
		}
		
		return ac;
	}
}
