package com.mvplugin.testingbukkit.answers;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class SetterAnswer<ARG_TYPE> implements Answer<Void> {

    @Override
    public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
        set((ARG_TYPE) invocationOnMock.getArguments()[0]);
        return null;
    }

    protected abstract void set(ARG_TYPE value);
}
