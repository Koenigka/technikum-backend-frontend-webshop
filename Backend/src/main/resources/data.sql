SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE category;
TRUNCATE TABLE product;
TRUNCATE TABLE address;
TRUNCATE TABLE users;

-- Kategorien
INSERT INTO category (active, description, img_url, title)
VALUES
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'white_dark_cookie_1920.jpg', 'Cookies'),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'cupcake_1920.jpg', 'Cupcakes'),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'browniex_1920.jpg', 'Brownies'),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'macarons-5264197_1920.jpg', 'Macarones');

-- Produkte
INSERT INTO product (active, description, img, price, stock, title, category_id)
VALUES
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'white_cookie1920.jpg', 6.99, 30, 'White Cookie', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'dark_cookie_1920.jpg', 5.99, 30, 'Dark Chocolate Cookie', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'CreamCookies.jpg', 5.99, 30, 'Cookies filled with cream', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'white_dark_cookie_1920.jpg', 5.99, 30, 'Cookies Mix', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'classicBrownie.jpg', 7.99, 30, 'Classic Brownie', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'BrownieNuts.jpg', 7.99, 30, 'Brownie with Nuts', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'caramelBrownie.jpg', 6.99, 30, 'Caramel Brownie', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'brownieVanilla.jpg', 5.99, 30, 'Brownie with white Chocolate', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'loveCupcake.jpg', 8.99, 30, 'Love Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'redVelvet.jpg', 8.99, 30, 'Red Velvet Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'oreoCupcake.jpg', 6.99, 30, 'Oreo Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'carrotcakeCupcake.jpg', 6.99, 30, 'Carrot Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'macaronsJam.jpg', 5.99, 30, 'Macarons with Marmelade', 4),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'chocolateMacarons.jpg', 5.99, 30, 'Chocolate Macarons', 4),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'macaronshero-7716584_1920.jpg', 5.99, 30, '12 Macarons Mix', 4),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'matchaMacarons.jpg', 5.99, 30, 'Matcha Macarons', 4);

--Address
INSERT INTO address (address, city, zip)
    VALUES
    ('Teststr. 1', 'Vienna', 1100);
-- Users
-- PW: test12!
INSERT INTO users (title, firstname, lastname, address_id, username, email, password, is_active, roles)
VALUES
    ('Ms', 'Test', 'Admin', 1, 'Admin', 'admin@test.at', '$2a$10$hfqhVFPc8CbVGbtCjp909udpt1Ckm3SD81qvAPNcA.x2KXxD07tJ.', true, 'ADMIN'),
    ('Ms', 'Test', 'User', 1, 'User', 'user@test.at', '$2a$10$jYAKNTvogtsVgZCGqzA0Q.2MmotJP.aNa/mMzdH1cTk1FVTa8Ixaq', true, 'USER'),
    ('Ms', 'Inactive', 'User', 1, 'User', 'inactive@test.at', '$2a$10$jYAKNTvogtsVgZCGqzA0Q.2MmotJP.aNa/mMzdH1cTk1FVTa8Ixaq', false, 'USER');



SET REFERENTIAL_INTEGRITY TRUE;