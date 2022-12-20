package com.example.springsecuritydemoproject.utils.cqrs;

public interface Bus {

    <R, C extends Command<R>> R executeCommand(C command);

    <R, Q extends Query<R>> R executeQuery(Q query);

}
