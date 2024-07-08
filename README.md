# Search App

## Overview

Search App is a Spring Boot web application that allows users to search for books using the Open Library API. Users can enter their name and a search term to retrieve book titles. The application also maintains a record of previous searches and provides sorting options for the results.

## Features

- Search for books by entering a username and a search term.
- Display search results with the number of results and the first 10 book titles.
- Save search records to a database using JPA and Hibernate ORM.
- List previous search records.
- Sort search results by title, username, or result count.
- Display a loading spinner while fetching data from the API.

## Prerequisites

- Java 11
- Maven
- An internet connection to fetch data from the Open Library API

## Installation

1. **Clone the Repository:**

    ```sh
    git clone https://github.com/tkaden/search-app.git
    cd search-app
    ```

2. **Build the Application:**

    ```sh
    mvn clean package
    ```

3. **Run the Application:**

    ```sh
    java -jar target/search-0.0.1-SNAPSHOT.jar
    ```

    The application will start on `http://localhost:8080`.

4. **Run the Application (with Docker):**

    Build the Container
    ```sh
    docker build -t search-app .
    ```

    Run the container
    ```sh
    docker run -p 8080:8080 search-app
    ```
## Usage

1. **Open the Application:**
    Open your web browser and navigate to `http://localhost:8080`.

2. **Search for Books:**
    - Enter your username in the "User Name" field.
    - Enter a search term in the "Search Term" field.
    - Click the "Search" button.
    - The results will display the number of books found and the first 10 titles.

3. **Clear Fields:**
    - Click the "Clear" button to clear the input fields and search results.

4. **List Previous Searches:**
    - Click the "List Previous" button to display all previous search records.
    - You can sort the records by title, username, or result count using the dropdown.

## Endpoints

- **GET /api/search**

    Query Parameters:
    - `username` (String): The username of the user performing the search.
    - `searchTerm` (String): The search term to query the Open Library API.
    - `sortBy` (String): (Optional) The field to sort the results by (`title`, `username`, or `resultCount`).

- **GET /api/previous**

    Query Parameters:
    - `sortBy` (String): (Optional) The field to sort the previous search records by (`title`, `username`, or `resultCount`).

## Project Structure

- **src/main/java/com/example/search**
    - `SearchApplication.java`: Main application class.
    - `SearchController.java`: REST controller for handling search requests.
    - `SearchService.java`: Service layer for business logic.
    - `SearchRecord.java`: Entity class for search records.
    - `SearchRecordRepository.java`: Repository interface for search records.

- **src/test/java/com/example/search**
    - `SearchControllerTests.java`: Unit tests for the `SearchController`.
    - `SearchServiceTests.java`: Unit tests for the `SearchService`.
    - `SearchRecordRepositoryTests.java`: Unit tests for the `SearchRecordRepository`.

- **src/main/resources**
    - `application.properties`: Configuration file for the application.

- **src/main/resources/static**
    - `index.html`: Frontend HTML file for the application using generic Bootstrap CSS and jQuery for handling user interactions.

## Running Tests

To run the tests, use the following command:

```sh
mvn test
```

## Notes
Template provided by [spring initializer](https://start.spring.io/)