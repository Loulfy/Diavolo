package lcorbel.diavolo;

public class Diavolo
{		
	private boolean alive;
	private boolean stop;
	private int size;
	
	private boolean colored;
	private boolean chooseColor;
	
	public boolean firstTurn;
	
	public Gameboard g;
	
	public Diavolo(int size)
	{
		this.alive = true;
		this.stop = false;
		this.size = size;
		
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
					//g.switchPlayer();
					g.lastMove[0][0] = size;
					g.lastMove[0][1] = 0;
					g.lastMove[1][0] = size;
					g.lastMove[1][1] = size;
					//System.out.println("switch");
				}
				else
				{
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
				System.out.println("switch"+g.player);
				break;
			case STOP:
				if(!colored)
				{
					stop = true;
				}
				else
				{
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
			if(!firstTurn)
			{
				g.nextMove();
				
				int x1 = g.lastMove[0][0];
				int y1 = g.lastMove[0][1];
				int x2 = g.lastMove[1][0];
				int y2 = g.lastMove[1][1];
				
				System.out.println(x1+":"+y1+"|"+x2+":"+y2);
				
				if(g.lastMove[0][0] != 0)
				{
					if(g.lastMovePawn)
					{
						g.addPawn(x1, y1);
						g.addPawn(x2, y2);
						
						ac = new Action(Type.PAWNS, x1, y1, x2, y2);
					}
					else
					{
						g.addBridge(x1, y1, x2, y2);
						
						ac = new Action(Type.BRIDGE, x1, y1, x2, y2);
					}
				}
				else
				{
					// STOP
					System.out.println("stop");
					stop = true;
					return new Action(Type.STOP);
				}
				
				/*
				if(g.lastMovePawn)
				{
					if(g.lastMove[0][0] != 0)
					{
						g.addPawn(x1, y1);
						g.addPawn(x2, y2);
					}
					else
					{
						// STOP
						stop = true;
						return new Action(Type.STOP);
					}
									
					ac = new Action(Type.PAWNS, x1, y1, x2, y2);
				}
				else
				{
					g.addBridge(x1, y1, x2, y2);
					
					ac = new Action(Type.BRIDGE, x1, y1, x2, y2);
				}*/
				
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
				colored = false;
			}
		}
		else
		{	
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
