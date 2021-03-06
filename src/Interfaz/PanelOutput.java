package Interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

public class PanelOutput extends JPanel
{

	private JTable tabInfo;

	public PanelOutput()
	{
		setLayout( new BorderLayout() );
		setBorder( new TitledBorder( "Datos:" ) );
		tabInfo = new JTable();
		add(tabInfo, BorderLayout.CENTER);
		
	}
	
	public void actualizar( String[] colNames, String[][] data )
	{
		this.removeAll();
		tabInfo = new JTable( data, colNames);
		tabInfo.setPreferredScrollableViewportSize( new Dimension(10, 10) );
		tabInfo.setFillsViewportHeight(true);
		
		JScrollPane sp = new JScrollPane(tabInfo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tabInfo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
        add(sp, BorderLayout.CENTER);
        tabInfo.repaint();
        this.revalidate();
        this.repaint();
        this.updateUI();
	}

}
