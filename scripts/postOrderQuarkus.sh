.  $(dirname "$0")/determineIngressIp.sh

curl -v http://"$IP"/quarkus/order-controller/orders \
  --header 'Content-Type: application/json' \
  --data "{\"productName\":\"Product $(date +%Y%m%dT%T)\", \"amount\":5}"

