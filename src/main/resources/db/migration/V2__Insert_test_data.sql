
INSERT INTO personal_details (phone_number, first_name, last_name, email, city)
VALUES ('+1234567890', 'John', 'Doe', 'john.doe@example.com', 'New York'),
       ('+0987654321', 'Jane', 'Smith', 'jane.smith@example.com', 'Los Angeles'),
       ('+1122334455', 'Admin', 'User', 'admin@example.com', 'Chicago');

INSERT INTO employee (position, personal_details_id, created_at)
VALUES ('Manager', 1, CURRENT_TIMESTAMP),
       ('Employee', 2, CURRENT_TIMESTAMP),
       ('Admin', 3, CURRENT_TIMESTAMP);


INSERT INTO "user" (username, password, employee_id, created_at, role)
VALUES ('johndoe', '1234', 1, CURRENT_TIMESTAMP, 'MANAGER'),
       ('janesmith', '1234', 2, CURRENT_TIMESTAMP, 'EMPLOYEE'),
       ('admin', '1234', 3, CURRENT_TIMESTAMP, 'ADMIN');