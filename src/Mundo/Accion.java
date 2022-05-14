package Mundo;

public class Accion 
{

	private String ticker;
	private int[] retornos;
	
	public Accion( String pticker )
	{
		ticker = pticker;
	}
	
	public void asignarRetornos( int[] pRetornos )
	{
		retornos = pRetornos;
	}
	
	public int[] darRetornos()
	{
		return retornos;
	}
	
	public String darTicker()
	{
		return ticker;
	}
	
}
