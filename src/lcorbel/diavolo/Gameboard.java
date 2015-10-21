package lcorbel.diavolo;

import java.util.Arrays;

public class Gameboard
{
	public int size;
	
	public int player;
	private int island;
	
	public int lastMove[][];
	public boolean lastMovePawn;
	//private boolean firstPawn;
	
	private int save[][];

	private int pawns[][];
	private int bridges[][];
	private boolean visited[][];
	private boolean visitedDiag[][];
	
	private final int orientation[][] = {{2,0},{2,1},{2,2},{1,2},{0,2},{-1,2},{-2,2},{-2,1},{-2,0},{-2,-1},{-2,-2},{-1,-2},{0,-2},{1,-2},{2,-2},{2,-1}};
	
	public Gameboard(int size)
	{
		this.size = size;
		this.player = 1;
		this.pawns = new int[size+2][size+2];
		this.bridges = new int[size+2][size+2];
		this.visited = new boolean[size+2][size+2];
		this.visitedDiag = new boolean[size+2][size+2];
		
		this.lastMove = new int[2][2];
		this.lastMove[0][0] = size;
		this.lastMove[0][1] = 0;
		this.lastMove[1][0] = size;
		this.lastMove[1][1] = size;
		
		this.lastMovePawn = true;
		//this.firstPawn = true;
	}
	
	public Gameboard(Gameboard gb)
	{
		this(gb.size);
		this.player = gb.player;
		this.lastMovePawn = gb.lastMovePawn;
		
		for(int i=0; i < size+2; i++)
		{
			this.pawns[i] = gb.pawns[i].clone();
			this.bridges[i] = gb.pawns[i].clone();
			this.visited[i] = gb.visited[i].clone();
			this.visitedDiag[i] = gb.visitedDiag[i].clone();
		}
		for(int i=0; i < 2; i++)
		{
			this.lastMove[i] = gb.lastMove[i].clone();
		}
	}
	
	@Override
	public Gameboard clone()
	{
		return new Gameboard(this);
	}
	
	@Override
	public String toString()
	{
		String aff = "";
		
		for(int y=0; y < size+2; y++)
		{
			for(int x=0; x < size+2; x++)
			{
				if(pawns[y][x] < 0)
				{
					aff+=(pawns[y][x])+" ";
				}
				else
				{
					aff+="."+(pawns[y][x])+" ";
				}
			}
			aff+="\n";
		}
		
		aff+="----\n";
		for(int y=0; y < size+2; y++)
		{
			for(int x=0; x < size+2; x++)
			{
				if(bridges[y][x] < 10)
				{
					aff+="."+(bridges[y][x])+" ";
				}
				else
				{
					aff+=(bridges[y][x])+" ";
				}
				
			}
			aff+="\n";
		}
		
		return aff;
	}
	
	public void switchPlayer()
	{
		player = -player;
	}
	
	public void savePawns()
	{
		if(save == null)
		{
			this.save = new int[size+2][size+2];
		}
		for(int y=0; y < size+2; y++)
		{
			for(int x=0; x < size+2; x++)
			{
				save[x][y] = pawns[x][y];
			}
		}
	}
	
	public void loadPawns()
	{
		if(save != null)
		{
			for(int y=0; y < size+2; y++)
			{
				for(int x=0; x < size+2; x++)
				{
					pawns[x][y] = save[x][y];
				}
			}
		}
	}

	public boolean canAddPawn(int x, int y)
	{
		boolean ok = true;
		
		// First : empty case
		if(pawns[x][y] == 0)
		{
			// Second : not neighboring island
			if(pawns[x][y+1] == 4*player){ok=false;}
			if(pawns[x+1][y+1] == 4*player){ok=false;}
			if(pawns[x+1][y] == 4*player){ok=false;}
			if(pawns[x+1][y-1] == 4*player){ok=false;}
			if(pawns[x][y-1] == 4*player){ok=false;}
			if(pawns[x-1][y-1] == 4*player){ok=false;}
			if(pawns[x-1][y] == 4*player){ok=false;}
			if(pawns[x-1][y+1] == 4*player){ok=false;}
			
			// Third : not under a bridge
			if(bridges[x][y] != 0){ok=false;}
			
			// Fourth : not exceeding 4 island's size
			fillVisited(false);
			pawns[x][y] = player;
			island = 0;
			
			recRoutePawn(x, y);
			if(island > 4){ok=false;}
			pawns[x][y] = 0;
			
			// Sixth : island without sand
			fillVisitedDiag(false);
			if (island == 4 && diagRecRoutePawn(x, y))
			{
				pawns[x][y] = 0;
				ok=false;
			}
		}
		else
		{
			ok = false;
		}
		
		return ok;
	}
	
	public void addPawn(int x, int y)
	{	
		//Init
		fillVisited(false);
		pawns[x][y] = player;
		island = 0;
		
		//Route and set
		recRoutePawn(x, y);
		fillVisited(false);
		setRecRoutePawn(x, y);
	}
	
	public boolean canAddBridge(int x1, int y1, int x2, int y2)
	{
		boolean ok = true;
		
		if(pawns[x1][y1]*player > 0 && pawns[x2][y2]*player > 0 &&
		bridges[x1][y1] == 0 && bridges[x2][y2] == 0)
		{
			int mx = x1+x2;
			int my = y1+y2;
			
			if(mx%2 == 0 && my%2 == 0 && pawns[mx/2][my/2] != 0){ok=false;}
			if(mx%2 == 1 && (pawns[mx/2][my/2] != 0 || pawns[mx/2+1][my/2] != 0)){ok=false;}
			if(my%2 == 1 && (pawns[mx/2][my/2] != 0 || pawns[mx/2][my/2+1] != 0)){ok=false;}
			if(mx%2 == 1 && (bridges[mx/2][my/2] != 0 || bridges[mx/2+1][my/2] != 0)){ok=false;}
			if(my%2 == 1 && (bridges[mx/2][my/2] != 0 || bridges[mx/2][my/2+1] != 0)){ok=false;}
			
			int dir = vecToDir(x2-x1, y2-y1);
			if(dir == 3 && (bridges[x1+1][y1] == 7 || bridges[x1+2][y1-1] == 7 || bridges[x1+2][y1+1] == 7 || bridges[x1+3][y1] == 7)){ok=false;}
			if(dir == 7 && (bridges[x1-1][y1] == 3 || bridges[x1-2][y1-1] == 3 || bridges[x1-2][y1+1] == 3 || bridges[x1-3][y1] == 3)){ok=false;}
			if(dir == 11 && (bridges[x1-1][y1] == 15 || bridges[x1-2][y1-1] == 15 || bridges[x1-2][y1+1] == 15 || bridges[x1-3][y1] == 15)){ok=false;}
			if(dir == 15 && (bridges[x1+1][y1] == 11 || bridges[x1+2][y1-1] == 11 || bridges[x1+2][y1+1] == 11 || bridges[x1+3][y1] == 11)){ok=false;}
		}
		else
		{
			ok = false;
		}
		
		return ok;
	}
	
	private int[] dirToVec(int direction)
	{	
		return orientation[direction-1];
	}
	
	private int vecToDir(int x, int y)
	{
		for(int i=0; i < 15; i++)
		{
			if(orientation[i][0] == x && orientation[i][1] == y)
			{
				return (i+1);
			}
		}
		
		return 0;
	}
	
	public void addBridge(int x1, int y1, int x2, int y2)
	{
		bridges[x1][y1] = vecToDir(x2-x1, y2-y1);
		bridges[x2][y2] = vecToDir(x1-x2, y1-y2);
		
		int mx = x1+x2;
		int my = y1+y2;
		
		if(mx%2 == 0 && my%2 == 0)
		{
			bridges[mx/2][my/2] = 17;
		}
		if(mx%2 == 1)
		{
			bridges[mx/2][my/2] = 17;
			bridges[mx/2+1][my/2] = 17;
		}
		if(my%2 == 1)
		{
			bridges[mx/2][my/2] = 17;
			bridges[mx/2][my/2+1] = 17;
		}
	}
	
	private void fillVisited(boolean value)
	{
		for(boolean[] array : visited)
		{
			Arrays.fill(array, value);
		}
	}
	
	private void fillVisitedDiag(boolean value)
	{
		for(boolean[] array : visitedDiag)
		{
			Arrays.fill(array, value);
		}
	}
	
	private void recRoutePawn(int x, int y)
	{
		if(pawns[x][y]*player > 0 && !visited[x][y])
		{
			island++;
			visited[x][y] = true;
			
			recRoutePawn(x-1, y);
			recRoutePawn(x+1, y);
			recRoutePawn(x, y-1);
			recRoutePawn(x, y+1);
		}
	}
	
	private boolean diagRecRoutePawn(int x, int y)
	{
		if(visited[x][y] && !visitedDiag[x][y])
		{
			visitedDiag[x][y] = true;
			if(pawns[x-1][y-1]*player > 0 && !visited[x-1][y-1]){return false;}
			else if(pawns[x-1][y+1]*player > 0 && !visited[x-1][y+1]){return false;}
			else if(pawns[x+1][y-1]*player > 0 && !visited[x+1][y-1]){return false;}
			else if(pawns[x+1][y+1]*player > 0 && !visited[x+1][y+1]){return false;}

			if(!diagRecRoutePawn(x, y-1)){return false;}
			if(!diagRecRoutePawn(x-1, y)){return false;}
			if(!diagRecRoutePawn(x+1, y)){return false;}
			if(!diagRecRoutePawn(x, y+1)){return false;}
			return true;
		}
		return true;
	}
	
	private void setRecRoutePawn(int x, int y)
	{
		if(pawns[x][y]*player > 0 && !visited[x][y])
		{
			visited[x][y] = true;
			pawns[x][y] = island*player;
			setRecRoutePawn(x-1, y);
			setRecRoutePawn(x+1, y);
			setRecRoutePawn(x, y-1);
			setRecRoutePawn(x, y+1);
		}
	}
	
	public void nextMove()
	{		
		if(lastMovePawn)
		{
			nextMovePawn();
		}
		else
		{
			nextMoveBridge();
		}
	}
	
	private void nextMovePawn()
	{
		savePawns();
		
		int x1 = lastMove[0][0];
		int x2 = lastMove[1][0];
		int y1 = lastMove[0][1];
		int y2 = lastMove[1][1];
		
		while(true)
		{
			x2++;
			if(x2 > size)
			{
				x2 = 1;
				y2++;
				if(y2 > size)
				{
					x1++;
					if(x1 > size)
					{
						x1 = 1;
						y1++;
						if(y1 > size)
						{
							lastMove[0][0] = 0;
							break;
						}
					}
					x2=x1+1;
					y2=y1;
					if(x2 > size)
					{
						x2 = 1;
						y2++;
						if (y2 > size)
						{
							lastMove[0][0] = 0;
							break;
						}	
					}
				}
			}
			if(canAddPawn(x1, y1))
			{
				addPawn(x1, y1);
				if(canAddPawn(x2, y2))
				{
					lastMove[0][0] = x1;
					lastMove[0][1] = y1;
					lastMove[1][0] = x2;
					lastMove[1][1] = y2;
					loadPawns();
					break;
				}
				else
				{
					loadPawns();
				}
			}
		}
		if(y1 > size || y2 > size)
		{
			lastMove[0][0] = size;
			lastMove[0][1] = 0;
			lastMove[1][0] = size-2;
			lastMove[1][1] = 0;
			nextMoveBridge();
		}
	}
	
	public void nextMoveBridge()
	{
		lastMovePawn = false;
		int x1 = lastMove[0][0];
		int x2 = lastMove[1][0];
		int y1 = lastMove[0][1];
		int y2 = lastMove[1][1];
		int dir = 8;
		
		while(true)
		{
			//System.out.println(this);
			dir++;
			if(dir > 8)
			{
				dir = 1;
				x1++;
				if(x1 > size)
				{
					x1 = 1;
					y1++;
					if(y1 > size)
					{
						lastMove[0][0] = 0;
						break;
					}
				}
			}
			int vec[] = dirToVec(dir);
			x2 = x1+vec[0];
			y2 = y1+vec[1];
			if(x2 > 0 && x2 <= size && y2 <= size && canAddBridge(x1, y1, x2, y2))
			{
				lastMove[0][0] = x1;
				lastMove[0][1] = y1;
				lastMove[1][0] = x2;
				lastMove[1][1] = y2;
				break;
			}
		}
	}
}
