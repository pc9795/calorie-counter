# calorie-counter
A project to learn about REST

**Abstract**

An API where users can register and signin. They can add meals with the calorie information and if that info is not present nutritionix API  is used to auto-populate calorie details. This API supports different roles, pagination and advanced searching capabilites.

**Techical Learnings**
* Implemented authentication and authorization using Spring security.
* Created REST resources using Spring boot.
* Used Spring-Data and Hibernate for database related things.
* Used Spring REST client to consume an external API.
* Created Hibernate criteria queries using Specifications. 
* Implemetned pagination using `Pageable`.
* API testing using Spring MockMvc.
* Repository testing using `DataJpaTest`.
* Unit and Integration testing with Spring.
* Used Swagger for API documentation.

**Could be better**
* Login/Register is implemented using traditional POST request could be done with JSON.
* User can apply calorie limit but it is for all entries(BLUNDER). This should be a daily limit. Simple change is query.
* PUT is creating resource if not present. I found on some places that it is okay but it is a doubt.
* In user roles there is a loop hole where one role which manages users can downgrade admin.

**How to run**
* Pull the repo.
* Create a postgres database and update the details in `src/main/resources/application.properties`. 
* Uncomment `spring.jpa.hibernate.ddl-auto=create` so that it will create all the tables for you.
* Create an API key at https://developer.nutritionix.com/ and update the details in `src/main/resources/nutritionix.properties`.
* Run the spring boot application from `src/main/Application.java`.
* Check the available API endpoints at http://localhost:8080/swagger-ui.html.
