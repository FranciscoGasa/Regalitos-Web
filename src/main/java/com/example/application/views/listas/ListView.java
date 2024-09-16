package com.example.application.views.listas;

import com.example.application.backend.entity.Gift;
import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.GiftList;
import com.example.application.backend.entity.User;
import com.example.application.backend.repository.FriendRepository;
import com.example.application.backend.repository.GiftListRepository;
import com.example.application.backend.repository.UserRepository;
import com.example.application.backend.service.RegalitosService;
import com.example.application.views.MainLayout;
import com.example.application.views.gifts.GiftView;
import com.example.editor.FriendEditor;
import com.example.editor.GiftListEditor;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AnonymousAllowed
@Route(value = "lists", layout = MainLayout.class)
//@PageTitle("Tus listas")
//@Secured("USER")
public class ListView extends VerticalLayout {

    private final UserRepository userRepository;
    final Grid<GiftList> grid;
    private final GiftListRepository giftRepository;
    private final GiftListEditor giftlistEditor;
    final TextField filter;
    private final Button addNewBtn;
    private final RegalitosService regalitosService;

    @Autowired
    public ListView(
            GiftListEditor giftlistEditor,
            UserRepository userRepository,
            RegalitosService regalitosService,
            GiftListRepository giftRepository) {

        this.userRepository = userRepository;
        this.giftRepository = giftRepository;
        this.grid = new Grid<>(GiftList.class);
        this.giftlistEditor = giftlistEditor;
        this.filter = new TextField();
        this.addNewBtn = new Button("Añadir Lista", VaadinIcon.PLUS.create());
        this.regalitosService=regalitosService;

//        User user = userRepository.findByUsername("fran");
        User user= regalitosService.getCurrentUser();
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, giftlistEditor);

        grid.setHeight("300px");
        grid.setColumns("name","estadoLista","costeTotal");

        grid.addComponentColumn(gift -> {
            Button editButton = new Button("Editar", VaadinIcon.EDIT.create());
            editButton.addClickListener(click -> {
                UI.getCurrent().navigate(GiftView.class,gift.getId() );
            });
            return editButton;
        }).setHeader("Acciones");

        filter.setPlaceholder("Filtrar por name");

        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listGiftList(user.getId()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                giftlistEditor.editFriend(e.getValue());
            }
        });

        addNewBtn.addClickListener(e ->{
            GiftList newGiftList = new GiftList();
            newGiftList.setUser(user); // Asocia el nuevo amigo con user
            giftlistEditor.editFriend(newGiftList);
        } );

        giftlistEditor.setChangeHandler(() -> {
            giftlistEditor.setVisible(false);
            listGiftList(user.getId());
        });

        listGiftList(user.getId());
    }

private void listGiftList(Long userId) {
    String nameFilter = filter.getValue().trim(); // Obtener el valor del filtro

    if (!nameFilter.isEmpty()) {
        grid.setItems(giftRepository.findByUserIdAndNameContainingIgnoreCase(userId, nameFilter));
    } else {
        grid.setItems(giftRepository.findByUserId(userId));
    }
}
}
