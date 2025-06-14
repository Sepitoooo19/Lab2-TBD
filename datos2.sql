/* =======================================================
   1. MEDIOS DE PAGO
   =======================================================*/
INSERT INTO payment_methods (type) VALUES
                                       ('Efectivo'), ('Tarjeta Crédito'), ('Tarjeta Débito'), ('Transferencia Bancaria'), ('PayPal');

/* =======================================================
   2. EMPRESAS (5)
   =======================================================*/
INSERT INTO companies (name,email,phone,address,rut,type) VALUES
                                                              ('FreshBite SpA' , 'contacto@freshbite.cl' , '+56 2 1234 5678', 'Av. Providencia 1234, Santiago' , '76.123.456-7' , 'Productora'),
                                                              ('GreenFarm Ltda.', 'ventas@greenfarm.cl'  , '+56 2 2345 6789', 'Camino El Monte 4321, Talagante', '77.234.567-8' , 'Agrícola'),
                                                              ('PanDelight S.A.', 'info@pandelight.cl'   , '+56 2 3456 7890', 'Av. Las Condes 7654, Santiago'  , '78.345.678-9' , 'Panadería'),
                                                              ('SaborAndes SpA' , 'contacto@saborandes.cl', '+56 2 4567 8901', 'Av. Argentina 1122, Valparaíso', '76.456.789-0' , 'Alimentos'),
                                                              ('OceanCatch Ltda.', 'ventas@oceancatch.cl', '+56 32 567 8902', 'Puerto 345, Valparaíso'         , '79.567.890-K' , 'Pescados');

/* Asociar medios de pago a las empresas */
INSERT INTO company_payment_methods (company_id,payment_method_id) VALUES
                                                                       (1,1),(1,2),(1,4), (2,1),(2,2),(2,3), (3,1),(3,2),(3,3),(3,4), (4,1),(4,2),(4,5), (5,1),(5,2),(5,3),(5,4),(5,5);

/* =======================================================
   3. PRODUCTOS (20) – solo alimentos
   =======================================================*/
INSERT INTO products (name,stock,price,category,company_id) VALUES
                                                                ('Manzanas Gala Kg'          , 50, 1800, 'Fruta'    , 2), ('Lechuga Hidropónica'       , 40, 1000, 'Verdura'  , 2),
                                                                ('Pan Integral ½ Kg'         , 60, 1500, 'Panadería', 3), ('Croissant Mantequilla'     , 70, 1200, 'Panadería', 3),
                                                                ('Queso Fresco 250 g'        , 30, 2200, 'Lácteos'  , 1), ('Leche Descremada 1 L'      , 80, 1200, 'Lácteos'  , 1),
                                                                ('Huevos de Campo Docena'    , 90, 2500, 'Huevos'   , 4), ('Tomate Cherry 250 g'       , 45, 1300, 'Verdura'  , 2),
                                                                ('Palta Hass Kg'             , 35, 4500, 'Fruta'    , 2), ('Filete de Salmón 200 g'    , 40, 5200, 'Pescados' , 5),
                                                                ('Merluza 1 Kg'              , 50, 3800, 'Pescados' , 5), ('Yogurt Natural 170 g'      ,100,  600, 'Lácteos'  , 1),
                                                                ('Jugo Naranja 1 L'          , 70, 1600, 'Bebidas'  , 1), ('Agua Mineral 1.5 L'        ,120,  900, 'Bebidas'  , 1),
                                                                ('Mix Frutos Secos 200 g'    , 25, 3500, 'Snacks'   , 4), ('Granola 500 g'             , 40, 3200, 'Snacks'   , 4),
                                                                ('Miel de Ulmo 250 g'        , 30, 4200, 'Dulces'   , 4), ('Mermelada Frutilla 250 g'  , 50, 2600, 'Dulces'   , 4),
                                                                ('Tortilla de Maíz x10'      , 60, 2100, 'Panadería', 3), ('Atún en Lata 170 g'        , 80, 1800, 'Pescados' , 5);

/* =======================================================
   4. USUARIOS (10 clientes + 5 repartidores)
   =======================================================*/
INSERT INTO users (username,password,role) VALUES
                                               ('alice'  ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'), ('bruno'  ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'),
                                               ('carla'  ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'), ('diego'  ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'),
                                               ('elena'  ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'), ('felipe' ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'),
                                               ('gabriela','$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'), ('hector' ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'),
                                               ('isabel' ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'), ('juan'   ,'$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','CLIENT'),
                                               ('dealer1','$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','DEALER'), ('dealer2','$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','DEALER'),
                                               ('dealer3','$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','DEALER'), ('dealer4','$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','DEALER'),
                                               ('dealer5','$2a$10$w4E.g.hY3Zz.aJ.8j.r5yOTN/O.9h.2G.8i.2i.u.e.4J.f.6i.Y.','DEALER');

/* =======================================================
   5. PERFILES DE CLIENTE (10)
   =======================================================*/
INSERT INTO clients (name,rut,email,phone,address,user_id) VALUES
                                                               ('Alice Martínez' ,'19.123.456-7','alice@example.com' ,'+56 9 1111 1111','Rua 1 #100, Santiago' ,(SELECT id FROM users WHERE username='alice')),
                                                               ('Bruno Gómez'    ,'20.234.567-8','bruno@example.com' ,'+56 9 2222 2222','Rua 2 #200, Santiago' ,(SELECT id FROM users WHERE username='bruno')),
                                                               ('Carla Pérez'    ,'21.345.678-9','carla@example.com' ,'+56 9 3333 3333','Rua 3 #300, Santiago' ,(SELECT id FROM users WHERE username='carla')),
                                                               ('Diego Rodríguez','22.456.789-0','diego@example.com' ,'+56 9 4444 4444','Rua 4 #400, Santiago' ,(SELECT id FROM users WHERE username='diego')),
                                                               ('Elena Silva'    ,'23.567.890-1','elena@example.com' ,'+56 9 5555 5555','Rua 5 #500, Santiago' ,(SELECT id FROM users WHERE username='elena')),
                                                               ('Felipe Torres'  ,'24.678.901-2','felipe@example.com','+56 9 6666 6666','Rua 6 #600, Santiago' ,(SELECT id FROM users WHERE username='felipe')),
                                                               ('Gabriela Díaz'  ,'25.789.012-3','gabriela@example.com','+56 9 7777 7777','Rua 7 #700, Santiago' ,(SELECT id FROM users WHERE username='gabriela')),
                                                               ('Héctor Vargas'  ,'26.890.123-4','hector@example.com','+56 9 8888 8888','Rua 8 #800, Santiago' ,(SELECT id FROM users WHERE username='hector')),
                                                               ('Isabel Rojas'   ,'27.901.234-5','isabel@example.com','+56 9 9999 9999','Rua 9 #900, Santiago' ,(SELECT id FROM users WHERE username='isabel')),
                                                               ('Juan Castillo'  ,'28.012.345-6','juan@example.com'  ,'+56 9 1010 1010','Rua 10 #1000, Santiago',(SELECT id FROM users WHERE username='juan'));

/* =======================================================
   6. PERFILES DE REPARTIDOR (5)
   =======================================================*/
INSERT INTO dealers (rut,name,phone,email,vehicle,plate,user_id) VALUES
                                                                     ('15.123.456-7','Pedro López'  ,'+56 9 1111 2222','dealer1@example.com','Moto'     ,'AA-BB11',(SELECT id FROM users WHERE username='dealer1')),
                                                                     ('16.234.567-8','María Herrera','+56 9 2222 3333','dealer2@example.com','Auto'     ,'BB-CC22',(SELECT id FROM users WHERE username='dealer2')),
                                                                     ('17.345.678-9','José Fuentes' ,'+56 9 3333 4444','dealer3@example.com','Bicicleta','CC-DD33',(SELECT id FROM users WHERE username='dealer3')),
                                                                     ('18.456.789-0','Ana Morales'  ,'+56 9 4444 5555','dealer4@example.com','Moto'     ,'DD-EE44',(SELECT id FROM users WHERE username='dealer4')),
                                                                     ('19.567.890-1','Luis Sánchez' ,'+56 9 5555 6666','dealer5@example.com','Auto'     ,'EE-FF55',(SELECT id FROM users WHERE username='dealer5'));

/* =======================================================
   7. PEDIDOS (SIN RUTAS AÚN)
   =======================================================*/
INSERT INTO orders (id, order_date,delivery_date,status,client_id,dealer_id,total_price) VALUES
                                                                                             (1, '2025-04-01 10:15','2025-04-01 11:00','ENTREGADO',1,1,1800), (2, '2025-04-02 12:30','2025-04-02 13:10','ENTREGADO',2,2,1000),
                                                                                             (3, '2025-04-03 09:45','2025-04-03 10:20','ENTREGADO',3,3,1500), (4, '2025-04-04 14:05','2025-04-04 15:10','ENTREGADO',4,4,1200),
                                                                                             (5, '2025-04-05 18:20','2025-04-05 19:00','ENTREGADO',5,5,2200), (6, '2025-04-06 11:10','2025-04-06 11:50','ENTREGADO',6,1,3700),
                                                                                             (7, '2025-04-07 13:40','2025-04-07 14:30','ENTREGADO',7,2,5200), (8, '2025-04-08 16:25','2025-04-08 17:20','ENTREGADO',8,3,600),
                                                                                             (9, '2025-04-09 09:00','2025-04-09 09:45','ENTREGADO',9,4,1600), (10, '2025-04-10 10:30','2025-04-10 11:20','ENTREGADO',10,5,900),
                                                                                             (11, '2025-04-11 12:15','2025-04-11 13:05','ENTREGADO',1,1,3500), (12, '2025-04-12 15:50','2025-04-12 16:40','ENTREGADO',2,2,3200),
                                                                                             (13, '2025-04-13 17:00','2025-04-13 17:50','ENTREGADO',3,3,4200), (14, '2025-04-14 09:20','2025-04-14 10:00','ENTREGADO',4,4,2600),
                                                                                             (15, '2025-04-15 11:35','2025-04-15 12:25','ENTREGADO',5,5,1200), (16, '2025-04-16 13:30','2025-04-16 14:15','ENTREGADO',6,1,2100),
                                                                                             (17, '2025-04-17 16:45','2025-04-17 17:35','ENTREGADO',7,2,1800), (18, '2025-04-18 18:10','2025-04-18 18:55','ENTREGADO',8,3,3800),
                                                                                             (19, '2025-04-19 19:45','2025-04-19 20:35','ENTREGADO',9,4,2100), (20, '2025-04-20 08:50','2025-04-20 09:40','ENTREGADO',10,5,1800),
                                                                                             (21, '2025-05-01 10:00', NULL, 'URGENTE', 1, 1, 1900), (22, '2025-05-01 12:30', NULL, 'URGENTE', 2, 2, 2500),
                                                                                             (23, '2025-05-02 09:45', NULL, 'URGENTE', 3, 3, 1700), (24, '2025-05-02 14:20', NULL, 'URGENTE', 4, 4, 1300),
                                                                                             (25, '2025-05-03 11:15', NULL, 'URGENTE', 5, 5, 2300), (26, '2025-05-03 16:00', NULL, 'URGENTE', 6, 1, 3100),
                                                                                             (27, '2025-05-04 10:45', NULL, 'URGENTE', 7, 2, 1500), (28, '2025-05-04 13:10', NULL, 'URGENTE', 8, 3, 2200),
                                                                                             (29, '2025-04-25 09:15', NULL, 'FALLIDA', 9, 4, 1400), (30, '2025-04-26 11:30', NULL, 'FALLIDA', 10, 5, 2000),
                                                                                             (31, '2025-04-27 13:50', NULL, 'FALLIDA', 1, 1, 1600), (32, '2025-04-28 15:10', NULL, 'FALLIDA', 2, 2, 1800),
                                                                                             (33, '2025-04-29 16:45', NULL, 'FALLIDA', 3, 3, 1900), (34, '2025-04-30 18:00', NULL, 'FALLIDA', 4, 4, 1750)
ON CONFLICT (id) DO NOTHING;

/* =======================================================
   7.1. LÍNEAS PEDIDO-PRODUCTO (CORREGIDO Y AMPLIADO)
   =======================================================*/
INSERT INTO order_products (order_id,product_id) VALUES
                                                     (1,1),(1,2), (2,3), (3,4), (4,5), (5,6), (6,7),(6,8), (7,9), (8,12), (9,13), (10,14), (11,15),
                                                     (12,16), (13,17), (14,18), (15,4), (16,19), (17,20), (18,10), (19,11), (20,1),
                                                     -- Bloque añadido para pedidos URGENTES y FALLIDOS
                                                     (21, 5), (21, 12), (22, 9), (23, 3), (24, 8), (25, 10), (26, 1), (27, 2), (28, 17),
                                                     (29, 15), (30, 16), (31, 6), (32, 2), (33, 4), (34, 8);

/* =======================================================
   7.2. DETALLE DE PAGO POR PEDIDO
   =======================================================*/
INSERT INTO order_details (order_id,payment_method,total_products,price) VALUES
                                                                             (1 ,'Efectivo' ,2,2800), (2 ,'Tarjeta Crédito' ,1,1000), (3 ,'Tarjeta Débito' ,1,1500),
                                                                             (4 ,'Transferencia Bancaria',1,1200), (5 ,'Tarjeta Crédito' ,1,2200), (6 ,'Efectivo' ,2,3700),
                                                                             (7 ,'PayPal' ,1,5200), (8 ,'Tarjeta Débito' ,1, 600), (9 ,'Efectivo' ,1,1600),
                                                                             (10,'Tarjeta Crédito' ,1, 900), (11,'Tarjeta Crédito' ,1,3500), (12,'Transferencia Bancaria',1,3200),
                                                                             (13,'Efectivo' ,1,4200), (14,'Tarjeta Débito' ,1,2600), (15,'Efectivo' ,1,1200),
                                                                             (16,'Tarjeta Crédito' ,1,2100), (17,'PayPal' ,1,1800), (18,'Transferencia Bancaria',1,3800),
                                                                             (19,'Tarjeta Crédito' ,1,2100), (20,'Efectivo' ,1,1800), (21, 'Transferencia Bancaria', 3, 1900),
                                                                             (22, 'Tarjeta Débito', 2, 2500), (23, 'Tarjeta Crédito', 4, 1700), (24, 'Efectivo', 1, 1300),
                                                                             (25, 'Transferencia Bancaria', 2, 2300), (26, 'PayPal', 3, 3100), (27, 'Efectivo', 1, 1500),
                                                                             (28, 'Tarjeta Crédito', 2, 2200), (29, 'Tarjeta Débito', 1, 1400), (30, 'Transferencia Bancaria',1, 2000),
                                                                             (31, 'Efectivo', 1, 1600), (32, 'Tarjeta Crédito', 2, 1800), (33, 'PayPal', 1, 1900),
                                                                             (34, 'Tarjeta Débito', 1, 1750);

/* =======================================================
   8. CALIFICACIONES SIMULADAS
   =======================================================*/
INSERT INTO ratings (rating,comment,date,client_id,dealer_id,order_id) VALUES
                                                                           (5,'Entrega perfecta' ,'2025-04-01',1,1,1 ), (4,'Todo bien, solo un poco lento','2025-04-02',2,2,2 ),
                                                                           (5,'Producto fresco y rápido','2025-04-03',3,3,3 ), (5,'Excelente servicio' ,'2025-04-05',5,5,5 ),
                                                                           (3,'Llegó tibio pero en buen estado','2025-04-06',6,1,6 ), (4,'Buen servicio' ,'2025-04-07',7,2,7 ),
                                                                           (5,'Todo excelente' ,'2025-04-09',9,4,9 ), (2,'Llegó tarde' ,'2025-04-14',4,4,14),
                                                                           (4,'Todo ok' ,'2025-04-16',6,1,16), (5,'Rápido y amable' ,'2025-04-20',10,5,20);

/* =======================================================
   9. ZONAS DE COBERTURA
   =======================================================*/
INSERT INTO coverage_area (name, "coveragearea") VALUES
                                                     ('Zona Centro Santiago', ST_GeomFromText('POLYGON((-70.6600 -33.4600, -70.6600 -33.4200, -70.6000 -33.4200, -70.6000 -33.4600, -70.6600 -33.4600))', 4326)),
                                                     ('Zona Oriente Santiago', ST_GeomFromText('POLYGON((-70.6000 -33.4000, -70.6000 -33.4500, -70.5000 -33.4500, -70.5000 -33.4000, -70.6000 -33.4000))', 4326)),
                                                     ('Zona Central Valparaíso', ST_GeomFromText('POLYGON((-71.6500 -33.0600, -71.6500 -33.0200, -71.6000 -33.0200, -71.6000 -33.0600, -71.6500 -33.0600))', 4326)),
                                                     ('Zona Norte Santiago', ST_GeomFromText('POLYGON((-70.7000 -33.3800, -70.7000 -33.4200, -70.6500 -33.4200, -70.6500 -33.3800, -70.7000 -33.3800))', 4326)),
                                                     ('Zona Sur Santiago', ST_GeomFromText('POLYGON((-70.7000 -33.5000, -70.7000 -33.5500, -70.6500 -33.5500, -70.6500 -33.5000, -70.7000 -33.5000))', 4326));

/* =======================================================
   10. RELACIÓN EMPRESAS-ZONAS DE COBERTURA
   =======================================================*/
INSERT INTO coverage_area_company (company_id, coverage_id) VALUES
                                                                (1, 1), (1, 5), (2, 1), (2, 4), (3, 2), (4, 3), (5, 3);

/* =======================================================
   11. ACTUALIZACIÓN DE UBICACIONES
   =======================================================*/
UPDATE companies SET ubication = ST_SetSRID(ST_MakePoint(-70.64827, -33.43727), 4326) WHERE id = 1;
UPDATE companies SET ubication = ST_SetSRID(ST_MakePoint(-70.92792, -33.66819), 4326) WHERE id = 2;
UPDATE companies SET ubication = ST_SetSRID(ST_MakePoint(-70.57269, -33.41554), 4326) WHERE id = 3;
UPDATE companies SET ubication = ST_SetSRID(ST_MakePoint(-71.62000, -33.04500), 4326) WHERE id = 4;
UPDATE companies SET ubication = ST_SetSRID(ST_MakePoint(-71.63000, -33.04000), 4326) WHERE id = 5;

UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.65000, -33.43800), 4326) WHERE id = 1;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.64600, -33.43600), 4326) WHERE id = 2;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.65200, -33.44000), 4326) WHERE id = 3;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.65500, -33.43500), 4326) WHERE id = 4;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.64000, -33.43000), 4326) WHERE id = 5;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.57000, -33.41600), 4326) WHERE id = 6;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-70.57500, -33.42000), 4326) WHERE id = 7;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-71.62500, -33.04600), 4326) WHERE id = 8;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-71.62200, -33.04800), 4326) WHERE id = 9;
UPDATE clients SET ubication = ST_SetSRID(ST_MakePoint(-71.61800, -33.04200), 4326) WHERE id = 10;

UPDATE dealers SET ubication = ST_SetSRID(ST_MakePoint(-70.64700, -33.43900), 4326) WHERE id = 1;
UPDATE dealers SET ubication = ST_SetSRID(ST_MakePoint(-70.65500, -33.44500), 4326) WHERE id = 2;
UPDATE dealers SET ubication = ST_SetSRID(ST_MakePoint(-70.57000, -33.41500), 4326) WHERE id = 3;
UPDATE dealers SET ubication = ST_SetSRID(ST_MakePoint(-71.62400, -33.04400), 4326) WHERE id = 4;
UPDATE dealers SET ubication = ST_SetSRID(ST_MakePoint(-71.61900, -33.04700), 4326) WHERE id = 5;

/* =======================================================
   12. ACTUALIZACIÓN DE RUTAS ESTIMADAS
   =======================================================*/
UPDATE orders o
SET estimated_route = ST_MakeLine(d.ubication, c.ubication)
FROM dealers d, clients c
WHERE o.dealer_id = d.id AND o.client_id = c.id AND o.dealer_id IS NOT NULL AND o.client_id IS NOT NULL;

