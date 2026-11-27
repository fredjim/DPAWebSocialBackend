-- La entidad Institution.description ahora tiene length = 1000 (antes 300).
-- Ampliar la columna real en BD para que coincida.
ALTER TABLE public.institution
    ALTER COLUMN description TYPE character varying(1000);
