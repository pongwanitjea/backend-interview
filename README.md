# To run project

docker build -t mysql-image-allianz .

docker run --name mysql-allianz-instance -p 3306:3306 mysql-image-allianz

then run project with IntelliJ and OpenJDK 17 normally. Main class is located at src/main/java/pongwanit/backendallianzinterview/BackendAllianzInterviewApplication.java

#Swagger UI URL
http://localhost:1234/swagger-ui/index.html

POST /authenticate with request body

{
  "username": "user1",
  "password": "password1"
}
to get JWT token