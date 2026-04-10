package com.otterly76.ott.searchables.lang.expression.visitor;

import com.otterly76.ott.searchables.lang.expression.type.ComponentExpression;
import com.otterly76.ott.searchables.lang.expression.type.GroupingExpression;
import com.otterly76.ott.searchables.lang.expression.type.LiteralExpression;
import com.otterly76.ott.searchables.lang.expression.type.PairedExpression;

public interface ContextAwareVisitor<R, C> {

    R visitGrouping(GroupingExpression expr, C context);
    R visitComponent(ComponentExpression expr, C context);
    R visitLiteral(LiteralExpression expr, C context);
    R visitPaired(PairedExpression expr, C context);

    default R postVisit(R obj, C context) {
        return obj;
    }
}
