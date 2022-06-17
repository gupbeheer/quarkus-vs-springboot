#!/bin/bash

if uname -a | grep "Darwin" >/dev/null
then
  IP="127.0.0.1"
else
  IP="192.168.49.2"
fi