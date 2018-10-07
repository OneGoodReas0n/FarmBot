package com.reason.games.farm.model;

import com.reason.games.farm.model.GameField;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-10-05T19:30:52")
@StaticMetamodel(Player.class)
public class Player_ { 

    public static volatile SingularAttribute<Player, Double> money;
    public static volatile SingularAttribute<Player, GameField> field;
    public static volatile SingularAttribute<Player, String> name;
    public static volatile SingularAttribute<Player, Integer> playerId;

}