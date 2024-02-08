create sequence demo.iteration_seq;

create table demo.iteration_counter
(
    id        integer                                 not null -- id основной ключ для быстрого доступа
        constraint iteration_counter_pk
            primary key,
    iteration bigint default nextval('iteration_seq') not null -- значение текущей итерации
);

alter sequence demo.iteration_seq owned by demo.iteration_counter.iteration;

-- инициализация единственной записи что должна быть в таблице, нельзя ее удалять
insert into demo.iteration_counter (id)
values (1);