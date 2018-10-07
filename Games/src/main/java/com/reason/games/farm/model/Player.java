package com.reason.games.farm.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Cache
@Table(name = "player")
public class Player implements Serializable{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;
    private String name;
    private double balance;
    private final static double STARTMONEY = 100;
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "player")
    private GameField field;
    
    public Player() {
        this.balance = STARTMONEY;
    }
    
    public Player(String name) {
        this.name = name;
        this.balance = STARTMONEY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getId() {
        return playerId;
    }

    public void setField(GameField field) {
        this.field = field;
    }

    public GameField getField() {
        return field;
    }
    
    
}
