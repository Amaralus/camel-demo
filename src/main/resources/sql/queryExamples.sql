insert into demo.aggregated_data (iteration, group_field_1, group_field_2, aggregation_result)
values ((select iteration from demo.iteration_counter where id = 1), ?1, ?2, ?3)
on conflict on constraint iteration_group do update
    set aggregation_result = demo.aggregated_data.aggregation_result + ?3,
        updated_at = now()
returning id;


update demo.iteration_counter
set iteration = nextval('demo.iteration_seq')
where id = 1
returning iteration;