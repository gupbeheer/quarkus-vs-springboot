.  $(dirname "$0")/determineIngressIp.sh

curl http://"$IP"/spring-boot/order-controller/orders \
  --header 'Content-Type: application/json' | jq .

