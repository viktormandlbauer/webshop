INSERT INTO User (Email, Username, Password, Salt) VALUES
('john.doe@example.com', 'john_doe', 'hashed_password_1', 'salt_1'),
('jane.smith@example.com', 'jane_smith', 'hashed_password_2', 'salt_ Romance'),
('admin@example.com', 'super_admin', 'hashed_password_3', 'salt_3'),
('alice.brown@example.com', 'alice_b', 'hashed_password_4', 'salt_4'),
('bob.jones@example.com', 'bob_j', 'hashed_password_5', 'salt_5');

INSERT INTO Customer (UserID, FirstName, LastName, TelephoneNumber) VALUES
(1, 'John', 'Doe', '123-456-7890'),
(2, 'Jane', 'Smith', '234-567-8901'),
(4, 'Alice', 'Brown', '345-678-9012'),
(5, 'Bob', 'Jones', '456-789-0123');

INSERT INTO Administrator (UserID, Role) VALUES
(3, 'SuperAdmin');

INSERT INTO Category (Name) VALUES
('Electronics'),
('Books'),
('Clothing'),
('Home & Garden');

INSERT INTO Address (CustomerID, PostalCode, Country, City, StreetAddress) VALUES
(1, '12345', 'USA', 'New York', '123 Main St'),
(1, '12346', 'USA', 'New York', '456 Elm St'),
(2, '67890', 'USA', 'Los Angeles', '789 Oak Ave'),
(3, '54321', 'USA', 'Chicago', '321 Pine Rd'),
(4, '98765', 'USA', 'Seattle', '654 Birch Ln');

INSERT INTO Product (Name, Description, ImageURL, CategoryID, Stock, Price, AvgRating) VALUES
('Smartphone X', 'Latest model with 128GB storage', '/images/smartphone_x.jpg', 1, 50, 699.99, 4.50),
('Laptop Pro', 'High-performance laptop with 16GB RAM', '/images/laptop_pro.jpg', 1, 30, 1299.99, 4.75),
('Mystery Novel', 'Bestselling thriller book', '/images/mystery_novel.jpg', 2, 100, 19.99, 4.20),
('T-Shirt', 'Comfortable cotton t-shirt', '/images/tshirt.jpg', 3, 200, 14.99, 3.80),
('Garden Chair', 'Durable outdoor chair', '/images/garden_chair.jpg', 4, 25, 49.99, 4.00);

INSERT INTO Review (ProductID, CustomerID, Review, Rating) VALUES
(1, 1, 'Great phone, fast and reliable!', 5),
(1, 2, 'Good but battery life could be better.', 4),
(2, 3, 'Amazing laptop for work!', 5),
(3, 1, 'Really enjoyed this book.', 4),
(4, 4, 'Nice fit, but color faded after wash.', 3);

INSERT INTO `Order` (CustomerID, AddressID, `Date`, SumPrice) VALUES
(1, 1, '2025-04-01', 719.98),
(2, 3, '2025-04-02', 34.98),
(3, 4, '2025-04-03', 1299.99),
(4, 5, '2025-04-04', 64.98);

INSERT INTO OrderItem (ProductID, OrderID, Quantity) VALUES
(1, 1, 1), -- Smartphone X
(3, 1, 1), -- Mystery Novel
(4, 2, 2), -- T-Shirt
(2, 3, 1), -- Laptop Pro
(5, 4, 1), -- Garden Chair
(4, 4, 1); -- T-Shirt