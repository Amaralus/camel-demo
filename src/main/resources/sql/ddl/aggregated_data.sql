create table demo.aggregated_data
(
    id                    uuid                     default gen_random_uuid() not null
        constraint aggregated_data_pk
            primary key,
    iteration             bigint,                         -- текущая итерация агрегации
    group_field_1         text,
    group_field_2         bigint,
    aggregation_result    integer,
    aggregation_completed boolean default false not null, -- флаг состояния что агрегация проводок завершена
    created_at            timestamp with time zone default now()             not null,
    updated_at            timestamp with time zone default now()             not null,
    constraint iteration_group
        unique (iteration, group_field_1, group_field_2)
);
