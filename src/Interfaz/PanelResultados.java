package Interfaz;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		JScrollPane sp = new JScrollPane(textInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		textInfo.setEditable(false);
		add(sp, BorderLayout.CENTER);
		
	}
	
	public void actualizar( String nuevaCadena )
	{
		textInfo.append(nuevaCadena);
        this.revalidate();
        this.repaint();
        this.updateUI();
	}
	
	public void reiniciar( )
	{
		textInfo.setText("En la tabla se mostraran los pesos encontrados por la optimizacion,\ny aca se encontraran los resultados de cada uno de los escenarios\nout of sample o backtesting.\n***********************************************************************\n");
		this.revalidate();
        this.repaint();
        this.updateUI();
	}

}
