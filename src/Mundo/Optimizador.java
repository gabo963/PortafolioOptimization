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
			
			/**
			 * Empieza en el 2019, y va al futuro.
			 */
			
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
	
	

}
