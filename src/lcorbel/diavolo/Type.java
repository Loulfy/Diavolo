package lcorbel.diavolo;

public enum Type
{
	FIRST('P'), SECOND('S'), LIGHT('c'), DARK('f'), STOP('a'), END('F'), PAWNS('+'), BRIDGE('-');
	
	public final char code;
	
	Type(char code)
	{
		this.code = code;
	}
	
	public static Type code(int code)
	{
		Type type = null;
		
		switch(code)
		{
			case (int)'P':
				type = Type.FIRST;
				break;
			case (int)'S':
				type = Type.SECOND;
				break;
			case (int)'c':
				type = Type.LIGHT;
				break;
			case (int)'f':
				type = Type.DARK;
				break;
			case (int)'a':
				type = Type.STOP;
				break;
			case (int)'F':
				type = Type.END;
				break;
			case (int)'+':
				type = Type.PAWNS;
				break;
			case (int)'-':
				type = Type.BRIDGE;
				break;
		}
		
		return type;
	}
}
