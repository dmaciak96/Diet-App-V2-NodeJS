package pl.daveproject.dietapp.ui.component.filecomponent;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class FileDownloader extends Anchor {

    public FileDownloader(String fileName, byte[] fileData) {
        super();
        var resource = initializeStreamResource(fileName, fileData);
        initializeAnchor(resource);
    }

    private StreamResource initializeStreamResource(String fileName, byte[] fileData) {
        return new StreamResource(fileName, () -> new ByteArrayInputStream(fileData));
    }

    private void initializeAnchor(StreamResource resource) {
        this.setHref(resource);
        this.getElement().setAttribute("download", true);
        UI.getCurrent().getPage().open(this.getHref());
        this.setVisible(false);
    }
}
