create table role (
    idrole bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(45) NOT NULL,
    description varchar(45)
);

create table user(
    iduser varchar(36) PRIMARY KEY,
    first_name varchar(45) NOT NULL,
    last_name varchar(45) NOT NULL,
    email varchar(100) NOT NULL UNIQUE,
    password varchar(200) NOT NULL,
    active tinyint NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

create table user_role(
    iduser_role bigint PRIMARY KEY AUTO_INCREMENT,
    role_id bigint NOT NULL,
    user_id varchar(36) NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(idrole),
    FOREIGN KEY (user_id) REFERENCES user(iduser)
);

create table customer_type(
    idcustomer_type bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(45) NOT NULL
);

create table customer(
    idcustomer varchar(36) PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(120),
    customer_type_id bigint NOT NULL,
    active tinyint NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id varchar(36) NOT NULL,
    FOREIGN KEY (customer_type_id) REFERENCES customer_type(idcustomer_type),
    FOREIGN KEY (user_id) REFERENCES user(iduser)
);

create table item(
    iditem bigint PRIMARY KEY AUTO_INCREMENT,
    name varchar(500) NOT NULL,
    unit_measurement varchar(10) NOT NULL,
    unit_price decimal(12, 4) NOT NULL,
    active tinyint NOT NULL,
    user_id varchar(36) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(iduser)
);

create table budget(
    idbudget varchar(36) PRIMARY KEY,
    customer_id varchar(36),
    description varchar(120) NOT NULL,
    proposal_number varchar(50) NOT NULL,
    bdi decimal(10, 2),
    total_value decimal(10, 2),
    total_with_bdi decimal(10, 2),
    created_at datetime DEFAULT CURRENT_TIMESTAMP,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id varchar(36),
    active tinyint NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(idcustomer),
    FOREIGN KEY (user_id) REFERENCES user(iduser)
);

create table stage(
    idstage bigint PRIMARY KEY AUTO_INCREMENT,
    order_index varchar(20) NOT NULL,
    name varchar(100) NOT NULL,
    total_value decimal(10, 2),
    budget_id varchar(36) NOT NULL,
    FOREIGN KEY (budget_id) REFERENCES budget(idbudget)
);

create table budget_item(
    idbudget_item bigint PRIMARY KEY AUTO_INCREMENT,
    budget_id varchar(36) NOT NULL,
    item_id bigint NOT NULL,
    stage_id bigint,
    order_index varchar(20) NOT NULL,
    unit_price decimal(12, 4) NOT NULL,
    quantity decimal(10, 4) NOT NULL,
    total_value decimal(10, 2) NOT NULL ,
    total_with_bdi decimal(10, 2),
    FOREIGN KEY (budget_id) REFERENCES budget(idbudget),
    FOREIGN KEY (stage_id) REFERENCES stage(idstage) ON DELETE SET NULL,
    FOREIGN KEY (item_id) REFERENCES item(iditem)
);