package com.reason.games.farm.specification;

import com.reason.games.farm.model.Cell;
import com.reason.games.farm.model.Cell_;
import com.reason.games.farm.model.GameField;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CellSpecification extends Specification<Cell>{

    public static Specification<Cell> whereFieldIs(final GameField field){
        return new Specification<Cell>() {
            @Override
            public Predicate toPredicate(Root<Cell> root, CriteriaBuilder criteria) {
                return criteria.equal(root.get(Cell_.field), field);
            }
        };
    }   

    @Override
    public Predicate toPredicate(Root<Cell> root, CriteriaBuilder criteria) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
