# Cloud - Recipe App

Before building the application install:
 > Maven build management system
 
 > Google SDK installed
 
 > Google DataStore Emulator
 
 
 
 
## 1.Sign in using gcloud

    run  "gcloud init" to enter google login details + select "ohmyrecipes" project

 
 
## 2.Setup and run datastore emulator

    Install google Datastore emulator using the guide (https://cloud.google.com/datastore/docs/tools/datastore-emulator)

    Run the emulator and check with port it is running on.
    


## 3. Uncomment few lines of code in all the 3 classes in app/src/main/java/myrecipes/app/services/ to use datastore locally.

    DatastoreOptions.newBuilder().setHost("http://localhost:8081").setProjectId("ohmyrecipes").build().getService();

    Also change 8081 to the port your datastore emulator is listening for connections.



## 4. Build and Run the application locally at [http://localhost:8080][5] by using:

    `mvn package clean; mvn appengine:run`


## 5. Explore local server's API explorer by browsing to:

    [http://localhost:8080/_ah/api/explorer][13]


