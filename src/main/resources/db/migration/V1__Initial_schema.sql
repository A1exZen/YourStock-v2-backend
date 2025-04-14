CREATE TABLE personal_details
(
    id           BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(50)  NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    city         VARCHAR(255),
    created_at   TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employee
(
    id                  BIGSERIAL PRIMARY KEY,
    position            VARCHAR(255) NOT NULL,
    personal_details_id BIGINT       NOT NULL REFERENCES personal_details (id) ON DELETE CASCADE,
    created_at          TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE "user"
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    employee_id BIGINT       NOT NULL REFERENCES employee (id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    role        VARCHAR(50)  NOT NULL CHECK (role IN ('ADMIN', 'MANAGER', 'EMPLOYEE', 'MAT_MANAGER', 'DEP_MANAGER',
                                                      'STOCK_MANAGER')),
    google_id   VARCHAR(255) UNIQUE
);

CREATE INDEX idx_user_google_id ON "user" (google_id);

CREATE TABLE category
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    type       VARCHAR(50)  NOT NULL CHECK (type IN ('PRODUCT', 'MATERIAL')),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE supplier
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL UNIQUE,
    contact_person VARCHAR(255),
    email          VARCHAR(255),
    phone          VARCHAR(255),
    address        VARCHAR(500),
    status         VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE')),
    created_at     TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer
(
    id             BIGSERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL UNIQUE,
    contact_person VARCHAR(255),
    email          VARCHAR(255),
    phone          VARCHAR(255),
    address        VARCHAR(500),
    status         VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'INACTIVE')),
    created_at     TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE material
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255)   NOT NULL,
    category_id      BIGINT         NOT NULL REFERENCES category (id) ON DELETE SET NULL,
    supplier_id      BIGINT         NOT NULL REFERENCES supplier (id) ON DELETE CASCADE,
    price            DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    quantity         INTEGER        NOT NULL CHECK (quantity >= 0),
    unit             VARCHAR(50)    NOT NULL,
    created_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_material_supplier_id ON material (supplier_id);
CREATE INDEX idx_material_category_id ON material (category_id);

CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT,
    category_id BIGINT         NOT NULL REFERENCES category (id) ON DELETE SET NULL,
    quantity    INTEGER        NOT NULL CHECK (quantity >= 0),
    price       DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    unit        VARCHAR(50)    NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_category_id ON product (category_id);

CREATE TABLE product_material
(
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT         NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    material_id BIGINT         NOT NULL REFERENCES material (id) ON DELETE CASCADE,
    quantity    DECIMAL(10, 2) NOT NULL CHECK (quantity > 0),
    unit        VARCHAR(50)    NOT NULL
);

CREATE INDEX idx_product_material_product_id ON product_material (product_id);
CREATE INDEX idx_product_material_material_id ON product_material (material_id);

CREATE TABLE "order"
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP,
    customer_id BIGINT      REFERENCES customer (id) ON DELETE SET NULL,
    employee_id BIGINT      REFERENCES employee (id) ON DELETE SET NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'ACCEPTED' CHECK (status IN
                                                               ('ACCEPTED', 'PROCESSING', 'SHIPPED', 'DELIVERED',
                                                                'CANCELLED')),
    comment     TEXT
);

CREATE INDEX idx_order_customer_id ON "order" (customer_id);
CREATE INDEX idx_order_employee_id ON "order" (employee_id);

CREATE TABLE order_product
(
    id             BIGSERIAL PRIMARY KEY,
    product_id     BIGINT         NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    order_id       BIGINT         NOT NULL REFERENCES "order" (id) ON DELETE CASCADE,
    quantity       INTEGER        NOT NULL CHECK (quantity > 0),
    price_at_order DECIMAL(10, 2) NOT NULL CHECK (price_at_order > 0)
);

CREATE INDEX idx_order_product_order_id ON order_product (order_id);
CREATE INDEX idx_order_product_product_id ON order_product (product_id);

CREATE TABLE report (
                        id          BIGSERIAL PRIMARY KEY,
                        action      VARCHAR(50) NOT NULL CHECK (action IN ('PRODUCT_RECEIVED', 'PRODUCT_SHIPPED', 'MATERIAL_RECEIVED', 'MATERIAL_SHIPPED', 'PRODUCT_PRODUCED', 'MATERIAL_TRANSFERRED', 'PRODUCT_TRANSFERRED', 'ORDER_CREATED', 'ORDER_UPDATED', 'INVENTORY_CHECK')),
    product_id  BIGINT      REFERENCES product (id) ON DELETE SET NULL,
    material_id BIGINT      REFERENCES material (id) ON DELETE SET NULL,
    details     TEXT,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    employee_id BIGINT      NOT NULL REFERENCES employee (id) ON DELETE CASCADE
);

CREATE INDEX idx_report_employee_id ON report (employee_id);
CREATE INDEX idx_report_product_id ON report (product_id);
CREATE INDEX idx_report_material_id ON report (material_id);