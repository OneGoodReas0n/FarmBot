package com.reason.games.farm.model;

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
    
    public void clear(){
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

    public void showInfo() {
        showIcon();
        if (getPlant() != null) {
            System.out.println("Растение: " + getPlant().getName());
            System.out.println("Период: "+changeStatus(this.status));
            System.out.println("Осталось времени: "+getLeftTime());
        }
        else{
            System.out.println("\nРастение: пусто");
        }
    }
    
    public String changeStatus(Status status){
        String result = "null";
        if(status == status.OCCUPIED){result = "созревает";}
        if(status == status.READY){result = "созрело";}
        return result;
    }
}
