package MatrixOperations;

public class DeterminantInverse implements Runnable
{
	private double[][] matrix;
	private int x, y;
	private double result;
	private volatile boolean finish;
	
	public DeterminantInverse( double[][] matrix, int x, int y )
	{
		this.matrix = matrix;
		this.y = y;
		this.x = x;
		this.result = 0;
		this.finish = false;
	}
			
	@Override
	public void run( )
	{
		try
		{
			result = determinant( coFactor(this.matrix, this.x, this.y) );
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
		
		finish = true;
		
		synchronized (this) {
			this.notify();
		}
	}
	
	public double getResult() throws InterruptedException
	{
		synchronized (this) {
			if( !finish )
			{
				this.wait();
			}
		}
		return result;
	}
	
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
}
