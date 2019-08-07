# Swift Tasjeel
Android Application that allows a user to utilize multiple services involved in car registration renewal in one platform, eliminating the need to visit numerous platforms or physical locations.
Swift Tasjeel provides functionalities for road users to renew their car registration with minimal hassle. With the current need to renew insurance plans, pay outstanding traffic fines, visit an RTA-certified testing center, and pay RTA fines, it is difficult to find time to do these tedious tasks. This application integrates the four main steps of car registration renewal into one platform. A user can log onto the system if they are registered, and they can renew their insurance plan (or register for a new one with any of the registered insurance companies), pay their traffic fines, book an appointment to get their car tested, and pay their RTA renewal fees. These functionalities brought into one platform can save users a lot of time going between different applications and/or physical locations.

The client is an Android application that communicates with a broker server hosted on the cloud using Amazon Web Services (AWS). The client uses MongoDB Stitch to securely access a database hosted on MongoDB Atlas. The server is a simple Java application that accesses exposed services by four dummy entities (A number of insurance companies, a number of testing centers, Dubai Police, RTA). Since this project was for academic purposes, no real APIs belonging to real entities were used. However, the dummy entities served the purpose, as they were also Java applications hosted on the cloud, each of which accessed its own cloud-hosted database on MongoDB Atlas. This architecture provided an ideal environment for simulating the broker system without the need for real APIs.

## Running the application
### Step 1
In order for the application to work as expected, the broker server (code: https://github.com/gehad-aboarab/swift-tasjeel-server) must be deployed and running on the AWS. For this, some parts of the code linking to the AWS instance must be changed.

### Step 2
Additionally, the following Java applications represent dummy entities that expose services for the server:
- Insurance Company: https://github.com/gehad-aboarab/cloud-insurance-services
- Testing Center: https://github.com/gehad-aboarab/cloud-testing-center-services
- Dubai Police: https://github.com/gehad-aboarab/cloud-dubai-police-services
- RTA: https://github.com/gehad-aboarab/cloud-rta-services
These must also be hosted on AWS for the server to receive meaningful data and relay to the application.

### Step 3
Build and run the application on an Android device.
