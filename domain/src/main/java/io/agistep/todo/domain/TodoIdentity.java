package io.agistep.todo.domain;

import io.agistep.identity.Identity;
import lombok.Value;

@Value
class TodoIdentity implements Identity<Long> {
	Long value;
}
