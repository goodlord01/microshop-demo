ALTER DATABASE jcart1 CHARACTER SET utf8 COLLATE utf8_unicode_ci;

insert into categories(id, name, disp_order,disabled) values
(1,'Flowers',1,false),
(2,'Toys',2,false),
(3,'Birds',3,false)
;

INSERT INTO products (id,cat_id,sku,name,description,image_url,price,disabled,created_on) VALUES
 (1,1,'P1001','氧泡泡油污净多用途清洁剂','特别适合清理重油污，泡沫粘得牢，去油更强效','1.jpg','430.00',false,now())
;


INSERT INTO customers (id,firstname,lastname,email,phone,password)
VALUES
  (1,'文','李','sivaprasadreddy.k@gmail.com','999999999','$2a$10$UFEPYW7Rx1qZqdHajzOnB.VBR3rvm7OI7uSix4RadfQiNhkZOi2fi'),
  (2,'Ramu','P','ramu@gmail.com','8888888888','$2a$10$UoEimdoV95/jTs2E99ARLO.eUBxYVcDZamedqhkwfPUx9iOMFEfyq')
  ;

insert into addresses (id, address_line) values
(1, '杭州市西湖区文二路');

insert into orders (id, order_number, cust_id, delivery_addr_id, status, created_on, product_id, quantity) values
(1, '1447737431927', 1, 1, 'NEW', now(), 1, 2);