package com.example.application.views;

import com.example.application.backend.entity.User;
import com.example.application.backend.service.RegalitosService;
import com.example.application.views.friends.FriendsView;
import com.example.application.views.iniciarsesion.CerrarsesionView;
import com.example.application.views.iniciarsesion.IniciarsesionView;
import com.example.application.views.listas.ListView;
import com.example.application.views.main.MainView;
import com.example.application.views.registro.RegistroView;
import com.example.config.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;
    private SecurityService securityService;
    private final RegalitosService regalitosService;

    public MainLayout(@Autowired SecurityService securityService,
                      RegalitosService regalitosService) {
        this.securityService = securityService;
        this.regalitosService = regalitosService;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();


        String imageUrl = "https://cdn.icon-icons.com/icons2/1252/PNG/512/1494258859-7_84393.png";
        Image logo = new Image(imageUrl, "Logo de Regalitos");
        logo.setHeight("40px");
        logo.setWidth("auto");

        Span appName = new Span("Regalitos");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);

        HorizontalLayout header;
        if (securityService.getAuthenticatedUser() != null) {
            Button logout = new Button("Logout", click -> securityService.logout());
            header = new HorizontalLayout(logo, appName, logout);
        } else {
            header = new HorizontalLayout(logo, appName);
        }

        addToNavbar(header);
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        Span appName = new Span("Regalitos");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        User user= regalitosService.getCurrentUser();
        nav.addItem(new SideNavItem("Inicio", MainView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        if(user == null) {
            nav.addItem(new SideNavItem("Registro", RegistroView.class, LineAwesomeIcon.USER.create()));
            nav.addItem(new SideNavItem("Iniciar sesión", IniciarsesionView.class, LineAwesomeIcon.USER.create()));}
        if(user!=null){
            nav.addItem(new SideNavItem("Allegados", FriendsView.class, LineAwesomeIcon.USER.create()));
            nav.addItem(new SideNavItem("Mis listas", ListView.class, LineAwesomeIcon.USER.create()));
            nav.addItem(new SideNavItem("Cerrar sesión", CerrarsesionView.class, LineAwesomeIcon.USER.create()));
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}