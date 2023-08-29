# FoodSpring - Food and Beverage Web App

FoodSpring is a web application built using Spring Boot and Thymeleaf that allows users to explore a variety of food and beverage options. The application also integrates with the Midtrans Snap API for payment processing. This README.md provides an overview of the project's technology stack and instructions for running the application. This is a individual project, not for commercial use. For any questions or business inquiries, contact us at [foodspringltd@gmail.com](mailto:foodspringltd@gmail.com)

## Tech Stack

The FoodSpring project utilizes the following technologies:

- **Spring Boot:** An application framework that simplifies the process of building production-ready applications using the Spring Framework.
- **Thymeleaf:** A server-side Java template engine for processing and rendering HTML templates.
- **Maven:** A popular build automation and project management tool used for managing project dependencies and building the application.
- **Spring Data JPA:** Part of the Spring Data project, it provides an easy and efficient way to interact with databases using Java Persistence API (JPA).
- **MySQL:** A widely used open-source relational database management system.
- **Bootstrap:** A front-end framework used for designing responsive and visually appealing web pages.
- **HTML and CSS:** Standard technologies for creating web page structure and styling.
- **RabbitMQ:** A message broker that enables communication between different parts of the application using messaging patterns.
- **Git:** A version control system for tracking changes in the source code during software development.
- **[Midtrans Snap API](https://midtrans.com):** An API for integrating with the Midtrans payment gateway to enable secure and seamless online payment processing.

## How to Run

Follow these steps to run the FoodSpring application on your local machine:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/hakimamarullah/foodspring.git
   cd foodspring
   ```

2. **Database Configuration:**

    - Make sure you have MySQL installed and running on your machine.
    - Open `src/main/resources/application.properties` and configure the database settings:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/foodspring
      spring.datasource.username=your-username
      spring.datasource.password=your-password
      ```

3. **Build and Run:**

    - Build the application using Maven:
      ```bash
      mvn clean package
      ```

    - Run the application using the following command:
      ```bash
      java -jar target/foodspring.jar
      ```

   - Run the application using the following command (using Maven):
     ```bash
     mvn spring-boot:run
     ```

   - You can also use the docker-compose (customize some credentials for your specific environment):
      ```bash
      docker-compose up -d --build
      ```

4. **Access the Application:**

    - Open your web browser and navigate to `http://localhost:8080` to access the FoodSpring application.

5. **Usage:**

    - Browse through the various food and beverage options.
    - Interact with the user interface to explore different features.
    - Enjoy using FoodSpring to discover delicious treats!
    - CRUD Products **(Admin only)**
    - CRUD Category **(Admin only)**
    - CRUD Discount **(Admin only)**
    - Manage user's orders **(Admin only)**
    - Checkout and payment using any of payment method provided by [Midtrans](https://midtrans.com)
    - Enjoy using FoodSpring to discover delicious treats!

6. **Shutdown:**

    - To stop the application, go back to the terminal and press `Ctrl + C`.


## License

This project is licensed under the [MIT License](LICENSE).

---

For any questions or inquiries, contact us at [foodspringltd@gmail.com](mailto:foodspringltd@gmail.com)

© 2023 FoodSpring Ltd. All rights reserved.
