# About the project
Product Review App Complete <b> was inspired by </b> my habit of reviewing products for myself to better understand my habits. In fact, I wrote my <b> BSc diploma work </b> about designing a product rating database server. This is the underlying database structure that I am using for this application. I also wanted to build a complex project to learn <b> React </b> and to showcase my skills in <i> Spring, relational database management systems and Java </i>.
## Underlying database structure functionality
The system in question allows for the evaluation of products based on various criteria, such as taste, packaging, color, etc. Products can be food items, consumer goods, electronics, and so on.

The system maintains records of conceptual products, i.e., items that can be purchased as specific products in various packaging options at stores. Each item has a brand and can be categorized, and the categories can be hierarchically organized (main category and subcategories) up to a depth of 3. For example, a main category could be beverages, with its subcategories being tea or coffee. Each category has characteristics (e.g., color, taste), and products belonging to that category can have these characteristics. Additionally, every category can be evaluated based on certain criteria (e.g., taste for the beverage category). Products belonging to a given category can be rated on a scale from 1 to 5 as part of the product reviews, based on the criteria specific to the category, as well as inherited criteria from higher-level categories. Product images can also be stored for each product. Each evaluation comes from an individual and pertains to a specific product. Every data modification is logged in the system.
# Configuration
## Microsoft SQL Server
First you will have to create the product review database in SQL Management Studio (SSMS), as well as a login to the database server instance running on your local machine, and then a user assigned to the created database which uses the created login.
> [!IMPORTANT]
> You will need to have Microsoft SQL Server 16 or higher installed on your machine, as well as SQl Server Management Studio (SSMS). These are <b> only available on Windows. </b>
```
CREATE DATABASE product_review_db COLLATE LATIN1_GENERAL_100_CI_AS_SC_UTF8
GO
CREATE LOGIN review_login WITH PASSWORD = 'im!SDdpC3ndpcQsQ6B%S#hRx', DEFAULT_DATABASE=[product_review_db], CHECK_EXPIRATION=ON, CHECK_POLICY=ON;
GO
CREATE USER review_user FOR LOGIN review_login;
GO
ALTER ROLE db_owner ADD MEMBER review_user;
```
> [!TIP]
> If you want to use your own password and username, you can modify this script, and then you <b> have to modify the application.properties file too </b> to add the new connection String to the database server!
## application.properties
You will have to change the <serverName> in the <i> application.properties </i> to match your SQL Server's name. If you open SSMS to join to the server you will see what the server's name is set to, usually it is the name of the PC the server is running on.
> [!IMPORTANT]
> You must remove the brackets (<>) from around the server name!
 
