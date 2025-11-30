#!/bin/bash
cd "$(dirname "$0")" 
echo "Profis-Backend is starting in 3 seconds" 
for i in {1..3};
do
    echo $i
    sleep 1
done
./mvnw spring-boot:run
