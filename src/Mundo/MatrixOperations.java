package Mundo;

public class MatrixOperations {
	
	/**
	 * Transpone la matriz ingresada.
	 * @param matrix la matriz a transponer.
	 * @return la matriz transpuesta.
	 */
	public static double[][] transpose( double[][] matrix )
	{
		double[][] result = new double[matrix[0].length][matrix.length];

		for( int i = 0; i < result.length; i++ )
		{
			for( int j = 0; j < result[0].length; j++ )
			{
				result[i][j] = matrix[j][i];
			}
		}
		return result;
	}

	/**
	 * Multiplica la matriz dada por una constante.
	 * @param matrix matriz a la que se le quiere multiplicar la constante.
	 * @param constant constante que se quiere multiplicar.
	 * @return la matriz multiplicada por la constante.
	 */
	public static double[][] singleMultiplication( double[][] matrix, double constant )
	{
		double[][] result = new double[matrix.length][matrix[0].length];

		for( int i = 0; i < result.length; i++ )
		{
			for( int j = 0; j < result[0].length; j++ )
			{
				result[i][j] = constant * matrix[i][j];
			}
		}

		return result;
	}

	/**
	 * Calcula el producto punto entre dos matrices.
	 * @param matrix1 Matriz 1
	 * @param matrix2 Matriz 2
	 * @return El resultante del producto punto.
	 * @throws Exception si las columnas de la matriz 1 no son iguales a las filas de la matriz 2.
	 */
	public static double[][] dotProduct( double[][] matrix1, double[][] matrix2 ) throws Exception
	{

		if( matrix1[0].length != matrix2.length )
		{
			throw new Exception("Las columnas de la matriz 1 deben ser las mismas que las filas de la matriz 2.");
		}

		double[][] result = new double[matrix1.length][matrix2[0].length];

		for( int i = 0; i < result.length; i++ )
		{
			for( int j = 0; j < result[0].length; j++ )
			{
				for( int k = 0; k < matrix1[0].length; k ++ )
				{
					result[i][j] += matrix1[i][k]*matrix2[k][j];
				} 
			}
		}

		return result;
	}

	/**
	 * Suma o resta matrices con el mismo tamanio.
	 * @param matrix1 La matriz uno
	 * @param matrix2 La matriz dos
	 * @param signo puede ser 1 o -1 respresentando (+, -) respectivamente.
	 * @return el resultado de la suma de las dos matrices.
	 * @throws Exception si las matrices no son del mismo tamanio o el |signo| no es igual a 1.
	 */
	public static double[][] addOrSubstract( double[][] matrix1, double[][] matrix2, int signo ) throws Exception
	{	
		if( matrix1.length != matrix2.length || matrix1[0].length != matrix2[0].length )
		{
			throw new Exception("Las matrices no son del mismo tamanio.");
		}
		if( Math.abs(signo) != 1 )
		{
			throw new Exception("El valor de signo no es igual a 1.");
		}

		double[][] result = new double[matrix1.length][matrix1[0].length];

		for( int i = 0; i < result.length; i++ )
		{
			for( int j = 0; j < result[0].length; j++ )
			{
				result[i][j] = matrix1[i][j] + (matrix2[i][j])*Double.valueOf(signo);
			}
		}

		return result;
	}

	/**
	 * Encuentra el cofactor dependiendo de la posicion dada de una matriz.
	 * @param matrix matriz en la que se encuentra el cofactor.
	 * @param i fila donde no esta el cofactor.
	 * @param j columna donde no esta el cofactor.
	 * @return el cofactor de la matriz segun las posiciones.
	 */
	public static double[][] coFactor(double[][] matrix, int x, int y)
	{
		double[][] result = new double[matrix.length-1][matrix[0].length-1];

		int i = 0;
		int j = 0;

		for( int k = 0; k < matrix.length; k++ )
		{
			j = 0;
			for( int l = 0; l< matrix[0].length; l++ )
			{
				if( k != x && l != y )
				{
					result[i][j] = matrix[k][l]; 
				}
				if( l != y )
				{
					j++;
				}
			}

			if( k != x )
			{
				i++;
			}
		}
		return result;
	}

	/**
	 * Retorna el determinante de una matriz.
	 * @param matrix matriz a la que se le calculara el determinante.
	 * @return el determinante.
	 * @throws Exception si la matriz no es cuadrada.
	 */
	public static double determinant( double[][] matrix ) throws Exception
	{
		if( matrix.length != matrix[0].length )
		{
			throw new Exception("La matriz no es cuadrada");
		}

		if( matrix.length == 1 )
		{
			return matrix[0][0];
		}

		if( matrix.length == 2 )
		{
			return (matrix[0][0]*matrix[1][1]) - (matrix[0][1]*matrix[1][0]);
		}

		double sum = 0;
		
		for( int j = 0; j < matrix[0].length; j++ )
		{
			double res = matrix[0][j] * Double.valueOf(Math.pow( -1.0 , ((1.0)+(j+1.0)))) * determinant(coFactor(matrix, 0, j));	
			sum += res;
		}
		return sum;
	}

	/**
	 * Invierte la matriz dada por parametro utilizando el determinante.
	 * @param matrix la matriz que se quiere invertir.
	 * @return la matriz inversa.
	 * @throws Exception si la matriz no es cuadrada.
	 */
	public static double[][] inverseDet( double[][] matrix ) throws Exception
	{
		double det = determinant(matrix);
		
		if( det == 0 )
		{
			throw new Exception("No es invertible");
		}
		
		if( matrix.length == 1 )
		{
			matrix[0][0] = 1 / matrix[0][0]; 
			return matrix;
		}  
		
		double[][] result = new double[matrix.length][matrix[0].length];

		for( int i = 0; i < matrix.length; i++ )
		{
			for( int j = 0; j < matrix[0].length; j++ )
			{	
				result[i][j] = determinant( coFactor(matrix, i, j) ) * Double.valueOf(Math.pow( -1.0 , ((i+1)+(j+1))));		
			}
		}

		result = singleMultiplication(transpose(result), (1/det));

		return result;
	}
}