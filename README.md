### Setup the K8s
#### Install kubectl
```sh
brew install kubectl 
kubectl config use-context minikube
```

#### Install helm
```sh
brew install helm
```

### Create IDM service based on [keycloak](https://www.keycloak.org/)

1. Create namespace
```sh
kubectl create namespace keycloak
```
2. Add the repo 

```sh
helm repo add codecentric https://codecentric.github.io/helm-charts
```
3. Install and keycloak

Note: I reimported already realm.json
 
```sh
kubectl create secret generic realm-secret --from-file=realm.json -n keycloak
helm install keycloak codecentric/keycloak -n keycloak --values values.yaml
```

4. Port forwarding  
```sh
kubectl -n keycloak port-forward service/keycloak-http 9080:80
```

### Create database service 
```
kubctl create -f
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

This will result in an image with mgalalm/swiftybook-place:1.0.0-SNAPSHOT


You can verify the image 
```sh
docker images -a
```
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
kubectl logs -f pod/postgres-68f6497d59-94jds
