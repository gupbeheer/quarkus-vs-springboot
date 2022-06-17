#!/usr/bin/env bash

SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]:-$0}"; )" &> /dev/null && pwd 2> /dev/null; )";
cd $SCRIPT_DIR/.. || exit

ACTION=$1
case $ACTION in
  create-mount)
    mkdir -p data/postgres
    mkdir -p data/kafka
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
  delete-minikube)
    minikube delete
    exit
    ;;
  start-ingress)
    kubectl apply -f ingress.yml
    kubectl get ingress
    echo "Connect your browser to the shown ip address and port number (http://192.168.49.2:80/<version>/<service>)"
    exit
    ;;
  drain)
    kubectl drain --ignore-daemonsets minikube-m02
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
echo "  - create-mount         Create the data folders"
echo "  - start-minikube       Start a multi-node kubernetes cluster"
echo "  - stop-minikube        Stop the minikube cluster"
echo "  - delete-minikube      Delete the minikube cluster"
echo "  - start-ingress        Start kubernetes Ingress"
echo "  - drain                Drain a node"
echo "  - uncordon             Restore a nod to schedule tasks"
echo "  - build-kafka          Create docker images for kafka"
echo "  - start-kafka          Start the kafka services"
echo "  - stop-kafka           Stop the kafka services"
echo "  - kafka-port-forward   Forward the pod port to the host system"
echo "  - start-postgres       Start postgres (postgresdb, admin, test123)"
echo "  - stop-postgres        Stop postgres"
echo
echo "  The rest of the actions need a 'version' and 'service' as well:"
echo "  - compile           Compile the service"
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

