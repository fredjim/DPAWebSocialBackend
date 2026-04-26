-- section.nav_item_id → nav_item.uuid: cascade delete sections when nav_item is deleted
ALTER TABLE section
    ADD CONSTRAINT fk_section_nav_item
    FOREIGN KEY (nav_item_id) REFERENCES nav_item(uuid) ON DELETE CASCADE;

-- article.section_id → section.uuid: cascade delete articles when section is deleted
ALTER TABLE article
    ADD CONSTRAINT fk_article_section
    FOREIGN KEY (section_id) REFERENCES section(uuid) ON DELETE CASCADE;
