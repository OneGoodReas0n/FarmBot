package com.reason.games.farm.model;

import com.reason.games.farm.model.Cell;
import com.reason.games.farm.model.Player;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-10-13T17:45:15")
@StaticMetamodel(GameField.class)
public class GameField_ { 

    public static volatile ListAttribute<GameField, Cell> cells;
    public static volatile SingularAttribute<GameField, Integer> fieldId;
    public static volatile SingularAttribute<GameField, Player> player;

}