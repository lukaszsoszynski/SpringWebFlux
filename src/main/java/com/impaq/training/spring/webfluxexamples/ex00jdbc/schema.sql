CREATE SEQUENCE billing_record_seq;

create table billing_record(
    id bigint DEFAULT nextval('billing_record_seq') primary key,
    first_name character varying(50),
    last_name character varying(50),
    type character varying(10),
    start_time timestamp without time zone NOT NULL,
    duration bigint
);