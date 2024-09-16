package com.example.application.views.iniciarsesion;


import com.example.application.backend.service.RegalitosService;
import com.example.application.views.MainLayout;
import com.example.application.views.main.MainView;
import com.example.config.AuthenticatedUser;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


//@PageTitle("Iniciar sesion")
@Route(value = "login", layout = MainLayout.class)
@AnonymousAllowed
public class IniciarsesionView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser pauthenticatedUser;
    private final RegalitosService regalitosService;

    public IniciarsesionView(AuthenticatedUser ppauthenticatedUser, RegalitosService regalitosService) {
        this.pauthenticatedUser = ppauthenticatedUser;
        this.regalitosService = regalitosService;


        setAlignItems(Alignment.CENTER);
        setSpacing(false);


        H1 title = new H1("Regalitos");


        TextField username = new TextField("Nombre de usuario");
        PasswordField password = new PasswordField("Contraseña");
        Button loginButton = new Button("Iniciar sesión", event -> {
            String usernameValue = username.getValue();
            String passwordValue = password.getValue();
            try {
                if (regalitosService.authenticateb(usernameValue, passwordValue)) {

                    UI.getCurrent().navigate(MainView.class);
                } else {
                    Notification.show("Credenciales inválidas. Nombre de usuario o contraseña incorrectas", 3000, Notification.Position.MIDDLE);
                }
            }catch(UsernameNotFoundException e){
                Notification.show("Credenciales inválidas.Nombre de usuario o contraseña incorrectas", 3000, Notification.Position.MIDDLE);
            }
        });

        Button registerButton = new Button("Registrarse", event -> {
            getUI().ifPresent(ui -> ui.navigate("/register"));
        });
        registerButton.getStyle().set("margin-top", "10px");

        add(title, username, password, loginButton, registerButton);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!IniciarsesionView.class.equals(event.getNavigationTarget())){
            event.rerouteTo(IniciarsesionView.class);
            if(event.getLocation()
                    .getQueryParameters()
                    .getParameters()
                    .containsKey("error")) {
                event.forwardTo("");
            }
        }

    }
}