# Spring boot
cd SpringBootProject
mvn package -DskipTests
wait
java -jar target/Lookup-0.0.1-SNAPSHOT.jar &

# Consumers
cd ../NodeJSRabbitMQProject
node consumer_salesforce.js &
node consumer_servicenow.js &
wait

# Producer
node producer.js &

# Get pid of spring process: lsof -i:8080 -t
