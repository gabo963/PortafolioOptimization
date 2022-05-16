package Interfaz;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelResultados extends JPanel
{

	private JTextArea textInfo;

	public PanelResultados()
	{
		setLayout( new BorderLayout() );
		setSize(200,700);
		setBorder( new TitledBorder( "Resultados:" ) );
		textInfo = new JTextArea("En la tabla se mostraran los pesos encontrados por la optimizacion,\ny aca se encontraran los resultados de cada uno de los escenarios\nout of sample o backtesting.\n***********************************************************************\n");
		textInfo.setEditable(false);
		add(textInfo, BorderLayout.CENTER);
		
	}
	
	public void actualizar( String nuevaCadena )
	{
		textInfo.append(nuevaCadena);
        this.revalidate();
        this.repaint();
        this.updateUI();
	}

}
