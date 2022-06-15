curl -v http://192.168.49.2/quarkus/order-controller/orders \
  --header 'Content-Type: application/json' \
  --data '{"productName":"Product name 1", "amount":5}'

