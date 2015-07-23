package com.mvplugin.testingbukkit.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class NoArgVoidAnswer implements Answer<Void> {

    @Override
    public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
        call();
        return null;
    }

    protected abstract void call();
}
