# WSSimulator
 
 [![Circle CI](https://circleci.com/gh/CognitiveJ/wssimulator.png?style=badge)](https://circleci.com/gh/CognitiveJ/wssimulator)
 
 WSSimulator quickly allows for validation of web service calls and to simulate service call responses. Ideal for when don’t want, or can’t hit real web services.
 
 
 **When would you use WSSimulator?**

 
 *	For Integration level tests when real web service calls cannot be made (for example, when the producing service doesn’t exist, costs money to call the service or isn’t accessible from the system running).
 *	When you quickly want to serve up static content over HTTP  for 3rd party applications to consume.
 

**Getting Started**

*   Java 8
*   Add the dependency from JCenter


To simulate web service call, you first need to describe the simulation that will be served by WSSimulator. Simulations are created in a YAML format and the only mandatory field within a simulation is the path of the call.

###### Simple Simulation
```yaml
path: /hello
```

And

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


Distribution
There are 2 ways to use WSSimulator as a standalone application or to embed it into an application 

In Standalone Mode
WSSimulator is packaged here and supports been executed on both *nix & windows systems;


*	For *nix systems
    -	Unizip the ws-simulator-x.x.zip file to a local directory
    -	Call: ./wssimulator <options>
*	For Windows
    -	Unizip the ws-simulator-x.x.zip file to a local directory
    -	Call: ./wssimulator.bat <options>


**Standalone Options**

-y Reference to a single yaml file or to a directory (which will load all *.yml files within the target directory)

**In Embedded Mode**
In addition to being able to be launched from the command line, the WSSimulator can be embedded into other JVM applications.  

Class: WSSimulator
When using WSSimulator as an embedded library, here is a gateway class called WSSimulator which holds a number of static helper methods that adds simulations for you. 

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


######  Example with JSON Validation
```java
public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/wsdl.yml").toURI()));
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/endpoint.yml").toURI()));
    }
```

 
###### SOAP Example
```java
public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/wsdl.yml").toURI()));
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/endpoint.yml").toURI()));
    }
```


**Defaults:**

* Http Port: 4567
* Success Status: 201
* Bad Request: 400


