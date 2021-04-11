# Swiftbook project

### Meeting Requirements 

- Service discovery and registration: made using the Kubernetes Deployment & Service objects.
- Externalized configuration: made using the Kubernetes ConfigMap and Secret objects.
- Security Between Services (Admin /User): implemented using the SmallRye JWT Quarkus Extension and Keycloak.
- Circuit breaker

Implementing the Circuit Breaker

add this dependency to the pom.xml

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-fault-tolerance</artifactId>
</dependency>
```

```java
  @CircuitBreaker(requestVolumeThreshold = 10, delay = 15000)
  @Retry(maxRetries = 4)
  @Timeout(500)
```

The first line reads as stop making the API Calls for 15 seconds, if we have a 50% of
failing request from the last 10 requests.

## Current services
1. Place service
2. User service: A lightweight application  service, unlike other service user service it doesn't depend on a database, which makes the artifact way lighter.
   
3. Customer service
4. Booking service 
5. IDMS service
6. DB service(s)
7. Centralized Logging service (ELK Stack)


### Technologies and Toolchains 

- Quarkus: a full-stack, Kubernetes-native, Java Application Framework.
- https://code.quarkus.io/
- IntelliJ IDEA & VS code
- Minikube
- Helm
- Postgres
- Docker and Dockerhub
- Postman
- Open API & Swagger
- ELK stack

File -> New Project form existing source

Select Maven and click finish 
### Setup the K8s

#### Install Minikube
Start minikube
```sh
    minikube start --driver=docker
```
#### Install kubectl
```sh
    brew install kubectl 
    kubectl config use-context minikube
```
#### Install helm
```sh
    brew install helm
```

### Create IDMS service based on [keycloak](https://www.keycloak.org/)

1. Spin up a development instance either native or using docker (recommended)

```sh
docker run -d --name docker-keycloak \
-e KEYCLOAK_USER=admin \ 
-e KEYCLOAK_PASSWORD=admin \
-e DB_VENDOR=h2 \
-p 9080:8080 \ 
-p 8443:8443 \ 
-p 9990:9990 \
jboss/keycloak:11.0.0
```
2. Export the realm configuration 
We need to export the realm configuration form a development instance

```sh
docker exec -it docker-keycloak bash
cd /opt/jboss/keycloak/bin/
mkdir backup
./standalone.sh -Djboss.socket.binding.port-offset=1000 \
-Dkeycloak.migration.realmName=quarkushop-realm \
-Dkeycloak.migration.action=export \
-Dkeycloak.migration.provider=dir \
-Dkeycloak.migration.dir=./backup/
```

in the host machine 
```sh
mkdir ~/keycloak-realms
```

```sh
docker cp docker-keycloak:/opt/jboss/keycloak/bin/backup ~/keycloak-realms
```


1. Create namespace
```sh
kubectl create namespace keycloak
```
3. Add the repo 

```sh
helm repo add codecentric https://codecentric.github.io/helm-charts
```
4. Install and keycloak

Note: I reimported already realm.json
 
```sh
kubectl create secret generic realm-secret --from-file=realm.json -n keycloak
helm install keycloak codecentric/keycloak -n keycloak --values values.yaml
```

5. Port forwarding  
```sh
kubectl -n keycloak port-forward service/keycloak-http 9080:80
```

### Create database service 
```
kubctl create -f .
```
```
minikube service postgres --url
```


### Increase the resources of docker engine 
### Install minikube

### Build service pod
Provide the image name and group name in the application.properties

```
quarkus.container-image.name=swiftybook-place
quarkus.container-image.group=mgalalm
```
#### Build the image 
```
mvn clean install -Pnative -Dquarkus.native.container-build=false -Dquarkus.container-image.build=true -DskipTests=true  -Dquarkus.native.container-build=true
```

This will result in an image with mgalalm/swiftybook-place:1.0.0-SNAPSHOT. 


You can verify the image 
```sh
docker images -a
```

In addition to the image, k8s descriptors will be generated in in targets/kubernetes.

#### Push the image 

there is two ways to push the image manually or by with quarkus
##### Manually
1. Login to dockerhub
```sh
    docker login 
```
2. docker push 
```sh
docker push mgalalm/swiftybook-place:1.0.0-SNAPSHOT
```

or by appending -Dquarkus.container-image.push=true to the building image command
#### Deploy as pod 
```sh
kubectl apply -f target/kubernetes/kubernetes.yml
```
kubectl get pods

postgres-68f6497d59-94jds           1/1     Running   0          49m
swiftybook-place-554579fb8d-rrzth   1/1     Running   0          2m36s

```sh
kubectl port-forward swiftybook-place-554579fb8d-rrzth 8080:8080
```

You can test it now 

http://localhost:8080/api/places

### Logging 

we use quarkus-logging-gelf extension 

```sh
 helm repo add elastic https://helm.elastic.co
```

```sh
  helm repo update
```
```sh
helm install elasticsearch elastic/elasticsearch -f swiftybook-logging/elasticsearch-values.yaml
```
```sh
    kubectl get all -l release=elasticsearch
```

The output should be something like that
```
pod/elasticsearch-master-0   1/1     Running   0          2m28s
pod/elasticsearch-master-1   1/1     Running   0          2m27s
pod/elasticsearch-master-2   1/1     Running   0          2m27s

NAME                                    TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)             AGE
service/elasticsearch-master            ClusterIP   10.102.253.77   <none>        9200/TCP,9300/TCP   2m28s
service/elasticsearch-master-headless   ClusterIP   None            <none>        9200/TCP,9300/TCP   2m28s

NAME                                    READY   AGE
statefulset.apps/elasticsearch-master   3/3     2m28s
```

To test elasticsearch locally 

```sh
kubectl port-forward service/elasticsearch-master 9200
```
The output should be 
```sh
{
  "name" : "elasticsearch-master-0",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "VGONUQe_RTSdmN2FFJgjUg",
  "version" : {
    "number" : "7.12.0",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "78722783c38caa25a70982b5b042074cde5d3b3a",
    "build_date" : "2021-03-18T06:17:15.410153305Z",
    "build_snapshot" : false,
    "lucene_version" : "8.8.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}
```

#### Install kibana
```sh
helm install kibana elastic/kibana --set fullnameOverride=quarkushop-kibana
```
```sh
kubectl get all -l release=kibana
```

To put it to tets
```sh
 kubectl port-forward service/quarkushop-kibana 5601
```

You may test the UI in the browser

#### Install logstash
```sh
helm install -f ./logstash-values.yaml logstash elastic/logstash --set fullnameOverride=quarkushop-logstash
```
```sh
 kubectl get all -l chart=logstash
```
It may take a few minutes for the pods to come up.

### Jagger 
```sh
 kubectl apply -f 
```

```sh
    kubectl port-forward service/jaeger-query 8888:80
```

In browser localhost:8888

### Troubleshooting 

To check what went worng with the pod you can use kubectl logs -f [name of pods]

```sh
kubectl logs -f pod/postgres-68f6497d59-94jds
```

Any time you want to stop port forwarding, either CTRL + C OR find the process by typing the following command and 

```sh
 ps aux | grep kubectl 
``` 
```sh
kill -9 the process id 
```

Database

to see the database tables, you can connect to them using intrejidea connection to show all schemes, right clikc porpeorites -> scehems tab and check the scheme 




### Development 


Create .env file in the root directory of each service

QUARKUS_DATASOURCE_USERNAME=developer
QUARKUS_DATASOURCE_PASSWORD=p4SSW0rd
QUARKUS_DATASOURCE_JDBC_URL=jdbc:postgresql://localhost:5432/booking
PLACE_SERVICE_URL=http://127.0.0.1:57810/api

Create a postregs database, typically using docker
```sh
docker run -d --name demo-postgres \
-e POSTGRES_USER=developer \
-e POSTGRES_PASSWORD=p4SSW0rd \
-e POSTGRES_DB=demo \
-p 5432:5432 postgres:13
```
/etc/hosts
127.0.0.1 postgres
#### Next steps
### Must have 
1. Handle the case when the payment done but the booking service is unavailable, maybe in process state to be introduced  
2. Circuit breaker
3. Authentication
4. Validation
5. Store application passwords securely 

### Nice to have
1. Diagrams
   -  UML
   -  All services page 318
   -  ELK Stack 330
2. metrics
3. test
4. UI
5. command line banner banner.txt
6. Health check

### Presentation 

- List of all running services in command line

