package com.example.editor;


import com.example.application.backend.entity.Gift;
import com.example.application.backend.entity.GiftList;
import com.example.application.backend.repository.GiftListRepository;
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
public class GiftListEditor extends VerticalLayout implements KeyNotifier {

    private final GiftListRepository repository;
    private final GiftRepository giftRepository;

    private GiftList friend;

    /* Fields to edit properties in Customer entity */
    TextField name = new TextField("Nombre");

    /* Action buttons */
    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());
    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    Binder<GiftList> binder = new Binder<>(GiftList.class);
    private ChangeHandler changeHandler;

    @Autowired
    public GiftListEditor(GiftListRepository repository, GiftRepository giftRepository) {
        this.repository = repository;
        this.giftRepository = giftRepository;

        add(name, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        binder.forField(name).bind(GiftList::getName, GiftList::setName);
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
            // Eliminar todos los regalos asociados a la lista
            List<Gift> giftsToDelete = giftRepository.findByGiftListId(friend.getId());
            giftRepository.deleteInBatch(giftsToDelete);

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

    public final void editFriend(GiftList c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {

            friend = repository.findById(c.getId()).get();
        }
        else {
            friend = c;
        }
       // cancel.setVisible(persisted);

        delete.setVisible(persisted);

        binder.setBean(friend);

        setVisible(true);


        name.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }

}
