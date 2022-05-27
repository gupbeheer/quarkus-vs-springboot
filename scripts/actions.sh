#!/usr/bin/env bash

DIRNAME=`pwd`
cd $DIRNAME/.. || exit

VERSION=$1
cd $VERSION || exit

ACTION=$2
case $ACTION in
  start-minikube)
    minikube start --nodes 3
    minikube addons enable ingress
    ;;
  stop-minikube)
    minikube stop
    ;;
  delete-minikube)
    minikube delete
    ;;
  compile-controller)
    cd order-controller || exit
    gradle build
    ;;
  build-controller-image)
    cd order-controller || exit
    gradle build
    minikube image build --all --tag spring-boot/order-controller:latest .
    ;;
  start-controller-pods)
    cd order-controller || exit
    kubectl apply -f kubernetes.yml
    ;;
  start-ingress)
    cd $DIRNAME/.. || exit
    kubectl apply -f ingress.yml
    kubectl get ingress
    echo "Connect your browser to the shown ip address and port number"
    ;;
  *)
    echo "Usage:"
    echo "  $0 <version> <action>"
    echo
    echo "  version:"
    echo "  - spring-boot"
    echo "  - quarkus"
    echo
    echo "  action:"
    echo "  - start-minikube            Start a multi-node kubernetes cluster"
    echo "  - stop-minikube             Stop the minikube cluster"
    echo "  - delete-minikube           Delete the minikube cluster"
    echo "  - compile-controller        Compile the order-controller"
    echo "  - build-controller-image    Build the order-controller docker image"
    echo "  - start-controller-pods     Start the order-controller in kubernetes"
    echo "  - start-ingress             Start kubernetes Ingress"
esac
