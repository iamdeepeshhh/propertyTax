-- V3__assign_notice_number.sql

DROP FUNCTION IF EXISTS assign_final_property_number(integer);

CREATE OR REPLACE FUNCTION assign_final_property_number(ward_number INTEGER) RETURNS VOID AS $$
DECLARE
    property RECORD;
    seq INTEGER := 1;
    mainProp VARCHAR;
    subProp INTEGER;
    formatted_seq VARCHAR;
    formatted_ward VARCHAR;
    last_main_prop VARCHAR := '';
    sub_prop_count INTEGER := 0;
BEGIN
    FOR property IN SELECT * FROM property_details WHERE pd_ward_i = ward_number ORDER BY pd_surypropno_vc
    LOOP
        formatted_ward := lpad(ward_number::text, 2, '0');
        formatted_seq := lpad(seq::text, 4, '0');

        -- Determine mainProp and subProp
        IF position('+' in property.pd_surypropno_vc) > 0 THEN
            mainProp := split_part(property.pd_surypropno_vc, '+', 1);
            subProp := cast(split_part(property.pd_surypropno_vc, '+', 2) as INTEGER);
            sub_prop_count := subProp;
        ELSIF position('/' in property.pd_surypropno_vc) > 0 THEN
            mainProp := split_part(property.pd_surypropno_vc, '/', 1);
            IF mainProp = last_main_prop THEN
                sub_prop_count := sub_prop_count + 1;
            ELSE
                sub_prop_count := 1;
            END IF;
            subProp := sub_prop_count;
        ELSE
            mainProp := property.pd_surypropno_vc;
            subProp := 0;
            sub_prop_count := 0;
        END IF;

        -- Update property with final property number and notice number
        UPDATE property_details SET
            pd_finalpropno_vc = format('%s%s', formatted_ward, formatted_seq),
            pd_noticeno_vc = format('WA%s_%s', formatted_ward, formatted_seq),
            pd_suryprop1_vc = mainProp,
            pd_suryprop2_vc = subProp,
            updated_at = CURRENT_TIMESTAMP
        WHERE pd_newpropertyno_vc = property.pd_newpropertyno_vc;

        last_main_prop := mainProp;
        seq := seq + 1;
    END LOOP;
    RETURN;
END;
$$ LANGUAGE plpgsql;

--ALTER FUNCTION assign_final_property_number(integer) OWNER TO postgres;
