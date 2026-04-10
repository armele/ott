package com.otterly76.ott.searchables.lang.expression.visitor;

import com.otterly76.ott.searchables.lang.expression.type.ComponentExpression;
import com.otterly76.ott.searchables.lang.expression.type.GroupingExpression;
import com.otterly76.ott.searchables.lang.expression.type.LiteralExpression;
import com.otterly76.ott.searchables.lang.expression.type.PairedExpression;

public interface Visitor<R> {

    R visitGrouping(GroupingExpression expr);
    R visitComponent(ComponentExpression expr);
    R visitLiteral(LiteralExpression expr);
    R visitPaired(PairedExpression expr);

    default R postVisit(R obj) {
        return obj;
    }
}
