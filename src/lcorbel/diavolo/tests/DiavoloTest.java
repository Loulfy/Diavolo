package lcorbel.diavolo.tests;

import static org.junit.Assert.*;
import lcorbel.diavolo.Action;
import lcorbel.diavolo.Diavolo;
import lcorbel.diavolo.Gameboard;
import lcorbel.diavolo.Type;

import org.junit.Test;

public class DiavoloTest
{
	@Test
	public void testCopyGameboard()
	{
		Gameboard gb1 = new Gameboard(4);
		Gameboard gb2 = gb1.clone();
		assertNotSame(gb1, gb2);
	}
	
	@Test
	public void testCanAddPawn()
	{
		Gameboard gb = new Gameboard(4);
		//gb.addPawn(1, 1);
		gb.addPawn(1, 2);
		gb.addPawn(2, 1);
		gb.addPawn(1, 3);
		//gb.addPawn(3, 2);
		//System.out.println(gb);
		assertTrue(gb.canAddPawn(3, 2));
	}
	
	@Test
	public void testCanAddBridge()
	{
		Gameboard gb = new Gameboard(4);
		gb.addPawn(1, 1);
		gb.addPawn(3, 3);
		gb.addPawn(2, 1);
		gb.addPawn(1, 3);
		gb.addPawn(3, 2);
		gb.addPawn(1, 4);
		//gb.addBridge(1, 1, 3, 3);
		//System.out.println(gb);
		assertTrue(gb.canAddBridge(3, 2, 1, 4));
		//assertFalse(gb.canAddPawn(2, 2));
	}
	
	@Test
	public void testSaveLoadPawns()
	{
		Gameboard gb = new Gameboard(4);
		gb.addPawn(2, 1);
		gb.savePawns();
		gb.addPawn(1, 1);
		//System.out.println(gb);
		gb.loadPawns();
		//System.out.println(gb);
	}
	
	@Test
	public void testNextMove()
	{
		Gameboard gb = new Gameboard(4);
		gb.addPawn(2, 1);
		gb.addPawn(3, 1);
		gb.addPawn(2, 2);
		gb.addPawn(3, 2);
		gb.addPawn(2, 4);
		
		gb.nextMove();
		System.out.println(gb.lastMove[0][0]+"x"+gb.lastMove[0][1]);
		System.out.println(gb.lastMove[1][0]+"x"+gb.lastMove[1][1]);
		
		gb.nextMove();
		System.out.println(gb.lastMove[0][0]+"x"+gb.lastMove[0][1]);
		System.out.println(gb.lastMove[1][0]+"x"+gb.lastMove[1][1]);
		
		gb.nextMove();
		System.out.println(gb.lastMove[0][0]+"x"+gb.lastMove[0][1]);
		System.out.println(gb.lastMove[1][0]+"x"+gb.lastMove[1][1]);
		
		gb.nextMove();
		System.out.println(gb.lastMove[0][0]+"x"+gb.lastMove[0][1]);
		System.out.println(gb.lastMove[1][0]+"x"+gb.lastMove[1][1]);
	}
	
	@Test
	public void testProtocol()
	{
		Diavolo d = new Diavolo(4);
		Action ac = null;
		
		ac = new Action(Type.FIRST);
		System.out.println(ac);
		ac = d.play(ac);
		System.out.println(ac);
		
		/*
		ac = new Action(Type.PAWNS, 1, 1, 1, 2);
		System.out.println(ac);
		ac = d.play(ac);
		System.out.println(ac);
		*/
		
		ac = new Action(Type.LIGHT);
		System.out.println(ac);
		ac = d.play(ac);
		System.out.println(ac);
		
		ac = new Action(Type.PAWNS, 2, 2, 3, 3);
		System.out.println(ac);
		ac = d.play(ac);
		System.out.println(ac);
		
		ac = new Action(Type.PAWNS, 1, 3, 4, 4);
		System.out.println(ac);
		ac = d.play(ac);
		System.out.println(ac);
		
		ac = new Action(Type.PAWNS, 4, 3, 3, 4);
		System.out.println(ac);
		ac = d.play(ac);
		System.out.println(ac);

		/*
		d.nextAction(new Action(Type.FIRST));
		d.nextAction();
		d.nextAction(new Action(Type.DARK));
		d.nextAction(new Action(Type.PAWNS, 2, 2, 3, 3));
		d.nextAction();
		*/
		//d.nextAction();
		//d.nextAction(new Action(Type.));
	}
}
