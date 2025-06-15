-- 1. Insertar usuarios
INSERT INTO users (username, password, role) VALUES
                                                 ('admin1', 'adminpass', 'ADMIN'),
                                                 ('client1', 'clientpass1', 'CLIENT'),
                                                 ('client2', 'clientpass2', 'CLIENT'),
                                                 ('dealer1', 'dealerpass1', 'DEALER'),
                                                 ('dealer2', 'dealerpass2', 'DEALER');

-- 2. Insertar compañías
INSERT INTO companies (name, email, phone, address, rut, type, ubication) VALUES
                                                                              ('Compañía A', 'compania_a@test.com', '+56912345678', 'Calle Principal 123', '12345678-9', 'TECNOLOGIA', ST_GeomFromText('POINT(-70.6500 -33.4500)')),
                                                                              ('Compañía B', 'compania_b@test.com', '+56987654321', 'Avenida Secundaria 456', '98765432-1', 'ALIMENTOS', ST_GeomFromText('POINT(-70.7000 -33.4000)'));

-- 3. Insertar métodos de pago
INSERT INTO payment_methods (type) VALUES
                                       ('Tarjeta Crédito'),
                                       ('Transferencia Bancaria'),
                                       ('Efectivo');

-- 4. Insertar clientes
INSERT INTO clients (name, rut, email, phone, address, user_id, ubication) VALUES
                                                                               ('Juan Pérez', '11111111-1', 'juan@test.com', '+56911111111', 'Calle Falsa 123', 2, ST_GeomFromText('POINT(-70.6510 -33.4510)')),
                                                                               ('María González', '22222222-2', 'maria@test.com', '+56922222222', 'Avenida Siempre Viva 456', 3, ST_GeomFromText('POINT(-70.6520 -33.4520)'));

-- 5. Insertar repartidores
INSERT INTO dealers (rut, name, phone, email, vehicle, plate, user_id, ubication) VALUES
                                                                                      ('33333333-3', 'Carlos López', '+56933333333', 'carlos@test.com', 'Moto', 'AB1234', 4, ST_GeomFromText('POINT(-70.6530 -33.4530)')),
                                                                                      ('44444444-4', 'Ana Martínez', '+56944444444', 'ana@test.com', 'Camioneta', 'CD5678', 5, ST_GeomFromText('POINT(-70.6540 -33.4540)'));

-- 6. Insertar productos
INSERT INTO products (name, stock, price, category, company_id) VALUES
                                                                    ('Laptop Gamer', 50, 1500000, 'Tecnología', 1),
                                                                    ('Smartphone Pro', 100, 800000, 'Tecnología', 1),
                                                                    ('Arroz 5kg', 200, 5000, 'Alimentos', 2),
                                                                    ('Aceite 1L', 150, 3000, 'Alimentos', 2);

-- 7. Insertar órdenes con rutas estimadas
INSERT INTO orders (order_date, delivery_date, status, client_id, dealer_id, total_price, estimated_route) VALUES
-- Orden 1: Con ruta simple (línea recta)
(NOW() - INTERVAL '3 days', NULL, 'PENDIENTE', 1, 1, 2300000,
 ST_GeomFromText('LINESTRING(-70.6510 -33.4510, -70.6530 -33.4530)')),

-- Orden 2: Con ruta más compleja
(NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day', 'ENTREGADO', 2, 2, 8000,
 ST_GeomFromText('LINESTRING(-70.6520 -33.4520, -70.6525 -33.4525, -70.6530 -33.4530, -70.6540 -33.4540)')),

-- Orden 3: Sin repartidor asignado aún
(NOW() - INTERVAL '1 day', NULL, 'PREPARACION', 1, NULL, 5000,
 NULL),

-- Orden 4: Con ruta circular (para pruebas especiales)
(NOW(), NULL, 'EN_CAMINO', 2, 1, 1500000,
 ST_GeomFromText('LINESTRING(-70.6520 -33.4520, -70.6530 -33.4530, -70.6540 -33.4540, -70.6520 -33.4520)')),
-- Orden 5: Ruta con 5 puntos (Dealer 1)
(NOW() - INTERVAL '5 hours', NULL, 'EN_CAMINO', 1, 1, 1200000,
 ST_GeomFromText('LINESTRING(-70.6510 -33.4510, -70.6515 -33.4515, -70.6520 -33.4520, -70.6525 -33.4525, -70.6530 -33.4530)')),

-- Orden 6: Ruta con 7 puntos (Dealer 1)
(NOW() - INTERVAL '4 hours', NULL, 'EN_CAMINO', 2, 1, 850000,
 ST_GeomFromText('LINESTRING(-70.6520 -33.4520, -70.6522 -33.4522, -70.6524 -33.4524, -70.6526 -33.4526, -70.6528 -33.4528, -70.6530 -33.4530, -70.6532 -33.4532)')),

-- Orden 7: Ruta en zigzag (Dealer 2)
(NOW() - INTERVAL '3 hours', NULL, 'EN_CAMINO', 1, 2, 950000,
 ST_GeomFromText('LINESTRING(-70.6520 -33.4520, -70.6525 -33.4515, -70.6530 -33.4520, -70.6535 -33.4515, -70.6540 -33.4520)')),

-- Orden 8: Ruta circular detallada (Dealer 2)
(NOW() - INTERVAL '2 hours', NULL, 'EN_CAMINO', 2, 2, 1100000,
 ST_GeomFromText('LINESTRING(-70.6520 -33.4520, -70.6525 -33.4525, -70.6530 -33.4530, -70.6535 -33.4535, -70.6540 -33.4540, -70.6535 -33.4535, -70.6530 -33.4530, -70.6525 -33.4525, -70.6520 -33.4520)')),

-- Orden 9: Ruta con desvío (Dealer 1)
(NOW() - INTERVAL '1 hour', NULL, 'EN_CAMINO', 1, 1, 750000,
 ST_GeomFromText('LINESTRING(-70.6510 -33.4510, -70.6512 -33.4512, -70.6510 -33.4515, -70.6515 -33.4518, -70.6520 -33.4520, -70.6525 -33.4525, -70.6530 -33.4530)')),

-- Orden 10: Ruta larga con 10 puntos (Dealer 2)
(NOW(), NULL, 'EN_CAMINO', 2, 2, 1500000,
 ST_GeomFromText('LINESTRING(-70.6520 -33.4520, -70.6522 -33.4522, -70.6524 -33.4524, -70.6526 -33.4526, -70.6528 -33.4528, -70.6530 -33.4530, -70.6532 -33.4532, -70.6534 -33.4534, -70.6536 -33.4536, -70.6540 -33.4540)'));



-- 8. Insertar relaciones order_products
INSERT INTO order_products (order_id, product_id) VALUES
                                                      (1, 1),
                                                      (1, 2),
                                                      (2, 3),
                                                      (2, 4),
                                                      (3, 3),
                                                      (4, 1),
                                                      (5, 1),
                                                      (5, 3),
                                                      (6, 2),
                                                      (7, 1),
                                                      (7, 4),
                                                      (8, 2),
                                                      (8, 3),
                                                      (9, 4),
                                                      (10, 1),
                                                      (10, 2);

-- 9. Insertar reportes de emergencia
INSERT INTO emergency_report (order_id, dealer_id, ubication) VALUES
    (2, 2, ST_GeomFromText('POINT(-70.6535 -33.4535)'));

-- 10. Insertar calificaciones
INSERT INTO ratings (rating, comment, date, client_id, dealer_id, order_id) VALUES
                                                                                (5, 'Excelente servicio', CURRENT_DATE - 1, 2, 2, 2),
                                                                                (3, 'Demoró un poco pero bien', CURRENT_DATE - 2, 1, 1, 1);

-- 11. Actualizar order_details
INSERT INTO order_details (order_id, payment_method, total_products, price) VALUES
                                                                                (1, 'Tarjeta Crédito', 2, 2300000),
                                                                                (2, 'Efectivo', 2, 8000),
                                                                                (3, 'Transferencia Bancaria', 1, 5000),
                                                                                (4, 'Tarjeta Crédito', 1, 1500000),
                                                                                (5, 'Tarjeta Crédito', 2, 1200000),
                                                                                (6, 'Transferencia Bancaria', 1, 850000),
                                                                                (7, 'Efectivo', 2, 950000),
                                                                                (8, 'Tarjeta Crédito', 2, 1100000),
                                                                                (9, 'Efectivo', 1, 750000),
                                                                                (10, 'Transferencia Bancaria', 2, 1500000);

-- 12. Crear áreas de cobertura
INSERT INTO coverage_area (name, coverageArea) VALUES
                                                   ('Zona Norte', ST_GeomFromText('POLYGON((-70.6600 -33.4400, -70.6400 -33.4400, -70.6400 -33.4600, -70.6600 -33.4600, -70.6600 -33.4400))')),
                                                   ('Zona Sur', ST_GeomFromText('POLYGON((-70.6600 -33.4600, -70.6400 -33.4600, -70.6400 -33.4800, -70.6600 -33.4800, -70.6600 -33.4600))'));

-- 13. Relacionar compañías con áreas de cobertura
INSERT INTO coverage_area_company (company_id, coverage_id) VALUES
                                                                (1, 1),
                                                                (2, 1),
                                                                (2, 2);

-- 14. Relacionar compañías con métodos de pago
INSERT INTO company_payment_methods (company_id, payment_method_id) VALUES
                                                                        (1, 1),
                                                                        (1, 2),
                                                                        (2, 2),
                                                                        (2, 3);

-- Verificar datos insertados
SELECT 'Usuarios' as tabla, COUNT(*) as cantidad FROM users UNION ALL
SELECT 'Compañías', COUNT(*) FROM companies UNION ALL
SELECT 'Clientes', COUNT(*) FROM clients UNION ALL
SELECT 'Repartidores', COUNT(*) FROM dealers UNION ALL
SELECT 'Productos', COUNT(*) FROM products UNION ALL
SELECT 'Órdenes', COUNT(*) FROM orders UNION ALL
SELECT 'Order_Products', COUNT(*) FROM order_products UNION ALL
SELECT 'Reportes Emergencia', COUNT(*) FROM emergency_report UNION ALL
SELECT 'Calificaciones', COUNT(*) FROM ratings;