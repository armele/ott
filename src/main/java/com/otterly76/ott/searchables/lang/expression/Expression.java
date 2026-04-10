package com.otterly76.ott.searchables.lang.expression;

import com.otterly76.ott.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.otterly76.ott.searchables.lang.expression.visitor.Visitor;

public abstract class Expression {

    public abstract <R> R accept(final Visitor<R> visitor);

    public abstract <R, C> R accept(final ContextAwareVisitor<R, C> visitor, final C context);
}
