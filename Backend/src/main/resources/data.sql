-- Kategorien
INSERT INTO category (active, description, img_url, title)
VALUES
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/white_dark_cookie_1920.jpg', 'Cookies'),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/cupcake_1920.jpg', 'Cupcakes'),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/browniex_1920.jpg', 'Brownies'),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/macarons-5264197_1920.jpg', 'Macarones');

-- Produkte
INSERT INTO product (active, description, img, price, stock, title, category_id)
VALUES
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/white_cookie1920.jpg', 6.99, 30, 'White Cookie', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/dark_cookie_1920.jpg', 5.99, 30, 'Dark Chocolate Cookie', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/CreamCookies.jpg', 5.99, 30, 'Cookies filled with cream', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/white_dark_cookie_1920.jpg', 5.99, 30, 'Cookies Mix', 1),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/classicBrownie.jpg', 7.99, 30, 'Classic Brownie', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/BrownieNuts.jpg', 7.99, 30, 'Brownie with Nuts', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/caramelBrownie.jpg', 6.99, 30, 'Caramel Brownie', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/brownieVanilla.jpg', 5.99, 30, 'Brownie with white Chocolate', 3),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/loveCupcake.jpg', 8.99, 30, 'Love Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/redVelvet.jpg', 8.99, 30, 'Red Velvet Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/oreoCupcake.jpg', 6.99, 30, 'Oreo Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/carrotcakeCupcake.jpg', 6.99, 30, 'Carrot Cupcake', 2),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/macaronsJam.jpg', 5.99, 30, 'Macarons with Marmelade', 4),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/chocolateMacarons.jpg', 5.99, 30, 'Chocolate Macarons', 4),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/macaronshero-7716584_1920.jpg', 5.99, 30, '12 Macarons Mix', 4),
    (true, 'Some quick example text to build on the card title and make up the bulk of the card''s content.', 'img/matchaMacarons.jpg', 5.99, 30, 'Matcha Macarons', 4);

--Address
INSERT INTO address (address, city, zip)
    VALUES
    ('Teststr. 1', 'Vienna', 1100);
-- Users
-- PW: test12!
INSERT INTO user (title, firstname, lastname, address_id, username, email, password, is_active, role)
VALUES
    ('Ms', 'Test', 'Admin', '1 Your Address Street', 'TestAdmin', 'test@admin.at', '123Abc!$2a$10$IzDStehb3v/YT9sKFc6Msu6FpQ5bAALugfFCZjW8R5kHkWl9zkTG.', true, 'ADMIN');