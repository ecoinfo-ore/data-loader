
## Data-Loader

==

 Load Semantic Files to Blazegraph Database Using Rest Api

==

```bash
   mvn clean install assembly:single
```

Example :

```bash

     java -DUrl="http://localhost:9999/blazegraph/"    \
          -DVerbose=true                               \
          -DFileOrDir='/coby/data/'                    \
          -DMediaType='text/rdf+n3'                    \
          -DNamespace=kb                               \
          -DConnectTimeOut=60                          \
          -DWriteTimeOut=60                            \
          -DReadTimeOut=60                             \
          -RemoveOnLoad=true                           \
          -jar data-loader-1.0-jar-with-dependencies.jar 

```
