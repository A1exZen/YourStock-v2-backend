CREATE TABLE personal_details
(
    id           BIGSERIAL PRIMARY KEY,
    phone_number VARCHAR(50)  NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL UNIQUE,
    city         VARCHAR(255)
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

CREATE TABLE supplier
(
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL UNIQUE,
    personal_details_id BIGINT       REFERENCES personal_details (id) ON DELETE SET NULL,
    created_at          TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customer
(
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL UNIQUE,
    personal_details_id BIGINT       REFERENCES personal_details (id) ON DELETE SET NULL,
    created_at          TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE material
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT           NOT NULL,
    supplier_id BIGINT         NOT NULL REFERENCES supplier (id) ON DELETE CASCADE,
    price       DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    quantity    INTEGER        NOT NULL CHECK (quantity >= 0),
    unit        VARCHAR(50)    NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_material_supplier_id ON material (supplier_id);

CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL,
    description TEXT           NOT NULL,
    category    VARCHAR(255)   NOT NULL,
    quantity    INTEGER        NOT NULL CHECK (quantity >= 0),
    price       DECIMAL(10, 2) NOT NULL CHECK (price > 0),
    unit        VARCHAR(50)    NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_product_category ON product (category);

CREATE TABLE "order"
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMPTZ             DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) NOT NULL CHECK (total_price > 0),
    customer_id BIGINT         REFERENCES customer (id) ON DELETE SET NULL,
    status      VARCHAR(20)    NOT NULL DEFAULT 'ACCEPTED' CHECK (status IN
                                                                  ('ACCEPTED', 'PROCESSING', 'SHIPPED', 'DELIVERED',
                                                                   'CANCELLED')),
    comment     TEXT
);

CREATE TABLE order_product
(
    id             BIGSERIAL PRIMARY KEY,
    product_id     BIGINT         NOT NULL REFERENCES product (id) ON DELETE CASCADE,
    order_id       BIGINT         NOT NULL REFERENCES "order" (id) ON DELETE CASCADE,
    quantity       INTEGER        NOT NULL CHECK (quantity > 0),
    price_at_order DECIMAL(10, 2) NOT NULL CHECK (price_at_order > 0)
);

CREATE TABLE report
(
    id          BIGSERIAL PRIMARY KEY,
    action      VARCHAR(50) NOT NULL,
    details     TEXT,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    employee_id BIGINT      NOT NULL REFERENCES employee (id) ON DELETE CASCADE
);

CREATE INDEX idx_report_employee_id ON report (employee_id);

CREATE TABLE report_product
(
    id         BIGSERIAL PRIMARY KEY,
    report_id  BIGINT NOT NULL REFERENCES report (id) ON DELETE CASCADE,
    product_id BIGINT NOT NULL REFERENCES product (id) ON DELETE CASCADE
);

CREATE INDEX idx_report_product_report_id ON report_product (report_id);

CREATE TABLE report_material
(
    id          BIGSERIAL PRIMARY KEY,
    report_id   BIGINT NOT NULL REFERENCES report (id) ON DELETE CASCADE,
    material_id BIGINT NOT NULL REFERENCES material (id) ON DELETE CASCADE
);

CREATE INDEX idx_report_material_report_id ON report_material (report_id);