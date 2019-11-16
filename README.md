# calorie-counter
A project to learn about REST

**Abstract**
An API where users can register and signin. They can add meals with the calorie information and if that info is not present nutritionix API to fetch 
is used to auto-populate calorie details. This API supports different roles, pagination and searching capabilites by available attributes.

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

**Could be better**
* Login/Register is implemented using traditional POST request could be done with JSON.
* User can apply calorie limit but it is for all entries(BLUNDER). This should be a daily limit. Simple change is query.
* PUT is creating resource if not present. I found on some places that it is okay but it is a doubt.
* In user roles there is a loop hole where one role which can manage users could downgrade admin
