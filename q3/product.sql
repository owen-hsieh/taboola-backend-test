CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY, 
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    added_by VARCHAR(255) NOT NULL
);

CREATE TABLE product_price (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    discount_percent DECIMAL(5, 2) DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TABLE product_price_change_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    old_price DECIMAL(10,2) NOT NULL,
    new_price DECIMAL(10,2) NOT NULL,
    change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id)
);

CREATE TRIGGER insert_product_price_logger
AFTER INSERT ON product_price
FOR EACH ROW
BEGIN
	INSERT INTO product_price_change_log (product_id, old_price, new_price, changed_by)
	VALUES (NEW.product_id, 0, NEW.price, NEW.updated_by);
END;


CREATE TRIGGER update_product_price_logger
AFTER UPDATE ON product_price
FOR EACH ROW
BEGIN
	INSERT INTO product_price_change_log (product_id, old_price, new_price, changed_by)
	VALUES (NEW.product_id, OLD.price, NEW.price, NEW.updated_by);
END;


CREATE TRIGGER delete_product_price_logger
AFTER DELETE ON product_price
FOR EACH ROW
BEGIN
	INSERT INTO product_price_change_log (product_id, old_price, new_price, changed_by)
	VALUES (OLD.product_id, OLD.price, 0, OLD.updated_by);
END;

SELECT p.name, p.category, pp.price, pp.last_updated, pp.updated_by
FROM product p
JOIN product_price pp ON p.id = pp.product_id;