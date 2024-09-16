package com.example.application.backend.entity;

import com.example.application.backend.EstadoRegalo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Gift extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    private double price;

    private EstadoRegalo estadoRegalo;

    private String url;

    @ManyToOne
    @JoinColumn(name="giftlist_id")
    @NotNull
    @JsonIgnoreProperties({"gifts"})
    private GiftList giftList;

    @ManyToOne
    @JoinColumn(name="friend_id")
    @NotNull
    @JsonIgnoreProperties({"gifts"})
    private Friend friend;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public GiftList getGiftList() {
        return giftList;
    }

    public void setGiftList(GiftList giftList) {
        this.giftList = giftList;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public EstadoRegalo getEstadoRegalo() {
        return estadoRegalo;
    }
    public void setEstadoRegalo(EstadoRegalo estadoRegalo) {
        this.estadoRegalo = estadoRegalo;
    }
}
