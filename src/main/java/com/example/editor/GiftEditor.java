package com.example.editor;


import com.example.application.backend.EstadoRegalo;
import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.Gift;
import com.example.application.backend.entity.User;
import com.example.application.backend.repository.FriendRepository;
import com.example.application.backend.repository.GiftRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class GiftEditor extends VerticalLayout implements KeyNotifier {

    private final GiftRepository repository;
    private final FriendRepository friendRepository;
    private Gift gift;
    private Long listaId;
    private User currentUser;

    /* Fields to edit properties in Gift entity */
    TextField name = new TextField("Regalo");
    NumberField price = new NumberField("Precio");
    ComboBox<EstadoRegalo> estadoRegalo = new ComboBox<>("Estado Regalo");
    ComboBox<Friend> friendComboBox = new ComboBox<>("Friend");
    TextField url = new TextField("Url");
    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
//    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel); //He quitado delte, cancel no sale

    Binder<Gift> binder = new Binder<>(Gift.class);
    private ChangeHandler changeHandler;

    @Autowired
    public GiftEditor(GiftRepository repository, FriendRepository friendRepository) {
        this.repository = repository;
        this.friendRepository = friendRepository;

        estadoRegalo.setItems(EstadoRegalo.values());
        List<Friend> friends = friendRepository.findAll();
        friendComboBox.setItems(friends);
        friendComboBox.setItemLabelGenerator(Friend::getNickname); // Mostrar el nombre del amigo

        add(name, price, estadoRegalo, friendComboBox, url, actions);

        binder.bindInstanceFields(this);

        binder.forField(name).bind(Gift::getName, Gift::setName);
        binder.forField(price).bind(Gift::getPrice, Gift::setPrice);
        binder.forField(estadoRegalo).bind(Gift::getEstadoRegalo, Gift::setEstadoRegalo);
        binder.forField(friendComboBox)
                .bind(Gift::getFriend, Gift::setFriend); // Directamente trabajar con Friend
        binder.forField(url).bind(Gift::getUrl, Gift::setUrl);

        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
//        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());
        setVisible(false);
    }

    void delete() {
        if (gift != null && gift.getId() != null) {
            repository.delete(gift);
            changeHandler.onChange(listaId);
            setVisible(false);
        } else {
            System.out.println("Gift o gift.getId() es nulo. No se puede eliminar.");
        }
    }

    void save() {
        repository.save(gift);
        changeHandler.onChange(listaId);
        setVisible(false);
    }

    void cancel(){
        changeHandler.onChange(listaId);
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange(Long listaId);
    }

    public final void editGift(Gift c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            gift = repository.findById(c.getId()).orElse(null);
            if (gift == null) {
                // Manejar caso donde gift no se encuentra en la base de datos
                System.out.println("No se encontró el regalo en la base de datos.");
                setVisible(false);
                return;
            }
        } else {
            gift = c;
        }
       // cancel.setVisible(persisted);

        //delete.setVisible(persisted);

        binder.setBean(gift);

        setVisible(true);

        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }

    public void setListaId(Long listaId) {
        this.listaId = listaId;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // Filtrar amigos basados en el usuario actual
        List<Friend> friends = friendRepository.findByUserId(user.getId());
        friendComboBox.setItems(friends);
    }
}