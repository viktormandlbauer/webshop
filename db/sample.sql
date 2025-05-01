-- Insert into Category table
INSERT INTO Category (Name) VALUES
('Electronics'),
('Clothing'),
('Books');

-- Insert into Product table
INSERT INTO Product (Name, Description, ImageURL, CategoryID, Stock, Price, AvgRating) VALUES
('Smartphone X', 'Latest smartphone with 128GB storage and 5G', 'https://example.com/images/smartphone.jpg', 1, 50, 699.99, 4.50),
('Wireless Headphones', 'Noise-cancelling over-ear headphones', 'https://example.com/images/headphones.jpg', 1, 30, 199.99, 4.20),
('Graphic T-Shirt', '100% cotton t-shirt with unique design', 'https://example.com/images/tshirt.jpg', 2, 100, 19.99, 4.00),
('Denim Jeans', 'Slim-fit denim jeans in blue', 'https://example.com/images/jeans.jpg', 2, 80, 49.99, 4.30),
('Python Programming', 'Comprehensive guide to Python programming', 'https://example.com/images/python_book.jpg', 3, 40, 29.99, 4.75),
('Data Science Intro', 'Introduction to data science with R and Python', 'https://example.com/images/datascience_book.jpg', 3, 25, 39.99, 4.60);