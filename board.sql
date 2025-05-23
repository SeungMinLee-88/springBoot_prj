select *
from board
 where board_title like replace('%%', '\\', '\\\\') 
order by created_time desc
;
select *
from board
 where board_writer like replace('%aa%', '\\', '\\\\') 
order by created_time desc
;

select board.*
from board
;


select
        be1_0.id,
        be1_0.board_contents,
        be1_0.board_hits,
        be1_0.board_pass,
        be1_0.board_title,
        be1_0.board_writer,
        be1_0.created_time,
        be1_0.file_attached,
        be1_0.updated_time 
    from
        board be1_0 
    where
        be1_0.board_title like replace('%%', '\\', '\\\\') 
    order by
        be1_0.board_title desc 