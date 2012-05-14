/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Eng.MohamedKamel
 */
public class CellModelRender extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof JLabel) {
            JLabel jl = (JLabel) value;
            setText(jl.getText());
            setIcon(jl.getIcon());
        } else {
            if (value instanceof String) {
                setText((String) value);
            } else {
                setText(value.toString());
            }
        }

// highlight selected item
        setBackground(isSelected ? list.getSelectionBackground()
                : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground()
                : list.getForeground());

        return this;
    }
}
