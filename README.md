# quarkus-openshift-clusterrefresh project

This is a simple quarkus-based cli app for extending the expiry date of an OSD cluster by 7 days. You need to specify the cluster id and either an OCM access token or a refresh token for auth. The easiest way to use the tool is to be logged in to OCM and use the docker one-liner, below. You also need to identify your cluster id. To quickly get your cluster ids:
```
ocm get clusters | jq '.items[] | .id'
```
The OCM API baseUrl https://api.stage.openshift.com is assumed in the one-liner, below, but change as required. When you have the cluster id, run:
```
CLUSTER=`ocm list clusters | grep devexp | awk '{ print $1}'`
docker run -i --rm quay.io/mmclaugh/osd-cluster-extender extend -u https://api.stage.openshift.com -c ${CLUSTER} -r `jq -r < ~/.ocm.json '.refresh_token'`
```
Requirements for the above:
- ocm: https://github.com/openshift-online/ocm-cli/releases
- jq: https://stedolan.github.io/jq/
- docker: https://www.docker.com/products/docker-desktop

If you want to build your own image, run:
```./mvnw clean package -Pnative -Dquarkus.native.container-build=true
docker build -f src/main/docker/Dockerfile.native -t quarkus/osd-cluster-extender .
```

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-openshift-clusterrefresh-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/quarkus-openshift-clusterrefresh-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-openshift-clusterrefresh-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# RESTEasy JAX-RS

<p>A Hello World RESTEasy resource</p>

Guide: https://quarkus.io/guides/rest-json
