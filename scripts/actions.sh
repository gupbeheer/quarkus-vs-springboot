#!/usr/bin/env bash

SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]:-$0}"; )" &> /dev/null && pwd 2> /dev/null; )";
cd "$SCRIPT_DIR"/.. || exit

.  "$SCRIPT_DIR"/determineIngressIp.sh

ACTION=$1
case $ACTION in
  create-mount)
    mkdir -p "$(pwd)/data/postgres"
    mkdir -p "$(pwd)/data/kafka"
    exit
    ;;
  start-minikube)
    minikube start --nodes 3 --cpus 2 --memory 2000 --mount --mount-string="$(pwd)/data:/mnt/data"
    minikube addons enable ingress
    exit
    ;;
  stop-minikube)
    minikube stop
    exit
    ;;
  dashboard)
    minikube dashboard
    exit
    ;;
  delete-minikube)
    minikube delete
    exit
    ;;
  start-ingress)
    kubectl apply -f ingress.yml
    kubectl get ingress
    echo "Connect your browser to the shown ip address and port number (http://$IP:80/<version>/<service>)"
    if [ "$IP" == "127.0.0.1" ] ; then
      minikube tunnel
    fi
    exit
    ;;
  drain)
    kubectl drain --ignore-daemonsets --delete-emptydir-data minikube-m02
    exit
    ;;
  uncordon)
    kubectl uncordon minikube-m02
    exit
    ;;
  build-kafka)
    cd kafka || exit
    minikube image build --all --tag kafka/kafka:latest .
    exit
    ;;
  start-kafka)
    cd kafka || exit
    kubectl apply -f kubernetes.yml
    exit
    ;;
  stop-kafka)
    cd kafka || exit
    kubectl delete -f kubernetes.yml
    exit
    ;;
  kafka-port-forward)
    kubectl port-forward kafka-0 9092:9092
    exit
    ;;
  start-postgres)
    cd postgres || exit
    kubectl apply -f kubernetes.yml
    exit
    ;;
  stop-postgres)
    cd postgres || exit
    kubectl delete -f kubernetes.yml
    exit
    ;;
  postgres-port-forward)
    kubectl port-forward postgres-0 5432:5432
    exit
    ;;
  refresh-services)
    for version in quarkus spring-boot; do
      for service in order-controller order-service; do
        "$SCRIPT_DIR"/actions.sh build-image $version $service
        "$SCRIPT_DIR"/actions.sh start-pods $version $service
      done
    done
    exit
    ;;
esac

VERSION=$2
SERVICE=$3
if [ "$VERSION" != "" ]; then
  cd $VERSION/$SERVICE || exit

  case $ACTION in
    compile)
      gradle build
      exit
      ;;
    build-image)
      gradle build
      minikube image build --all --tag $VERSION/$SERVICE:latest .
      exit
      ;;
    dev)
      if [ "$VERSION" == "spring-boot" ]; then gradle bootRun
      else gradle quarkusDev
      fi
      exit
      ;;
    start-pods)
      kubectl delete -f kubernetes.yml
      kubectl apply -f kubernetes.yml
      exit
      ;;
    replicas)
      kubectl scale --replicas=$4 deployment/$VERSION-$SERVICE
      exit
      ;;
  esac
fi

echo "Usage:"
echo "  $0 <action> <version> <service> [replicas]"
echo
echo "  action:"
echo "  - create-mount            Create the data folders"
echo "  - start-minikube          Start a multi-node kubernetes cluster"
echo "  - stop-minikube           Stop the minikube cluster"
echo "  - dashboard               Start the kubernetes dashboard"
echo "  - delete-minikube         Delete the minikube cluster"
echo "  - start-ingress           Start kubernetes Ingress"
echo "  - drain                   Drain a node"
echo "  - uncordon                Restore a nod to schedule tasks"
echo "  - build-kafka             Create docker images for kafka"
echo "  - start-kafka             Start the kafka services"
echo "  - stop-kafka              Stop the kafka services"
echo "  - kafka-port-forward      Forward the pod port to the host system"
echo "  - start-postgres          Start postgres (postgresdb, admin, test123)"
echo "  - stop-postgres           Stop postgres"
echo "  - postgres-port-forward   Forward the pod port to the host system"
echo "  - refresh-services        Compile and restart all services"
echo
echo "  The rest of the actions need a 'version' and 'service' as well:"
echo "  - compile           Compile the service"
echo "  - dev               Run the service locally in dev mode"
echo "  - build-image       Build the service docker image inside the minikube cluster"
echo "  - start-pods        Start the service in kubernetes"
echo "  - replicas          Set the number of replicas"
echo
echo "  version:"
echo "  - spring-boot"
echo "  - quarkus"
echo
echo "  service:"
echo "  - order-controller"
echo "  - order-service"

