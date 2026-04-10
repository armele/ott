package com.otterly76.ott.searchables.lang;

import com.otterly76.ott.searchables.lang.expression.Expression;
import com.otterly76.ott.searchables.lang.expression.visitor.ContextAwareVisitor;
import com.otterly76.ott.searchables.lang.expression.visitor.Visitor;

import java.util.Optional;

public class StringSearcher {

    public static <T> Optional<T> search(final String search, final Visitor<T> visitor) {
        SLParser slParser = new SLParser(new SLScanner(search).scanTokens());
        return slParser.parse().map(expression -> expression.accept(visitor)).map(visitor::postVisit);
    }

    public static <T, C> Optional<T> search(final String search, final ContextAwareVisitor<T, C> visitor, final C context) {
        SLParser slParser = new SLParser(new SLScanner(search).scanTokens());
        return slParser.parse()
                .map(expression -> expression.accept(visitor, context))
                .map(t -> visitor.postVisit(t, context));
    }

    public static Optional<Expression> expression(final String search) {
        SLParser slParser = new SLParser(new SLScanner(search).scanTokens());
        return slParser.parse();
    }
}
