package com.reason.games.farm.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public abstract class Specification<T> {

    public abstract Predicate toPredicate(Root<T> root, CriteriaBuilder criteria);
}

