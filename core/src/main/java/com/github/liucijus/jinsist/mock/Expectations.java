package com.github.liucijus.jinsist.mock;

import com.github.liucijus.jinsist.UnmetExpectations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Expectations {
    private List<Expectation> expectations = new ArrayList<>();
    private boolean wasUnexpectedInvocation = false;

    <ReturnType, MockType> void recordStub(
            Class<MockType> classToMock,
            MockType instance,
            Method method,
            Object[] arguments,
            ReturnType result
    ) {
        Invocation<MockType> invocation = new Invocation<>(classToMock, instance, method, arguments);
        expectations.add(new Expectation<>(invocation, result));
    }

    public <MockType> Object execute(
            Class<MockType> classToMock,
            MockType instance,
            Method method,
            Object[] arguments
    ) {
        Expectation expectation = expectations.remove(0);
        Invocation<MockType> invocation = new Invocation<>(classToMock, instance, method, arguments);

        verifyExpectationMatchesInvocation(expectation, invocation);

        return expectation.getResult();
    }

    private <MockType> void verifyExpectationMatchesInvocation(
            Expectation expectation,
            Invocation<MockType> invocation
    ) {
        if (!expectation.isFor(invocation)) {
            wasUnexpectedInvocation = true;
            throw new UnexpectedInvocation(expectation, invocation);
        }
    }

    public void verify() {
        if (!expectations.isEmpty() || wasUnexpectedInvocation) {
            throw new UnmetExpectations();
        }
    }
}
