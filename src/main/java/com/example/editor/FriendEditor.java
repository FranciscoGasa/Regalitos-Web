package com.example.editor;

import com.example.application.backend.entity.Friend;
import com.example.application.backend.entity.Gift;
import com.example.application.backend.repository.FriendRepository;
import com.example.application.backend.repository.GiftRepository;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class FriendEditor extends VerticalLayout implements KeyNotifier {

    private final FriendRepository repository;
    private final GiftRepository giftRepository;

    private Friend friend;

    /* Fields to edit properties in Customer entity */
    TextField nickname = new TextField("Nombre");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<Friend> binder = new Binder<>(Friend.class);
    private ChangeHandler changeHandler;

    @Autowired
    public FriendEditor(FriendRepository repository, GiftRepository giftRepository) {
        this.repository = repository;
        this.giftRepository = giftRepository;

        add(nickname, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        binder.forField(nickname).bind(Friend::getNickname, Friend::setNickname);
        // Configure and style components
        setSpacing(true);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        addKeyPressListener(Key.ENTER, e -> save());

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> cancel());
        setVisible(false);
    }

    void delete() {
        if (friend != null && friend.getId() != null) {
            // Eliminar todos los regalos asociados al amigo
            List<Gift> giftsToDelete = giftRepository.findByFriendId(friend.getId());
            giftRepository.deleteInBatch(giftsToDelete);

            // Eliminar al amigo
            repository.delete(friend);
            changeHandler.onChange();
            setVisible(false);
        }
    }

    void save() {
        repository.save(friend);
        changeHandler.onChange();
        setVisible(false);
    }

    void cancel(){
        changeHandler.onChange();
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editFriend(Friend c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            // In a more complex app, you might want to load
            // the entity/DTO with lazy loaded relations for editing
            friend = repository.findById(c.getId()).get();
        }
        else {
            friend = c;
        }
        //cancel.setVisible(persisted);

        delete.setVisible(persisted);

        binder.setBean(friend);

        setVisible(true);

        // Focus first name initially
        nickname.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        changeHandler = h;
    }

}
