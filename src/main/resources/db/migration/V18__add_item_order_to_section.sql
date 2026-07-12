-- Agregar columna item_order a section (orden dentro de cada nav_item)
ALTER TABLE public.section
    ADD COLUMN IF NOT EXISTS item_order integer;

-- Backfill: numerar sections existentes dentro de cada (institution_id, nav_item_id),
-- preservando el orden visible actual (created_date ASC). Las sections sin nav_item_id
-- se numeran en su propio grupo por institución.
WITH ordered AS (
    SELECT id,
           ROW_NUMBER() OVER (
               PARTITION BY institution_id, nav_item_id
               ORDER BY created_date ASC, id ASC
           ) AS rn
    FROM public.section
)
UPDATE public.section s
SET item_order = ordered.rn
FROM ordered
WHERE s.id = ordered.id;

ALTER TABLE public.section
    ALTER COLUMN item_order SET NOT NULL;
