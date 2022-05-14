package Interfaz;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelInfo extends JPanel implements ActionListener 
{

	public final static String CARGAR = "Cargar Portafolios";

	private static final long serialVersionUID = 1L;

	private InterfazOptimizador principal;

	private JButton btnCargar;

	public PanelInfo( InterfazOptimizador pPrincipal )
	{
		setLayout( new GridLayout( 1,1) );
		setBorder( new TitledBorder( "Controles:" ) );

		principal = pPrincipal;

		btnCargar = new JButton( CARGAR );
		btnCargar.setActionCommand( CARGAR );
		btnCargar.addActionListener( this );
		add(btnCargar);


	}

	public void actionPerformed(ActionEvent f) 
	{
		if( f.getActionCommand() == CARGAR )
		{
			principal.cargarArchivo();
		}

	}

}
