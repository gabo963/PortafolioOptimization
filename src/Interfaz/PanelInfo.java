package Interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelInfo extends JPanel implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	
	public final static String CARGAR = "Cargar Portafolios";
	public final static String CALCULAR = "Calcular Optimizacion";

	private InterfazOptimizador principal;

	private JButton btnCargar;
	private JButton btnCalcu;

	public PanelInfo( InterfazOptimizador pPrincipal )
	{
		setLayout( new GridLayout( 1,2) );
		setBorder( new TitledBorder( "Controles:" ) );

		principal = pPrincipal;

		btnCargar = new JButton( CARGAR );
		btnCargar.setActionCommand( CARGAR );
		btnCargar.addActionListener( this );
		add(btnCargar);
		
		btnCalcu = new JButton( CALCULAR );
		btnCalcu.setActionCommand( CALCULAR );
		btnCalcu.addActionListener( this );
		btnCalcu.setEnabled(false);
		add(btnCalcu);

	}
	
	public void activarCalcular()
	{
		btnCalcu.setEnabled(true);
	}

	public void actionPerformed(ActionEvent f) 
	{
		if( f.getActionCommand() == CARGAR )
		{
			principal.cargarArchivo();
		}
		if( f.getActionCommand() == CALCULAR )
		{
			principal.calcular();
		}

	}

}
