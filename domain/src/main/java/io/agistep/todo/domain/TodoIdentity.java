package io.agistep.todo.domain;

import io.agistep.identity.Identity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

@Data
@AllArgsConstructor
class TodoIdentity implements Identity<Long> {
	Long value;
}
