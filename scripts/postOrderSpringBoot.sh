.  $(dirname "$0")/determineIngressIp.sh

curl -v http://$IP/spring-boot/order-controller/orders \
  --header 'Content-Type: application/json' \
  --data '{"productName":"Product name 1", "amount":5}'
