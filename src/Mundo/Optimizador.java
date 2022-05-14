package Mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Optimizador {
	
	private String ruta;
	private String line;
	
	
	public Optimizador( File archivo ) throws Exception
	{
		try 
		{
			BufferedReader br = new BufferedReader( new FileReader( archivo ));
			
			/**
			 * Empieza en el 2019, y va al futuro.
			 */
			
			while( (line = br.readLine()) != null )
			{
				System.out.println(line);
				line = br.readLine();
				break;
			}
			
			br.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	

}
