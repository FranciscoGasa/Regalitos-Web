package com.example.application.views.registro;

import com.example.application.backend.service.RegalitosService;
import com.example.application.views.MainLayout;
import com.example.application.views.iniciarsesion.IniciarsesionView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

//@PageTitle("Registro")
@Route(value = "register", layout = MainLayout.class)
@AnonymousAllowed
public class RegistroView extends VerticalLayout {

    private final RegalitosService userService;

    @Autowired
    public RegistroView(RegalitosService userService) {
        this.userService = userService;

        TextField firstnameField = new TextField("Nombre");
        TextField lastnameField = new TextField("Apellido");
        TextField usernameField = new TextField("Nombre de usuario");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Contraseña");
        PasswordField confirmPasswordField = new PasswordField("Confirmar contraseña");

        Button registerButton = new Button("Registrar");
        registerButton.addClickListener(event -> {
            String firstname = firstnameField.getValue();
            String lastname = lastnameField.getValue();
            String username = usernameField.getValue();
            String email = emailField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                System.err.println("Todos los campos son obligatorios");
                Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                Notification.show("Las contraseñas no coinciden", 3000, Notification.Position.MIDDLE);
                System.err.println("Las contraseñas no coinciden");
                return;
            }

            try {

                userService.registerUser(firstname, lastname, username, email, password);
                System.err.println("Usuario registrado con éxito");
                Notification.show("Usuario registrado con éxito", 3000, Notification.Position.MIDDLE);
                getUI().ifPresent(ui -> ui.navigate(IniciarsesionView.class));
            } catch (IllegalArgumentException e) {
                System.err.println("Error al registrar usuario: " + e.getMessage());
                Notification.show("Error al registrar usuario: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                System.err.println("Error al registrar usuario: " + e.getMessage());
                Notification.show("Error al registrar usuario. Compruebe que todos los campos son correctos " , 3000, Notification.Position.MIDDLE);
                e.printStackTrace();
            }
        });

        Button inicioButton = new Button("Iniciar Sesión", event -> {
            // Navegar a la vista de registro
            getUI().ifPresent(ui -> ui.navigate(IniciarsesionView.class));
        });

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstnameField, lastnameField, usernameField, emailField, passwordField, confirmPasswordField, registerButton, inicioButton);

        add(formLayout);
    }
}
