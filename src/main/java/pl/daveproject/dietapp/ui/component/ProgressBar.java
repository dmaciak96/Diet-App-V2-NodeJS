package pl.daveproject.dietapp.ui.component;

import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;

public class ProgressBar extends VerticalLayout {
    private final com.vaadin.flow.component.progressbar.ProgressBar progressBar;
    private final NativeLabel labelText;
    private final Span labelValue;

    public ProgressBar(String text) {
        this.progressBar = new com.vaadin.flow.component.progressbar.ProgressBar();
        this.progressBar.setValue(0.0);
        this.labelText = new NativeLabel(text);
        this.labelText.setId("pblabel");
        this.progressBar.getElement().setAttribute("aria-labelledby", "pblabel");
        this.labelValue = new Span("0%");

        var progressBarLabel = new HorizontalLayout(labelText, labelValue);
        progressBarLabel.setWidthFull();
        progressBarLabel.setJustifyContentMode(JustifyContentMode.BETWEEN);
        add(progressBarLabel, progressBar);
    }

    public void updateLabel(String text) {
        this.labelText.setText(text);
    }

    public void setValue(double value) {
        this.progressBar.setValue(value);
        this.labelValue.setText(Math.round(value * 100) + "%");
    }

    public void setSuccessColor() {
        progressBar.addThemeVariants(ProgressBarVariant.LUMO_SUCCESS);
    }

    public void setErrorColor() {
        progressBar.addThemeVariants(ProgressBarVariant.LUMO_ERROR);
    }
}
