package com.example.application.views.iniciarsesion;

import com.example.application.backend.service.RegalitosService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route(value = "logout", layout = MainLayout.class)
@AnonymousAllowed
public class CerrarsesionView extends VerticalLayout {

    private final RegalitosService regalitosService;

    public CerrarsesionView(RegalitosService regalitosService) {
        this.regalitosService = regalitosService;
        regalitosService.setCurrentUser(null);
        Span message = new Span("Sesión cerrada con éxito. Hasta pronto");
        add(message);
    }
}
