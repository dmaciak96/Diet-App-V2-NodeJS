package pl.daveproject.webdiet.ui.component;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dialog.Dialog;

public class MessageDialog extends Dialog {
    public MessageDialog(String message) {
        this.setDraggable(true);
        this.add(new Text(message));
    }
}
