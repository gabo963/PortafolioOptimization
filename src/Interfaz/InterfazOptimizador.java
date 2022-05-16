package Interfaz;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import Mundo.Optimizador;

public class InterfazOptimizador extends JFrame 
{
	/**
	 * Serial Version.
	 */
	private static final long serialVersionUID = 2960162770155122459L;

	private Optimizador mundo;

	// Paneles.

	private PanelInfo panelInfo;

	private PanelOutput panelOutput;
	
	private PanelResultados panelResultados;

	public InterfazOptimizador() throws Exception
	{
		setTitle("Optimizador de Portafolios") ;
		setSize(1000,700);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// Van los paneles.

		panelInfo = new PanelInfo(this);
		add( panelInfo, BorderLayout.NORTH );

		panelOutput = new PanelOutput();
		add( panelOutput, BorderLayout.CENTER );
		
		panelResultados = new PanelResultados();
		add(panelResultados, BorderLayout.EAST);
	}

	public void cargarArchivo()
	{
		JFileChooser selectorDeArchivos = new JFileChooser("./" );
		selectorDeArchivos.setDialogTitle( "Elegir el archivo de Portafolios" );

		selectorDeArchivos.setFileFilter( new FileFilter() {

			public String getDescription() {
				return "CSV Files (*.csv)";
			}

			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					String filename = f.getName().toLowerCase();
					return filename.endsWith(".csv");
				}
			}
		} );

		File archivo = null;
		int resultado = selectorDeArchivos.showOpenDialog( this );

		if( resultado == JFileChooser.APPROVE_OPTION )
		{
			archivo = selectorDeArchivos.getSelectedFile();
		}

		if( archivo != null )
		{
			try 
			{
				mundo = new Optimizador( archivo );
				panelOutput.actualizar(mundo.colNames(), mundo.data());
				panelInfo.activarCalcular();
			} 
			catch (Exception e) 
			{
				e.printStackTrace( );
				JOptionPane.showMessageDialog( this, e.getMessage( ), "Cargar", JOptionPane.WARNING_MESSAGE );
			}
		}
	}

	public void calcular() 
	{
		panelResultados.reiniciar();
		try 
		{
			int indiceInicio = 0;
			int indiceFin = 100;
			int indiceTest = 112;
			double retorno = 0.0008;
			int saltos = 12;
			int k = 1;
			
			ArrayList<double[]> pesos = new ArrayList<double[]>();
			
			double retProm = 0;
			double rieProm = 0;
			
			while( indiceTest < mundo.darTamanoFechas())
			{
				//Optimiza y muestra
				
				double[][] peso = mundo.encontrarPesosOptimos(retorno, indiceInicio, indiceFin);
				pesos.add(peso[0]);
				
				double riesgoNum = mundo.calcularRiesgoPortafolio( mundo.calcularVarCovar(indiceFin+1, indiceTest), peso);
				double retornoNum = mundo.calcularRetornoPortafolio(indiceFin+1,indiceTest, peso);
				
				retProm += retornoNum;
				rieProm += riesgoNum;
				
				String riesgo = (""+(100*riesgoNum));
				String retorno1 = (""+(100*retornoNum));
				String cadena = "Optimizacion # " + k + "\n" + "Periodo in sample:\nFechas: " + mundo.darFecha(indiceInicio) + " - " + mundo.darFecha(indiceFin) + "\n" +
				"Periodo out of sample:\nFechas: " + mundo.darFecha(indiceFin+1) + " - " + mundo.darFecha(indiceTest) + "\n" + "Para out of sample: (Cifras anuales.)" + "\n" + "Riesgo: " + riesgo.substring(0, riesgo.indexOf(".")+3) + "% \n" +"Retorno: " + retorno1.substring(0, retorno1.indexOf(".")+3) + "%." 
				+"\n--------------------------------------\n";
				
				panelResultados.actualizar(cadena);
				
				//Continua
				
				indiceInicio += saltos;
				indiceFin += saltos;
				indiceTest += saltos;
				k++;
			}
			
			retProm = retProm / k * 100;
			rieProm = rieProm / k * 100;
			
			String cadena = "Promedios"+ "\n" + "Para out of sample: (Cifras anuales.)" + "\n" + "Riesgo Promedio: " + (""+rieProm).substring(0, (""+rieProm).indexOf(".")+3) + "% \n" +"Retorno promedio: " + (""+retProm).substring(0, (""+retProm).indexOf(".")+3) + "%." 
					+"\n--------------------------------------\n";
					
					panelResultados.actualizar(cadena);
			
			
				
			panelOutput.actualizar(mundo.pesosNames(), mundo.pesosTransform(pesos));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, e.getMessage( ), "Optimización", JOptionPane.ERROR_MESSAGE );
		}
	}

	public static void main(String[] args) 
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			InterfazOptimizador interfaz = new InterfazOptimizador();
			interfaz.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void backtesting() 
	{
		panelResultados.reiniciar();
		
		int ventanaInSample = Integer.parseInt( JOptionPane.showInputDialog(this, "Ingresar la ventana de dias in-sample.") );
		int ventanaOutOfSample = Integer.parseInt( JOptionPane.showInputDialog(this, "Ingresar la ventana de dias out of sample.") );
		double retorno = Double.parseDouble( JOptionPane.showInputDialog(this, "Ingresar el retorno objetivo.") );
		try 
		{
			int indiceInicio = 0;
			int indiceFin = indiceInicio + ventanaInSample;
			int indiceTest = indiceFin + ventanaOutOfSample;
			int saltos = ventanaOutOfSample;
			int k = 1;
			
			ArrayList<double[]> pesos = new ArrayList<double[]>();
			
			double retProm = 0;
			double rieProm = 0;
			
			while( indiceTest < mundo.darTamanoFechas())
			{
				//Optimiza y muestra
				
				double[][] peso = mundo.encontrarPesosOptimos(retorno, indiceInicio, indiceFin);
				pesos.add(peso[0]);
				
				double riesgoNum = mundo.calcularRiesgoPortafolio( mundo.calcularVarCovar(indiceFin+1, indiceTest), peso);
				double retornoNum = mundo.calcularRetornoPortafolio(indiceFin+1,indiceTest, peso);
				
				retProm += retornoNum;
				rieProm += riesgoNum;
				
				String riesgo = (""+(100*riesgoNum));
				String retorno1 = (""+(100*retornoNum));
				String cadena = "Optimizacion # " + k + "\n" + "Periodo in sample:\nFechas: " + mundo.darFecha(indiceInicio) + " - " + mundo.darFecha(indiceFin) + "\n" +
				"Periodo out of sample:\nFechas: " + mundo.darFecha(indiceFin+1) + " - " + mundo.darFecha(indiceTest) + "\n" + "Para out of sample: (Cifras anuales.)" + "\n" + "Riesgo: " + riesgo.substring(0, riesgo.indexOf(".")+3) + "% \n" +"Retorno: " + retorno1.substring(0, retorno1.indexOf(".")+3) + "%." 
				+"\n--------------------------------------\n";
				
				panelResultados.actualizar(cadena);
				
				//Continua
				
				indiceInicio += saltos;
				indiceFin += saltos;
				indiceTest += saltos;
				k++;
			}
			
			retProm = retProm / k * 100;
			rieProm = rieProm / k * 100;
			
			String cadena = "Promedios"+ "\n" + "Para out of sample: (Cifras anuales.)" + "\n" + "Riesgo Promedio: " + (""+rieProm).substring(0, (""+rieProm).indexOf(".")+3) + "% \n" +"Retorno promedio: " + (""+retProm).substring(0, (""+retProm).indexOf(".")+3) + "%." 
					+"\n--------------------------------------\n";
					
					panelResultados.actualizar(cadena);
			
			
				
			panelOutput.actualizar(mundo.pesosNames(), mundo.pesosTransform(pesos));
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, e.getMessage( ), "Optimización", JOptionPane.ERROR_MESSAGE );
		}
	}
}
