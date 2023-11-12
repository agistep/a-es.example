package io.agistep.todo.applications;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnCapture<T> implements Answer<T> {
	private T result;

	@Override
	public T answer(InvocationOnMock invocation) throws Throwable {
		Object ret = invocation.callRealMethod();
		// TODO unchecked cast
		this.result = (T) ret;
		return this.result;
	}

	public T get() {
		return result;
	}
}
