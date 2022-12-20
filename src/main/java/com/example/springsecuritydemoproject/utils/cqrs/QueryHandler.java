package com.example.springsecuritydemoproject.utils.cqrs;

public interface QueryHandler<R, Q extends Query<R>> {

    R handle(Q query);

}
