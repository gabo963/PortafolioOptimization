package Mundo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Optimizador {

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

	public String[][] dataVarCo(double[][] varco) 
	{
		String[][] data = new String[acciones.length][acciones.length+1];

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

	public double calcularRiesgoPortafolio( double[][] varco, double[][] pesos ) throws Exception
	{
		double[][] riesgo;
		
		riesgo = MatrixOperations.dotProduct(MatrixOperations.dotProduct(pesos, varco), MatrixOperations.transpose(pesos));
		
		return Math.pow(riesgo[0][0], (1.0/2.0))*Math.sqrt(252);
	}
	
	public double calcularRetornoPortafolio( int indiceInicio, int indiceFin, double[][] pesos ) throws Exception
	{
		double[][] retornos = new double[acciones.length][1];
		
		for( int i = 0; i < acciones.length; i++ )
		{
			retornos[i][0] = acciones[i].darRetornoMedio(indiceInicio, indiceFin);
		}
		
		retornos = MatrixOperations.dotProduct(pesos,retornos);
		
		return retornos[0][0]*252;
	}
	
	public double[][] encontrarPesosOptimos( double retorno, int indiceInicio, int indiceFin ) throws Exception
	{
		double[][] pesos = new double[acciones.length][1];
		
		double[][] varco = calcularVarCovar(indiceInicio, indiceFin);
		
		//Construccion de la matriz A.
		
		double[][] a = new double[varco.length+2][varco[0].length+2];
		
		for( int i = 0; i < varco.length; i++ )
		{
			for( int j = 0; j < varco[0].length; j++ )
			{
				a[i][j] = varco[i][j];
			}
		}
		
		for( int i = 0; i < acciones.length; i++ )
		{
			a[acciones.length][i] = acciones[i].darRetornoMedio(indiceInicio, indiceFin);
			a[i][acciones.length] = acciones[i].darRetornoMedio(indiceInicio, indiceFin);
			a[acciones.length+1][i] = -1.0;
			a[i][acciones.length+1] = -1.0;
		}
		
		a[acciones.length][acciones.length] = 0.0;
		a[acciones.length+1][acciones.length] = 0.0;
		a[acciones.length][acciones.length+1] = 0.0;
		a[acciones.length+1][acciones.length+1] = 0.0;
		
		//Construccion de la matriz de retorno.
		
		double[][] r = new double[varco.length+2][1];
		
		r[acciones.length+1][0] = 1.0;
		r[acciones.length][0] = retorno;
		
		//Construccion de la matriz de pesos.
		
		double[][] x = new double[varco.length+2][1];
		
		for( int i = 0; i < varco.length; i++ )
		{
			x[i][0] = 1.0/Double.valueOf(varco.length);
		}
		
		x[varco.length][0] = 1.0;
		x[varco.length+1][0] = 1.0;
		
		//Calculo de resultados.
		
		double[][] result = conjugateGradient(a, x, r);
		
		for( int i = 0; i < acciones.length; i++ )
		{
			pesos[i][0] = result[i][0];
		}
		
		return MatrixOperations.transpose(pesos);
	}
	
	public double[][] conjugateGradient( double[][] Q, double[][] x, double[][] b ) throws Exception
	{	
		double[][] s = MatrixOperations.addOrSubstract(b, MatrixOperations.dotProduct(Q, x),-1);
		double[][] p = s;
		
		double e = 0.0000001;
		
		int k = 0;
		
		while( MatrixOperations.dotProduct(MatrixOperations.transpose(s), s)[0][0] >= e )
		{
			
			double[][] alpha1 = MatrixOperations.dotProduct(MatrixOperations.transpose(s), s);
			
			double[][] alpha2 = MatrixOperations.dotProduct(MatrixOperations.dotProduct(MatrixOperations.transpose(p),Q),p);
			
			double[][] a = MatrixOperations.dotProduct(alpha1, MatrixOperations.inverseDet(alpha2));
			
			x = MatrixOperations.addOrSubstract(x, MatrixOperations.dotProduct(p, a), 1);
			s = MatrixOperations.addOrSubstract(s, MatrixOperations.dotProduct(MatrixOperations.dotProduct(Q,p),a), -1);
			
			double[][] beta1 = MatrixOperations.dotProduct(MatrixOperations.transpose(s), s);
			double[][] B = MatrixOperations.dotProduct(beta1, MatrixOperations.inverseDet(alpha1));
			
			p = MatrixOperations.addOrSubstract(s, MatrixOperations.dotProduct(p, B), 1);
			
			k++;
		}
		
		return x;
	}
	
	public String[][] mostrarA( double retorno, int indiceInicio, int indiceFin ) throws Exception
	{		
		double[][] varco = calcularVarCovar(indiceInicio, indiceFin);
		
		//Construccion de la matriz A.
		
		String[][] a = new String[varco.length+2][varco[0].length+2];
		
		for( int i = 0; i < varco.length; i++ )
		{
			for( int j = 0; j < varco[0].length; j++ )
			{
				a[i][j] = varco[i][j] + "";
			}
		}
		
		for( int i = 0; i < acciones.length; i++ )
		{
			a[acciones.length][i] = acciones[i].darRetornoMedio(indiceInicio, indiceFin)+ "";
			a[i][acciones.length] = acciones[i].darRetornoMedio(indiceInicio, indiceFin)+ "";
			a[acciones.length+1][i] = -1+ "";
			a[i][acciones.length+1] = -1+ "";
		}
		
		a[acciones.length][acciones.length] = 0 +"";
		a[acciones.length+1][acciones.length] = 0+"";
		a[acciones.length][acciones.length+1] = 0+"";
		a[acciones.length+1][acciones.length+1] = 0+"";
		
		return a;
	}
	
	public String[] aNames() 
	{
		String[] colNames = new String[acciones.length+2];

		for(int i = 0; i < acciones.length; i++ )
		{
			colNames[i] = acciones[i].darTicker();
		}
		
		colNames[acciones.length] = "r";
		colNames[acciones.length+1] = "e";

		return colNames;
	}
	
	
}
