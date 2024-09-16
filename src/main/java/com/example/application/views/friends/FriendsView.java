package com.example.application.views.friends;

import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.User;
import com.example.application.backend.repository.FriendRepository;
import com.example.application.backend.repository.UserRepository;
import com.example.application.backend.service.RegalitosService;
import com.example.application.views.MainLayout;
import com.example.editor.FriendEditor;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.awt.*;

//@SpringComponent
//@Scope("prototype")
//@PermitAll
@AnonymousAllowed
@Route(value = "friends", layout = MainLayout.class)
//@PageTitle("Tus allegados")
//@Secured("USER")
public class FriendsView extends VerticalLayout {

    private final UserRepository userRepository;
    final Grid<Friend> grid;
    private final FriendRepository friendRepository;
    private final FriendEditor friendEditor;
    final TextField filter;
    private final Button addNewBtn;
    private final RegalitosService regalitosService;

    @Autowired
    public FriendsView(
            FriendEditor friendEditor,
            UserRepository userRepository,
            FriendRepository friendRepository,
            RegalitosService regalitosService) {

        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.grid = new Grid<>(Friend.class);
        this.friendEditor = friendEditor;
        this.filter = new TextField();
        this.addNewBtn = new Button("Añadir Amigo", VaadinIcon.PLUS.create());
        this.regalitosService = regalitosService;

//        User user = userRepository.findByUsername("fran");
        User user = regalitosService.getCurrentUser();
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no autenticado");
        }

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, friendEditor);

        grid.setHeight("300px");
        grid.setColumns("nickname");

        filter.setPlaceholder("Filtrar por nickname");

        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listFriends(user.getId()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                friendEditor.editFriend(e.getValue()); //e.getvalue?
            }
        });

        addNewBtn.addClickListener(e -> {
            Friend newFriend = new Friend();
            newFriend.setUser(user); // Asocia el nuevo amigo con el user
            friendEditor.editFriend(newFriend);
//
        });

        friendEditor.setChangeHandler(() -> {
            friendEditor.setVisible(false);
            listFriends(user.getId());
        });

        listFriends(user.getId());
    }

    private void listFriends(Long userId) {
        String nicknameFilter = filter.getValue().trim(); // Obtener el valor del filtro

        if (!nicknameFilter.isEmpty()) {

            grid.setItems(friendRepository.findByUserIdAndNicknameContainingIgnoreCase(userId, nicknameFilter));
        } else {

            grid.setItems(friendRepository.findByUserId(userId));
        }
    }
}