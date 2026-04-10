package com.otterly76.ott.searchables.api.autcomplete;

import com.otterly76.ott.searchables.api.TokenRange;
import com.otterly76.ott.searchables.lang.StringSearcher;
import com.otterly76.ott.searchables.lang.expression.type.ComponentExpression;
import com.otterly76.ott.searchables.lang.expression.type.GroupingExpression;
import com.otterly76.ott.searchables.lang.expression.type.LiteralExpression;
import com.otterly76.ott.searchables.lang.expression.type.PairedExpression;
import com.otterly76.ott.searchables.lang.expression.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Consumer;

public class CompletionVisitor implements Visitor<TokenRange>, Consumer<String> {

    private final List<TokenRange> tokens = new ArrayList<>();
    private TokenRange lastRange = TokenRange.EMPTY;

    public void reset() {
        tokens.clear();
        lastRange = TokenRange.EMPTY;
    }

    protected void reduceTokens() {
        ListIterator<TokenRange> iterator = tokens.listIterator(tokens.size());
        TokenRange last = null;
        while (iterator.hasPrevious()) {
            TokenRange previous = iterator.previous();
            if (last != null && last.covers(previous)) {
                last.addRange(previous);
                iterator.remove();
            } else {
                last = previous;
            }
        }
    }

    public List<TokenRange> tokens() { return tokens; }

    public Optional<TokenRange> tokenAt(final int position) {
        return tokens.stream().filter(range -> range.contains(position)).findFirst();
    }

    public TokenRange rangeAt(final int position) {
        return tokenAt(position).orElse(TokenRange.EMPTY);
    }

    @Override
    public TokenRange visitGrouping(final GroupingExpression expr) {
        TokenRange leftRange = expr.left().accept(this);
        getAndPushRange();
        TokenRange rightRange = expr.right().accept(this);
        return TokenRange.encompassing(leftRange, rightRange);
    }

    @Override
    public TokenRange visitComponent(final ComponentExpression expr) {
        TokenRange leftRange = expr.left().accept(this);
        addToken(getAndPushRange());
        TokenRange rightRange = expr.right().accept(this);
        return addToken(TokenRange.encompassing(leftRange, rightRange));
    }

    @Override
    public TokenRange visitLiteral(final LiteralExpression expr) {
        return addToken(getAndPushRange(expr.displayValue().length()));
    }

    @Override
    public TokenRange visitPaired(final PairedExpression expr) {
        TokenRange leftRange = addToken(expr.first().accept(this));
        TokenRange rightRange = addToken(expr.second().accept(this));
        return addToken(TokenRange.encompassing(leftRange, rightRange));
    }

    private TokenRange addToken(final TokenRange range) {
        this.tokens.add(range.recalculate());
        return range;
    }

    private TokenRange getAndPushRange() {
        return getAndPushRange(1);
    }

    private TokenRange getAndPushRange(final int end) {
        TokenRange oldRange = lastRange;
        lastRange = TokenRange.between(lastRange.end(), lastRange.end() + end);
        return TokenRange.between(oldRange.end(), oldRange.end() + end);
    }

    @Override
    public void accept(final String search) {
        reset();
        StringSearcher.search(search, this);
    }

    @Override
    public TokenRange postVisit(final TokenRange obj) {
        this.reduceTokens();
        return Visitor.super.postVisit(obj);
    }
}
