-- CREATING DATABASE --
CREATE DATABASE product_review_db COLLATE LATIN1_GENERAL_100_CI_AS_SC_UTF8
GO

-- CREATING TABLES --
USE product_review_db;

CREATE TABLE country (
	country_code char(3),
	[name] varchar(100) NOT NULL,
	CONSTRAINT pk_country_country_code PRIMARY KEY(country_code),
	CONSTRAINT uq_country_name UNIQUE([name])
);

CREATE TABLE [role] (
	[name] varchar(100),
	CONSTRAINT pk_role_name PRIMARY KEY([name])
);

CREATE TABLE [user] (
	id int IDENTITY(1,1),
	username varchar(100) NOT NULL,
	[password] varchar(1000) NOT NULL,
	country char(3) NOT NULL,
	registered datetime2 NOT NULL CONSTRAINT df_user_registered DEFAULT SYSDATETIME(),
	[role] varchar(100) NOT NULL,
	is_active bit NOT NULL,
	CONSTRAINT pk_user_id PRIMARY KEY (id),
	CONSTRAINT fk_user_country FOREIGN KEY (country) REFERENCES country(country_code),
	CONSTRAINT fk_user_role FOREIGN KEY ([role]) REFERENCES [role]([name]),
	CONSTRAINT uq_user_username UNIQUE (username),
	CONSTRAINT chk_user_date_is_recent CHECK(
		registered <= SYSDATETIME() AND
		registered >= DATEADD(MINUTE, -1, SYSDATETIME())
	)
);

CREATE TABLE brand (
	id int IDENTITY(1,1),
	[name] varchar(100) NOT NULL,
	country char(3) NOT NULL,
	[description] varchar(1000),
	CONSTRAINT pk_brand_id PRIMARY KEY (id),
	CONSTRAINT fk_brand_country FOREIGN KEY (country) REFERENCES country(country_code),
	CONSTRAINT uq_brand_name UNIQUE ([name])
);

CREATE TABLE category (
	id int IDENTITY(1,1),
	[name] varchar(100) NOT NULL,
	parent_category int,
	[description] varchar(1000),
	CONSTRAINT pk_category_id PRIMARY KEY(id),
	CONSTRAINT fk_category_category FOREIGN KEY (parent_category) REFERENCES category(id),
	CONSTRAINT uq_category_name UNIQUE ([name])
);

CREATE TABLE characteristic (
	id int IDENTITY(1,1),
	[name] varchar(100) NOT NULL,
	unit_of_measure_name varchar(100),
	unit_of_measure varchar(100),
	[description] varchar(100),
	CONSTRAINT pk_characteristic_id PRIMARY KEY(id),
	CONSTRAINT uq_characteristic_unit_of_measure UNIQUE ([name], unit_of_measure),
	-- unit of measure and its name must be both present or both null
	CONSTRAINT chk_characteristic_unit_and_unit_name_both_present CHECK
		((unit_of_measure_name IS NULL AND unit_of_measure IS NULL) OR
		 (unit_of_measure_name IS NOT NULL AND unit_of_measure IS NOT NULL))
);

CREATE TABLE category_x_characteristic (
    id int IDENTITY(1,1),
	category int,
	characteristic int,
	CONSTRAINT pk_category_x_characteristic PRIMARY KEY (id),
	CONSTRAINT fk_category_x_characteristic_category FOREIGN KEY (category) REFERENCES category(id),
	CONSTRAINT fk_category_x_characteristic_characteristic FOREIGN KEY (characteristic) REFERENCES characteristic(id)
);

CREATE TABLE article (
	id int IDENTITY(1,1),
	[name] varchar(100) NOT NULL,
	brand int NOT NULL,
	category int NOT NULL,
	[description] varchar(1000),
	CONSTRAINT pk_article_id PRIMARY KEY (id),
	CONSTRAINT fk_article_brand FOREIGN KEY (brand) REFERENCES brand(id),
	CONSTRAINT fk_article_category FOREIGN KEY (category) REFERENCES category(id)
);

CREATE TABLE packaging (
	id int IDENTITY(1,1),
	name varchar(100) NOT NULL,
	amount smallint NOT NULL,
	unit_of_measure varchar(100),
	unit_of_measure_name varchar(100),
	size varchar(100),
	CONSTRAINT pk_packaging_id PRIMARY KEY (id),
	CONSTRAINT chk_packaging_amount_non_negative CHECK (amount >= 0),
	-- unit of measure and its name must be both present or both null
	CONSTRAINT chk_packaging_unit_and_unit_name_both_present CHECK
		((unit_of_measure_name IS NULL AND unit_of_measure IS NULL) OR
		 (unit_of_measure_name IS NOT NULL AND unit_of_measure IS NOT NULL))
);

CREATE TABLE product (
	id int IDENTITY(1,1),
	article int NOT NULL,
	packaging int NOT NULL,
	CONSTRAINT pk_product PRIMARY KEY (id),
	CONSTRAINT fk_product_article FOREIGN KEY (article) REFERENCES article(id),
	CONSTRAINT fk_product_packaging FOREIGN KEY (packaging) REFERENCES packaging(id),
	CONSTRAINT uq_product UNIQUE (article, packaging)
);

CREATE TABLE product_characteristic_value (
	product int,
	characteristic int,
	[value] varchar(100) NOT NULL,
	CONSTRAINT pk_product_characteristic_value PRIMARY KEY (product, characteristic),
	CONSTRAINT fk_product_characteristic_value_article FOREIGN KEY (product) REFERENCES product(id),
);

CREATE TABLE product_image (
	id int IDENTITY(1,1),
	product int NOT NULL,
	[image] varbinary(max) NOT NULL,
	CONSTRAINT pk_product_image PRIMARY KEY (id),
	CONSTRAINT fk_product_image_product FOREIGN KEY (product) REFERENCES product(id)
);

CREATE TABLE aspect (
	id int IDENTITY(1,1),
	[name] varchar(100) NOT NULL,
	question varchar(100) NOT NULL,
	category int NOT NULL,
	CONSTRAINT pk_aspect PRIMARY KEY (id),
	CONSTRAINT fk_aspect_category FOREIGN KEY (category) REFERENCES category(id)
);

CREATE TABLE review_head (
	[user] int,
	product int,
	[date] datetime2 NOT NULL CONSTRAINT df_review_head_date DEFAULT SYSDATETIME(),
	overall_review varchar(1000) NOT NULL,
	recommended bit NOT NULL,
	purchase_country char(3) NOT NULL,
	value_for_price tinyint NOT NULL,
	CONSTRAINT pk_review_head PRIMARY KEY ([user], product),
	CONSTRAINT fk_review_head_user FOREIGN KEY ([user]) REFERENCES [user](id),
	CONSTRAINT fk_review_head_product FOREIGN KEY (product) REFERENCES product(id),
	CONSTRAINT fk_review_head_country FOREIGN KEY (purchase_country) REFERENCES country(country_code),
	CONSTRAINT chk_value_for_price_between_1_and_5 CHECK(
		value_for_price >= 1 AND value_for_price <= 5),
	CONSTRAINT chk_review_head_date_is_recent CHECK(
	[date] <= SYSDATETIME() AND
	[date] >= DATEADD(MINUTE, -1, SYSDATETIME())
	)
);

CREATE TABLE review_body (
	[user] int,
	product int,
	aspect int,
	score tinyint NOT NULL,
	CONSTRAINT pk_review_body PRIMARY KEY ([user], product, aspect),
	CONSTRAINT fk_review_body_review_head FOREIGN KEY ([user], product) REFERENCES review_head([user], product),
	CONSTRAINT chk_review_body_score_between_1_and_5 CHECK (
		score >= 1 AND score <= 5)
);

CREATE TABLE [log] (
	id int IDENTITY(1,1),
	[date] datetime2 NOT NULL CONSTRAINT df_log_date DEFAULT SYSDATETIME(),
	[user] varchar(128) NOT NULL CONSTRAINT df_log_user DEFAULT SUSER_SNAME(),
	DML_type varchar(6) NOT NULL,
	[table] varchar(100) NOT NULL,
	[description] varchar(max) NOT NULL,
	CONSTRAINT pk_log PRIMARY KEY (id)
);

GO
-- CREATING VIEWS --
CREATE OR ALTER VIEW v_products_recommended_by_at_least_one_user
AS
SELECT DISTINCT product
FROM review_head
WHERE recommended = 1
AND value_for_price > 3

GO

CREATE OR ALTER VIEW v_favourite_product_of_each_user_from_each_category
AS
WITH user_rating_averages AS (
		SELECT [user], category, product, AVG(CAST(rb.score AS decimal)) AS average
		FROM review_body AS rb
		INNER JOIN product p ON rb.product = p.id
		INNER JOIN article a ON p.article = a.id
		GROUP BY [user], category, product
	),
	user_best_ratings AS (
		SELECT [user], category, product, average,
			RANK() OVER (PARTITION BY [user], category ORDER BY average DESC) as ranking
		FROM user_rating_averages
	)
	SELECT *
	FROM user_best_ratings
	WHERE ranking = 1

GO

CREATE OR ALTER VIEW v_how_many_percent_of_users_buy_mostly_domestic_products
AS
WITH users_with_product AS (
		SELECT rh.[user],b.country AS product_country,
				rh.product, u.country AS user_country
		FROM review_head rh
		INNER JOIN product p ON rh.product = p.id
		INNER JOIN article a ON p.article = a.id
		INNER JOIN brand b ON a.brand = b.id
		INNER JOIN [user] u ON rh.[user] = u.id
	),
	users_with_domestic_product AS (
		SELECT [user], COUNT(product) AS domestic_products
		FROM users_with_product
		WHERE user_country = product_country
		GROUP BY [user]
	),
	users_total_products AS (
		SELECT [user], COUNT(product) AS total_products
		FROM users_with_product
		GROUP BY [user]
	),
	users_domestic_percentage AS (
		SELECT utp.[user],
			CASE
				WHEN total_products = 0 THEN 0
				ELSE
					domestic_products / CAST(total_products AS decimal)
			END AS 'domestic_percentage'
		FROM users_total_products utp
		INNER JOIN users_with_domestic_product udp ON udp.[user] = utp.[user]
	),
	users_domestic_above_50 AS (
		SELECT COUNT([user]) as number_of_users
		FROM users_domestic_percentage
		WHERE domestic_percentage > 0.5
	)
	SELECT
		CASE
			WHEN COUNT(DISTINCT [user]) = 0 THEN 0
			ELSE (SELECT number_of_users FROM users_domestic_above_50)
					* 100 / CAST(COUNT(DISTINCT [user]) AS decimal)
		 END AS 'percent'
	FROM users_with_product

GO

CREATE OR ALTER VIEW v_most_popular_products_of_companies
AS
WITH product_rating_averages AS (
		SELECT brand, product, AVG(CAST(rb.score AS decimal)) AS average
		FROM review_body AS rb
		INNER JOIN product p ON rb.product = p.id
		INNER JOIN article a ON p.article = a.id
		GROUP BY brand, product
	),
	product_best_ratings AS (
		SELECT brand, product, average,
			RANK() OVER (PARTITION BY brand ORDER BY average DESC) as ranking
		FROM product_rating_averages
	)
	SELECT *
	FROM product_best_ratings
	WHERE ranking = 1

GO

CREATE OR ALTER VIEW v_which_packaging_is_the_most_popular_for_each_article
AS
WITH packaging_rating_averages AS (
		SELECT a.id, p.packaging, AVG(CAST(rb.score AS decimal)) AS average
		FROM review_body AS rb
		INNER JOIN product p ON rb.product = p.id
		INNER JOIN article a ON p.article = a.id
		GROUP BY a.id, p.packaging
	),
	packaging_best_ratings AS (
		SELECT id, packaging, average,
			RANK() OVER (PARTITION BY id ORDER BY average DESC) as ranking
		FROM packaging_rating_averages
	)
	SELECT id AS article, packaging AS most_popular_packaging
	FROM packaging_best_ratings
	WHERE ranking = 1

GO

CREATE OR ALTER VIEW v_best_rated_products_per_category
AS
WITH join_cte AS (
		SELECT a.category, p.id, AVG(CAST(rb.score AS decimal) + rh.value_for_price) AS average
		FROM review_body AS rb
		INNER JOIN review_head rh ON rb.[user] = rh.[user]
			AND rb.product = rh.product
		INNER JOIN product p ON rb.product = p.id
		INNER JOIN article a ON p.article = a.id
		GROUP BY a.category, p.id
	),
	category_best_ratings AS (
		SELECT category, id, average,
			RANK() OVER (PARTITION BY category ORDER BY average DESC) as ranking
		FROM join_cte
	)
	SELECT category, id AS most_popular_product
	FROM category_best_ratings
	WHERE ranking = 1

GO

CREATE OR ALTER VIEW v_weakness_of_most_popular_products
AS
WITH get_most_popular_products_ratings_cte AS (
		SELECT v.most_popular_product, aspect, AVG(CAST(score AS decimal)) as average
		FROM v_best_rated_products_per_category AS v
		INNER JOIN review_body rb
			ON v.most_popular_product = rb.product
		GROUP BY v.most_popular_product, aspect
	),
	most_popular_weakest_avg AS (
		SELECT most_popular_product, aspect, average,
			RANK() OVER (PARTITION BY aspect ORDER BY average ASC) as ranking
		FROM get_most_popular_products_ratings_cte
		WHERE average < 3
	)
	SELECT most_popular_product AS product, aspect AS weakest_aspect
	FROM most_popular_weakest_avg
	WHERE ranking = 1

GO

CREATE OR ALTER VIEW v_user_with_most_ratings
AS
	WITH ratings_per_user AS (
		SELECT [user], COUNT(*) AS rating_amount
		FROM review_head
		GROUP BY [user]
	),
	ranked_ratings AS (
		SELECT [user], rating_amount,
			RANK() OVER (ORDER BY rating_amount DESC) as ranking
		FROM ratings_per_user
	)
	SELECT rpu.[user], u.username, rpu.rating_amount
	FROM ranked_ratings AS rpu
	INNER JOIN [user] AS u ON rpu.[user] = u.id
	WHERE ranking = 1

GO

CREATE OR ALTER VIEW v_who_rated_multiple_products_negatively_within_one_hour
AS
	SELECT [user], [date], product, recommended
	FROM review_head rh
	WHERE recommended = 0
		AND
		[date] IN (SELECT [date]
					FROM review_head rh2
					WHERE rh2.[user] = rh.[user]
					AND rh2.[date] <> rh.[date]
					AND rh2.recommended = 0
					AND rh2.[date] BETWEEN rh.[date] AND DATEADD(HOUR, 1, rh.[date])
					OR rh2.[date] BETWEEN DATEADD(HOUR, -1, rh.[date]) AND rh.[date]
					)

GO

CREATE OR ALTER VIEW v_who_had_7_day_rating_streak
AS
	SELECT DISTINCT [user], MIN([date]) AS streak_start
	FROM review_head rh
	WHERE EXISTS (SELECT 1
					FROM review_head rh2
					WHERE rh.[user] = rh2.[user]
					AND CAST(rh2.[date] AS date) = CAST(DATEADD(DAY, 1, rh.[date]) AS date))
		AND
		EXISTS (SELECT 1
					FROM review_head rh2
					WHERE rh.[user] = rh2.[user]
					AND CAST(rh2.[date] AS date) = CAST(DATEADD(DAY, 2, rh.[date]) AS date))
		AND
		EXISTS (SELECT 1
					FROM review_head rh2
					WHERE rh.[user] = rh2.[user]
					AND CAST(rh2.[date] AS date) = CAST(DATEADD(DAY, 3, rh.[date]) AS date))
		AND
		EXISTS (SELECT 1
					FROM review_head rh2
					WHERE rh.[user] = rh2.[user]
					AND CAST(rh2.[date] AS date) = CAST(DATEADD(DAY, 4, rh.[date]) AS date))
		AND
		EXISTS (SELECT 1
					FROM review_head rh2
					WHERE rh.[user] = rh2.[user]
					AND CAST(rh2.[date] AS date) = CAST(DATEADD(DAY, 5, rh.[date]) AS date))
		AND
		EXISTS (SELECT 1
					FROM review_head rh2
					WHERE rh.[user] = rh2.[user]
					AND CAST(rh2.[date] AS date) = CAST(DATEADD(DAY, 6, rh.[date]) AS date))
		GROUP BY [user]

GO

CREATE OR ALTER VIEW v_which_brand_has_the_most_articles
AS
	WITH articles_per_brand AS (
		SELECT brand, COUNT(id) as article_amount
		FROM article
		GROUP BY brand
	)
	SELECT brand, article_amount
	FROM articles_per_brand
	WHERE article_amount = (SELECT MAX(article_amount)
							FROM articles_per_brand)

GO

CREATE OR ALTER VIEW v_which_brand_has_the_most_products
AS
	WITH products_per_brand AS (
		SELECT brand, COUNT(p.id) as product_amount
		FROM product AS p
		INNER JOIN article AS a ON p.article = a.id
		GROUP BY brand
	)
	SELECT brand, product_amount
	FROM products_per_brand
	WHERE product_amount = (SELECT MAX(product_amount)
							FROM products_per_brand)

GO

CREATE OR ALTER VIEW v_top_3_best_rated_company
AS
	WITH brand_product_average AS (
		SELECT brand, AVG(CAST(score AS decimal)) as rating_average
		FROM review_body AS rb
		INNER JOIN product AS p ON rb.product = p.id
		INNER JOIN article AS a ON p.article = a.id
		GROUP BY brand
	),
	companies_ranked AS (
		SELECT brand, rating_average, RANK() OVER (ORDER BY rating_average DESC) as ranking
		FROM brand_product_average
	)
	SELECT ranking, b.[name], CONCAT(ROUND(rating_average, 2), ' / 5.0') as rating_average
	FROM companies_ranked
	INNER JOIN brand AS b ON brand = b.id
	WHERE ranking < 4

GO

CREATE OR ALTER VIEW v_user_reviewed_all_products_of_a_brand
AS
	WITH total_products_per_brand AS (
		SELECT a.brand, COUNT(p.id) AS total_products
		FROM product AS p
		INNER JOIN article AS a ON p.article = a.id
		GROUP BY a.brand
	),
	total_reviews_per_user_per_brand AS (
		SELECT rb.[user], a.brand, COUNT(rb.product) AS rating_amount
		FROM review_body AS rb
		INNER JOIN product AS p ON rb.product = p.id
		INNER JOIN article AS a ON p.article = a.id
		GROUP BY rb.[user], a.brand
	)
	SELECT [user], brand
	FROM total_reviews_per_user_per_brand AS main_table
	WHERE rating_amount = (SELECT total_products
							FROM total_products_per_brand
							WHERE brand = main_table.brand)
GO

/*--------------------------
   TRIGGERS AND FUNCTIONS
*/--------------------------

-- ensure category depth is maximum 3 and that
-- there are no circular references after insert
CREATE OR ALTER TRIGGER trg_check_category_depth_and_circular_reference
ON category
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @depth tinyint;
	DECLARE @circular_reference bit = 0;

	WITH recursive_cte AS (
		SELECT id, parent_category, 1 as depth
		FROM category
		WHERE id = (SELECT id FROM inserted)

		UNION ALL

		SELECT c2.id, c2.parent_category, rc.depth + 1
		FROM category AS c2
		INNER JOIN recursive_cte AS rc ON c2.id = rc.parent_category
		WHERE rc.depth < 4
	),
	occurrences_cte AS (
		SELECT id, COUNT(id) as id_count
		FROM recursive_cte
		GROUP BY id
	),
	multiple_occurrences AS (
		SELECT
		CASE
			WHEN MAX(id_count) = 1 THEN 0
			ELSE 1
		END AS result
		FROM occurrences_cte
	)
	SELECT 	@circular_reference =  o.result,
			@depth = (SELECT MAX(depth) FROM recursive_cte)
	FROM multiple_occurrences o

	IF @circular_reference = 1
	BEGIN
		;THROW 51000, 'Circular references are not allowed.', 1;
	END
	ELSE IF @depth > 3 -- if depth surpasses the limit an error is thrown
	BEGIN
		;THROW 51000, 'Category hierarchy can not exceed 3 in depth.', 1;
	END
END;

GO

CREATE OR ALTER TRIGGER trg_category_prevent_update
ON category
INSTEAD OF UPDATE
AS
BEGIN
    SET NOCOUNT ON;

	DECLARE @old_category int = (SELECT id FROM deleted);
	DECLARE @characteristics_assigned bit = 0;
	DECLARE @children_rated bit = 0;

	WITH before_update_category_children AS (
		SELECT id, parent_category
		FROM category
		WHERE id = (SELECT id FROM deleted)

		UNION ALL

		SELECT c2.id, c2.parent_category
		FROM category AS c2
		INNER JOIN before_update_category_children AS bucc ON c2.parent_category = bucc.id
	),
	has_characteristic AS (
		SELECT
		CASE
			WHEN COUNT(*) = 0 THEN 0
			ELSE 1
		END AS result
		FROM category_x_characteristic cxc
		INNER JOIN before_update_category_children buff ON cxc.category = buff.id
	),
	children_was_reviewed AS (
		SELECT
		CASE
			WHEN COUNT(*) = 0 THEN 0
			ELSE 1
		END AS result
		FROM review_body AS rb
		INNER JOIN product AS p ON rb.product = p.id
		INNER JOIN article AS a ON p.article = a.id
		WHERE a.category IN (SELECT id FROM before_update_category_children)
	)
	SELECT
	@characteristics_assigned = hc.result,
	@children_rated = cwr.result
	FROM has_characteristic AS hc
	CROSS JOIN children_was_reviewed AS cwr

	IF @characteristics_assigned = 1
	BEGIN
		;THROW 51000, 'Changing this category is not allowed because characteristics are already assigned to it or it''s subcategories.', 1;
	END
	ELSE IF @children_rated = 1
	BEGIN
		;THROW 51000, 'Changing this category is not allowed because products in this category were already rated.', 1;
	END
	ELSE
	BEGIN
		UPDATE category
		SET
		[name] = (SELECT [name] FROM inserted),
		parent_category = (SELECT parent_category FROM inserted),
		[description] = (SELECT [description] FROM inserted)
		WHERE id = (SELECT id FROM deleted)
	END
END;

GO

CREATE OR ALTER FUNCTION dbo.get_category_hierarchy
(
    @categoryId INT
)
RETURNS TABLE
AS
RETURN
(
    WITH category_hierarchy_child_to_parent AS (
        SELECT id, parent_category
        FROM category
        WHERE id = @categoryId

        UNION ALL

        SELECT c2.id, c2.parent_category
        FROM category AS c2
        INNER JOIN category_hierarchy_child_to_parent AS ch ON c2.id = ch.parent_category
    ),
    category_hierarchy_parent_to_child AS (
        SELECT id, parent_category
        FROM category
        WHERE id = @categoryId

        UNION ALL

        SELECT c2.id, c2.parent_category
        FROM category AS c2
        INNER JOIN category_hierarchy_parent_to_child AS ch ON c2.parent_category = ch.id
    )
	SELECT id, parent_category
	FROM category_hierarchy_child_to_parent
	UNION
	SELECT id, parent_category
	FROM category_hierarchy_parent_to_child
);

GO

CREATE OR ALTER TRIGGER trg_prevent_characteristic_modification_if_used
ON characteristic
AFTER UPDATE
AS
BEGIN
	DECLARE @modified_characteristic INT = (SELECT id FROM inserted);
	IF @modified_characteristic IN (SELECT characteristic FROM category_x_characteristic)
	BEGIN
		;THROW 51000, 'Modifying characteristics that already describe a category is not allowed.', 1;
	END
END;

GO

/* Ensure that inserted characteristics are not
assigned to any parent or child categories already */

CREATE OR ALTER TRIGGER trg_check_characteristic_already_inherited
ON category_x_characteristic
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @already_inherited bit;
	DECLARE @inserted_category int = (SELECT category FROM inserted);
	DECLARE @inserted_characteristic int = (SELECT characteristic FROM inserted);

    WITH hierarchy_characteristic AS (
        SELECT characteristic
        FROM dbo.get_category_hierarchy(@inserted_category) ch INNER JOIN
            category_x_characteristic AS cxc ON ch.id = cxc.category
    )
    SELECT @already_inherited = 1
    FROM hierarchy_characteristic
    WHERE characteristic = @inserted_characteristic

    IF @already_inherited = 1
    BEGIN
        ;THROW 51000, 'Characteristic already assigned to the category hierarchy, check parent or subcategories!', 1;
    END
    ELSE
		INSERT INTO category_x_characteristic (category, characteristic)
		SELECT category, characteristic
		FROM inserted;
END;

GO

/* Only allow the modification of category - characteristic pairs
 that do not have values defined with a product in the product
 characteristic value table */

CREATE OR ALTER TRIGGER trg_check_characteristic_before_deletion
ON category_x_characteristic
AFTER DELETE, UPDATE
AS
BEGIN
	SET NOCOUNT ON;
	DECLARE @deleted_category int = (SELECT category FROM deleted);
	DECLARE @deleted_characteristic int = (SELECT characteristic FROM deleted);
		IF (
			SELECT COUNT(*)
			FROM product_characteristic_value AS pcv
			INNER JOIN product AS p ON p.id = pcv.product
			INNER JOIN article AS a ON a.id = p.article
			WHERE @deleted_category IN (SELECT id FROM dbo.get_category_hierarchy(a.category))
			AND pcv.characteristic = @deleted_characteristic
		) != 0
			BEGIN
				;THROW 51000, 'Cannot modify because a product already has values for this characteristic. Check the product characteristic value table!', 1;
			END
END

GO

CREATE FUNCTION fn_is_leaf_category(@category_id INT)
RETURNS BIT
AS
BEGIN
    DECLARE @is_leaf BIT;

    SELECT @is_leaf = CASE WHEN COUNT(id) = 0 THEN 1 ELSE 0 END
    FROM category
    WHERE parent_category = @category_id;

    RETURN @is_leaf;
END;

GO

-- Only allow articles to be categorised into leaf-level categories
CREATE OR ALTER TRIGGER trg_is_article_category_on_leaf_level
ON article
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
	IF (SELECT dbo.fn_is_leaf_category(category) FROM inserted) = 0
		THROW 51000, 'The article''s category must be a leaf-level category (so it has no subcategories)!', 1;
END;

GO

CREATE OR ALTER TRIGGER trg_prevent_article_category_change_if_product_exists
ON article
INSTEAD OF UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if the category is modified
    IF (SELECT category FROM deleted) != (SELECT category FROM inserted)
    BEGIN

		IF EXISTS (
                SELECT 1
                FROM product AS p
                WHERE p.article = (SELECT id FROM deleted)
            )
        BEGIN
            ;THROW 51000, 'Cannot change article category because this article already has products.', 1;
        END

		-- Check if the new category is leaf-level
		ELSE IF (SELECT dbo.fn_is_leaf_category(category) FROM inserted) = 0
		BEGIN
			;THROW 51000, 'The article''s category must be a leaf-level category (so it has no subcategories)!', 1;
		END
		ELSE
			UPDATE article
			SET
				[name] = (SELECT [name] FROM inserted),
				brand = (SELECT brand FROM inserted),
				category = (SELECT category FROM inserted),
				[description] = (SELECT [description] FROM inserted)
				WHERE id = (SELECT id FROM deleted)

    END
	ELSE
    UPDATE article
    SET
        [name] = (SELECT [name] FROM inserted),
        brand = (SELECT brand FROM inserted),
        category = (SELECT category FROM inserted),
        [description] = (SELECT [description] FROM inserted)
		WHERE id = (SELECT id FROM deleted)
END;

GO

/*
Ensure that the article characteristics are characteristics that
belong to the category or are inherited from parent categories
*/

CREATE OR ALTER TRIGGER trg_check_if_characteristic_is_inherited
ON product_characteristic_value
INSTEAD OF INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @characteristic_present bit = 0;
	DECLARE @inserted_product int = (SELECT product FROM inserted);
	DECLARE @inserted_characteristic int = (SELECT characteristic FROM inserted);
	DECLARE @old_product int = (SELECT product FROM deleted);
	DECLARE @old_characteristic int = (SELECT characteristic FROM deleted);
	DECLARE @old_value varchar(100) = (SELECT [value] FROM deleted);
	DECLARE @new_value varchar(100) = (SELECT [value] FROM inserted);

	-- if operation update on the value field
	-- then we just update without checking anything
	IF (
		@old_value <> @new_value
		AND @inserted_product = @old_product
		AND @inserted_characteristic = @old_characteristic
	)
	BEGIN
		UPDATE product_characteristic_value
		SET
			[value] = (SELECT [value] FROM inserted)
			WHERE
			product = (SELECT product FROM deleted) AND
			characteristic = (SELECT characteristic FROM deleted)
	END
	ELSE
	BEGIN
		WITH get_article_from_product AS (
			SELECT a.id, a.category
			FROM product p
			INNER JOIN article a ON p.article = a.id
			WHERE p.id = @inserted_product
		),
		-- list all of the parent categories of the article
		article_category_hierarchy AS (
			SELECT id, parent_category
			FROM category
			WHERE id = (SELECT category FROM get_article_from_product)

			UNION ALL

			SELECT c2.id, c2.parent_category
			FROM category AS c2
			INNER JOIN article_category_hierarchy AS ch ON c2.id = ch.parent_category
		),
		hierarchy_characteristic AS (
			SELECT characteristic
			FROM article_category_hierarchy AS ach INNER JOIN
				category_x_characteristic AS cxc ON ach.id = cxc.category
		),
		current_characteristic_present AS (
			SELECT
				CASE
					WHEN @inserted_characteristic IN (SELECT characteristic FROM hierarchy_characteristic) THEN 1
					ELSE 0
				END AS already_present
		)
		SELECT @characteristic_present = already_present
		FROM current_characteristic_present

		IF @characteristic_present = 0
		BEGIN
			;THROW 51000, 'The selected characteristic does not belong to the selected product''s category!', 1;
		END
		ELSE
			IF (SELECT COUNT(*) FROM deleted) = 0
			BEGIN
				INSERT INTO product_characteristic_value (product, characteristic, [value])
				SELECT product, characteristic, [value]
				FROM inserted;
			END
			ELSE
			BEGIN
				UPDATE product_characteristic_value
				SET
					product = (SELECT product FROM inserted),
					characteristic = (SELECT characteristic from inserted)
					WHERE
					product = (SELECT product FROM deleted) AND
					characteristic = (SELECT characteristic FROM deleted)
			END
	END
END;

GO

CREATE OR ALTER TRIGGER trg_check_aspect_already_inherited
ON aspect
INSTEAD OF INSERT
AS
BEGIN
    SET NOCOUNT ON;
	DECLARE @already_inherited bit = 0;
	DECLARE @inserted_name varchar(100) = (SELECT [name] from inserted);
	DECLARE @inserted_category int = (SELECT category FROM inserted);

	BEGIN
		WITH hierarchy_aspect AS (
			SELECT [name]
			FROM aspect
			WHERE category IN (SELECT id FROM dbo.get_category_hierarchy(@inserted_category))
		),
		name_is_present AS (
			SELECT
			CASE
				WHEN LOWER(@inserted_name) IN (SELECT LOWER([name]) FROM hierarchy_aspect) THEN 1
				ELSE 0
			END AS already_present
		)
		SELECT @already_inherited = already_present
		FROM name_is_present

		IF @already_inherited = 1
		BEGIN
			;THROW 51000, 'An aspect with the same name is already assigned to a category in the category hierarchy.', 1;
		END
		ELSE
			INSERT INTO aspect ([name],	 question, category)
			SELECT [name], question, category
			FROM inserted;
	END
END;

GO

CREATE OR ALTER TRIGGER trg_check_aspect_update
ON aspect
INSTEAD OF UPDATE
AS
BEGIN
    SET NOCOUNT ON;
	DECLARE @already_inherited bit = 0;
	DECLARE @inserted_name varchar(100) = (SELECT [name] from inserted);
	DECLARE @inserted_category int = (SELECT category FROM inserted);
	DECLARE @inserted_aspect_id int = (SELECT id FROM inserted);
	-- if the aspect has reviews that belong to it, throw immediate error
	IF NOT EXISTS (
		SELECT aspect
		FROM review_body rb
		WHERE rb.aspect = @inserted_aspect_id
	)
	BEGIN
		-- if the name is the same as the name in the deleted table then
		IF NOT(@inserted_name = (SELECT [name] FROM deleted))
		BEGIN
			WITH hierarchy_aspect AS (
				SELECT [name]
				FROM aspect
				WHERE category IN (SELECT id FROM dbo.get_category_hierarchy(@inserted_category))
			),
			name_is_present AS (
				SELECT
				CASE
					WHEN LOWER(@inserted_name) IN (SELECT LOWER([name]) FROM hierarchy_aspect) THEN 1
					ELSE 0
				END AS already_present
			)
			SELECT @already_inherited = already_present
			FROM name_is_present

			IF @already_inherited = 1
			BEGIN
				;THROW 51000, 'Aspect already assigned to a category in the category hierarchy.', 1;
			END
			ELSE
				UPDATE aspect
				SET
				[name] = (SELECT [name] FROM inserted),
				question = (SELECT question FROM inserted),
				category = (SELECT category FROM inserted)
				WHERE id = (SELECT id FROM deleted)
		END
		-- if the name stayed the same no need to check for duplicate names
		-- category can change at this point as there are no references
		-- we can just perform the update
		ELSE
			UPDATE aspect
			SET
			[name] = (SELECT [name] FROM inserted),
			question = (SELECT question FROM inserted),
			category = (SELECT category FROM inserted)
			WHERE id = (SELECT id FROM deleted)
	END
	ELSE
		BEGIN
			;THROW 51000, 'This aspect cannot be modified because it already has reviews that were made from this aspect.', 1;
		END
END;

GO

CREATE OR ALTER TRIGGER trg_prevent_aspect_deletion_if_reviews_exist
ON aspect
INSTEAD OF DELETE
AS
BEGIN
    SET NOCOUNT ON;
	DECLARE @deleted_aspect int = (SELECT id FROM deleted);
	-- if the aspect has reviews that belong to it, throw immediate error
	IF EXISTS (
		SELECT aspect
		FROM review_body rb
		WHERE rb.aspect = @deleted_aspect
	)
	BEGIN
		;THROW 51000, 'This aspect cannot be deleted because it already has reviews that were made from this aspect.', 1;
	END
	-- if the aspect has no reviews deletion is permitted
	ELSE
		DELETE FROM aspect
		WHERE id = @deleted_aspect
END;

GO

CREATE OR ALTER TRIGGER trg_aspect_is_valid_for_reviewed_product
ON review_body
AFTER INSERT, UPDATE
AS
BEGIN
    SET NOCOUNT ON;

	DECLARE @valid_aspect bit = 0;
	DECLARE @inserted_aspect int = (SELECT aspect FROM inserted);
	DECLARE @inserted_product int = (SELECT product FROM inserted);

	-- Firstly get the article and category of the rated product
    WITH article_from_product AS (
		SELECT article, category
		FROM product INNER JOIN article ON product.article = article.id
		WHERE product.id = @inserted_product
	),
	-- Secondly get the category hierarchy of the product
	-- because aspects are also inherited from parent categories
	category_hierarchy_of_product AS (
		SELECT id, parent_category
        FROM category
        WHERE id = (SELECT category FROM article_from_product)

        UNION ALL

        SELECT c2.id, c2.parent_category
        FROM category AS c2
        INNER JOIN category_hierarchy_of_product AS ch ON c2.id = ch.parent_category
    ),
	category_aspect AS (
        SELECT id
        FROM aspect
        WHERE category IN (SELECT id FROM category_hierarchy_of_product)
    ),
    aspect_is_valid AS (
		SELECT
		CASE
			WHEN @inserted_aspect IN (SELECT id FROM category_aspect) THEN 1
			ELSE 0
		END AS result
	)
    SELECT @valid_aspect = result
    FROM aspect_is_valid

	IF @valid_aspect = 0
    BEGIN
        ;THROW 51000, 'Products can only be review by aspects that belong to their categories!', 1;
    END
END;

GO

CREATE OR ALTER TRIGGER trg_prevent_product_modification
ON product
INSTEAD OF UPDATE
AS
BEGIN
	;THROW 51000, 'Modifying already existing products is not permitted. Please delete and re-register the product if needed!', 1;
END

GO

CREATE OR ALTER TRIGGER trg_prevent_packaging_modification_if_used
ON packaging
AFTER UPDATE
AS
BEGIN
	IF EXISTS (
		SELECT 1
		FROM product AS p
		WHERE packaging = (SELECT id FROM inserted)
	)
	BEGIN
	;THROW 51000, 'Cannot change this packaging because it is used for at least one product.', 1;
	END
END;

GO

CREATE OR ALTER TRIGGER trg_log_prevent_dml
ON [log]
FOR INSERT, UPDATE, DELETE
AS
BEGIN
    IF TRIGGER_NESTLEVEL() = 1
    BEGIN
        ;THROW 51000, 'Editing the log table is not permitted.', 1;
    END
END;

GO

-- CREATING USERS --
CREATE LOGIN review_login WITH PASSWORD = 'im!SDdpC3ndpcQsQ6B%S#hRx', DEFAULT_DATABASE=[product_review_db], CHECK_EXPIRATION=OFF, CHECK_POLICY=ON;
GO
CREATE USER review_user FOR LOGIN review_login;
GO
ALTER ROLE db_owner ADD MEMBER review_user;
GO
DENY UPDATE, DELETE ON [log] TO review_user;

GO

-- INSERTING countries --
INSERT INTO role ([name]) VALUES
('admin'),
('user')

GO

INSERT INTO country (country_code, [name]) VALUES
('AFG', 'Afghanistan'),
('ALB', 'Albania'),
('DZA', 'Algeria'),
('AND', 'Andorra'),
('AGO', 'Angola'),
('ARG', 'Argentina'),
('ARM', 'Armenia'),
('AUS', 'Australia'),
('AUT', 'Austria'),
('AZE', 'Azerbaijan'),
('BHR', 'Bahrain'),
('BGD', 'Bangladesh'),
('BLR', 'Belarus'),
('BEL', 'Belgium'),
('BEN', 'Benin'),
('BTN', 'Bhutan'),
('BOL', 'Bolivia'),
('BIH', 'Bosnia and Herzegovina'),
('BRA', 'Brazil'),
('BRN', 'Brunei'),
('BGR', 'Bulgaria'),
('BFA', 'Burkina Faso'),
('BDI', 'Burundi'),
('CPV', 'Cabo Verde'),
('KHM', 'Cambodia'),
('CMR', 'Cameroon'),
('CAN', 'Canada'),
('CAF', 'Central African Republic'),
('TCD', 'Chad'),
('CHL', 'Chile'),
('CHN', 'China'),
('COL', 'Colombia'),
('COM', 'Comoros'),
('COG', 'Congo'),
('CRI', 'Costa Rica'),
('HRV', 'Croatia'),
('CUB', 'Cuba'),
('CYP', 'Cyprus'),
('CZE', 'Czech Republic'),
('DNK', 'Denmark'),
('DJI', 'Djibouti'),
('DOM', 'Dominican Republic'),
('ECU', 'Ecuador'),
('EGY', 'Egypt'),
('SLV', 'El Salvador'),
('GNQ', 'Equatorial Guinea'),
('ERI', 'Eritrea'),
('EST', 'Estonia'),
('SWZ', 'Eswatini'),
('ETH', 'Ethiopia'),
('FJI', 'Fiji'),
('FIN', 'Finland'),
('FRA', 'France'),
('GAB', 'Gabon'),
('GMB', 'Gambia'),
('GEO', 'Georgia'),
('DEU', 'Germany'),
('GHA', 'Ghana'),
('GRC', 'Greece'),
('GTM', 'Guatemala'),
('GIN', 'Guinea'),
('GNB', 'Guinea-Bissau'),
('GUY', 'Guyana'),
('HTI', 'Haiti'),
('HND', 'Honduras'),
('HUN', 'Hungary'),
('ISL', 'Iceland'),
('IND', 'India'),
('IDN', 'Indonesia'),
('IRN', 'Iran'),
('IRQ', 'Iraq'),
('IRL', 'Ireland'),
('ISR', 'Israel'),
('ITA', 'Italy'),
('JAM', 'Jamaica'),
('JPN', 'Japan'),
('JOR', 'Jordan'),
('KAZ', 'Kazakhstan'),
('KEN', 'Kenya'),
('KWT', 'Kuwait'),
('KGZ', 'Kyrgyzstan'),
('LAO', 'Laos'),
('LVA', 'Latvia'),
('LBN', 'Lebanon'),
('LSO', 'Lesotho'),
('LBR', 'Liberia'),
('LBY', 'Libya'),
('LTU', 'Lithuania'),
('LUX', 'Luxembourg'),
('MDG', 'Madagascar'),
('MWI', 'Malawi'),
('MYS', 'Malaysia'),
('MDV', 'Maldives'),
('MLI', 'Mali'),
('MLT', 'Malta'),
('MRT', 'Mauritania'),
('MUS', 'Mauritius'),
('MEX', 'Mexico'),
('MDA', 'Moldova'),
('MNG', 'Mongolia'),
('MNE', 'Montenegro'),
('MAR', 'Morocco'),
('MOZ', 'Mozambique'),
('MMR', 'Myanmar'),
('NAM', 'Namibia'),
('NPL', 'Nepal'),
('NLD', 'Netherlands'),
('NIC', 'Nicaragua'),
('NER', 'Niger'),
('NGA', 'Nigeria'),
('PRK', 'North Korea'),
('MKD', 'North Macedonia'),
('NOR', 'Norway'),
('OMN', 'Oman'),
('PAK', 'Pakistan'),
('PAN', 'Panama');