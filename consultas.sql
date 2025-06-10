-- ========================
-- CONSULTAS SQL
-- ========================

--- CONSULTA COMPLEJA 1---
SELECT c.id, c.name, c.rut, c.email, c.phone, c.address, SUM(o.total_price) AS total_spent
FROM orders o
         JOIN clients c ON o.client_id = c.id
WHERE o.status = 'ENTREGADO'
GROUP BY c.id, c.name, c.rut, c.email, c.phone, c.address
ORDER BY total_spent DESC
    LIMIT 1

--- CONSULTA COMPLEJA 2---
SELECT
    p.category,
    p.name AS product_name,
    COUNT(op.product_id) AS product_count
FROM
    products p
        JOIN
    order_products op ON p.id = op.product_id
        JOIN
    orders o ON o.id = op.order_id
WHERE
        o.order_date >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
  AND o.order_date < DATE_TRUNC('month', CURRENT_DATE)
GROUP BY
    p.category, p.name
ORDER BY
    p.category, product_count DESC

--- CONSULTA COMPLEJA 3---
SELECT
    c.id,
    c.name,
    c.email,
    c.phone,
    c.address,
    c.rut,
    c.type,
    COUNT(o.id) AS deliveries, -- Total de entregas
    SUM(CASE WHEN o.status = 'FALLIDA' THEN 1 ELSE 0 END) AS failed_deliveries, -- Total de entregas fallidas
    SUM(o.total_price) AS total_sales -- Total de ventas
FROM
    companies c
        LEFT JOIN orders o ON c.id = o.client_id -- Relación entre empresas y pedidos
GROUP BY
    c.id, c.name, c.email, c.phone, c.address, c.rut, c.type

--- CONSULTA COMPLEJA 4---
SELECT
    d.id AS dealer_id,
    d.name AS dealer_name,
    AVG(EXTRACT(EPOCH FROM (o.delivery_date - o.order_date)) / 3600) AS avg_delivery_time_hours
FROM
    orders o
        JOIN
    dealers d ON o.dealer_id = d.id
WHERE
        o.status = 'ENTREGADO' AND o.delivery_date IS NOT NULL
GROUP BY
    d.id, d.name
ORDER BY
    avg_delivery_time_hours ASC;

--- CONSULTA COMPLEJA 5---
SELECT
    d.id AS dealer_id,
    d.name AS dealer_name,
    COUNT(o.id) AS total_deliveries,
    COALESCE(AVG(r.rating), 0) AS avg_rating,
    (COUNT(o.id) * 0.7 + COALESCE(AVG(r.rating), 0) * 0.3) AS performance_score
FROM
    dealers d
        LEFT JOIN
    orders o ON d.id = o.dealer_id AND o.status = 'ENTREGADO'
        LEFT JOIN
    ratings r ON d.id = r.dealer_id
GROUP BY
    d.id, d.name
ORDER BY
    performance_score DESC
    LIMIT 3;


--- CONSULTA COMPLEJA 6---
SELECT
    od.payment_method,
    COUNT(*) AS usage_count
FROM
    orders o
        JOIN
    order_details od ON o.id = od.order_id
WHERE
        o.status = 'URGENTE'
GROUP BY
    od.payment_method
ORDER BY
    usage_count DESC
    LIMIT 1;