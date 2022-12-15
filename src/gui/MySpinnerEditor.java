package gui;

import javax.swing.*;
import java.awt.*;

public class MySpinnerEditor extends DefaultCellEditor {
    JSpinner sp;
    JSpinner.DefaultEditor defaultEditor;
    JTextField text;

    public MySpinnerEditor() {
        super(new JTextField());
        SpinnerModel model = new SpinnerNumberModel(1, 1, 25, 1);
        sp = new JSpinner(model);
        defaultEditor = ((JSpinner.DefaultEditor)sp.getEditor());
        text = defaultEditor.getTextField();
    }

    public Component getTableCellEditorComponent(JTable table, Object
            value, boolean isSelected, int row, int column)
    {
        sp.setValue(value);
        return sp;
    }

    public Object getCellEditorValue() {
        return sp.getValue();
    }
}
