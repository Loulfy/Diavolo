package lcorbel.diavolo;

public class Action
{
	
	public Type type;
	public int[][] pos;
	
	public Action(Type type, int x1, int y1, int x2, int y2)
	{
		this.type = type;
		this.pos = new int[2][2];
		this.pos[0][0] = x1;
		this.pos[0][1] = y1;
		this.pos[1][0] = x2;
		this.pos[1][1] = y2;
	}
	
	public Action(Type type)
	{
		this(type, 0, 0, 0, 0);
	}
	
	public Action(int code)
	{
		this(code, 0, 0, 0, 0);
	}
	
	public Action(int code, int x1, int y1, int x2, int y2)
	{
		this.pos = new int[2][2];
		this.pos[0][0] = x1;
		this.pos[0][1] = y1;
		this.pos[1][0] = x2;
		this.pos[1][1] = y2;
		
		this.type = Type.code(code);
	}
	
	public String stream()
	{
		String code = "";
		
		if(type == Type.PAWNS || type == Type.BRIDGE)
		{			
			code+= Integer.toString(pos[0][0]-1);
			code+= Integer.toString(pos[0][1]-1);
			code+= type.code;
			code+= Integer.toString(pos[1][0]-1);
			code+= Integer.toString(pos[1][1]-1);
		}
		else
		{
			code+= type.code;
		}
		
		return code;
	}
	
	@Override
	public String toString()
	{
		String aff = "";
		aff+= "Action : "+type+" ";
		aff+= Integer.toString(pos[0][0])+"x"+Integer.toString(pos[0][1])+" ";
		aff+= Integer.toString(pos[1][0])+"x"+Integer.toString(pos[1][1]);
		
		return aff;
	}
}