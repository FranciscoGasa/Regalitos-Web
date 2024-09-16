package com.example.application.views.friends;

import com.example.application.backend.entity.Friend;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class FriendForm extends FormLayout {
    TextField nickname = new TextField("Nickname");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Friend> binder = new BeanValidationBinder<>(Friend.class);
    
    public FriendForm() {
        addClassName("friend-form");
        binder.bindInstanceFields(this);
        
        add(
                nickname,
                createButtonsLayout()
        );
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setFriend(Friend friend) {
        binder.setBean(friend);
    }

    // Events
    public static abstract class FriendFormEvent extends ComponentEvent<FriendForm> {
        private Friend friend;

        protected FriendFormEvent(FriendForm source, Friend contact) {
            super(source, false);
            this.friend = contact;
        }

        public Friend getFriend() {
            return friend;
        }
    }

    public static class DeleteEvent extends FriendFormEvent {
        DeleteEvent(FriendForm source, Friend friend) {
            super(source, friend);
        }

    }

    public static class SaveEvent extends FriendFormEvent {
        SaveEvent(FriendForm source, Friend friend) {
            super(source, friend);
        }
    }

    public static class CloseEvent extends FriendFormEvent {
        CloseEvent(FriendForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }
    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
