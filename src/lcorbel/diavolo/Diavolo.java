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
				if(colored)
				{
					stop = true;
				}
				break;
			case END:
				alive = false;
				break;
		}
	}
	
	public Action play(Action ac)
	{
		Action rac = null;
		
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
		
		return rac;
	}
	
	public Action nextAction()
	{
		Action ac = null;
		
		if(!stop)
		{
			if(!firstTurn)
			{
				g.nextMove();
				
				int x1 = g.lastMove[0][0];
				int y1 = g.lastMove[0][1];
				int x2 = g.lastMove[1][0];
				int y2 = g.lastMove[1][1];
				
				if(g.lastMovePawn)
				{
					if(g.canAddPawn(x1, y1) && g.canAddPawn(x2, y2))
					{
						g.addPawn(x1, y1);
						g.addPawn(x2, y2);
					}
					else
					{
						g.nextMove();
						
						x1 = g.lastMove[0][0];
						y1 = g.lastMove[0][1];
						x2 = g.lastMove[1][0];
						y2 = g.lastMove[1][1];
						
						g.addBridge(x1, y1, x2, y2);
					}
									
					ac = new Action(Type.PAWNS, x1, y1, x2, y2);
				}
				else
				{
					g.addBridge(x1, y1, x2, y2);
					
					ac = new Action(Type.BRIDGE, x1, y1, x2, y2);
				}
				
				g.switchPlayer();
			}
			
			if(!colored && firstTurn)
			{
				g.addPawn(1, 1);
				g.addPawn(1, 2);
				ac = new Action(Type.PAWNS, 1, 1, 1, 2);
				
				g.lastMove[0][0] = 1;
				g.lastMove[0][1] = 1;
				g.lastMove[1][0] = 1;
				g.lastMove[1][1] = 2;
				
				firstTurn = false;
				
				g.switchPlayer();
			}
			if(colored && firstTurn)
			{
				ac = new Action(Type.LIGHT);
				
				firstTurn = false;
			}
		}
		else
		{
			g.lastMovePawn = false;
			g.nextMove();
			
			int x1 = g.lastMove[0][0];
			int y1 = g.lastMove[0][1];
			int x2 = g.lastMove[1][0];
			int y2 = g.lastMove[1][1];
			
			g.addBridge(x1, y1, x2, y2);
		}
		
		return ac;
	}
}
