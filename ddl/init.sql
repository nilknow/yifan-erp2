CREATE OR REPLACE FUNCTION add_alert_row()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.inventory_count_alert <= NEW.count THEN
        INSERT INTO Alert (content) VALUES ('物料 '||NEW.name||' 库存不足，请添加库存');
    END IF;
    RETURN NEW;
END
$$ LANGUAGE plpgsql;

CREATE TRIGGER material_inventory_alert_trigger
    AFTER UPDATE OF inventory_count_alert ON Material
    FOR EACH ROW
EXECUTE FUNCTION add_alert_row();