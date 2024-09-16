package com.example.application.backend.entity;

import com.example.application.backend.EstadoLista;
import com.example.application.backend.EstadoRegalo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.PostLoad;
import java.util.LinkedList;
import java.util.List;

@Entity
public class GiftList extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @ManyToOne
    @JoinColumn(name="user_id")
    @NotNull
    @JsonIgnoreProperties({"giftLists"})
    private User user;

    @OneToMany(mappedBy = "giftList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gift> gifts = new LinkedList<>();

    public List<Gift> getGift() {
        return gifts;
    }
    public void setGift(List<Gift> gifts) {
        this.gifts = gifts;
    }

    private double costeTotal;

    private EstadoLista estadoLista;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getCosteTotal() {
        return costeTotal;
    }
    public void setCosteTotal(double costeTotal) {
        this.costeTotal = costeTotal;
    }
    public EstadoLista getEstadoLista() {
        return estadoLista;
    }
    public void setEstadoLista(EstadoLista estadoLista) {
        this.estadoLista = estadoLista;
    }

    @PostLoad
    private void postload(){
        calcularCosteTotal();
        calcularEstadoLista();
    }

    private void calcularCosteTotal() {
        double total = 0;
        for (Gift gift : gifts) {
            total += gift.getPrice();
        }
        this.costeTotal = total;
    }

    private void calcularEstadoLista() {
        if(costeTotal != 0) {
            boolean encontrado=false;
            for (Gift gift : gifts) {
                if(gift.getEstadoRegalo()== EstadoRegalo.POR_COMPRAR) {
                    encontrado=true;
                    break;
                }
            }
            if(encontrado)
                this.estadoLista=EstadoLista.PENDIENTE;
            else
                this.estadoLista=EstadoLista.CERRADA;
        }

    }



}
