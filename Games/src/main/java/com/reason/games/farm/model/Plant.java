package com.reason.games.farm.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Cache
@Table(name = "plant")
public class Plant implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private double cost;
    private double profit;
    private int growthTime;

    public Plant() {
    }

    public Plant(String name, double cost, double profit, int growthTime) {
        this.name = name;
        this.cost = cost;
        this.profit = profit;
        this.growthTime = growthTime;
    }

    public int getPlantId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public double getProfit() {
        return round(profit * cost, 2);
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(int growthTime) {
        this.growthTime = growthTime;
    }

    @Override
    public String toString() {
        return "Название " + getName() + " (стоимость: " + getCost()
                + " стоимость урожая: " + getProfit() + " время созревания: "
                + getGrowthTime() + "s)";
    }

    private double round(double number, int scale) {
        int pow = 10;
        for (int i = 1; i < scale; i++) {
            pow *= 10;
        }
        double tmp = number * pow;
        return (double) (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) / pow;
    }
}
