package pl.daveproject.dietapp.ui.layout;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import pl.daveproject.dietapp.ui.dashboard.DashboardView;

@CssImport("./style.css")
@JsModule("./prefers-color-scheme.js")
public class AbstractAppLayout extends AppLayout {
    private static final String APPLICATION_NAME = "Diet App";

    public AbstractAppLayout() {
        this(false, false);
    }

    public AbstractAppLayout(boolean withDrawerToggle, boolean withNavbar) {
        if (withDrawerToggle) {
            addToNavbar(new DrawerToggle());
        }
        if (withNavbar) {
            addToNavbar(createHeader());
        }
    }

    private HorizontalLayout createHeader() {
        var title = new H3(APPLICATION_NAME);
        title.addClickListener(e -> UI.getCurrent().navigate(DashboardView.class));
        title.getStyle().set("cursor", "pointer");

        var header = new HorizontalLayout(title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.setHeightFull();
        header.addClassNames(LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        return header;
    }
}
