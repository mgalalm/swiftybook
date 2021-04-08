INSERT INTO place.categories (id, created_date, last_modified_date, description, name)
VALUES  (1, current_timestamp, current_timestamp, 'test', 'Hotel'),
        (2, current_timestamp, current_timestamp, 'test', 'Restaurant');
INSERT INTO place.places (id, created_date, last_modified_date, available_from, available_to, description, title, price, status, address_1, address_2, city, country, postcode, image_url, category_id)
VALUES  (1, current_timestamp, current_timestamp, current_date  , current_date + 30, 'test', 'Cork Hotel', 300.00, 'AVAILABLE', null, null, 'Cork', 'IE', null, null, 1),
        (2, current_timestamp, current_timestamp, current_date + 1, current_date + 60, 'test', 'Four star pizza', 300.00, 'AVAILABLE', null, null, 'Limerick', 'IE', null, null, 2);
INSERT INTO place.reviews (id, created_date, last_modified_date, description, rating, title)
VALUES  (1, current_timestamp, current_timestamp, 'Horrible', 1, 'Horrible'),
        (2, current_timestamp, current_timestamp, 'Excellent', 1, 'Excellent');
INSERT INTO place.places_reviews (place_id, reviews_id)
VALUES  (1, 1);