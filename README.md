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
 
# Original creation #
The Quarkus projects are created using the [Quarkus CLI](https://quarkus.io/guides/cli-tooling)  
The Spring Boot projects are created using the [Spring Initializr](https://start.spring.io/)

Both projects with Gradle and Kotlin options on

# needed tools #
- Java
- Docker
- Kubectl
- Minikube
- JQ

# setup minikube #
There is an `actions.sh` script in `/scripts`. Use this to perform the following steps:
- `./actions.sh create-mount`
  Make mounts for Minikube to store persistent data
- `./actions.sh start-minikube`
  Start Minikube with the correct settings
- `./actions.sh build-kafka`
  Build a Kafka that works without Zookeeper
- `./actions.sh start-kafka`
  Start Kafka in Minikube 
- `./actions.sh start-postgres`
  Start Postgres in Minikube
- `./actions.sh start-ingress`
  Start Ingress in Minikube (if you run this on Apple Silicon then it will create a tunnel for Ingress and not come back with a prompt. (You also probably will be asked to enter the sudo password))
- `./actions.sh refrest-services`
  Build and deploy all services to Minikube

# pod scaling demo #
There is an `actions.sh` script in `/scripts`. Use this to perform the following steps:
- `./actions.sh`

First for *spring-boot*:
- `./actions.sh build-image spring-boot order-controller`
- `./actions.sh start-pods spring-boot order-controller` <br/>
  This will barely be able to start the pods in time and by doing so use a lot of resources.
- `./actions.sh replicas spring-boot order-controller 30` <br/>
  This will take forever with a lot of `CrashLoopBackOff`. Better do this in small increments
  of 4 replicas a time.
- `./actions.sh drain` <br/>
  This will drain one of the nodes and move all pods to another node at once. See the load 
  increase and wait forever for this to have all pods running again.
- `./actions.sh uncordon`
  Make the node available again
- `./actions.sh replicas spring-boot order-controller 1` 
  Scale back to one

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
  Make the node available again
- `./action.sh replicas quarkus order-controller 1`
  Scale back to one


