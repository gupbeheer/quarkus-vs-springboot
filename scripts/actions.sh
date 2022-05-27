#!/usr/bin/env bash

DIRNAME=`pwd`
cd $DIRNAME/.. || exit

VERSION=$1
cd $VERSION || exit

ACTION=$2
case $ACTION in
  compile-controller)
    cd order-controller || exit
    gradle build
    ;;
  build-controller-image)
    cd order-controller || exit
    docker build . --tag $VERSION/order-controller:latest
    ;;
  run-controller-image)
    docker run $VERSION/order-controller:latest
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
    echo "  - compile-controller        Compile the controller-service"
    echo "  - build-controller-image    Build the order-service docker image"
    echo "  - run-controller-image      Start the controller-service in docker"
esac
