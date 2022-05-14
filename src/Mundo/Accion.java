package Mundo;

import java.util.ArrayList;

public class Accion 
{

	private String ticker;
	private ArrayList<Double> retornos;
	
	public Accion( String pticker )
	{
		ticker = pticker;
		retornos = new ArrayList<Double>();
	}
	
	public void anadirRetornos( double pRetorno )
	{
		retornos.add(pRetorno);
	}
	
	public ArrayList<Double> darRetornos()
	{
		return retornos;
	}
	
	public String darTicker()
	{
		return ticker;
	}
	
	public double darRetornoMedio(int indiceInicio, int indiceFin)
	{
		double retornoMed = 0;
		
		for( int i = indiceInicio; i < indiceFin; i++ )
		{
			retornoMed += retornos.get(i);
		}
		
		double n = indiceFin-indiceInicio;
		retornoMed = (1/n)*retornoMed;	
		return retornoMed;
	}	
}
