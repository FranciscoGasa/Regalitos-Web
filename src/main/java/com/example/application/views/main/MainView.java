package com.example.application.views.main;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "")
@AnonymousAllowed
public class MainView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    public MainView() {
        Image image = new Image("https://www.icegif.com/wp-content/uploads/2023/03/icegif-1566.gif", "Descripción de la imagen");
        image.setHeight("900px"); // Ajusta el tamaño de la imagen según tus necesidades
        image.setWidth("auto"); // Ajusta el ancho automáticamente

        add(image);
    }
}
