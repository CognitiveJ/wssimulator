# WSSimulator
 
 [![Circle CI](https://circleci.com/gh/CognitiveJ/wssimulator.png?style=badge)](https://circleci.com/gh/CognitiveJ/wssimulator)
 
WSSimulator is a pure Java library that easily allows for you to validate and simulate web service calls and responses. Ideal for when don’t want, or can’t hit real web services.

 
 
 **When would you use WSSimulator?**

 
 *	For Integration level tests when real web service calls cannot be made (for example, when the producing service does not yet exist, costs prohibit calling services within tests or the service isn’t accessible).
 *	When you quickly want to serve up static repeatable content over HTTP.
 *  You need to validate the structure of your outbound requests against a schema that an external service will be expecting.
 *  Check your outbound service ability to handle failure (resilience) 
 
 

**Getting Started**

*   Java 8
*   The dependency from JCenter or the Standalone [distro](https://github.com/CognitiveJ/wssimulator/releases/download/0.2.4/wssimulator-0.2.4.zip "Download Standalone Version")


To simulate web service calls, you first need to describe the simulation. This process is very easy as Simulations are created in a YAML format and you don't need to 'simulate' much to get start as the only required field for you to define is _path_ and WSSimulator will default the other options.

###### Simple Simulation which shows that you only need to supply a path in order to launch WSSimulator
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
There are 2 ways to use WSSimulator; as a standalone application or to embed it into an application 

## In Standalone Mode
WSSimulator is packaged here and supports been executed on both *nix & windows systems;

*	For *nix systems
    -	Unizip the ws-simulator-0.2.4.zip file to a local directory
    -	Then Call: 
    
    ./wssimulator <options>
    
*	For Windows
    -	Unizip the ws-simulator-0.2.4.zip file to a local directory
    -	Then Call: 
    
    ./wssimulator.bat <options>

### Standalone Example
```shell
./wssimulator -y /local/directory
./wssimulator -y /local/directory/simulation1.yml
./wssimulator -p80 -y /local/directory/simulation1.yml
./wssimulator "path:/helloworld"
```
### Options

* -y Reference to a single yaml simulation file or a directory which will load all *.yml files within the target directory
* -s Print out a sample YAML Simulation file
* -p Set the HTTP Port to start the server on (1 to 65535)


## Embedded Mode
In addition to standalone mode, WSSimulator can be be used within your java application as a Java dependency. The gateway class is called _WSSimulator_ (same name as the library) and its holds a number of static helper methods that manages the launch of simulations for you. 

### Dependency Management

WSSimulator is hosted on jcenter() 

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

### Examples when using WSSimulator within your application

######  Example with JSON/XML Validation
```java
public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(ValidationExample.class.getResource("/json/json1.yml").toURI()));
        WSSimulator.addSimulation(new File(ValidationExample.class.getResource("/xml/xmlValidationExample.yml").toURI()));
    }
```
 
###### SOAP Example (Launches valid WSDL & Endpoint Simulations) 
```java
public static void main(String[] args) throws URISyntaxException {
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/wsdl.yml").toURI()));
        WSSimulator.addSimulation(new File(SOAPExample.class.getResource("/soap/endpoint.yml").toURI()));
    }
```

Further Examples can be found on [here](https://github.com/CognitiveJ/wssimulator/tree/master/src/test/groovy/wssimulator "Tests").

### Validation

WSSimulator will validate request call when not the _requestStructure_ and _consumes_ fields are populated and currently supports XSD & JSON Schema validations. 
See [Tests](https://github.com/CognitiveJ/wssimulator/tree/master/src/test/groovy/wssimulator "Tests") for validation examples.


### Resilience

WSSimulator can simulate random failures when the _resilience_ field is set below 1 with a 0 value meaning that the simulation will always failing. _resilienceFailureCode_ allows you to set the failure response code to send back when failure does occur
See [Tests](https://github.com/CognitiveJ/wssimulator/tree/master/src/test/groovy/wssimulator "Tests") for more examples.


**Assumed Defaults if not passed**

```yaml
Http Port: 4567
Success Status: 201
Bad Request: 400
httpMethod: get
consumes: text/plain
resilience: 1
resilienceFailureCode: 501
```

