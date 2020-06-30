# spring-boot-vuejs

## Setup Vue.js & Spring Boot

### Prerequisites

#### MacOSX

```
brew install node maven
```

#### Linux

```
sudo apt update
sudo apt install node maven
```

#### Windows

```
choco install maven
choco install npm
```

## Project layout

```
harbour
├─┬ backend     → backend module with Spring Boot code
│ ├── src
│ └── pom.xml
├─┬ frontend    → frontend module with Vue.js code
│ ├── src
│ └── pom.xml
└── pom.xml     → Maven parent pom managing both modules
```


## First App run

Inside the root directory, do a: 

```
mvn clean install
```

Run our complete Spring Boot App:

```
mvn --projects backend spring-boot:run
```

Now go to [http://localhost:8088/](http://localhost:8088/)



## Front end dev webpack-dev-server

The webpack-dev-server, which will update and build every change through all the parts of the JavaScript frontend is pre-configured in Vue.js.  
So the only thing needed to get fast feedback development-cycle is to navigate to `frontend` and run:

```
npm run serve
```
And then each change made in the `.js/.ts` code will trigger a rebuild. 
NOTE: This will spawn a new server at [http://localhost:8088/](http://localhost:8088/) and for API responses you will also need to run the Spring Boot server


## Browser developer tools extension
There is a nice VueJS browser extension that will make front end development & debugging easier it lives [HERE](https://github.com/vuejs/vue-devtools)


## IntelliJ integration

There's a blog post: https://blog.jetbrains.com/webstorm/2018/01/working-with-vue-js-in-webstorm/
Note that a lot of the JS features are also available in IntelliJ IDEA (Not community edition) as plugins


## Testing 

### Jest

Intro-Blogpost: https://blog.codecentric.de/2017/06/javascript-unit-tests-sind-schwer-aufzusetzen-keep-calm-use-jest/

Examples: https://github.com/vuejs/vue-test-utils-jest-example

Vue.js Jest Docs: https://vue-test-utils.vuejs.org/guides/#testing-single-file-components-with-jest

To pass Component props while using Vue.js Router, see https://stackoverflow.com/a/37940045/4964553.

How to test components with `router-view` or `router-link` https://vue-test-utils.vuejs.org/guides/using-with-vue-router.html#testing-components-that-use-router-link-or-router-view.

The test files itself could be named `xyz.spec.js` or `xyz.test.js` - and could reside nearly everywhere in the project.

## Build and run with Docker

```
docker build . --tag freightmate-harbour:latest
```

If you want to see the typical Spring Boot startup logs, just use `docker logs 745e854d7781 --follow`:

```
$ docker logs 745e854d7781 --follow

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.2.6.RELEASE)

2020-06-30 17:42:55.175  INFO 56448 --- [  restartedMain] com.freightmate.harbour.Harbour          : Starting Harbour on S06272-MBPR with PID 56448 (/Users/james.boyce/dev/freightmate/harbour/backend/target/classes started by james.boyce in /Users/james.boyce/dev/freightmate/harbour/backend)
2020-06-30 17:42:55.176  INFO 56448 --- [  restartedMain] com.freightmate.harbour.Harbour          : No active profile set, falling back to default profiles: default
2020-06-30 17:42:55.218  INFO 56448 --- [  restartedMain] o.s.b.devtools.restart.ChangeableUrls    : The Class-Path manifest attribute in /Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/jaxb-runtime-2.3.2.jar referenced one or more files that do not exist: file:/Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/jakarta.xml.bind-api-2.3.2.jar,file:/Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/txw2-2.3.2.jar,file:/Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/istack-commons-runtime-3.0.8.jar,file:/Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/stax-ex-1.8.1.jar,file:/Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/FastInfoset-1.2.16.jar,file:/Users/james.boyce/.m2/repository/org/glassfish/jaxb/jaxb-runtime/2.3.2/jakarta.activation-api-1.2.1.jar
2020-06-30 17:42:55.218  INFO 56448 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : Devtools property defaults active! Set 'spring.devtools.add-properties' to 'false' to disable
2020-06-30 17:42:55.218  INFO 56448 --- [  restartedMain] .e.DevToolsPropertyDefaultsPostProcessor : For additional web related logging consider setting the 'logging.level.web' property to 'DEBUG'
2020-06-30 17:42:56.072  INFO 56448 --- [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2020-06-30 17:42:56.130  INFO 56448 --- [  restartedMain] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 48ms. Found 1 JPA repository interfaces.
2020-06-30 17:42:56.780  INFO 56448 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8098 (http)
2020-06-30 17:42:56.794  INFO 56448 --- [  restartedMain] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2020-06-30 17:42:56.794  INFO 56448 --- [  restartedMain] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.33]
2020-06-30 17:42:56.892  INFO 56448 --- [  restartedMain] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2020-06-30 17:42:56.892  INFO 56448 --- [  restartedMain] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1674 ms
Loading class `com.mysql.jdbc.Driver'. This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
2020-06-30 17:42:57.135  INFO 56448 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2020-06-30 17:42:57.139  WARN 56448 --- [  restartedMain] com.zaxxer.hikari.util.DriverDataSource  : Registered driver with driverClassName=com.mysql.jdbc.Driver was not found, trying direct instantiation.
2020-06-30 17:42:57.327  INFO 56448 --- [  restartedMain] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.
2020-06-30 17:42:57.376  INFO 56448 --- [  restartedMain] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2020-06-30 17:42:57.460  INFO 56448 --- [  restartedMain] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 5.4.12.Final
2020-06-30 17:42:57.632  INFO 56448 --- [  restartedMain] o.hibernate.annotations.common.Version   : HCANN000001: Hibernate Commons Annotations {5.1.0.Final}
2020-06-30 17:42:57.750  INFO 56448 --- [  restartedMain] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.MySQL5InnoDBDialect
2020-06-30 17:42:58.276  INFO 56448 --- [  restartedMain] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000490: Using JtaPlatform implementation: [org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform]
2020-06-30 17:42:58.283  INFO 56448 --- [  restartedMain] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2020-06-30 17:42:58.324  WARN 56448 --- [  restartedMain] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning
2020-06-30 17:42:58.788  INFO 56448 --- [  restartedMain] o.s.s.web.DefaultSecurityFilterChain     : Creating filter chain: any request, [org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@d637986, org.springframework.security.web.context.SecurityContextPersistenceFilter@12460621, org.springframework.security.web.header.HeaderWriterFilter@268c2ee1, org.springframework.security.web.authentication.logout.LogoutFilter@54dcfffb, org.springframework.security.web.authentication.www.BasicAuthenticationFilter@6f732fa2, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@3b1cc57c, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@618e32f0, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@3ccad9ac, org.springframework.security.web.session.SessionManagementFilter@7bdf2491, org.springframework.security.web.access.ExceptionTranslationFilter@4c51c774, org.springframework.security.web.access.intercept.FilterSecurityInterceptor@5f6fca4b]
2020-06-30 17:42:58.932  INFO 56448 --- [  restartedMain] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
2020-06-30 17:42:59.006  INFO 56448 --- [  restartedMain] o.s.b.a.w.s.WelcomePageHandlerMapping    : Adding welcome page: class path resource [public/index.html]
2020-06-30 17:42:59.171  INFO 56448 --- [  restartedMain] o.s.b.d.a.OptionalLiveReloadServer       : LiveReload server is running on port 35729
2020-06-30 17:42:59.174  INFO 56448 --- [  restartedMain] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 2 endpoint(s) beneath base path '/actuator'
2020-06-30 17:42:59.234  INFO 56448 --- [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8098 (http) with context path ''
2020-06-30 17:42:59.238  INFO 56448 --- [  restartedMain] com.freightmate.harbour.Harbour          : Started Harbour in 4.415 seconds (JVM running for 4.884)
```