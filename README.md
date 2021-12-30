# Spring Demo Application
Spring Boot demo application to play with different aspects of Spring Boot

# Starting off
It has been a hot minute since I delved into Spring. To get reacquainted I thought
to create a simple web application and start adding functionality. I began with the boilerplate
I from [HERE](https://www.djamware.com/post/5b2f000880aca77b083240b2/spring-boot-security-and-data-mongodb-authentication-example).
This got me off to a start of a some pages and a simple employee creation and login functionality.

I am also using [MongoDB Atlas](https://www.mongodb.com/atlas) as a cloud based DB. Easy to work with and a NoSQL DB will 
fit well with the model. In addition, as this is really just a prototype I want the flexibility that is inherent in NoSQL.

From there; I thought since the company I work for still uses hard copy timesheet why not create a timesheet app
and go from there...

# Into the thick of it
So with the basic app need to refactor a few things. Need to rename the packages and Classes to make more sense 
of the project. Since this is going to be a scheduling and timesheet app will go with `org.dinism.scheduler` as the base package name.
Then change `User` class to `Employee` along with appropriate repository and service classes.



