package com.reason.games.farm.model;

import com.reason.bot.info.StaticInfo;
import com.reason.games.farm.enums.Info;
import com.reason.games.farm.enums.Status;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Cache
@Table(name = "cell")
public class Cell implements Serializable {

    @Id
    @GeneratedValue
    private int cellId;
    private Status status = Status.FREE;
    private final Map<Status, String> icons = new HashMap<>();
    private Plant plant;
    private int leftTime;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "FieldID", nullable = false)
    private GameField field;
    @Transient
    public final static int COUNT = 8;
    @Transient
    private StaticInfo staticInfo = new StaticInfo();

    public Cell() {
        init();
    }

    public void init() {
        icons.put(Status.FREE, getIconFreePlace());
        icons.put(Status.OCCUPIED, getIconPlantPlanted());
        icons.put(Status.READY, getIconPlantReady());
    }

    public int getId() {
        return cellId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPlant(Plant plant) {
        this.status = Status.OCCUPIED;
        this.plant = plant;
        this.leftTime = plant.getGrowthTime();
    }

    public void clear() {
        this.status = Status.FREE;
        this.plant = null;
    }

    public Plant getPlant() {
        if (this.plant != null) {
            return plant;
        }
        return null;
    }

    public GameField getField() {
        return field;
    }

    public void setField(GameField field) {
        this.field = field;
    }

    public int getCellId() {
        return cellId;
    }

    public Map<Status, String> getIcons() {
        return icons;
    }

    private String getIconFreePlace() {
        return "...";
    }

    private String getIconPlantPlanted() {
        return "~ ~";
    }

    private String getIconPlantReady() {
        return "$ $";
    }

    public int getLeftTime() {
        return leftTime;
    }

    public void setLeftTime(int leftTime) {
        this.leftTime = leftTime;
    }

    public void showIcon() {
        for (Map.Entry<Status, String> elem : getIcons().entrySet()) {
            if (elem.getKey().equals(this.status)) {
                System.out.print(elem.getValue() + " ");
            }
        }
    }

    public String showInfo(Map<Info, String> lang) {
        showIcon();
        if (getPlant() != null) {
            if (getStatus() == Status.READY) {
                if (lang.get(Info.LANG).equalsIgnoreCase("ru")) {
                    return String.format(lang.get(Info.CELLHASSTATUSREADY), getPlant().getAlternativeName(), changeStatus(this.status, lang));
                } else {
                    return String.format(lang.get(Info.CELLHASSTATUSREADY), getPlant().getName(), changeStatus(this.status, lang));
                }
            } else {
                if (lang.get(Info.LANG).equalsIgnoreCase("ru")) {
                    return String.format(lang.get(Info.CELLHASSTATUS), getPlant().getAlternativeName(), changeStatus(this.status, lang), "" + this.leftTime) + "s";
                } else {
                    return String.format(lang.get(Info.CELLHASSTATUS), getPlant().getName(), changeStatus(this.status, lang), "" + this.leftTime) + "s";
                }
            }

        } else {
            return lang.get(Info.NOPLANT);
        }
    }

    public String changeStatus(Status status, Map<Info, String> lang) {
        String result = "null";
        if (status == Status.OCCUPIED) {
            result = lang.get(Info.STATUSMATURES);
        }
        if (status == Status.ALLMOSTREADY) {
            result = lang.get(Info.STATUSHALFREADY);
        }
        if (status == Status.READY) {
            result = lang.get(Info.STATUSREADY);
        }
        return result;
    }
}
