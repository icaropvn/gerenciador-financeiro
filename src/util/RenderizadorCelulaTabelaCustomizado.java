package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorCelulaTabelaCustomizado extends DefaultTableCellRenderer {
	@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if(value instanceof String) {
            String texto = (String)value;
            if(texto.equals("Despesa"))
                cell.setForeground(Color.decode("#dd4b4b"));
            else if(texto.equals("Receita"))
            	cell.setForeground(Color.decode("#5c7bed"));
        }
        
        return cell;
    }
}
