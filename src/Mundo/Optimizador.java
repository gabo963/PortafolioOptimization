package Mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Optimizador {
	
	private String ruta;
	private String line;
	private Accion[] acciones;
	private ArrayList<String> fechas;
	
	public Optimizador( File archivo ) throws Exception
	{
		try 
		{
			BufferedReader br = new BufferedReader( new FileReader( archivo ));
			
			line = br.readLine();
			
			String[] valores = line.split(",");
			
			// Se inicializan los arreglos
			acciones = new Accion[ valores.length - 1 ];
			fechas = new ArrayList<String>();
			
			/**
			 * Se crean las acciones con sus tickers.
			 */
			for( int i = 0; i < acciones.length; i++ )
			{
				acciones[i] = new Accion( valores[i+1] );
			}
			
			while( (line = br.readLine()) != null )
			{
				valores = line.split(",");
				
				fechas.add( valores[0] );
				
				for( int i = 0; i < acciones.length; i++ )
				{
					acciones[i].anadirRetornos( Double.parseDouble(valores[i+1]) );
				}
			}
			
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	public String[] colNames() 
	{
		String[] colNames = new String[acciones.length+1];
		
		colNames[0] = "Date";
		
		for(int i = 0; i < acciones.length; i++ )
		{
			colNames[i+1] = acciones[i].darTicker();
		}
		
		return colNames;
	}


	public String[][] data() 
	{
		String[][] data = new String[acciones[0].darRetornos().size()][acciones.length+1];
		
		for( int i = 0; i < acciones[0].darRetornos().size(); i++ )
		{
			data[i][0] = fechas.get(i);
			
			for( int j = 0; j < acciones.length; j++  )
			{
				data[i][j+1] = acciones[j].darRetornos().get(i) + "";
			}
		}
		
		return data;
	}
	
	//Estimacion de parametros.
	public double[][] calcularVarCovar(int indiceInicio, int indiceFin)
	{
		double[][] matriz = new double[acciones.length][acciones.length];
		double n = indiceFin-indiceInicio;
		
		for( int i = 0; i < matriz.length; i++ )
		{
			double media1 = acciones[i].darRetornoMedio(indiceInicio, indiceFin);
			for( int j = 0; j < matriz[0].length; j++ )
			{	
				double media2 = acciones[j].darRetornoMedio(indiceInicio, indiceFin);
				double suma = 0;
				
				for( int k = indiceInicio; k < indiceFin; k++ )
				{
					
					suma += (acciones[i].darRetornos().get(k)-media1)*(acciones[j].darRetornos().get(k)-media2);
				}
				
				matriz[i][j] = (1/(n-1)) * suma;
			}
		}
		return matriz;
	}
	
	public String[] colVarcoNames() 
	{
		String[] colNames = new String[acciones.length+1];
		
		colNames[0] = "";
		
		for(int i = 0; i < acciones.length; i++ )
		{
			colNames[i+1] = acciones[i].darTicker();
		}
		
		return colNames;
	}
	
	public String[][] dataVarCo() 
	{
		String[][] data = new String[acciones.length][acciones.length+1];
		double[][] varco = calcularVarCovar(0, 100);
		
		for( int i = 0; i < acciones.length; i++ )
		{
			data[i][0] = acciones[i].darTicker();
			
			for( int j = 0; j < acciones.length; j++  )
			{
				data[i][j+1] = ""+varco[i][j];
			}
		}
		
		return data;
	}
	
	//TODO: Optimizacion del port.
	
}
