CREATE OR REPLACE FUNCTION assign_final_property_number(ward_number INTEGER) RETURNS VOID AS $$
DECLARE
    property RECORD;
    seq INTEGER := 1;
    mainProp VARCHAR;
    subProp INTEGER;
    formatted_seq VARCHAR;
    formatted_ward VARCHAR;
    last_main_prop VARCHAR := '';  -- Keep track of the last main property number to manage sub-properties count
    sub_prop_count INTEGER := 0;  -- Counter for sub-properties
BEGIN
    FOR property IN SELECT * FROM property_details WHERE pd_ward_i = ward_number ORDER BY pd_surypropno_vc
    LOOP
        -- Format sequence and ward number with leading zeros
        formatted_ward := lpad(ward_number::text, 2, '0');
        formatted_seq := lpad(seq::text, 4, '0');

        -- Handle merged properties
        IF position('+' in property.pd_surypropno_vc) > 0 THEN
            mainProp := split_part(property.pd_surypropno_vc, '+', 1);
            subProp := cast(split_part(property.pd_surypropno_vc, '+', 2) as INTEGER);
            sub_prop_count := subProp;  -- Assume merging uses the number part for ordering or identification
        -- Handle sub-properties
        ELSIF position('/' in property.pd_surypropno_vc) > 0 THEN
            mainProp := split_part(property.pd_surypropno_vc, '/', 1);
            IF mainProp = last_main_prop THEN
                sub_prop_count := sub_prop_count + 1;
            ELSE
                sub_prop_count := 1;  -- Reset counter for new main property
            END IF;
            subProp := sub_prop_count;
        -- Handle standard properties
        ELSE
            mainProp := property.pd_surypropno_vc;
            subProp := 0;
            sub_prop_count := 0;  -- Reset when a new main property is encountered
        END IF;

        UPDATE property_details SET
            pd_finalpropno_vc = format('%s%s', formatted_ward, formatted_seq),
            pd_suryprop1_vc = mainProp,
            pd_suryprop2_vc = subProp,
            updated_at = CURRENT_TIMESTAMP
        WHERE pd_newpropertyno_vc = property.pd_newpropertyno_vc;

        last_main_prop := mainProp;  -- Update last main property for next iteration comparison
        seq := seq + 1;  -- Increment sequence for next property
    END LOOP;
    RETURN;
END;
$$ LANGUAGE plpgsql;
