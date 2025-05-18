USE webshop;

-- Create the User table (for authentication and shared user data)
CREATE TABLE IF NOT EXISTS User
(
    UserID           INT AUTO_INCREMENT PRIMARY KEY,
    Email            VARCHAR(100)               NOT NULL UNIQUE,
    Username         VARCHAR(50)                NOT NULL UNIQUE,
    Password         VARCHAR(255)               NOT NULL,
    FirstName        VARCHAR(50)                NOT NULL,
    LastName         VARCHAR(50)                NOT NULL,
    Salutation       VARCHAR(10)                NOT NULL,
    BillingAddressId INT                        NOT NULL,
    DateOfBirth      DATE                       NOT NULL,
    ROLE             ENUM ('Customer', 'Admin') NOT NULL DEFAULT 'Customer'
);

-- Create the Address table
CREATE TABLE IF NOT EXISTS Address
(
    AddressID     INT AUTO_INCREMENT PRIMARY KEY,
    UserID        INT,
    PostalCode    VARCHAR(20)  NOT NULL,
    Country       VARCHAR(100) NOT NULL,
    City          VARCHAR(100) NOT NULL,
    StreetAddress VARCHAR(255) NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User (UserID) ON DELETE CASCADE
);

-- Check if the foreign key constraint already exists before adding it
SET @constraint_exists = (SELECT COUNT(*)
                          FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
                          WHERE CONSTRAINT_SCHEMA = DATABASE()
                            AND TABLE_NAME = 'User'
                            AND CONSTRAINT_NAME = 'FK_User_BillingAddress'
                            AND CONSTRAINT_TYPE = 'FOREIGN KEY');

SET @sql = IF(@constraint_exists = 0,
              'ALTER TABLE User ADD CONSTRAINT FK_User_BillingAddress FOREIGN KEY (BillingAddressId) REFERENCES Address(AddressID) ON DELETE CASCADE',
              'SELECT ''Constraint FK_User_BillingAddress already exists'' AS status'
           );

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Create the Category table
CREATE TABLE IF NOT EXISTS Category
(
    CategoryID INT AUTO_INCREMENT PRIMARY KEY,
    Name       VARCHAR(100) NOT NULL UNIQUE
);

-- Create the Product table
CREATE TABLE IF NOT EXISTS Product
(
    ProductID   INT AUTO_INCREMENT PRIMARY KEY,
    Name        VARCHAR(100)   NOT NULL,
    Description TEXT           NOT NULL,
    ImageURL    VARCHAR(255)   NOT NULL,
    CategoryID  INT            NOT NULL,
    Stock       INT            NOT NULL DEFAULT 0,
    Price       DECIMAL(10, 2) NOT NULL,
    AvgRating   DECIMAL(3, 2)           DEFAULT 0.00, -- Rating between 0 and 5, e.g., 4.75
    FOREIGN KEY (CategoryID) REFERENCES Category (CategoryID) ON DELETE RESTRICT
);

-- Create the Review table
CREATE TABLE IF NOT EXISTS Review
(
    ReviewID  INT AUTO_INCREMENT PRIMARY KEY,
    ProductID INT NOT NULL,
    UserID    INT NOT NULL,
    Review    TEXT,
    Rating    INT NOT NULL CHECK (Rating >= 1 AND Rating <= 5), -- Rating between 1 and 5
    FOREIGN KEY (ProductID) REFERENCES Product (ProductID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES User (UserID) ON DELETE CASCADE
);

-- Create the Order table
CREATE TABLE IF NOT EXISTS `Order`
(
    OrderID   INT AUTO_INCREMENT PRIMARY KEY,
    UserID    INT            NOT NULL,
    AddressID INT            NOT NULL,
    `Date`    DATE           NOT NULL,
    SumPrice  DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (UserID) REFERENCES User (UserID) ON DELETE RESTRICT,
    FOREIGN KEY (AddressID) REFERENCES Address (AddressID) ON DELETE RESTRICT
);

-- Create the OrderItem table
CREATE TABLE IF NOT EXISTS OrderItem
(
    OrderItemID INT AUTO_INCREMENT PRIMARY KEY,
    ProductID   INT NOT NULL,
    OrderID     INT NOT NULL,
    Quantity    INT NOT NULL CHECK (Quantity > 0),
    FOREIGN KEY (ProductID) REFERENCES Product (ProductID) ON DELETE RESTRICT,
    FOREIGN KEY (OrderID) REFERENCES `Order` (OrderID) ON DELETE CASCADE
);

INSERT IGNORE INTO Category (Name)
VALUES
    ('Action'),
    ('RPGs'),
    ('Strategie'),
    ('Simulationen'),
    ('Shooter'),
    ('Adventure'),
    ('Jump \'n\' Run'),
    ('MOBA'),
    ('Survival'),
    ('Open World');

