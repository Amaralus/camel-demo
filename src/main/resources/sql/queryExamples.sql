insert into demo.aggregated_data (iteration, group_field_1, group_field_2, aggregation_result, aggregated_ids)
values ((select iteration from demo.iteration_counter where id = 1), ?1, ?2, ?3, array [?4])
on conflict on constraint iteration_group do update
    set aggregation_result = demo.aggregated_data.aggregation_result + ?3,
        aggregated_ids     = array_append(demo.aggregated_data.aggregated_ids, ?4),
        updated_at         = now();


update demo.iteration_counter
set iteration = nextval('demo.iteration_seq')
where id = 1
returning iteration;