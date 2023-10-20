INSERT INTO users (id, name, email) VALUES (1, 'Wesley Ramos', 'wesley.ramos@gmail.com');
INSERT INTO products (id, name) VALUES (1, 'Coca cola 2L');

INSERT INTO orders (id, product_id, user_id, quantity, completed, created_at) VALUES (1, 1, 1, 5, false, '2023-10-20 22:00:00');
INSERT INTO orders (id, product_id, user_id, quantity, completed, created_at) VALUES (2, 1, 1, 8, false,'2023-10-21 12:00:00');
