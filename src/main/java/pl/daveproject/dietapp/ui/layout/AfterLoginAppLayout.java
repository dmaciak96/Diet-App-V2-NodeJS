package pl.daveproject.dietapp.ui.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;
import pl.daveproject.dietapp.bmi.ui.BmiView;
import pl.daveproject.dietapp.caloricneeds.ui.TotalCaloricNeedsView;
import pl.daveproject.dietapp.exception.UserNotFoundException;
import pl.daveproject.dietapp.exception.UserNotLoginException;
import pl.daveproject.dietapp.product.ui.ProductView;
import pl.daveproject.dietapp.recipe.ui.RecipeView;
import pl.daveproject.dietapp.security.service.UserService;
import pl.daveproject.dietapp.shoppinglist.ui.ShoppingListView;
import pl.daveproject.dietapp.ui.component.WebdietNotification;
import pl.daveproject.dietapp.ui.component.type.WebdietNotificationType;
import pl.daveproject.dietapp.ui.dashboard.DashboardView;

@Slf4j
public class AfterLoginAppLayout extends AbstractAppLayout {

    private final UserService userService;

    public AfterLoginAppLayout(UserService userService) {
        super(true, true);
        this.userService = userService;
        addToNavbar(createAvatar());
        addToDrawer(getMenuTabs());
    }

    private HorizontalLayout createAvatar() {
        var avatar = new Avatar();
        try {
            var currentUserEmail = userService.getCurrentUser().getEmail();
            var currentUser = userService.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new UserNotFoundException(currentUserEmail));            avatar = new Avatar(currentUser.getFullName());
        } catch (UserNotLoginException e) {
            WebdietNotification.show(getTranslation("error-message.user-not-login"), WebdietNotificationType.ERROR);
            log.error("Login user not found for current session: ", e);
        } catch (RuntimeException e) {
            WebdietNotification.show(getTranslation("error-message.unexpected"), WebdietNotificationType.ERROR);
            log.error("Jwt token processing error: ", e);
        }

        var layout = new HorizontalLayout(createAvatarMenuBar(avatar));
        layout.setWidthFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        layout.addClassNames(LumoUtility.Margin.Right.MEDIUM);
        return layout;
    }

    private MenuBar createAvatarMenuBar(Avatar avatar) {
        var menuBar = new MenuBar();
        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
        var subMenu = menuBar.addItem(avatar).getSubMenu();
        subMenu.addItem(getTranslation("bmi-view.title"), e ->
                UI.getCurrent().navigate(BmiView.class));
        subMenu.addItem(getTranslation("total-caloric-needs.title"), e ->
                UI.getCurrent().navigate(TotalCaloricNeedsView.class));
        subMenu.addItem(getTranslation("avatar.logout"), e ->
                UI.getCurrent().getPage().setLocation("/logout"));
        return menuBar;
    }

    private Tabs getMenuTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.DASHBOARD, getTranslation("side-menu.dashboard"), DashboardView.class),
                createTab(VaadinIcon.CONNECT, getTranslation("side-menu.products"), ProductView.class),
                createTab(VaadinIcon.COFFEE, getTranslation("side-menu.recipes"), RecipeView.class),
                createTab(VaadinIcon.CART, getTranslation("side-menu.shopping-lists"), ShoppingListView.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String menuLabel, Class<? extends Component> viewClass) {
        var icon = viewIcon.create();
        icon.addClassNames("menu-icon");

        var link = new RouterLink();
        link.add(icon, new Span(menuLabel));
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}
