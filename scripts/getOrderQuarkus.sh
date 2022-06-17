# shellcheck disable=SC2046
.  $(dirname "$0")/determineIngressIp.sh

curl http://"$IP"/quarkus/order-controller/orders \
  --header 'Content-Type: application/json' | jq .

