#!/usr/bin/env bash

SCRIPT_DIR="$( cd -- "$( dirname -- "${BASH_SOURCE[0]:-$0}"; )" &> /dev/null && pwd 2> /dev/null; )";
cd $SCRIPT_DIR/.. || exit

ACTION=$1
case $ACTION in
  start-minikube)
    minikube start --nodes 3 --cpus 2 --memory 2000
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
    cd $DIRNAME/.. || exit
    kubectl apply -f ingress.yml
    kubectl get ingress
    echo "Connect your browser to the shown ip address and port number (http://192.168.49.2:80/<version>/<service>)"
    exit
    ;;
  install-kafka)
    #https://github.com/d1egoaz/minikube-kafka-cluster
    git clone https://github.com/d1egoaz/minikube-kafka-cluster
    cd minikube-kafka-cluster || exit
    kubectl apply -f 00-namespace/
    kubectl apply -f 01-zookeeper/
    kubectl apply -f 02-kafka/
    kubectl apply -f 03-yahoo-kafka-manager/
    kubectl apply -f 04-kafka-monitor/
    cd ..
    rm -rf minikube-kafka-cluster
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
      kubectl delete deployment $VERSION-$SERVICE
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
echo "  - start-minikube    Start a multi-node kubernetes cluster"
echo "  - stop-minikube     Stop the minikube cluster"
echo "  - delete-minikube   Delete the minikube cluster"
echo "  - start-ingress     Start kubernetes Ingress"
echo "  - drain             Drain a node"
echo "  - uncordon          Restore a nod to schedule tasks"
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

