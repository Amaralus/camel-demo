create sequence demo.iteration_seq;

create table demo.iteration_counter
(
    id        integer                                 not null
        constraint iteration_counter_pk
            primary key,
    iteration bigint default nextval('iteration_seq') not null
);

alter sequence demo.iteration_seq owned by demo.iteration_counter.iteration;

insert into demo.iteration_counter (id)
values (1);