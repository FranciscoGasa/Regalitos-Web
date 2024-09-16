package com.example.application.views.gifts;


import com.example.application.backend.EstadoRegalo;
import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.Gift;
import com.example.application.backend.entity.GiftList;
import com.example.application.backend.entity.User;
import com.example.application.backend.repository.FriendRepository;
import com.example.application.backend.repository.GiftListRepository;
import com.example.application.backend.repository.GiftRepository;
import com.example.application.backend.repository.UserRepository;
import com.example.application.backend.service.RegalitosService;
import com.example.application.views.MainLayout;
import com.example.editor.GiftEditor;
import com.example.editor.GiftListEditor;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@AnonymousAllowed
@Route(value = "/giftview/{listaId}", layout = MainLayout.class)
public class GiftView extends VerticalLayout implements HasUrlParameter<Long> {

    private final GiftRepository giftRepository;
    private final Grid<Gift> grid = new Grid<>(Gift.class,false);
    private final TextField filter = new TextField();
    private final Button addNewBtn = new Button("Añadir Regalo", VaadinIcon.PLUS.create());
    private final UserRepository userRepository;
    private final GiftEditor giftEditor;
    private final GiftListRepository giftlistRepository;
    private final FriendRepository friendRepository;
    private static GiftList lista;
    private Long listaIde;
    private final RegalitosService regalitosService;

    public GiftView(GiftRepository giftRepository,
                    UserRepository userRepository,
                    GiftEditor giftEditor,
                    FriendRepository friendRepository,
                    GiftListRepository giftlistRepository,
                    RegalitosService regalitosService) {

        this.giftRepository = giftRepository;
        this.userRepository = userRepository;
        this.giftEditor = giftEditor;
        this.giftlistRepository = giftlistRepository;
        this.friendRepository = friendRepository;
        this.regalitosService = regalitosService;

//        User user = userRepository.findByUsername("fran");
        User user= regalitosService.getCurrentUser();
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, giftEditor);

        grid.setHeight("300px");

        grid.addColumn(Gift::getName).setHeader("Regalo");
        grid.addColumn(gift -> {
            Friend friend = gift.getFriend();
            return friend != null ? friend.getNickname() : "N/A";
        }).setHeader("Allegado");
        grid.addColumn(gift -> gift.getEstadoRegalo() != null ? gift.getEstadoRegalo().name() : "N/A")
                .setHeader("Estado Regalo");
        grid.addColumn(Gift::getPrice).setHeader("Precio");
        grid.addColumn(new ComponentRenderer<>(gift -> {
            if (gift.getUrl() != null && !gift.getUrl().isEmpty()) {
                Anchor anchor = new Anchor(gift.getUrl(), gift.getUrl());
                anchor.setTarget("_blank");
                return anchor;
            } else {
                return new Span(""); // Mensaje alternativo si la URL es nula o vacía
            }
        })).setHeader("URL");

        filter.setPlaceholder("Filtrar por nombre...");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listGifts(lista != null ? lista.getId() : null));


        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                giftEditor.editGift(e.getValue());
            }
        });

        addNewBtn.addClickListener(e -> {
                Gift newGift = new Gift();
                newGift.setGiftList(lista);
                giftEditor.editGift(newGift);
        });

        giftEditor.setChangeHandler(listaIde-> {
            giftEditor.setVisible(false);
            listGifts(listaIde);
        });

        giftEditor.setCurrentUser(user);

        listGifts(null);
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long listaId) {
        if (listaId != null) {
            lista = giftlistRepository.findById(listaId)
                    .orElseThrow(() -> new IllegalArgumentException("GiftList not found"));
            listaIde=lista.getId();
            giftEditor.setListaId(listaId);
            listGifts(listaId);
            System.out.println("GiftView.setParameter - listaId: " + listaId + ", lista: " + lista);
        } else {
            listGift();
            System.out.println("GiftView.setParameter - No listaId provided.");
        }

    }

    private void listGifts(Long giftListId) {
        String nameFilter = filter.getValue().trim();
        if (!nameFilter.isEmpty()) {
            grid.setItems(giftRepository.findByGiftListIdAndNameContainingIgnoreCase(giftListId, nameFilter));
        } else {
            grid.setItems(giftRepository.findByGiftListId(giftListId));
        }
    }

    private void listGift() {
        String nameFilter = filter.getValue().trim();
        if (!nameFilter.isEmpty()) {
            grid.setItems(giftRepository.findByNameContainingIgnoreCase(nameFilter));
        } else {
            grid.setItems(giftRepository.findAll());
        }
    }
}
