# WSSimulator
 
 WSSimulator quickly allows for validation of web service calls and to simulate service call responses. Ideal for when don’t want, or can’t hit real web services.
 
 
 **When would you use WSSimulator?**

 
 *	For Integration level tests when real web service calls cannot be made (for example, when the producing service doesn’t exist, costs money to call the service or isn’t accessible from the system running).
 *	When you quickly want to serve up static content over HTTP  for 3rd party applications to consume.
 

**Getting Started**

*   Java 8
*   Add the dependency from JCenter


To simulate web service calls, you first need to describe the simulation that will be served up by WSSimulator. Simulations are created in a YAML format. 
Note, the only required field within a simulation is the path and WSSimulator will fall back to the defaults.

###### Simple Simulation that shows you only need to supply a path
```yaml
path: /hello
```

###### A More complete Simulation
```yaml
path: /publish
consumes: application/xml
httpMethod: post
successResponseCode:201
badRequestResponseCode: 500
response: <results>ok</results>
requestStructure: <?xml version="1.0" encoding="UTF-8"?>
                   <xs:schema attributeFormDefault="unqualified" elementFormDefault="qualified"
                              xmlns:xs="http://www.w3.org/2001/XMLSchema">
                       <xs:element name="customer" type="customerType"/>
                       <xs:complexType name="customerType">
                           <xs:sequence>
                               <xs:element type="xs:string" name="name"/>
                               <xs:element type="xs:string" name="address"/>
                           </xs:sequence>
                       </xs:complexType>
                   </xs:schema>
```


# Distribution
There are 2 ways to use WSSimulator as a standalone application or to embed it into an application 

## In Standalone Mode
WSSimulator is packaged here and supports been executed on both *nix & windows systems;

*	For *nix systems
    -	Unizip the ws-simulator-x.x.zip file to a local directory
    -	Then Call: 
    
    ./wssimulator <options> <yaml string>
    
*	For Windows
    -	Unizip the ws-simulator-x.x.zip file to a local directory
    -	Then Call: 
    
    ./wssimulator.bat <options> <yaml string>

### Standalone Example

./wssimulator path:/helloworld

./wssimulator -d /local/directory

### Options

* -y Reference to a single yaml simulation file or a directory which will load all *.yml files within the target directory
* -s Print out a sample YAML Simulation file
* -p Set the HTTP Port to start the server on (1 to 65535)

## Embedded Mode
In addition to standalone mode, WSSimulator can be be used within you java application and there is a gateway class called _'WSSimulator'_ which holds a number of static helper methods that manages the launch of simulations. 

### Dependency Management

###### Gradle
```groovy
repositories {
        jcenter()
    }
    
    dependencies {
    compile "cognitivej:wssimulator:0.2.4"
    ...
    }
    
```
###### Maven
```xml
    <dependency>
      <groupId>cognitivej</groupId>
      <artifactId>wssimulator</artifactId>
      <version>0.2.4</version>
      <type>pom</type>
    </dependency>
```

### Examples when using wssimulator within your application


######  Example with JSON Validation
```java
public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/wsdl.yml").toURI()));
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/endpoint.yml").toURI()));
    }
```

 
###### SOAP Example (Launches valid WSDL & Endpoint Simulations) 
```java
public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/wsdl.yml").toURI()));
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/endpoint.yml").toURI()));
    }
```

Further Example can be found on [here](https://github.com/CognitiveJ/wssimulator/tree/master/src/test/groovy/wssimulator "Tests").

### Validation

WSSimulator will validate request call when the ‘requestStructure’ and 'consumes' fields are populated and currently supports XSD & JSON Schema validations. 
See [Tests](https://github.com/CognitiveJ/wssimulator/tree/master/src/test/groovy/wssimulator "Tests") for validation examples.


**Assumed Defaults if not passed in the simulation**

```yaml
Http Port: 4567
Success Status: 201
Bad Request: 400
httpMethod: get
consumes: text/plain
```

