package pl.daveproject.dietapp.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DownloadButton extends Button {
    public DownloadButton() {
        setText(getTranslation("component.download-button"));
        setIcon(new Icon(VaadinIcon.DOWNLOAD));
    }
}
