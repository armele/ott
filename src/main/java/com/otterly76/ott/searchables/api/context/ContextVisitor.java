package com.otterly76.ott.searchables.api.context;

import com.otterly76.ott.searchables.lang.expression.type.ComponentExpression;
import com.otterly76.ott.searchables.lang.expression.type.GroupingExpression;
import com.otterly76.ott.searchables.lang.expression.type.LiteralExpression;
import com.otterly76.ott.searchables.lang.expression.type.PairedExpression;
import com.otterly76.ott.searchables.lang.expression.visitor.Visitor;

public final class ContextVisitor<T> implements Visitor<SearchContext<T>> {

    private final SearchContext<T> context = new SearchContext<>();

    @Override
    public SearchContext<T> visitGrouping(final GroupingExpression expr) {
        expr.left().accept(this);
        expr.right().accept(this);
        return context;
    }

    @Override
    public SearchContext<T> visitComponent(final ComponentExpression expr) {
        if (expr.left() instanceof LiteralExpression leftLit && expr.right() instanceof LiteralExpression rightLit) {
            context.add(new SearchComponent<>(leftLit.value(), rightLit.value()));
        }
        return context;
    }

    @Override
    public SearchContext<T> visitLiteral(final LiteralExpression expr) {
        context.add(new SearchLiteral<>(expr.value()));
        return context;
    }

    @Override
    public SearchContext<T> visitPaired(final PairedExpression expr) {
        expr.first().accept(this);
        expr.second().accept(this);
        return context;
    }
}
