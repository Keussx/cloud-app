# Cloud - Recipe App


- [Google App Engine Standard][1]

- [Java][2]

- [Google Cloud Endpoints Frameworks v2.0][8]
- [Google Cloud Endpoints Frameworks v1.0][3]

- [Google App Engine Maven plugin][14]
- [Google App Engine Gradle plugin][15]

- [Google Cloud Endpoints Frameworks Maven Plugin][10]
- [Google Cloud Endpoints Frameworks Gradle Plugin][11]


1. Add your API method to `src/main/java/myrecipes/app/YourFirstAPI.java`.

1. [Optional]: Use Cloud Endpoints Frameworks v1.0.

    - Uncomment Endpoints Frameworks v1.0 sections and comment
      Endpoints Frameworks v2.0 sections in the following files.

      ```
        pom.xml
        build.gradle
        src/main/webapp/WEB-INF/web.xml
      ```



1. Build and Run the application locally at [http://localhost:8080][5] by using:

    `mvn clean appengine:run`

1. Explore local server's API explorer by browsing to:

    [http://localhost:8080/_ah/api/explorer][13]

1. Generate the client library with a readme located at `target/client-libs/helloworld-v1-java.zip`
   by using:

    `mvn endpoints-framework:clientLibs`

1. Build and Deploy your application to Google App Engine by using:

    `mvn clean appengine:deploy`


1. Build and Run the application locally at [http://localhost:8080][5] by using:

    `gradle clean appengineRun`

1. Explore local server's API explorer by browsing to:

    [http://localhost:8080/_ah/api/explorer][13]

1. Generate the client library with a readme located at
   `build/endpointsClientLibs/helloworld-v1-java.zip` by using:

    `gradle endpointsClientLibs`

1. Build and Deploy your application to Google App Engine by using:

    `gradle clean appengineDeploy`

[1]: https://cloud.google.com/appengine/docs/java/
[2]: http://java.com/en/
[3]: https://cloud.google.com/endpoints/docs/frameworks/legacy/v1/java
[4]: https://cloud.google.com/appengine/docs/java/tools/maven
[5]: http://localhost:8080/
[6]: https://console.developers.google.com/project/_/apiui/credential
[7]: https://cloud.google.com/endpoints/docs/frameworks/legacy/v1/java/migrating
[8]: https://cloud.google.com/endpoints/docs/frameworks/java/about-cloud-endpoints-frameworks
[9]: https://cloud.google.com/endpoints/docs/frameworks/java/quickstart-frameworks-java
[10]: https://github.com/GoogleCloudPlatform/endpoints-framework-maven-plugin
[11]: https://github.com/GoogleCloudPlatform/endpoints-framework-gradle-plugin
[12]: https://cloud.google.com/endpoints/docs/authenticating-users-frameworks
[13]: http://localhost:8080/_ah_api/explorer
[14]: https://github.com/GoogleCloudPlatform/app-maven-plugin
[15]: https://github.com/GoogleCloudPlatform/app-gradle-plugin
