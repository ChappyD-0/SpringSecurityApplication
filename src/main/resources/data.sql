-- Inserta roles iniciales
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_MODERATOR') ON CONFLICT DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT DO NOTHING;

-- Inserta tipos de reacciones (ajusta el nombre de la tabla y columnas según tu modelo)
INSERT INTO reaction_type (name) VALUES ('REACTION_LIKE') ON CONFLICT DO NOTHING;
INSERT INTO reaction_type (name) VALUES ('REACTION_LOVE') ON CONFLICT DO NOTHING;
INSERT INTO reaction_type (name) VALUES ('REACTION_HATE') ON CONFLICT DO NOTHING;
INSERT INTO reaction_type (name) VALUES ('REACTION_SAD') ON CONFLICT DO NOTHING;
INSERT INTO reaction_type (name) VALUES ('REACTION_ANGRY') ON CONFLICT DO NOTHING;
