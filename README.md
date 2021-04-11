# Swiftbook project

### Requirements 

- Service discovery and registration: made using the Kubernetes Deployment & Service objects.
- Externalized configuration: made using the Kubernetes ConfigMap and Secret objects.
- Security Between Services (Admin /User): implemented using the SmallRye JWT Quarkus Extension and Keycloak.

## Current services
1. Place service
2. User service
3. Customer service
4. Booking service 
5. IDMS service
6. DB service(s)


### Technology and Toolchain 

- https://code.quarkus.io/
- IntelliJ IDEA
- Minikube
- postgres
- docker
- postman for test

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

1. Export the confguration 
We need to export the realm configuration form alreay running instance

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

```
docker cp docker-keycloak:/opt/jboss/keycloak/bin/backup ~/keycloak-realms
```


2. Create namespace
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

to see the database tables, you can connect to them using intrejidea connection to shwow all schemes, right clikc porpeorites -> scehems tab and check the scheme 




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
1. Authentication
2. Validation
3. Store application passwords securely 

### Nice to have
1. Digarams
   -  All services page 318
2. metrics
3. test
4. UI
5. command line banner banner.txt
6. Health check
