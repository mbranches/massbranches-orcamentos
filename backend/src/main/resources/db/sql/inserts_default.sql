INSERT IGNORE INTO role(idrole, name, description) VALUES(1, 'ADMIN', 'has all system permissions ');
INSERT IGNORE INTO role(idrole, name, description) VALUES(2, 'CLIENT', 'has limited system permissions ');

INSERT IGNORE INTO customer_type(idcustomer_type, name) VALUES(1, 'PESSOA_FISICA');
INSERT IGNORE INTO customer_type(idcustomer_type, name) VALUES(2, 'PESSOA_JURIDICA');