package com.reason.games.farm.model;

import com.reason.games.farm.factory.FarmGameFactory;
import com.reason.games.farm.repository.CellRepository;
import com.reason.games.farm.specification.CellSpecification;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.Cache;

@Entity
@Cache
@Table(name = "field")
public class GameField implements Serializable{
 
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fieldId;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Player player;
    @OneToMany(mappedBy = "field")
    private final List<Cell> cells = new ArrayList<>();
    @Transient
    private final CellRepository cellRepository = FarmGameFactory.createCellRepository();
    
    public GameField() {
    }

    public int getId() {
        return fieldId;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setGameCells(Cell cell) {
        this.cells.add(cell);
    }
        
    public void showGameField(){
        int i=0;
        System.out.println("Игровые клетки\n");
        for (Cell cell : cellRepository.getQuery(CellSpecification.whereFieldIs(this))) {
            cell.showIcon();
            i++;
            if(i%4==0){
                System.out.println();
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getFieldId() {
        return fieldId;
    }
    
    /*public Cell getOneCell(int num){
        if(getCells().get(num-1)!=null){
            return getCells().get(num-1);
        }   
        return null;
    }*/
    
}
