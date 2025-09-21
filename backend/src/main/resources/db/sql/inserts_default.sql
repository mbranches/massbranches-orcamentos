INSERT INTO role(idrole, name, description) VALUES(1, 'ADMIN', 'has all system permissions ') ON CONFLICT (idrole) DO NOTHING;
INSERT INTO role(idrole, name, description) VALUES(2, 'CLIENT', 'has limited system permissions ') ON CONFLICT (idrole) DO NOTHING;

INSERT INTO customer_type(idcustomer_type, name) VALUES(1, 'PESSOA_FISICA') ON CONFLICT (idcustomer_type) DO NOTHING;
INSERT INTO customer_type(idcustomer_type, name) VALUES(2, 'PESSOA_JURIDICA') ON CONFLICT (idcustomer_type) DO NOTHING;
