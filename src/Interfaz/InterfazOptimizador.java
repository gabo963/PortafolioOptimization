package Interfaz;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import com.sun.org.apache.xml.internal.resolver.helpers.Debug;

import Mundo.Optimizador;

public class InterfazOptimizador extends JFrame 
{

	private Optimizador mundo;
	
	// Paneles.
	
	private PanelInfo panelInfo;
	
	private PanelOutput panelOutput;
	
	public InterfazOptimizador() throws Exception
	{
		setTitle("Optimizador de Portafolios") ;
		setSize(700,700);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		// Van los paneles.
		
		panelInfo = new PanelInfo(this);
		add( panelInfo, BorderLayout.NORTH );
		
		panelOutput = new PanelOutput();
		add( panelOutput, BorderLayout.CENTER );

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
		panelOutput.actualizar(mundo.colVarcoNames(), mundo.dataVarCo());
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
