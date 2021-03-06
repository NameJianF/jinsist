package jinsist.expectations;

import jinsist.matchers.Arguments;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class OrderedExpectations implements Expectations {
    private List<Expectation> expectations = new ArrayList<>();
    private boolean wasUnexpectedInvocation = false;

    @Override
    public <ReturnType, MockType> void recordStub(
            Class<MockType> classToMock,
            MockType instance,
            Method method,
            Arguments arguments,
            ReturnType result
    ) {
        ExpectedInvocation<MockType> invocation = new ExpectedInvocation<>(classToMock, instance, method, arguments);
        expectations.add(new Expectation<>(invocation, result));
    }

    @Override
    public <MockType> Object execute(
            Class<MockType> classToMock,
            MockType instance,
            Method method,
            Object[] arguments
    ) {
        Invocation<MockType> invocation = new Invocation<>(classToMock, instance, method, arguments);
        if (expectations.isEmpty()) {
            throw new UnexpectedInvocation(invocation);
        }
        Expectation expectation = expectations.remove(0);

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

    @Override
    public void verify() {
        if (!expectations.isEmpty() || wasUnexpectedInvocation) {
            throw new UnmetExpectations();
        }
    }
}
