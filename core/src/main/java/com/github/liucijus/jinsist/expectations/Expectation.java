package com.github.liucijus.jinsist.expectations;

public class Expectation<MockType, ReturnType> {
    private Invocation<MockType> invocation;
    private final ReturnType result;

    Expectation(Invocation<MockType> invocation, ReturnType result) {
        this.invocation = invocation;
        this.result = result;
    }

    Object getResult() {
        return result;
    }

    boolean isFor(Invocation invocation) {
        return this.invocation.equals(invocation);
    }

    Invocation getInvocation() {
        return invocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expectation that = (Expectation) o;

        return invocation.equals(that.invocation)
                && (result != null ? result.equals(that.result) : that.result == null);
    }

    @Override
    public int hashCode() {
        int result1 = invocation.hashCode();
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        return result1;
    }
}