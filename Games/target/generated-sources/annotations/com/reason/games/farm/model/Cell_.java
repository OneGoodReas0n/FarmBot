package com.reason.games.farm.model;

import com.reason.games.farm.enums.Status;
import com.reason.games.farm.model.GameField;
import com.reason.games.farm.model.Plant;
import java.util.Map;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-10-05T19:30:52")
@StaticMetamodel(Cell.class)
public class Cell_ { 

    public static volatile SingularAttribute<Cell, GameField> field;
    public static volatile SingularAttribute<Cell, Plant> plant;
    public static volatile SingularAttribute<Cell, Integer> cellId;
    public static volatile SingularAttribute<Cell, Map> icons;
    public static volatile SingularAttribute<Cell, Status> status;

}