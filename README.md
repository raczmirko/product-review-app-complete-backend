# About the project
Product Review App Complete <b> was inspired by </b> my habit of reviewing products for myself to better understand my habits. In fact, I wrote my <b> BSc diploma work </b> about designing a product rating database server. This is the underlying database structure that I am using for this application. I also wanted to build a complex project to learn <b> React </b> and to showcase my skills in <i> Spring, relational database management systems and Java </i>.
## Underlying database structure functionality
The system in question allows for the evaluation of products based on various criteria, such as taste, packaging, color, etc. Products can be food items, consumer goods, electronics, and so on.

The system maintains records of conceptual products, i.e., items that can be purchased as specific products in various packaging options at stores. Each item has a brand and can be categorized, and the categories can be hierarchically organized (main category and subcategories) up to a depth of 3. For example, a main category could be beverages, with its subcategories being tea or coffee. Each category has characteristics (e.g., color, taste), and products belonging to that category can have these characteristics. Additionally, every category can be evaluated based on certain criteria (e.g., taste for the beverage category). Products belonging to a given category can be rated on a scale from 1 to 5 as part of the product reviews, based on the criteria specific to the category, as well as inherited criteria from higher-level categories. Product images can also be stored for each product. Each evaluation comes from an individual and pertains to a specific product. Every data modification is logged in the system.
# Configuration
## Microsoft SQL Server
The full database creation script is located in the <i> src/resources/db/create_database.sql </i> file. You have to manually run this <b> before </b> running the application!
> [!IMPORTANT]
> You will need to have Microsoft SQL Server 16 or higher installed on your machine, as well as SQl Server Management Studio (SSMS). These are <b> only available on Windows. </b>
> If you want to use your own password and username, you can modify this script, and then you <b> have to modify the application.properties file too </b> to add the new connection String to the database server!

## application.properties
You will have to change the <serverName> in the <i> application.properties </i> to match your SQL Server's name. If you open SSMS to join to the server you will see what the server's name is set to, usually it is the name of the PC the server is running on.
> [!IMPORTANT]
> You must remove the brackets (<>) from around the server name!
 
