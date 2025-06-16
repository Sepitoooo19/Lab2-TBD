-- ========================
-- RESET DATABASE (Eliminar todo antes de recrear)
-- ========================

-- Opción 1: DROP CASCADE para eliminar tablas y dependencias
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS order_products CASCADE;
DROP TABLE IF EXISTS company_payment_methods CASCADE;
DROP TABLE IF EXISTS order_details CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS dealers CASCADE;
DROP TABLE IF EXISTS clients CASCADE;
DROP TABLE IF EXISTS payment_methods CASCADE;
DROP TABLE IF EXISTS companies CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS coverage_area CASCADE;
DROP TABLE IF EXISTS coverage_area_company CASCADE;
DROP TABLE IF EXISTS emergency_report CASCADE;


-- Eliminar procedimientos almacenados
DROP PROCEDURE IF EXISTS register_order_with_products(TIMESTAMP, VARCHAR, INT, INT[], INT);
DROP PROCEDURE IF EXISTS change_order_status(INT, VARCHAR, TIMESTAMP);

-- Eliminar funciones de triggers
DROP FUNCTION IF EXISTS set_delivery_date_when_delivered() CASCADE;
DROP FUNCTION IF EXISTS log_failed_order() CASCADE;
DROP FUNCTION IF EXISTS insert_auto_rating_if_late() CASCADE;

-- Eliminar extensión si es necesario (opcional)
-- DROP EXTENSION IF EXISTS postgis CASCADE;

-- ========================
-- RECREAR ESTRUCTURA
-- ========================
CREATE EXTENSION IF NOT EXISTS postgis;

-- Tabla: users
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) CHECK (role IN ('ADMIN', 'CLIENT', 'DEALER')) NOT NULL
);

CREATE TABLE companies (
                           id SERIAL PRIMARY KEY,          -- Identificador único de la compañía
                           name VARCHAR(100) NOT NULL,     -- Nombre de la compañía
                           email VARCHAR(100),             -- Correo electrónico de la compañía
                           phone VARCHAR(20),              -- Teléfono de la compañía
                           address VARCHAR(255),           -- Dirección de la compañía
                           rut VARCHAR(20),                -- RUT de la compañía
                           type VARCHAR(50),               -- Tipo de compañía
                           deliveries INT DEFAULT 0,       -- Total de entregas realizadas
                           failed_deliveries INT DEFAULT 0,-- Total de entregas fallidas
                           total_sales FLOAT DEFAULT 0,     -- Total de ventas realizadas
                           ubication geometry(Point, 4326)
);

-- Tabla: payment_methods
CREATE TABLE payment_methods (
                                 id SERIAL PRIMARY KEY,
                                 type VARCHAR(50) NOT NULL
);

-- ========================
-- PRODUCTS & ORDERS
-- ========================

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          stock INT NOT NULL,
                          price FLOAT NOT NULL,
                          category VARCHAR(50),
                          company_id INT,
                          FOREIGN KEY (company_id) REFERENCES companies(id)
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        order_date TIMESTAMP,
                        delivery_date TIMESTAMP,
                        status VARCHAR(50),
                        client_id INT,
                        dealer_id INT,
                        total_price FLOAT,
                        estimated_route  GEOMETRY(LineString, 4326)
);

CREATE TABLE order_details (
                               id SERIAL PRIMARY KEY,
                               order_id INT UNIQUE,
                               payment_method VARCHAR(50),
                               total_products INT,
                               price FLOAT,
                               FOREIGN KEY (order_id) REFERENCES orders(id)
);


-- ========================
-- USERS (EXTENDED PROFILES)
-- ========================

-- Tabla: clients
CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL,
                         rut VARCHAR(20),
                         email VARCHAR(100),
                         phone VARCHAR(20),
                         address VARCHAR(255),
                         user_id INT UNIQUE,
                         ubication geometry(Point, 4326),  -- Nuevo campo para coordenadas geográficas
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Tabla: dealers
CREATE TABLE dealers (
                         id SERIAL PRIMARY KEY,
                         rut VARCHAR(20),
                         name VARCHAR(100),
                         phone VARCHAR(20),
                         email VARCHAR(100),
                         vehicle VARCHAR(50),
                         plate VARCHAR(20),
                         user_id INT UNIQUE,
                         ubication geometry(Point, 4326),
                         FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE coverage_area (
                               coverage_id SERIAL PRIMARY KEY,
                               name VARCHAR(100) NOT NULL,
                               coverageArea geometry(Polygon, 4326) NOT NULL
);

-- ========================
-- RATINGS
-- ========================

ALTER TABLE orders
    ADD FOREIGN KEY (client_id) REFERENCES clients(id),
    ADD FOREIGN KEY (dealer_id) REFERENCES dealers(id);

CREATE TABLE ratings (
                         id SERIAL PRIMARY KEY,
                         rating INT NOT NULL,
                         comment TEXT,
                         date DATE,
                         client_id INT,
                         dealer_id INT,
                         order_id INT,
                         FOREIGN KEY (client_id) REFERENCES clients(id),
                         FOREIGN KEY (dealer_id) REFERENCES dealers(id),
                         FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE emergency_report (
                                  id SERIAL PRIMARY KEY,
                                  order_id INT NOT NULL,
                                  dealer_id INT NOT NULL,
                                  ubication GEOMETRY(Point, 4326) NOT NULL,

                                  FOREIGN KEY (order_id) REFERENCES orders(id),
                                  FOREIGN KEY (dealer_id) REFERENCES dealers(id)

);


-- ========================
-- RELATIONAL TABLES (N:N)
-- ========================

-- Relación empresas-medios de pago
CREATE TABLE company_payment_methods (
                                         company_id INT,
                                         payment_method_id INT,
                                         PRIMARY KEY (company_id, payment_method_id),
                                         FOREIGN KEY (company_id) REFERENCES companies(id),
                                         FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
);

CREATE TABLE coverage_area_company
(
    company_id INT,
    coverage_id INT,
    PRIMARY KEY (company_id, coverage_id),
    FOREIGN KEY (company_id) REFERENCES companies(id),
    FOREIGN KEY (coverage_id) REFERENCES coverage_area(coverage_id)


);

-- Relación pedidos-productos
CREATE TABLE order_products (
                                order_id INT,
                                product_id INT,
                                PRIMARY KEY (order_id, product_id),
                                FOREIGN KEY (order_id) REFERENCES orders(id),
                                FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ========================
-- PROCEDIMIENTOS ALMACENADOS
-- ========================

-- 1 y 3 Registrar un pedido completo y descuenta el stock.
CREATE OR REPLACE PROCEDURE register_order_with_products(
    p_order_date TIMESTAMP,
    p_status VARCHAR,
    p_client_id INT,
    p_product_ids INT[],
    p_dealer_id INT DEFAULT NULL  -- Mantener este parámetro aunque no se use
)
LANGUAGE plpgsql
AS $$
DECLARE
v_order_id INT;
    v_product_id INT;
    v_total_price FLOAT := 0.0;
    v_client_location GEOMETRY;
    v_company_location GEOMETRY;
    v_estimated_route GEOMETRY;
BEGIN
    -- 1. Validar que haya productos
    IF array_length(p_product_ids, 1) IS NULL OR array_length(p_product_ids, 1) = 0 THEN
        RAISE EXCEPTION 'La orden debe contener al menos un producto';
END IF;

    -- 2. Obtener ubicación del cliente
SELECT ubication INTO v_client_location
FROM clients
WHERE id = p_client_id;

IF v_client_location IS NULL THEN
        RAISE EXCEPTION 'El cliente con ID % no tiene ubicación registrada', p_client_id;
END IF;

    -- 3. Obtener empresa del primer producto para la ubicación
SELECT c.ubication INTO v_company_location
FROM products p
         JOIN companies c ON p.company_id = c.id
WHERE p.id = p_product_ids[1]
    LIMIT 1;

IF v_company_location IS NULL THEN
        RAISE EXCEPTION 'No se pudo determinar la ubicación de la empresa para el producto ID %', p_product_ids[1];
END IF;

    -- 4. Calcular ruta recta (línea directa entre empresa y cliente)
    v_estimated_route := ST_MakeLine(v_company_location, v_client_location);

    -- 5. Calcular el precio total de los productos
SELECT COALESCE(SUM(price), 0)
INTO v_total_price
FROM products
WHERE id = ANY(p_product_ids);

-- 6. Insertar la orden con la ruta estimada
INSERT INTO orders (
    order_date,
    status,
    client_id,
    dealer_id,  -- Se usa el parámetro p_dealer_id
    total_price,
    estimated_route
)
VALUES (
           p_order_date,
           p_status,
           p_client_id,
           p_dealer_id,  -- Usar el parámetro en lugar de NULL fijo
           v_total_price,
           v_estimated_route
       )
    RETURNING id INTO v_order_id;

-- 7. Procesar productos
FOREACH v_product_id IN ARRAY p_product_ids LOOP
        -- Registrar producto en la orden
        INSERT INTO order_products (order_id, product_id)
        VALUES (v_order_id, v_product_id);

        -- Reducir stock
UPDATE products
SET stock = stock - 1
WHERE id = v_product_id AND stock > 0;

IF NOT FOUND THEN
            RAISE EXCEPTION 'Sin stock para el producto ID %', v_product_id;
END IF;
END LOOP;
END;
$$;

-- ========================

-- 2 Cambiar el estado de un pedido con validación
CREATE OR REPLACE PROCEDURE change_order_status(
    p_order_id INT,
    p_new_status VARCHAR,
    p_delivery_date TIMESTAMP DEFAULT NULL
)
LANGUAGE plpgsql
AS $$
DECLARE
v_current_status VARCHAR;
BEGIN
    -- Validar que el pedido exista y obtener su estado actual
SELECT status INTO v_current_status
FROM orders
WHERE id = p_order_id;

IF NOT FOUND THEN
        RAISE EXCEPTION 'Pedido con ID % no existe', p_order_id;
END IF;

    -- Validar que aún no esté finalizado
    IF v_current_status IN ('ENTREGADO', 'FALLIDA') THEN
        RAISE EXCEPTION 'El pedido ya ha sido finalizado con estado %', v_current_status;
END IF;

    -- Actualizar el estado y la fecha si corresponde
    IF p_new_status = 'ENTREGADO' THEN
UPDATE orders
SET status = p_new_status,
    delivery_date = COALESCE(p_delivery_date, NOW())
WHERE id = p_order_id;
ELSE
UPDATE orders
SET status = p_new_status
WHERE id = p_order_id;
END IF;
END;
$$;

-- ========================
-- TRIGGERS
-- ========================

-- 1. Insertar automáticamente la fecha de entrega al marcar como entregado.
-- Trigger function:
CREATE OR REPLACE FUNCTION set_delivery_date_when_delivered()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'ENTREGADO' AND NEW.delivery_date IS NULL THEN
        NEW.delivery_date := NOW();
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger:
CREATE TRIGGER trg_set_delivery_date
    BEFORE UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION set_delivery_date_when_delivered();

-- 2. Registrar una notificación si se detecta un problema crítico en el pedido.
-- Trigger function:
CREATE OR REPLACE FUNCTION log_failed_order()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status = 'FALLIDA' AND OLD.status IS DISTINCT FROM NEW.status THEN
        RAISE NOTICE 'Pedido % marcado como FALLIDA', NEW.id;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger:
CREATE TRIGGER trg_log_failed_order
    AFTER UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION log_failed_order();

-- 3. Insertar una calificación automática si no se recibe en 48 horas.
-- Trigger function:
CREATE OR REPLACE FUNCTION insert_auto_rating_if_late()
RETURNS TRIGGER AS $$
BEGIN
    -- Verifica si el estado se cambió a ENTREGADO y la entrega fue hace más de 48 horas
    IF NEW.status = 'ENTREGADO'
       AND OLD.status IS DISTINCT FROM NEW.status
       AND NEW.delivery_date IS NOT NULL
       AND NEW.delivery_date <= NOW() - INTERVAL '48 hours' THEN

        -- Verifica si ya existe una calificación para esta orden
        IF NOT EXISTS (
            SELECT 1 FROM ratings WHERE order_id = NEW.id
        ) THEN
            INSERT INTO ratings (rating, comment, date, client_id, dealer_id, order_id)
            VALUES (
                1,
                'Calificación automática: no se recibió calificación en 48h.',
                CURRENT_DATE,
                NEW.client_id,
                NEW.dealer_id,
                NEW.id
            );
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger:
CREATE TRIGGER trg_auto_rating_if_late
    AFTER UPDATE ON orders
    FOR EACH ROW
    WHEN (OLD.status IS DISTINCT FROM NEW.status)
EXECUTE FUNCTION insert_auto_rating_if_late();

-- ========================
-- MENSAJE DE CONFIRMACIÓN
-- ========================
DO $$
BEGIN
    RAISE NOTICE 'Base de datos reseteada y recreada exitosamente.';
END $$;