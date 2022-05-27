# quarkus-vs-springboot
A demo project that demonstrates the differences and similarities between the frameworks

Features:
 - Controller
   - REST interface to read all orders
   - REST interface to post a new order
   - New orders are send using Kafka to the order service
   - Existing orders are gotten trough HTTP from the order service
 - Service
   - Postgres database
   - Kafka consumer for new orders
   - REST interface for all orders (use paging)
 