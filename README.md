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
 
# demo #
There is an `action/sh` script in `/scripts`. Use this to perform the following steps:

First for *spring-boot*:
- `./action.sh build-image spring-boot order-controller`
- `./action.sh start-pods spring-boot order-controller` <br/>
  This will barely be able to start the pods in time and by doing so use a lot of resources.
- `./action.sh replicas spring-boot order-controller 30` <br/>
  This will take forever with a lot of `CrashLoopBackOff`. Better do this in small increments
  of 4 replicas a time.
- `./action.sh drain` <br/>
  This will drain one of the nodes and move all pods to another node at once. See the load 
  increase and wait forever for this to have all pods running again.
- `./action.sh uncordon`
- `./action.sh replicas spring-boot order-controller 1`

Second for *quarkus*:
- `./action.sh build-image quarkus order-controller`
- `./action.sh start-pods quarkus order-controller` <br/>
  This will quickly start the pods and use little resources.
- `./action.sh replicas quarkus order-controller 30` <br/>
  There might be some restarts but it reaches the 30 pods quite quickly.
- `./action.sh drain` <br/>
  This will drain one of the nodes and move all pods to another node at once. See how 
  quickly this finishes and moves all the drained pods to the other node.
- `./action.sh uncordon`
- `./action.sh replicas quarkus order-controller 1`

