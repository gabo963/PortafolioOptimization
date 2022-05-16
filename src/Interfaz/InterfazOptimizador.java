package Interfaz;

import java.awt.BorderLayout;
import java.io.File;

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
		setSize(900,700);
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
		try 
		{
			int indiceInicio = 12;
			int indiceFin = 112;
			double retorno = 0.0008;
			double[][] pesos = mundo.encontrarPesosOptimos(retorno, indiceInicio, indiceFin);
			
			System.out.println("Riesgo: " + mundo.calcularRiesgoPortafolio( mundo.calcularVarCovar(indiceInicio, indiceFin), pesos));
			System.out.println("Retorno: " + mundo.calcularRetornoPortafolio(indiceInicio, indiceFin, pesos));
			
			//panelOutput.actualizar(mundo.colVarcoNames(), mundo.dataVarCo(mundo.calcularVarCovar(0, 100)));
			panelOutput.actualizar( mundo.aNames() , mundo.mostrarA(retorno, indiceInicio, indiceFin));
			
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
}
