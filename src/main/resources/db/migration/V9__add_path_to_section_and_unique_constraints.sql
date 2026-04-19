-- Renombrar columna url → path en nav_item (existía desde V0)
ALTER TABLE public.nav_item
    RENAME COLUMN url TO path;

-- Agregar columna path a section
ALTER TABLE public.section
    ADD COLUMN IF NOT EXISTS path character varying(50);

-- Unique path por institución en nav_item (excluye filas eliminadas y sin path)
CREATE UNIQUE INDEX IF NOT EXISTS uq_nav_item_institution_path
    ON public.nav_item(institution_id, path)
    WHERE path IS NOT NULL AND deleted = false;

-- Unique path por institución en section (excluye filas eliminadas y sin path)
CREATE UNIQUE INDEX IF NOT EXISTS uq_section_institution_path
    ON public.section(institution_id, path)
    WHERE path IS NOT NULL AND deleted = false;
