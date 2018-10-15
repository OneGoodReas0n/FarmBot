package com.reason.games.farm.specification;

import com.reason.games.farm.model.GameField;
import com.reason.games.farm.model.GameField_;
import com.reason.games.farm.model.Player;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GameFieldSpecification extends Specification<GameField>{

    @Override
    public Predicate toPredicate(Root<GameField> root, CriteriaBuilder criteria) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static Specification<GameField> whereFieldIs(final Player player){
        return new Specification<GameField>() {
            @Override
            public Predicate toPredicate(Root<GameField> root, CriteriaBuilder criteria) {
                return criteria.equal(root.get(GameField_.player), player);
            }
        };
    }  
    
}
