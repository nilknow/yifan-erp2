CREATE TABLE IF NOT EXISTS public.alert
(
    id bigserial NOT NULL,
    content character varying(4000) COLLATE pg_catalog."default" NOT NULL,
    state integer NOT NULL DEFAULT 0,
    email_sent integer NOT NULL DEFAULT 0,
    company_id bigint,
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.alert
    OWNER to postgres;

COMMENT ON TABLE public.alert
    IS '预警表，包括库存预警';

COMMENT ON COLUMN public.alert.state
    IS '0: unread 1:working on it 2:solved';

COMMENT ON COLUMN public.alert.email_sent
    IS '0: not sent 1: sent';

CREATE TABLE IF NOT EXISTS public.alert_email
(
    id bigserial NOT NULL,
    address character varying(64) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT alert_email_pk PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.alert_email
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.authority
(
    id bigserial NOT NULL,
    name character varying(32) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT authority_pk PRIMARY KEY (id),
    CONSTRAINT authority_name_uk2 UNIQUE (name)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.authority
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.company
(
    id bigserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    update_time timestamp without time zone,
    CONSTRAINT company_pkey PRIMARY KEY (id),
    CONSTRAINT company_name_key UNIQUE (name)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.company
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.bug
(
    id bigserial NOT NULL,
    content character varying(4000) COLLATE pg_catalog."default" NOT NULL DEFAULT ''::character varying,
    priority character(4) COLLATE pg_catalog."default" NOT NULL DEFAULT '一般'::bpchar,
    email character varying(40) COLLATE pg_catalog."default",
    phone character varying(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone NOT NULL,
    company_id bigint,
    CONSTRAINT bug_pk PRIMARY KEY (id),
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.bug
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.category
(
    id bigserial NOT NULL,
    name character varying(16) COLLATE pg_catalog."default" NOT NULL,
    company_id bigint,
    CONSTRAINT category_pk PRIMARY KEY (id),
    CONSTRAINT category_name_uk UNIQUE (name, company_id)
        DEFERRABLE,
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.category
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.login_user
(
    id bigserial NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    password character(68) COLLATE pg_catalog."default" NOT NULL,
    update_time timestamp without time zone DEFAULT now(),
    company_id bigint,
    CONSTRAINT login_user_pk PRIMARY KEY (id),
    CONSTRAINT login_user_username_uk2 UNIQUE (username),
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.login_user
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.login_user_authority_rel
(
    id bigserial NOT NULL,
    login_user_id bigint NOT NULL,
    authority_id bigint NOT NULL,
    CONSTRAINT login_user_authority_rel_pk PRIMARY KEY (id),
    CONSTRAINT login_user_authority_rel_pk2 UNIQUE (login_user_id, authority_id),
    CONSTRAINT login_user_authority_rel_authority_id_fk FOREIGN KEY (authority_id)
        REFERENCES public.authority (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT login_user_authority_rel_login_user_id_fk FOREIGN KEY (login_user_id)
        REFERENCES public.login_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.login_user_authority_rel
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.material
(
    id bigserial NOT NULL,
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    count bigint NOT NULL,
    inventory_count_alert bigint NOT NULL,
    category character varying(64) COLLATE pg_catalog."default" NOT NULL,
    update_timestamp timestamp without time zone DEFAULT now(),
    company_id bigint,
    CONSTRAINT material_pk PRIMARY KEY (id),
    CONSTRAINT material_name_category_uk UNIQUE (name, category, company_id)
        DEFERRABLE,
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.material
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.product
(
    id bigserial NOT NULL,
    name character varying(64) COLLATE pg_catalog."default" NOT NULL,
    count bigint NOT NULL DEFAULT 0,
    category_id bigint NOT NULL,
    unit character varying COLLATE pg_catalog."default" NOT NULL,
    update_timestamp timestamp without time zone NOT NULL DEFAULT now(),
    company_id bigint,
    CONSTRAINT product_pk PRIMARY KEY (id),
    CONSTRAINT product_name_category_uk UNIQUE (name, category_id),
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT product_category_id_fk FOREIGN KEY (category_id)
        REFERENCES public.category (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.product
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.product_material_rel
(
    id bigserial NOT NULL,
    product_id bigint NOT NULL,
    material_id bigint NOT NULL,
    material_count bigint NOT NULL,
    CONSTRAINT product_material_rel_pk PRIMARY KEY (id),
    CONSTRAINT product_material_rel_pk2 UNIQUE (product_id, material_id),
    CONSTRAINT product_material_rel_material_id_fk FOREIGN KEY (material_id)
        REFERENCES public.material (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT product_material_rel_product_id_fk FOREIGN KEY (product_id)
        REFERENCES public.product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.product_material_rel
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.product_plan
(
    id bigserial NOT NULL,
    product_id bigint NOT NULL,
    count bigint NOT NULL DEFAULT 0,
    unit character varying(12) COLLATE pg_catalog."default" NOT NULL,
    company_id bigint,
    CONSTRAINT product_plan_pk PRIMARY KEY (id),
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES public.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT product_plan_product_id_fk FOREIGN KEY (product_id)
        REFERENCES public.product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.product_plan
    OWNER to postgres;

CREATE TABLE IF NOT EXISTS public.suggestion
(
    id bigserial NOT NULL,
    content character varying(4000) COLLATE pg_catalog."default" NOT NULL DEFAULT ''::character varying,
    email character(40) COLLATE pg_catalog."default",
    phone character(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone NOT NULL DEFAULT now(),
    solved integer NOT NULL DEFAULT 0,
    company_id bigint,
    CONSTRAINT suggestion_pk PRIMARY KEY (id),
    CONSTRAINT fk_company_id FOREIGN KEY (company_id)
        REFERENCES yifan_erp_test.company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.suggestion
    OWNER to postgres;


-- trigger
CREATE OR REPLACE FUNCTION public.add_alert_row()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$

BEGIN
    IF NEW.inventory_count_alert is not null and NEW.inventory_count_alert >= NEW.count THEN
        INSERT INTO Alert (content) VALUES ('物料 '||NEW.name||' 库存不足，请添加库存');
    END IF;
    RETURN NEW;
END
$BODY$;

ALTER FUNCTION public.add_alert_row()
    OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.bom_update()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    IF TG_OP = 'DELETE' THEN
        -- Handle DELETE operation
        -- Access old values using OLD record variable
        UPDATE public.product
        SET update_timestamp = now()
        WHERE id = OLD.product_id;
    ELSE
        update public.Product set update_timestamp=now()
        where id=NEW.product_id;
    end if;
    RETURN NEW;
END
$BODY$;

ALTER FUNCTION public.bom_update()
    OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.update_material_timestamp()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$
BEGIN
    NEW.update_timestamp := NOW();
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.update_material_timestamp()
    OWNER TO postgres;

CREATE OR REPLACE FUNCTION public.update_timestamp_function()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF
AS $BODY$

BEGIN
    NEW.update_timestamp = NOW();
    RETURN NEW;
END;
$BODY$;

ALTER FUNCTION public.update_timestamp_function()
    OWNER TO postgres;

CREATE TRIGGER material_inventory_alert_trigger
    AFTER UPDATE OF count, inventory_count_alert
    ON public.material
    FOR EACH ROW
EXECUTE PROCEDURE public.add_alert_row();

CREATE TRIGGER material_update_trigger
    BEFORE INSERT OR UPDATE
    ON public.material
    FOR EACH ROW
EXECUTE PROCEDURE public.update_material_timestamp();

CREATE TRIGGER bom_update
    AFTER INSERT OR DELETE OR UPDATE
    ON public.product_material_rel
    FOR EACH ROW
EXECUTE PROCEDURE yifan_erp_test.bom_update();