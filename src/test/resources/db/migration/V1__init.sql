CREATE TABLE item (
    id VARCHAR(20) PRIMARY KEY,
    description TEXT,
    price FLOAT,
    number_of_sales INTEGER,
    first_sale DATE,
    local_date_time_field TIMESTAMP(9),
    zoned_date_time_field TIMESTAMP(9)
);
