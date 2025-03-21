# File Task API

This project provides an API to generate a sequence of integers between `start` and `end`, save them as a comma-separated values (CSV) file, retrieve the stored data, and delete the file.

## Features
- Save a sequence of integers to a file
- Retrieve stored integer sequences
- Delete the stored file
- Handles various edge cases with appropriate error responses

## API Endpoints

### **POST /file** - Save to file
**Description:** Saves a sequence of integers between `start` and `end` to a CSV file.

#### **Request**
- **URL:** `http://localhost:9000/file`
- **Method:** `POST`
- **Content-Type:** `application/json`
- **Body:**
  ```json
  {
    "start": 2,
    "end": 6
  }
  ```
#### **Responses**
- **200 OK:**
  ```json
  {
    "status": "success"
  }
  ```
- **400 BAD REQUEST:** `start` is greater than `end`
  ```json
  {
    "status": "failed",
    "message": "Start must be less than or equal to end"
  }
  ```
- **400 BAD REQUEST:** Invalid data type
  ```json
  {
    "status": "failed",
    "message": "Validation failed",
    "errors": [
        {
            "field": "/start",
            "message": "Expected a number"
        }
    ]
  }
  ```
- **400 BAD REQUEST:** Missing `end` field
  ```json
  {
    "status": "failed",
    "message": "Validation failed",
    "errors": [
        {
            "field": "/end",
            "message": "This field is required"
        }
    ]
  }
  ```
- **400 BAD REQUEST:** Negative integer values
  ```json
  {
    "status": "failed",
    "message": "Start and end must be non-negative integers"
  }
  ```
- **403 FORBIDDEN:** Permission denied
  ```json
  {
    "status": "failed",
    "message": "Permission denied"
  }
  ```

### **GET /file** - Fetch from file
**Description:** Retrieves the stored sequence of integers from the file.

#### **Request**
- **URL:** `http://localhost:9000/file`
- **Method:** `GET`

#### **Responses**
- **200 OK:** File found and contains data
  ```json
  {
      "res": [2, 3, 4, 5, 6]
  }
  ```
- **200 OK:** File exists but is empty
  ```json
  {
      "res": []
  }
  ```
- **404 NOT FOUND:** File does not exist
  ```json
  {
      "status": "failed",
      "message": "File not found"
  }
  ```
- **422 UNPROCESSABLE ENTITY:** Invalid file content
  ```json
  {
      "status": "failed",
      "message": "Invalid file content: Non-integer value found"
  }
  ```

### **DELETE /file** - Delete file
**Description:** Deletes the stored file.

#### **Request**
- **URL:** `http://localhost:9000/file`
- **Method:** `DELETE`

#### **Responses**
- **200 OK:** File successfully deleted
  ```json
  {
      "success": true
  }
  ```

## **Edge Cases Handled**
- **Start is greater than end:** Returns `400 Bad Request`
- **Missing required fields:** Returns `400 Bad Request`
- **Negative integer inputs:** Returns `400 Bad Request`
- **Invalid data types (e.g., strings instead of numbers):** Returns `400 Bad Request`
- **File not found when retrieving data:** Returns `404 Not Found`
- **Empty file:** Returns `200 OK` with an empty array
- **Invalid content in the file:** Returns `422 Unprocessable Entity`
- **Permission issues when writing to the file:** Returns `403 Forbidden`

## **Server Information**
- Base URL: `http://localhost:9000`
- Available Routes:
  - `POST /file` - Save to file
  - `GET /file` - Fetch from file
  - `DELETE /file` - Delete file

## **Usage**
- Ensure the server is running on `localhost:9000`.
- Use API clients like Postman or `curl` to send requests.
- Alternatively, test the APIs using the provided `apiDemo.scala.html` template, which serves as a frontend interface accessible at `http://localhost:9000/api-demo`.
